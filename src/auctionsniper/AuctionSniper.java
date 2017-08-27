package auctionsniper;

import auctionsniper.UserRequestListener.Item;
import auctionsniper.util.Announcer;

/**
 * Created by bob on 29/07/2017.
 */
public class AuctionSniper implements AuctionEventListener {
    private final Announcer<SniperListener> listeners = Announcer.to(SniperListener.class);
    private final Auction auction;
    private final Item item;
    private SniperSnapshot snapshot;

    public AuctionSniper(Item item, Auction auction) {
        this.snapshot = SniperSnapshot.joining(item.identifier);
        this.auction = auction;
        this.item = item;
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                final int bid = price + increment;
                if (item.allowsBid(bid)) {
                    auction.bid(bid);
                    snapshot = snapshot.bidding(price, bid);
                } else {
                    snapshot = snapshot.losing(price);
                }
                break;
        }
        notifyChange();
    }

    @Override
    public void auctionFailed() {
        snapshot = snapshot.failed();
        listeners.announce().sniperStateChanged(snapshot);
    }

    private void notifyChange() {
        listeners.announce().sniperStateChanged(snapshot);
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    public void addSniperListener(SniperListener listener) {
        listeners.addListener(listener);
    }
}

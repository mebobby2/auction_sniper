package auctionsniper;

/**
 * Created by bob on 29/07/2017.
 */
public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;
    private final Auction auction;
    private boolean isWinning = false;
    private final String itemId;

    public AuctionSniper(String itemId, Auction auction, SniperListener listener) {
        this.itemId = itemId;
        this.sniperListener = listener;
        this.auction = auction;
    }

    public void auctionClosed() {
        if (isWinning) {
            sniperListener.sniperWon();
        } else {
            sniperListener.sniperLost();
        }
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == PriceSource.FromSniper;
        if (isWinning) {
            sniperListener.sniperWinning();
        } else {
            final int bid = price + increment;
            auction.bid(bid);
            sniperListener.sniperStateChanged(
                    new SniperSnapshot(itemId, price, bid, SniperState.BIDDING)
            );
        }
    }
}

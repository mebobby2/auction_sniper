package auctionsniper;

/**
 * Created by bob on 29/07/2017.
 */
public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;

    public AuctionSniper(SniperListener listener) {
        this.sniperListener = listener;
    }

    public void auctionClosed() {
        sniperListener.sniperLost();
    }

    @Override
    public void currentPrice(int price, int increment) {

    }
}

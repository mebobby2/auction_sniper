package auctionsniper;

/**
 * Created by bob on 29/07/2017.
 */
public interface Auction {
    void join();
    void bid(int amount);
    void addAuctionEventListener(AuctionEventListener listener);
}

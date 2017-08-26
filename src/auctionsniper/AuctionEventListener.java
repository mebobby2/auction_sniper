package auctionsniper;

import java.util.EventListener;

/**
 * Created by bob on 23/07/2017.
 */
public interface AuctionEventListener extends EventListener {
    enum PriceSource {
        FromSniper, FromOtherBidder
    }

    void auctionClosed();
    void currentPrice(int price, int increment, PriceSource priceSource);
    void auctionFailed();
}

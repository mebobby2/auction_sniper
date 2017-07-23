package auctionsniper;

import java.util.EventListener;

/**
 * Created by bob on 23/07/2017.
 */
public interface AuctionEventListener extends EventListener {

    void auctionClosed();
}

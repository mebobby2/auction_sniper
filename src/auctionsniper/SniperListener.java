package auctionsniper;

import java.util.EventListener;

/**
 * Created by bob on 29/07/2017.
 */
public interface SniperListener extends EventListener {
    void sniperLost();
    void sniperBidding();
    void sniperWinning();
}

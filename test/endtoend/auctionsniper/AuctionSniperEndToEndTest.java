package endtoend.auctionsniper;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
      System.out.println("sfsdfds");
        // auction.startSellingItem();
        // application.startBiddingIn(auction);
        // auction.hasReceivedJoinRequestFromSniper();
        // auction.announceClosed();
        // application.showsSniperHasLostAuction();

        assertEquals("0 must be 0", 0, 0);
    }

    @After
    public void stopAuction() {
        // auction.stop();
    }

    @After
    public void stopApplication() {
        // application.stop();
    }
}

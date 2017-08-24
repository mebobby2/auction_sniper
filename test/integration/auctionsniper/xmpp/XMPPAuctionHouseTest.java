package integration.auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.Main;
import auctionsniper.xmpp.XMPPAuction;
import auctionsniper.xmpp.XMPPAuctionException;
import auctionsniper.xmpp.XMPPAuctionHouse;
import endtoend.auctionsniper.ApplicationRunner;
import endtoend.auctionsniper.FakeAuctionServer;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static auctionsniper.xmpp.XMPPAuctionHouse.AUCTION_RESOURCE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;

public class XMPPAuctionHouseTest {
    private final FakeAuctionServer server = new FakeAuctionServer("item-54321");
    private XMPPAuctionHouse auctionHouse;
    private XMPPConnection connection;

    @Before
    public void openConnection() throws XMPPAuctionException, XMPPException {
        auctionHouse = XMPPAuctionHouse.connect(FakeAuctionServer.XMPP_HOSTNAME, ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD);

        connection = new XMPPConnection(FakeAuctionServer.XMPP_HOSTNAME);
        connection.connect();
        connection.login(ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD, AUCTION_RESOURCE);
    }

    @After
    public void closeConnection() {
        if (auctionHouse != null) {
            auctionHouse.disconnect();
            connection.disconnect();
        }
    }

    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = new XMPPAuction(connection, server.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

        auction.join();
        server.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
//        server.announceClosed();
//
//        assertThat("should have been closed", auctionWasClosed.await(2, SECONDS));
    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            @Override
            public void currentPrice(int price, int increment, PriceSource priceSource) {
                // not implemented
            }
        };
    }

}

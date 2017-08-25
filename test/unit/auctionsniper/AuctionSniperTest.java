package unit.auctionsniper;

import auctionsniper.*;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static auctionsniper.SniperState.*;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by bob on 29/07/2017.
 */
@RunWith(JMock.class)
public class AuctionSniperTest {
    private final Mockery context = new Mockery();
    private final Auction auction = context.mock(Auction.class);
    private final SniperListener sniperListener = context.mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction);
    private static final String ITEM_ID = "item-xxx";

    //    This is a “logical” representation of what’s going on inside the object, in this case the Sniper. It allows the
//    test to describe what it finds relevant about the Sniper, regardless of how the Sniper is actually implemented.
//    This separation will allow us to make radical changes to the implementation of the Sniper
//    without changing the tests.
    private final States sniperState = context.states("sniper");

    @Before
    public void attachListener() {
        sniper.addSniperListener(sniperListener);
    }

    @Test
    public void reportsLostWhenAuctionClosesImmediately() {
        context.checking(new Expectations() {{
            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(LOST)));
        }});

        sniper.auctionClosed();
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(BIDDING)));
                        then(sniperState.is("bidding"));

            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(LOST)));
            when(sniperState.is("bidding"));
        }});

        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(WINNING)));
                        then(sniperState.is("winning"));

            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(WON)));
            when(sniperState.is("winning"));
        }});

        sniper.currentPrice(132, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.auctionClosed();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;

        context.checking(new Expectations() {
            {
                //There are different opinions about whether test values should just be literals with “obvious” values, or
                // expressed in terms of the calculation they represent. Writing out the calculation may make the test more
                // readable but risks reimplementing the target code in the test, and in some cases the calculation will be
                // too complicated to reproduce. Here, we decide that the calculation is so trivial that we can just write
                // it into the test. This is referring to the `bid` variable.
                one(auction).bid(bid);
                atLeast(1).of(sniperListener).sniperStateChanged(
                        with(aSniperThatIs(BIDDING)));
            }
        });

        sniper.currentPrice(price, increment, AuctionEventListener.PriceSource.FromOtherBidder);
    }

    @Test
    public void reportIsWinningWhenCurrentPriceComesFromSniper() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(BIDDING)));
            then(sniperState.is("bidding"));

            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 135, 135, WINNING));
            when(sniperState.is("bidding"));
        }});

        sniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(135, 45, AuctionEventListener.PriceSource.FromSniper);
    }
    private Matcher<SniperSnapshot> aSniperThatIs(final SniperState state) {
        return new FeatureMatcher<SniperSnapshot, SniperState>(
                equalTo(state), "sniper that is ", "was")
        {
            @Override
            protected SniperState featureValueOf(SniperSnapshot actual) {
                return actual.state;
            }
        };
    }
}

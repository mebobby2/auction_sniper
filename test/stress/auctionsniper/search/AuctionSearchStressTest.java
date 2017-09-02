package stress.auctionsniper.search;

import auctionsniper.AuctionHouse;
import auctionsniper.search.AuctionDescription;
import auctionsniper.search.AuctionSearch;
import auctionsniper.search.AuctionSearchConsumer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import support.auctionsniper.StubAuctionHouse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

@RunWith(JMock.class)
public class AuctionSearchStressTest {
    private static final int NUMBER_OF_AUCTION_HOUSES = 16;
    private static final int NUMBER_OF_SEARCHES = 32;
    private static final Set<String> KEYWORDS = new HashSet<>(asList("sheep", "cheese"));

    final Synchroniser synchroniser = new Synchroniser();
    final Mockery context = new JUnit4Mockery() {{
        setThreadingPolicy(synchroniser);
    }};

    final AuctionSearchConsumer consumer = context.mock(AuctionSearchConsumer.class);
    final States searching = context.states("searching");

    final ExecutorService executor = Executors.newCachedThreadPool();
    final AuctionSearch search = new AuctionSearch(executor, auctionHouses(), consumer);

    @Test(timeout = 500)
    public void onlyOneAuctionSearchFinishedNotificationPerSearch() throws Exception {
        context.checking(new Expectations() {{
            ignoring(consumer).auctionSearchFound(with(any(List.class)));
        }});

        for (int i = 0; i < NUMBER_OF_SEARCHES; i++) {
            completeASearch();
        }
    }

    @After
    public void cleanUp() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
    }

    private void completeASearch() throws InterruptedException {
        searching.startsAs("in progress");
        context.checking(new Expectations() {{
            exactly(1).of(consumer).auctionSearchFinished(); then(searching.is("done"));
        }});

        search.search(KEYWORDS);
        synchroniser.waitUntil(searching.is("done"));
    }

    private List<AuctionHouse> auctionHouses() {
        ArrayList<AuctionHouse> houses = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_AUCTION_HOUSES; i++) {
            houses.add(stubbedAuctionHouse(i));
        }
        return houses;
    }

    private AuctionHouse stubbedAuctionHouse(final int id) {
        StubAuctionHouse house = new StubAuctionHouse();
        house.willReturnSearchResults(
                KEYWORDS, asList(new AuctionDescription(house, "id: " + id))
        );
        return house;
    }
}

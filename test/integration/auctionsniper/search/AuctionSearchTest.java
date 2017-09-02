package integration.auctionsniper.search;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import auctionsniper.UserRequestListener;
import auctionsniper.search.AuctionDescription;
import auctionsniper.search.AuctionSearch;
import auctionsniper.search.AuctionSearchConsumer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import support.auctionsniper.StubAuctionHouse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

@RunWith(JMock.class)
public class AuctionSearchTest {
    Mockery context = new JUnit4Mockery();
    final DeterministicExecutor executor = new DeterministicExecutor();
    final StubAuctionHouse houseA = new StubAuctionHouse();
    final StubAuctionHouse houseB = new StubAuctionHouse();

    List<AuctionDescription> resultsFromA = asList(new AuctionDescription(houseA, "1"));
    List<AuctionDescription> resultsFromB = asList(new AuctionDescription(houseB, "2"));

    final AuctionSearchConsumer consumer = context.mock(AuctionSearchConsumer.class);
    final AuctionSearch search = new AuctionSearch(executor, asList(houseA, houseB), consumer);

    @Test
    public void searchesAllAuctionHouses() throws Exception {
        final Set<String> keywords = new HashSet<>(asList("sheep", "cheese"));
        houseA.willReturnSearchResults(keywords, resultsFromA);
        houseB.willReturnSearchResults(keywords, resultsFromB);

        context.checking(new Expectations() {{
            final States searching = context.states("searching");

            oneOf(consumer).auctionSearchFound(resultsFromA); when(searching.isNot("done"));
            oneOf(consumer).auctionSearchFound(resultsFromB); when(searching.isNot("done"));
            oneOf(consumer).auctionSearchFinished(); then(searching.is("done"));
        }});

        search.search(keywords);
        executor.runUntilIdle();
    }
}

package auctionsniper.search;

import auctionsniper.AuctionHouse;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class AuctionSearch {
    private final Executor executor;
    private final List<AuctionHouse> auctionHouses;
    private final AuctionSearchConsumer consumer;

    private final AtomicInteger runningSearchCount = new AtomicInteger();

    public AuctionSearch(Executor executor, List<AuctionHouse> auctionHouses, AuctionSearchConsumer consumer) {
        this.executor = executor;
        this.auctionHouses = auctionHouses;
        this.consumer = consumer;
    }

    public void search(Set<String> keywords) {
        runningSearchCount.set(auctionHouses.size());

        for (AuctionHouse auctionHouse : auctionHouses) {
            startSearching(auctionHouse, keywords);
        }
    }

    private void startSearching(final AuctionHouse auctionHouse, final Set<String> keywords) {
        executor.execute(() -> search(auctionHouse, keywords));
    }

    private void search(AuctionHouse auctionHouse, Set<String> keywords) {
        consumer.auctionSearchFound(auctionHouse.findAuctions(keywords));

        if (runningSearchCount.decrementAndGet() == 0) {
            consumer.auctionSearchFinished();
        }
    }

}

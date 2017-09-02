package auctionsniper.search;

import java.util.List;

public interface AuctionSearchConsumer {
    void auctionSearchFound(List<AuctionDescription> auctionDescriptions);
    void auctionSearchFinished();
}

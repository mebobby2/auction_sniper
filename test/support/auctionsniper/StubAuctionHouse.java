package support.auctionsniper;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import auctionsniper.UserRequestListener;
import auctionsniper.search.AuctionDescription;

import java.util.List;
import java.util.Set;

public class StubAuctionHouse implements AuctionHouse {
    private List<AuctionDescription> searchResults;

    @Override
    public Auction auctionFor(UserRequestListener.Item item) {
        return null;
    }

    @Override
    public List<AuctionDescription> findAuctions(Set<String> keywords) {
        return searchResults;
    }

    public void willReturnSearchResults(Set<String> keywords, List<AuctionDescription> results) {
        this.searchResults = results;
    }
}

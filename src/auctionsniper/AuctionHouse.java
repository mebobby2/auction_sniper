package auctionsniper;

import auctionsniper.UserRequestListener.Item;
import auctionsniper.search.AuctionDescription;

import java.util.List;
import java.util.Set;

public interface AuctionHouse {
    Auction auctionFor(Item item);
    List<AuctionDescription> findAuctions(Set<String> keywords);
}

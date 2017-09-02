package auctionsniper.search;

import auctionsniper.AuctionHouse;

public class AuctionDescription {
    private final AuctionHouse auctionHouse;
    private final String description;

    public AuctionDescription(AuctionHouse auctionHouse, String description) {
        this.auctionHouse = auctionHouse;
        this.description = description;
    }
}

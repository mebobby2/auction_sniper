package auctionsniper;

public class SniperSnapshot {
    public int lastPrice;
    public int lastBid;
    public SniperState state;
    public String itemId;

    public SniperSnapshot(String itemId, int lasPrice, int lastBid, SniperState state) {
        this.lastPrice = lasPrice;
        this.lastBid = lastBid;
        this.state = state;
        this.itemId = itemId;
    }
}

package auctionsniper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SniperSnapshot {
    public String itemId;
    public int lastPrice;
    public int lastBid;
    public SniperState state;

    public SniperSnapshot(String itemId, int lasPrice, int lastBid, SniperState state) {
        this.lastPrice = lasPrice;
        this.lastBid = lastBid;
        this.state = state;
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static SniperSnapshot joining(String itemId) {
        return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
    }

    public SniperSnapshot bidding(int newLastPrice, int newLastBid) {
        return new SniperSnapshot(itemId, newLastPrice, newLastBid, SniperState.BIDDING);
    }

    public SniperSnapshot winning(int newLastPrice) {
        return new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.WINNING);
    }

    public SniperSnapshot losing(int newLastPrice) {
        return new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.LOSING);
    }

    public SniperSnapshot closed() {
        return new SniperSnapshot(itemId, lastPrice, lastBid, state.whenAuctionClosed());
    }

    public SniperSnapshot failed() {
        return new SniperSnapshot(itemId, 0,0, SniperState.FAILED);
    }

    public boolean isForSameItemAs(SniperSnapshot snapshot) {
        return itemId.equals(snapshot.itemId);
    }
}

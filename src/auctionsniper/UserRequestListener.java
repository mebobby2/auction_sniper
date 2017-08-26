package auctionsniper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.EventListener;

public interface UserRequestListener extends EventListener {
    void joinAuction(Item item);

    // There is a static nested class. In java, static classes have to be nested.
    // Static classes do not need an instance of the enclosing class in order to be instantiated itself
    public static class Item {
        public final String identifier;
        public final int stopPrice;

        public Item(String identifier, int stopPrice) {
            this.identifier = identifier;
            this.stopPrice = stopPrice;
        }

        public boolean allowsBid(int bid) {
            return bid <= stopPrice;
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
            return "Item: " + identifier + ", stop price: " + stopPrice;
        }

    }
}

package auctionsniper;

import auctionsniper.util.Defect;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by bob on 22/07/2017.
 */
public enum SniperState {
    JOINING,
    BIDDING,
    WINNING,
    LOST,
    WON;
}

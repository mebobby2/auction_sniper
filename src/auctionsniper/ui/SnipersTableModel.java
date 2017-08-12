package auctionsniper.ui;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
    private final static String[] STATUS_TEXT = {
            "Joining", "Bidding", "Winning", "Lost", "Won"
    };
    private SniperSnapshot snapShot;

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (snapShot == null) return ""; // Hack
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return snapShot.itemId;
            case LAST_PRICE:
                return snapShot.lastPrice;
            case LAST_BID:
                return snapShot.lastBid;
            case SNIPER_STATE:
                return textFor(snapShot.state);
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);

        }
    }

    @Override
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        this.snapShot = newSnapshot;
        fireTableRowsUpdated(0, 0);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public enum Column {
        ITEM_IDENTIFIER,
        LAST_PRICE,
        LAST_BID,
        SNIPER_STATE;

        public static Column at(int offset) {
            return values()[offset];
        }
    }
}

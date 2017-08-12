package auctionsniper.ui;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.ui.MainWindow.STATUS_JOINING;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
    private static String[] STATUS_TEXT = {MainWindow.STATUS_JOINING, MainWindow.STATUS_BIDDING, MainWindow.STATUS_WINNING};
    private SniperSnapshot snapShot;
    private String state;

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
        System.out.println("columnIndex is " + columnIndex);
        System.out.println("name is " + Column.at(columnIndex));
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                System.out.println("itemId is " + snapShot);
                return snapShot.itemId;
            case LAST_PRICE:
                return snapShot.lastPrice;
            case LAST_BID:
                return snapShot.lastBid;
            case SNIPER_STATE:
                return state;
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);

        }
    }

    public void setStatusText(String status) {
        state = status;
        fireTableRowsUpdated(0, 0);
    }

//    public void sniperStatusChanged(SniperState newSniperState, String newStatusText) {
//        sniperState = newSniperState;
//        statusText = newStatusText;
//        fireTableRowsUpdated(0, 0);
//    }

    @Override
    public void sniperLost() {

    }

    @Override
    public void sniperWon() {

    }

    @Override
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        System.out.println("sniperStateChangedsniperStateChangedsniperStateChangedsniperStateChanged");
        this.snapShot = newSnapshot;
        this.state = STATUS_TEXT[newSnapshot.state.ordinal()];

        fireTableRowsUpdated(0, 0);
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

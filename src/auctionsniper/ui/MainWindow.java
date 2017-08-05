package auctionsniper.ui;

import auctionsniper.SniperState;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;


/**
 * Created by bob on 22/07/2017.
 */
public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";

    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_WON = "won";
    private static final String SNIPERS_TABLE_NAME = "Snipers Table";

    private final SnipersTableModel snipers = new SnipersTableModel();

    public MainWindow() {
        super("Auction Sniper");
        setSize(300,130);
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void showStatus(String status) {
        snipers.setStatusText(status);
    }

    public void sniperStatusChanged(SniperState sniperState, String statusText) {
        snipers.sniperStatusChanged(sniperState, statusText);
    }
}

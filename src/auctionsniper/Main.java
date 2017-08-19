package auctionsniper;

import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;
import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Main {
    @SuppressWarnings("unused")
    private ArrayList<Auction> notToBeGCd = new ArrayList<>();

    private MainWindow ui;

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;

    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";

    public static final String BID_COMMAND_FORMAT = "SQLVersion 1.1; Command: BID; Price: %d;";
    public static final String JOIN_COMMAND_FORMAT = "";

    private final SnipersTableModel snipers = new SnipersTableModel();

    public Main() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(connection);

        main.addUserRequestListenerFor(connection);
    }

    private void addUserRequestListenerFor(XMPPConnection connection) {
        ui.addUserRequestListener(itemId -> {
            snipers.addSniper(SniperSnapshot.joining(itemId));
            Auction auction = new XMPPAuction(connection, itemId);
            notToBeGCd.add(auction);
            auction.addAuctionEventListener(
                    new AuctionSniper(itemId, auction,
                            new SwingThreadSniperListener(snipers))
            );
            auction.join();
        });
    }

    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection((hostname));
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);

        return connection;
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow(snipers));
    }

    public class SwingThreadSniperListener implements SniperListener {

        private final SniperListener listener;

        public SwingThreadSniperListener(SniperListener listener) {
            this.listener = listener;
        }

        @Override
        public void sniperStateChanged(SniperSnapshot snapshot) {
            SwingUtilities.invokeLater(() -> listener.sniperStateChanged(snapshot));
        }

    }
}

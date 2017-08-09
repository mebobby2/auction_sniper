package auctionsniper;

import auctionsniper.ui.MainWindow;
import auctionsniper.xmpp.AuctionMessageTranslator;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
  @SuppressWarnings("unused") private Chat notToBeGCd;

  private MainWindow ui;

  private static final int ARG_HOSTNAME = 0;
  private static final int ARG_USERNAME = 1;
  private static final int ARG_PASSWORD = 2;
  private static final int ARG_ITEM_ID  = 3;

  public static final String ITEM_ID_AS_LOGIN = "auction-%s";
  public static final String AUCTION_RESOURCE = "Auction";
  public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

  public static final String BID_COMMAND_FORMAT = "SQLVersion 1.1; Command: BID; Price: %d;";
  public static final String JOIN_COMMAND_FORMAT = "";

  public Main() throws Exception {
    startUserInterface();
  }

  public static void main(String... args) throws Exception {
    Main main = new Main();
    main.joinAuction(
      connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]),
            args[ARG_ITEM_ID]);
  }

  private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
    disconnectWhenUICloses(connection);

    final Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
    notToBeGCd = chat;

    Auction auction = new XMPPAuction(chat);
    chat.addMessageListener(
            new AuctionMessageTranslator(
                    connection.getUser(),
                    new AuctionSniper(itemId, auction, new SniperStateDisplayer())));
    auction.join();
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

  private static String auctionId(String itemId, XMPPConnection connection) {
    return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
  }

  private void startUserInterface() throws Exception { SwingUtilities.invokeAndWait(() -> ui = new MainWindow()); }

  // There is a static nested class. In java, static classes have to be nested.
  // Static classes do not need an instance of the enclosing class in order to be instantiated itself
  public static class XMPPAuction implements Auction {
    private final Chat chat;

    public XMPPAuction(Chat chat) {
      this.chat = chat;
    }

    @Override
    public void join() {
      sendMessage(JOIN_COMMAND_FORMAT);
    }

    @Override
    public void bid(int amount) {
      sendMessage(String.format(BID_COMMAND_FORMAT, amount));
    }

    private void sendMessage(final String message) {
      try {
        chat.sendMessage(message);
      } catch (XMPPException e) {
        e.printStackTrace();
      }
    }
  }

  public class SniperStateDisplayer implements SniperListener {

//    @Override
//    public void sniperBidding(SniperState state) {
//      SwingUtilities.invokeLater(() -> ui.sniperStatusChanged(state, MainWindow.STATUS_BIDDING));
//    }

    @Override
    public void sniperWinning() {
      showStatus(MainWindow.STATUS_WINNING);
    }

    @Override
    public void sniperWon() {
      showStatus(MainWindow.STATUS_WON);
    }

    @Override
    public void sniperStateChanged(SniperSnapshot snapshot) {
      SwingUtilities.invokeLater(() -> ui.sniperStatusChanged(snapshot));
    }

    @Override
    public void sniperLost() {
      showStatus(MainWindow.STATUS_LOST);
    }

    private void showStatus(final String status) {
      SwingUtilities.invokeLater(() -> ui.showStatus(status));
    }
  }
}

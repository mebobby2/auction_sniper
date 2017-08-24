package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import auctionsniper.UserRequestListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuctionHouse implements AuctionHouse {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    private final XMPPConnection connection;

    public XMPPAuctionHouse(XMPPConnection connection) {
        this.connection = connection;
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPAuctionException {
        XMPPConnection connection = new XMPPConnection(hostname);
        try {
            connection.connect();
            connection.login(username, password, AUCTION_RESOURCE);
            return new XMPPAuctionHouse(connection);
        } catch (XMPPException xmppe) {
            throw new XMPPAuctionException("Could not connect to auction "+ connection, xmppe);
        }
    }

    public void disconnect() {
        connection.disconnect();
    }

    @Override
    public Auction auctionFor(UserRequestListener.Item item) {
        return new XMPPAuction(connection, item.identifier);
    }
}

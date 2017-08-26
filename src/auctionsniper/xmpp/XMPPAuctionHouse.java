package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import auctionsniper.UserRequestListener;
import org.apache.commons.io.FilenameUtils;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class XMPPAuctionHouse implements AuctionHouse {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    private static final String LOGGER_NAME = "auction-sniper";
    private static final String LOG_FILE_NAME = "auction-sniper.log";
    private final XMPPConnection connection;
    private final XMPPFailureReporter failureReporter;

    public XMPPAuctionHouse(XMPPConnection connection) throws XMPPAuctionException {
        this.connection = connection;
        this.failureReporter = new LoggingXMPPFailureReporter(makeLogger());
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
        return new XMPPAuction(connection, item.identifier, failureReporter);
    }

    private Logger makeLogger() throws XMPPAuctionException {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.setUseParentHandlers(false);
        logger.addHandler(simpleFileHandler());
        return logger;
    }

    private FileHandler simpleFileHandler() throws XMPPAuctionException {
        try {
            FileHandler handler = new FileHandler(LOG_FILE_NAME);
            handler.setFormatter(new SimpleFormatter());
            return handler;
        } catch (Exception e) {
            throw new XMPPAuctionException("Could not create logger FileHandler " + FilenameUtils.getFullPath(LOG_FILE_NAME), e);
        }
    }
}

package unit.auctionsniper.xmpp;

import auctionsniper.xmpp.AuctionMessageTranslator;
import auctionsniper.AuctionEventListener;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

/**
 * Created by bob on 23/07/2017.
 */
public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator();

    private final Mockery context = new Mockery();
    private final AuctionEventListener listener = context.mock(AuctionEventListener.class);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {
        // Noticed the Expectation class returns an annoymous block
        context.checking(new Expectations() {{
            oneOf(listener).auctionClosed();
        }});

        Message message = new Message();
        message.setBody("SQLVersion: 1.1; Event: CLOSE;");

        translator.processMessage(UNUSED_CHAT, message);
    }
}

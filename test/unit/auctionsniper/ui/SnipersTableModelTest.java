package unit.auctionsniper.ui;

import auctionsniper.SniperState;
import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@RunWith(JMock.class)
public class SnipersTableModelTest {
    private final Mockery context = new Mockery();
    private TableModelListener listener = context.mock(TableModelListener.class);
    private final SnipersTableModel model = new SnipersTableModel();

    @Before
    public void attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(SnipersTableModel.Column.values().length));
    }

    @Test
    public void setsSniperValuesInColumns() {
        context.checking(new Expectations(){{
            one(listener).tableChanged(with(aRowChangedEvent()));
        }});

        model.sniperStatusChanged(new SniperState("item id", 555, 666), MainWindow.STATUS_BIDDING);

        assertColumnEquals(SnipersTableModel.Column.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(SnipersTableModel.Column.LAST_PRICE, 555);
        assertColumnEquals(SnipersTableModel.Column.LAST_BID, 666);
        assertColumnEquals(SnipersTableModel.Column.SNIPER_STATUS, MainWindow.STATUS_BIDDING);
    }

    private void assertColumnEquals(SnipersTableModel.Column column, Object expected) {
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model, 0));
    }
}
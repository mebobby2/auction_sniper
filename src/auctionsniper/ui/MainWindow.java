package auctionsniper.ui;

import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import auctionsniper.util.Announcer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;


/**
 * Created by bob on 22/07/2017.
 */
public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    private static final String SNIPERS_TABLE_NAME = "Snipers Table";
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String NEW_ITEM_ID_NAME = "New Item Id Textfield";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "New Item Stop Price Id Textfield";
    public static final String JOIN_BUTTON_NAME = "Join Button";

    private SniperPortfolio portfolio;
    private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);

    public MainWindow(SniperPortfolio portfolio) {
        super("Auction Sniper");
        this.portfolio = portfolio;
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(portfolio), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel makeControls() {
        final JTextField itemIdField = itemIdField();
        final JFormattedTextField stopPriceIdField = stopPriceField();

        JPanel controls = new JPanel(new FlowLayout());
        controls.add(new JLabel("Item:"));
        controls.add(itemIdField);
        controls.add(new JLabel("Stop price:"));
        controls.add(stopPriceIdField);

        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        controls.add(joinAuctionButton);

        joinAuctionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // we’ve converted an ActionListener event, which is internal to the user interface framework, to a
                // UserRequestListener event, which is about users interacting with an auction. These are two separate
                // domains and MainWindow’s job is to translate from one to the other.
                userRequests.announce().joinAuction(new UserRequestListener.Item(itemId(), stopPrice()));
            }

            private String itemId() {
                return itemIdField.getText();
            }

            private int stopPrice() {
                return ((Number)stopPriceIdField.getValue()).intValue();
            }
        });

        return controls;
    }


    private JTextField itemIdField() {
        JTextField itemIdField = new JTextField();
        itemIdField.setColumns(10);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        return itemIdField;
    }

    private JFormattedTextField stopPriceField() {
        JFormattedTextField stopPriceField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        stopPriceField.setColumns(7);
        stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);
        return stopPriceField;
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable(SniperPortfolio portfolio) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);
        final JTable snipersTable = new JTable(model);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void addUserRequestListener(UserRequestListener listener) {
        userRequests.addListener(listener);
    }
}

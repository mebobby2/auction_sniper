package auctionsniper.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;


/**
 * Created by bob on 22/07/2017.
 */
public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPER_STATUS_NAME = "sniper status";
    public static final String STATUS_LOST = "lost";
    private final JLabel sniperStatus = createLabel("joining");

    public MainWindow() {
        super("Auction Sniper");
        setSize(300,130);
        setName(MAIN_WINDOW_NAME);
        add(sniperStatus);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    public void showStatus(String status) {
        sniperStatus.setText(status);
    }
}

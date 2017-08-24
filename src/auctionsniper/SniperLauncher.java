package auctionsniper;

import auctionsniper.ui.SnipersTableModel;

import javax.swing.*;
import java.util.ArrayList;

public class SniperLauncher implements UserRequestListener {
    private final ArrayList<Auction> notToBeGCd = new ArrayList<>();
    private final AuctionHouse auctionHouse;
    private final SnipersTableModel snipers;

    public SniperLauncher(AuctionHouse auctionHouse, SnipersTableModel snipers) {
        this.auctionHouse = auctionHouse;
        this.snipers = snipers;
    }
    @Override
    public void joinAuction(String itemId) {
        snipers.addSniper(SniperSnapshot.joining(itemId));
        Item item = new Item(itemId, 0);
        Auction auction = auctionHouse.auctionFor(item);
        notToBeGCd.add(auction);
        AuctionSniper sniper = new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers));
        auction.addAuctionEventListener(sniper);
        auction.join();
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

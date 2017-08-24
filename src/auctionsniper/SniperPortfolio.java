package auctionsniper;

import java.util.ArrayList;

public class SniperPortfolio implements SniperCollector {
    private final ArrayList<AuctionSniper> snipers = new ArrayList<>();

    @Override
    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
    }
}

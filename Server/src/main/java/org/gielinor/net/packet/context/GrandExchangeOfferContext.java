package org.gielinor.net.packet.context;

import org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeOffer;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GrandExchangeOfferContext implements Context {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The offer to update.
     */
    private final GrandExchangeOffer offer;

    /**
     * Constructs a new <code>GrandExchangeOfferContext</code>.
     *
     * @param player The player.
     * @param offer  The grand exchange offer to update.
     */
    public GrandExchangeOfferContext(Player player, GrandExchangeOffer offer) {
        this.player = player;
        this.offer = offer;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the offer.
     *
     * @return The offer.
     */
    public GrandExchangeOffer getOffer() {
        return offer;
    }

}

package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class MultiCombatContext implements Context {

    /**
     * The player.
     */
    private Player player;

    /**
     * Whether or not the player is in a multi-way combat zone.
     */
    private boolean multi;


    public MultiCombatContext(Player player, boolean multi) {
        this.player = player;
        this.multi = multi;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets if the player is in a multi-way combat zone.
     *
     * @return <code>True</code> if so.
     */
    public boolean isMulti() {
        return multi;
    }
}

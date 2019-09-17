package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The {@link org.gielinor.net.packet.out.PlayerDetail} context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class PlayerDetailContext implements Context {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The player's member status.
     */
    private final boolean member;

    /**
     * The player's index.
     */
    private final int index;

    /**
     * Constructs a new {@code PlayerDetailContext} {@code Object}.
     *
     * @param player The player.
     * @param member The player's member status.
     * @param index  The player's index.
     */
    public PlayerDetailContext(Player player, boolean member, int index) {
        this.player = player;
        this.member = member;
        this.index = index;
    }

    /**
     * Gets the player.
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * The player's member status.
     */
    public boolean isMember() {
        return member;
    }

    /**
     * The player's index.
     */
    public int getIndex() {
        return index;
    }
}

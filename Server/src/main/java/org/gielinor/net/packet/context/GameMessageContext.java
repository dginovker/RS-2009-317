package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The game message packet context.
 *
 * @author Emperor
 */
public final class GameMessageContext implements Context {

    /**
     * The player reference.
     */
    private Player player;

    /**
     * The game message.
     */
    private String message;

    /**
     * The filter type.
     */
    private final byte filterType;

    /**
     * Construct a new {@code GameMessageContext} {@code Object}.
     *
     * @param player     The player.
     * @param message    The game message.
     * @param filterType The filter type.
     */
    public GameMessageContext(Player player, String message, int filterType) {
        this.player = player;
        this.message = message;
        this.filterType = (byte) filterType;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the game message.
     *
     * @return The game message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the filter type.
     *
     * @return The filter type.
     */
    public byte getFilterType() {
        return filterType;
    }
}

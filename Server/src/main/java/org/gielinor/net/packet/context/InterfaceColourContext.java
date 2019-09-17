package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The {@link org.gielinor.net.packet.out.InterfaceColour} context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class InterfaceColourContext implements Context {

    /**
     * The player.
     */
    private Player player;
    /**
     * The interface id.
     */
    private final int interfaceId;
    /**
     * The colour.
     */
    private final int colour;
    /**
     * Whether or not to use hex.
     */
    private final boolean hex;

    /**
     * Constructs a new {@code InterfaceColourContext} {@code Object}.
     *
     * @param player      The player.
     * @param interfaceId The interface id.
     * @param colour      The colour.
     * @param hex         Whether or not to use hex.
     */
    public InterfaceColourContext(Player player, int interfaceId, int colour, boolean hex) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.colour = colour;
        this.hex = hex;
    }

    /**
     * Constructs a new {@code InterfaceColourContext} {@code Object}.
     *
     * @param player      The player.
     * @param interfaceId The interface id.
     * @param colour      The colour.
     */
    public InterfaceColourContext(Player player, int interfaceId, int colour) {
        this(player, interfaceId, colour, false);
    }

    /**
     * Transforms this context for the new player & id.
     *
     * @param player The player.
     * @param id     The new interface id.
     * @return The interface context created.
     */
    public InterfaceColourContext transform(Player player, int id) {
        return new InterfaceColourContext(player, id, colour);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player.
     *
     * @param player The player.
     * @return This context instance.
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Gets the interfaceId.
     *
     * @return The interfaceId.
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Gets the colour.
     *
     * @return The colour.
     */
    public int getColour() {
        return colour;
    }

    /**
     * If we should use hex.
     *
     * @return <code>True</code> if so.
     */
    public boolean isHex() {
        return hex;
    }

}
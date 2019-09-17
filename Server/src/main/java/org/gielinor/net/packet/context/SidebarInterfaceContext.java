package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The sidebar interface context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class SidebarInterfaceContext implements Context {

    /**
     * The player.
     */
    private Player player;

    /**
     * The slot.
     */
    private int slot;

    /**
     * The interface id.
     */
    private final int interfaceId;

    /**
     * Constructs a new {@code SidebarInterfaceContext} {@code Object}.
     *
     * @param player      The player.
     * @param interfaceId The interface id.
     * @param slot        The slot.
     */
    public SidebarInterfaceContext(Player player, int interfaceId, int slot) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.slot = slot;
    }

    /**
     * Transforms this context for the new player & id.
     *
     * @param player The player.
     * @param id     The new interface id.
     * @return The interface context created.
     */
    public SidebarInterfaceContext transform(Player player, int id) {
        return new SidebarInterfaceContext(player, id, slot);
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
     * Gets the slot.
     *
     * @return The slot.
     */
    public int getSlot() {
        return slot;
    }

}

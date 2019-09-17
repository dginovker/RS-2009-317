package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The sidebar interface context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ChatboxInterfaceContext implements Context {

    /**
     * The player.
     */
    private Player player;

    /**
     * The interface id.
     */
    private final int interfaceId;

    /**
     * Whether or not this chatbox interface is walkable.
     */
    private boolean walkable;

    /**
     * Constructs a new {@code ChatboxInterfaceContext} {@code Object}.
     *
     * @param player      The player.
     * @param interfaceId The interface id.
     */
    public ChatboxInterfaceContext(Player player, int interfaceId) {
        this.player = player;
        this.interfaceId = interfaceId;
    }

    /**
     * Constructs a new {@code ChatboxInterfaceContext} {@code Object}.
     *
     * @param player      The player.
     * @param interfaceId The interface id.
     * @param walkable    Whether or not this chatbox interface is walkable.
     */
    public ChatboxInterfaceContext(Player player, int interfaceId, boolean walkable) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.walkable = walkable;
    }

    /**
     * Transforms this context for the new player & id.
     *
     * @param player The player.
     * @param id     The new interface id.
     * @return The interface context created.
     */
    public ChatboxInterfaceContext transform(Player player, int id) {
        return new ChatboxInterfaceContext(player, id, walkable);
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
     * Gets whether or not this chatbox interface is walkable.
     *
     * @return <code>True</code> if so.
     */
    public boolean isWalkable() {
        return walkable;
    }

}

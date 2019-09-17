package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The {@link org.gielinor.net.packet.out.InterfaceScrollPosition} packet context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class InterfaceScrollPositionContext implements Context {

    /**
     * The player reference.
     */
    private Player player;

    /**
     * The id of the interface.
     */
    private int interfaceId;

    /**
     * The position.
     */
    private int position;

    /**
     * Constructs a new {@code InterfaceMaxScrollContext}.
     *
     * @param player      The player.
     * @param interfaceId The id of the interface.
     * @param position    The position.
     */
    public InterfaceScrollPositionContext(Player player, int interfaceId, int position) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.position = position;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the id of the interface.
     *
     * @return The interface id.
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Get the position.
     *
     * @return The position.
     */
    public int getPosition() {
        return position;
    }

}

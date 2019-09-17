package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The {@link org.gielinor.net.packet.out.InterfaceMaxScrollPacket} packet context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class InterfaceMaxScrollContext implements Context {

    /**
     * The player reference.
     */
    private Player player;

    /**
     * The id of the interface.
     */
    private int interfaceId;

    /**
     * The max scroll length.
     */
    private int length;

    /**
     * Constructs a new {@code InterfaceMaxScrollContext}.
     *
     * @param player      The player.
     * @param interfaceId The id of the interface.
     * @param length      The max scroll length.
     */
    public InterfaceMaxScrollContext(Player player, int interfaceId, int length) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.length = length;
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
     * Get the max scroll length.
     *
     * @return The length.
     */
    public int getLength() {
        return length;
    }

}

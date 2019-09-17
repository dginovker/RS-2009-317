package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The {@link org.gielinor.net.packet.out.StringPacket} packet context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class StringContext implements Context {

    /**
     * The player reference.
     */
    private Player player;

    /**
     * The StringPacket string.
     */
    private String string;

    /**
     * The interface id.
     */
    private int interfaceId;

    /**
     * Constructs a new {@code StringContext} {@code Object}.
     *
     * @param player      The player.
     * @param string      The string to send.
     * @param interfaceId The interface id.
     */
    public StringContext(Player player, String string, int interfaceId) {
        this.player = player;
        this.string = string;
        this.interfaceId = interfaceId;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the StringPacket string.
     *
     * @return The string.
     */
    public String getString() {
        return string;
    }

    /**
     * Get the interface id.
     *
     * @return The interface id.
     */
    public int getInterfaceId() {
        return interfaceId;
    }

}

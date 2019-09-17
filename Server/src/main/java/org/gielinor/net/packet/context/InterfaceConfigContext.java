package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The {@link org.gielinor.net.packet.out.InterfaceConfig} {@link org.gielinor.net.packet.Context} instance.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class InterfaceConfigContext implements Context {

    /**
     * The player.
     */
    private Player player;

    /**
     * The config id.
     */
    private int id;

    /**
     * The config value.
     */
    private int value;

    /**
     * Construct a new {@link org.gielinor.net.packet.context.InterfaceConfigContext}.
     *
     * @param player The player.
     * @param id     The config id.
     * @param value  The config value.
     */
    public InterfaceConfigContext(Player player, int id, int value) {
        this.player = player;
        this.id = id;
        this.value = value;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the config id.
     *
     * @return The  id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the config value.
     *
     * @return The value.
     */
    public int getValue() {
        return value;
    }
}

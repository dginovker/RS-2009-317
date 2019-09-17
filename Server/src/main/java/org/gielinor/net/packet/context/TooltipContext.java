package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TooltipContext implements Context {


    /**
     * The player reference.
     */
    private Player player;

    /**
     * The inventory component id.
     */
    private int inventoryId;

    /**
     * The component id.
     */
    private int componentId;

    /**
     * The string.
     */
    private String[] strings;

    /**
     * Construct a new <code>TooltipContext</code>.
     *
     * @param player      The player.
     * @param inventoryId The inventory component id.
     * @param componentId The component id.
     * @param strings     The strings.
     */
    public TooltipContext(Player player, int inventoryId, int componentId, String... strings) {
        this.player = player;
        this.inventoryId = inventoryId;
        this.componentId = componentId;
        this.strings = strings;
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
     * Get the inventory component id.
     *
     * @return The id.
     */
    public int getInventoryId() {
        return inventoryId;
    }

    /**
     * Get the component id.
     *
     * @return The id.
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * Get the strings.
     *
     * @return The strings.
     */
    public String[] getStrings() {
        return strings;
    }
}

package org.gielinor.net.packet.context;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The inventory interface context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class InventoryInterfaceContext implements Context {

    /**
     * The player.
     */
    private Player player;

    /**
     * The component.
     */
    private final Component component;

    /**
     * The inventory component.
     */
    private final Component inventoryComponent;

    /**
     * Constructs a new {@code InventoryInterfaceContext} {@code Object}.
     *
     * @param player             The player.
     * @param component          The component.
     * @param inventoryComponent The inventory component.
     */
    public InventoryInterfaceContext(Player player, Component component, Component inventoryComponent) {
        this.player = player;
        this.component = component;
        this.inventoryComponent = inventoryComponent;
    }

    /**
     * Transforms this context for the new player & id.
     *
     * @param player             The player.
     * @param component          The component.
     * @param inventoryComponent The inventory component.
     * @return The interface context created.
     */
    public InventoryInterfaceContext transform(Player player, Component component, Component inventoryComponent) {
        return new InventoryInterfaceContext(player, component, inventoryComponent);
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
     * Gets the component.
     *
     * @return The component.
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Gets the inventory component.
     *
     * @return The inventory component.
     */
    public Component getInventoryComponent() {
        return inventoryComponent;
    }

}

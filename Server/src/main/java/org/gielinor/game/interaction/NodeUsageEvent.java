package org.gielinor.game.interaction;

import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents a node use-with (other node) option.
 *
 * @author Emperor
 */
public final class NodeUsageEvent {

    /**
     * The player.
     */
    private final Player player;
    /**
     * The component id.
     */
    private final int componentId;
    /**
     * The used node.
     */
    private final Node used;
    /**
     * The node we used the other node on.
     */
    private final Node with;
    /**
     * The opcode of the packet.
     */
    private int opcode;

    /**
     * Constructs a new {@code NodeUsageEvent} {@code Object}.
     *
     * @param player      The player.
     * @param componentId The component id.
     * @param used        The used node.
     * @param with        The node the other node is used on.
     */
    public NodeUsageEvent(Player player, int componentId, Node used, Node with) {
        this.player = player;
        this.componentId = componentId;
        this.used = used;
        this.with = with;
    }

    /**
     * Constructs a new {@code NodeUsageEvent} {@code Object}.
     *
     * @param player      The player.
     * @param componentId The component id.
     * @param used        The used node.
     * @param with        The node the other node is used on.
     * @param opcode      The opcode of the packet.
     */
    public NodeUsageEvent(Player player, int componentId, Node used, Node with, int opcode) {
        this.player = player;
        this.componentId = componentId;
        this.used = used;
        this.with = with;
        this.opcode = opcode;
    }

    /**
     * Gets the base item.
     *
     * @return The base item.
     */
    public Item getBaseItem() {
        return with instanceof Item ? (Item) with : null;
    }

    /**
     * Gets the used item.
     *
     * @return The used item.
     */
    public Item getUsedItem() {
        return used instanceof Item ? (Item) used : null;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the componentId.
     *
     * @return The componentId.
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * Gets the used.
     *
     * @return The used.
     */
    public Node getUsed() {
        return used;
    }

    /**
     * The node the other node is used on.
     *
     * @return The node.
     */
    public Node getUsedWith() {
        return with;
    }

    /**
     * Gets the opcode of the packet.
     *
     * @return The opcode.
     */
    public int getOpcode() {
        return opcode;
    }

    @Override
    public String toString() {
        return NodeUsageEvent.class.getName() + " [componentId=" + componentId + " used=" + used.getId() + " with=" + with.getId() + "]";
    }

    public boolean containsItem(int itemId) {
        return used.getId() == itemId || with.getId() == itemId;
    }

}
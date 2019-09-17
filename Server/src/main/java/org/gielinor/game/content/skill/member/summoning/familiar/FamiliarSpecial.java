package org.gielinor.game.content.skill.member.summoning.familiar;

import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.item.Item;

/**
 * A class which represents a familiar special.
 *
 * @author Aero
 */
public class FamiliarSpecial {

    /**
     * The node.
     */
    private Node node;

    /**
     * The interface id.
     */
    private int interfaceId;

    /**
     * The component.
     */
    private int component;

    /**
     * The item.
     */
    private Item item;

    /**
     * Constructs a new {@code FamiliarSpecial} {@code Object}.
     *
     * @param node The node.
     */
    public FamiliarSpecial(Node node) {
        this(node, -1, -1, null);
    }

    /**
     * Constructs a new {@code FamiliarSpecial} {@code Object}.
     *
     * @param node        The node.
     * @param interfaceId The interface id.
     * @param component   The component.
     * @param item        The item.
     */
    public FamiliarSpecial(Node node, int interfaceId, int component, Item item) {
        this.node = node;
        this.interfaceId = interfaceId;
        this.component = component;
        this.item = item;
    }

    /**
     * Gets the node.
     *
     * @return The node.
     */
    public Node getNode() {
        return node;
    }

    /**
     * Sets the node.
     *
     * @param node The node to set.
     */
    public void setNode(Node node) {
        this.node = node;
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
     * Sets the interfaceId.
     *
     * @param interfaceId The interfaceId to set.
     */
    public void setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    /**
     * Gets the component.
     *
     * @return The component.
     */
    public int getComponent() {
        return component;
    }

    /**
     * Sets the component.
     *
     * @param component The component to set.
     */
    public void setComponent(int component) {
        this.component = component;
    }

    /**
     * Gets the item.
     *
     * @return The item.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets the item.
     *
     * @param item The item to set.
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Gets the target.
     *
     * @return the target.
     */
    public Entity getTarget() {
        return (Entity) node;
    }

}
package org.gielinor.game.node;

import org.gielinor.game.interaction.DestinationFlag;
import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents a node which is anything that is interactable.
 *
 * @author Emperor
 */
public abstract class Node {

    /**
     * The name of the node;
     */
    protected transient String name;

    /**
     * The location.
     */
    protected transient Location location;

    /**
     * The index of the node.
     */
    protected transient int index;

    /**
     * The node's direction.
     */
    protected transient Direction direction;

    /**
     * The node's size.
     */
    protected transient int size = 1;

    /**
     * If the node is active.
     */
    protected transient boolean active = true;

    /**
     * The interaction instance.
     */
    protected transient Interaction interaction;

    /**
     * The destination flag.
     */
    protected transient DestinationFlag destinationFlag;

    /**
     * If the node is renderable.
     */
    protected transient boolean renderable = true;

    /**
     * Constructs a new {@code Node} {@code Object}.
     *
     * @param name     The name.
     * @param location The location.
     */
    public Node(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    /**
     * Casts the npc to a player.
     *
     * @return the npc.
     */
    public NPC asNpc() {
        return (NPC) this;
    }

    /**
     * Casts the player.
     *
     * @return the player.
     */
    public Player asPlayer() {
        return (Player) this;
    }

    /**
     * Casts the game object.
     *
     * @return the object.
     */
    public GameObject asObject() {
        return (GameObject) this;
    }

    /**
     * Casts the item.
     *
     * @return the item.
     */
    public Item asItem() {
        return (Item) this;
    }

    /**
     * Gets the node id.
     *
     * @return the id.
     */
    public int getId() {
        return -1;
    }

    /**
     * Gets the center location.
     *
     * @return The center location.
     */
    public Location getCenterLocation() {
        int offset = size >> 1;
        return location.transform(offset, offset, 0);
    }

    /**
     * Gets the name of this node.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get a formated username.
     *
     * @return The username.
     */
    public String getUsername() {
        return TextUtils.formatDisplayName(name);
    }

    /**
     * Gets the index.
     *
     * @return The index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index.
     *
     * @param index The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets the location.
     *
     * @return The location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location The location to set.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the direction.
     *
     * @return The direction.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction.
     *
     * @param direction The direction to set.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Rotates this NPC.
     */
    public Direction rotate() {
        switch (direction.toInteger()) {
            case 0:
                return Direction.get(1);
            case 1:
                return Direction.get(2);
            case 2:
                return Direction.get(3);
            case 3:
                return Direction.get(4);
            case 4:
                return Direction.get(5);
            case 5:
                return Direction.get(6);
            case 6:
                return Direction.get(7);
            case 7:
                return Direction.get(0);
        }
        return Direction.SOUTH;
    }

    /**
     * Gets the size.
     *
     * @return The size.
     */
    public int size() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size The size to set.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets the active.
     *
     * @return The active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the interaction.
     *
     * @return The interaction.
     */
    public Interaction getInteraction() {
        return interaction;
    }

    /**
     * Sets the interaction.
     *
     * @param interaction The interaction to set.
     */
    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    /**
     * Gets the destinationFlag.
     *
     * @return The destinationFlag.
     */
    public DestinationFlag getDestinationFlag() {
        return destinationFlag;
    }

    /**
     * Sets the destinationFlag.
     *
     * @param destinationFlag The destinationFlag to set.
     */
    public void setDestinationFlag(DestinationFlag destinationFlag) {
        this.destinationFlag = destinationFlag;
    }

    /**
     * Gets the renderable.
     *
     * @return The renderable.
     */
    public boolean isRenderable() {
        return renderable;
    }

    /**
     * Sets the renderable.
     *
     * @param renderable The renderable to set.
     */
    public void setRenderable(boolean renderable) {
        this.renderable = renderable;
    }

}

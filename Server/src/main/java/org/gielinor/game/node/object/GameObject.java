package org.gielinor.game.node.object;

import org.gielinor.cache.def.impl.ConfigFileDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.DestinationFlag;
import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.GameAttributes;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents a game object.
 *
 * @author Emperor
 */
public class GameObject extends Node {

    /**
     * The object id.
     */
    private final int id;

    /**
     * Object's type.
     */
    private final int type;

    /**
     * The rotation.
     */
    private int rotation;

    /**
     * The object's definition.
     */
    private final ObjectDefinition definition;

    /**
     * The restore pulse.
     */
    private Pulse restorePulse;

    /**
     * The destruction pulse.
     */
    private Pulse destructionPulse;

    /**
     * The charge of this object.
     */
    private int charge = 1000;

    /**
     * The entity's attributes.
     */
    private final GameAttributes attributes = new GameAttributes();

    /**
     * The child objects.
     */
    private final GameObject[] children;

    /**
     * The game object wrapper (used for object configurations).
     */
    private GameObject wrapper;

    /**
     * Whether or not this {@link GameObject} is hidden.
     */
    private boolean hidden;

    /**
     * Constructs a new game object.
     *
     * @param id The object id.
     * @param x  The object x-coordinate.
     * @param y  The object y-coordinate.
     * @param z  The object z-coordinate.
     */
    public GameObject(int id, int x, int y, int z) {
        this(id, Location.create(x, y, z), 10, 0);
    }

    /**
     * Constructs a new game object.
     *
     * @param id       The object id.
     * @param location The object's location.
     */
    public GameObject(int id, Location location) {
        this(id, location, 10, 0);
    }

    /**
     * Constructs a new game object.
     *
     * @param id       The object id.
     * @param location The object's location.
     * @param rotation The object's rotation.
     */
    public GameObject(int id, Location location, int rotation) {
        this(id, location, 10, rotation);
    }

    /**
     * Constructs a new {@code GameObject} {@code Object}.
     *
     * @param id       The object id.
     * @param x        The x-coordinate.
     * @param y        The y-coordinate.
     * @param z        The z-coordinate.
     * @param type     The object type.
     * @param rotation The rotation.
     */
    public GameObject(int id, int x, int y, int z, int type, int rotation) {
        this(id, Location.create(x, y, z), type, rotation);
    }

    /**
     * Constructs a new {@code GameObject} {@code Object}.
     *
     * @param id       The object id.
     * @param type     The object type.
     * @param rotation The rotation.
     */
    public GameObject(int id, int type, int rotation) {
        this(id, Location.create(0, 0, 0), type, rotation);
    }

    /**
     * Constructs a new {@code GameObject} {@code Object}.
     *
     * @param id       The object id.
     * @param location The location.
     * @param type     The object type.
     * @param rotation The rotation.
     */
    public GameObject(int id, Location location, int type, int rotation) {
        super(ObjectDefinition.forId(id).getName(), location);
        if (rotation < 0) {
            rotation += 4;
        }
        if (id < 1) {
            type = 22;
        }
        super.setDestinationFlag(destinationFlag = DestinationFlag.OBJECT);
        super.setDirection(direction = Direction.get(rotation));
        super.setInteraction(interaction = new Interaction(this));
        this.rotation = rotation;
        this.id = id;
        this.location = location;
        this.type = type;
        this.definition = ObjectDefinition.forId(id);
        super.setSize(size = definition.sizeX);
        interaction.setDefault();
        if (definition.childrenIds != null && definition.childrenIds.length > 0) {
            this.children = new GameObject[definition.childrenIds.length];
            for (int i = 0; i < children.length; i++) {
                children[i] = transform(definition.childrenIds[i]);
                children[i].wrapper = this;
            }
        } else {
            children = null;
        }
    }


    /**
     * Gets the current x-size.
     *
     * @return The current size.
     */
    public int getSizeX() {
        if (direction.toInteger() % 2 != 0) {
            return definition.sizeY;
        }
        return definition.sizeX;
    }

    /**
     * Gets the current y-size.
     *
     * @return The current size.
     */
    public int getSizeY() {
        if (direction.toInteger() % 2 != 0) {
            return definition.sizeX;
        }
        return definition.sizeY;
    }

    @Override
    public void setActive(boolean active) {
        if (super.isActive() && !active && destructionPulse != null) {
            destructionPulse.pulse();
        }
        super.setActive(active);
    }

    /**
     * Gets the child object shown for the current player.
     *
     * @param player The player.
     * @return The child object.
     */
    public GameObject getChild(Player player) {
        if (children != null) {
            ObjectDefinition def = definition.getChildObject(player);
            for (GameObject child : children) {
                if (child.getId() == def.getId()) {
                    return child;
                }
            }
        }
        return this;
    }

    /**
     * Sets the child object index.
     *
     * @param player The player.
     * @param index  The child object.
     */
    public void setChildIndex(Player player, int index) {
        ObjectDefinition def = getDefinition();
        if (children == null && wrapper != null) {
            def = wrapper.getDefinition();
        }
        if (def.getConfigFileId() > -1) {
            ConfigFileDefinition config = def.getConfigFile();
            if (config != null) {
                int value = player.getConfigManager().get(config.getConfigId());
                value |= index << config.getBitShift();
                player.getConfigManager().set(config.getConfigId(), value);
            }
        } else if (def.getConfigId() > -1) {
            player.getConfigManager().set(def.getConfigId(), index);
        }
    }

    /**
     * Gets a transformed object of this object.
     *
     * @param id The new object id.
     * @return The constructed game object.
     */
    public GameObject transform(int id) {
        return transform(id, rotation);
    }

    /**
     * Gets a transformed object of this object.
     *
     * @param id       The new object id.
     * @param rotation The new rotation.
     * @return The constructed game object.
     */
    public GameObject transform(int id, int rotation) {
        return new GameObject(id, location, type, rotation);
    }

    /**
     * Gets a transformed object of this object.
     *
     * @param id       The new object id.
     * @param rotation The new rotation.
     * @param location The new location.
     * @return The constructed game object.
     */
    public GameObject transform(int id, int rotation, Location location) {
        return new GameObject(id, location, type, rotation);
    }

    /**
     * Gets a transformed object of this object.
     *
     * @param id       The new object id.
     * @param rotation The new rotation.
     * @param type     The object type.
     * @return The constructed game object.
     */
    public GameObject transform(int id, int rotation, int type) {
        return new GameObject(id, location, type, rotation);
    }

    /**
     * If the object is permanent.
     *
     * @return <code>True</code> if so.
     */
    public boolean isPermanent() {
        return true;
    }

    /**
     * Gets this game object as Constructed object.
     *
     * @return The {@link Constructed} object.
     */
    public Constructed asConstructed() {
        return new Constructed(id, location, type, rotation);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @return the rotation
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * @return the location
     */
    @Override
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the definition.
     *
     * @return The definition.
     */
    public ObjectDefinition getDefinition() {
        return definition;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GameObject)) {
            return false;
        }
        GameObject other = (GameObject) obj;
        return other.id == id && other.location.equals(location) && rotation == other.rotation && other.type == type;
    }

    /**
     * Hides the object.
     */
    public void hide() {
        if (!hidden) {
            hidden = true;
        }
    }

    /**
     * Reveals the object if it is hidden.
     */
    public void reveal() {
        if (hidden) {
            hidden = false;
        }
    }

    /**
     * Checks if this gameobject is hidden.
     *
     * @return <code>True</code> if so.
     */
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public String toString() {
        return "[GameObject " + id + ", " + location + ", type=" + type + ", rot=" + rotation + "]";
    }

    /**
     * Gets the restorePulse.
     *
     * @return The restorePulse.
     */
    public Pulse getRestorePulse() {
        return restorePulse;
    }

    /**
     * Sets the restorePulse.
     *
     * @param restorePulse The restorePulse to set.
     */
    public void setRestorePulse(Pulse restorePulse) {
        this.restorePulse = restorePulse;
    }

    /**
     * Gets the charge.
     *
     * @return The charge.
     */
    public int getCharge() {
        return charge;
    }

    /**
     * Sets the charge.
     *
     * @param charge The charge to set.
     */
    public void setCharge(int charge) {
        this.charge = charge;
    }

    /**
     * Gets the destructionPulse.
     *
     * @return The destructionPulse.
     */
    public Pulse getDestructionPulse() {
        return destructionPulse;
    }

    /**
     * Sets the destructionPulse.
     *
     * @param destructionPulse The destructionPulse to set.
     */
    public void setDestructionPulse(Pulse destructionPulse) {
        this.destructionPulse = destructionPulse;
    }

    /**
     * @return the attributes.
     */
    public GameAttributes getAttributes() {
        return attributes;
    }

    /**
     * Gets the children.
     *
     * @return The children.
     */
    public GameObject[] getChildren() {
        return children;
    }

    /**
     * Gets the wrapper.
     *
     * @return The wrapper.
     */
    public GameObject getWrapper() {
        if (wrapper == null) {
            return this;
        }
        return wrapper;
    }

    /**
     * Sets the wrapper.
     *
     * @param wrapper The wrapper to set.
     */
    public void setWrapper(GameObject wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * Rotates this object left.
     */
    public int rotateLeft() {
        if (rotation == 0) {
            rotation = 6;
            return rotation;
        }
        rotation -= 1;
        return rotation;
    }

    /**
     * Rotates this object right.
     */
    public int rotateRight() {
        if (rotation == 6) {
            rotation = 0;
            return rotation;
        }
        rotation += 1;
        return rotation;
    }

    public void remove() {
    }
}
package org.gielinor.game.content.global;

import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents an equipment set.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum EquipmentSet {

    /**
     * Lumberjack set.
     */
    LUMBERJACK(new Item(10941), new Item(10939), new Item(10940), new Item(10933)),
    /**
     * Graceful set (inventory ids).
     */
    GRACEFUL_INVENTORY(new Item(14730), new Item(14732), new Item(14734), new Item(14736), new Item(14738), new Item(14740)),
    /**
     * Graceful set (worn ids).
     */
    GRACEFUL(new Item(14731), new Item(14733), new Item(14735), new Item(14737), new Item(14739), new Item(14741)),;
    /**
     * The items.
     */
    private final Item[] set;

    /**
     * Constructs a new {@code ArmourSet} {@Code Object}
     *
     * @param set The items needed to have an armour set.
     */
    private EquipmentSet(Item... set) {
        this.set = set;
    }

    /**
     * Handles the effect of the set.
     *
     * @param entity The entity using the effect.
     * @param nodes  The additional nodes to use against / with.
     * @return <code>True</code> if the effect occurred.
     */
    public boolean effect(Entity entity, Node... nodes) {
        return false;
    }

    /**
     * Checks if the entity is wearing this armour set.
     *
     * @param e The entity.
     * @return {@code True} if so.
     */
    public boolean isUsing(Entity e) {
        if (!(e instanceof Player)) {
            return false;
        }
        Player player = (Player) e;
        for (Item item : set) {
            if (!player.getEquipment().containsOneItem(item.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the set items.
     *
     * @return The set.
     */
    public Item[] getSet() {
        return set;
    }

}
package org.gielinor.game.node.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents a plugin for an item.
 *
 * @author Vexia
 */
public abstract class ItemPlugin implements Plugin<Object> {

    /**
     * The drop type identifier.
     */
    protected static final int DROP = 1;

    /**
     * Constructs a new {@Code ItemPlugin} {@Code Object}
     */
    public ItemPlugin() {
        /**
         * empty.
         */
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return this;
    }

    /**
     * Registers items to this plugin.
     *
     * @param ids the ids.
     */
    public void register(int... ids) {
        for (int id : ids) {
            ItemDefinition.forId(id).setItemPlugin(this);
        }
    }

    /**
     * Called when the item is removed from the player.
     *
     * @param player the player.
     * @param item   the item.
     * @param type   the type. (1=drop)
     */
    public void remove(Player player, Item item, int type) {

    }

    /**
     * Checks if the item can be picked up.
     *
     * @param player the player.
     * @param item   the item.
     * @param type   the pickup type (1=ground, 2=telegab)
     * @return <code>True</code> if so.
     */
    public boolean canPickUp(Player player, GroundItem item, int type) {
        return true;
    }

    /**
     * Checks if the item can be made as a drop.
     *
     * @param item   The item.
     * @param player The player.
     * @param npc    The npc.
     * @param l      The location.
     * @return
     */
    public boolean createDrop(Item item, Player player, NPC npc, Location l) {
        return true;
    }

    /**
     * Changes an item if needed.
     *
     * @param item the item.
     * @param npc  the npc.
     * @return the item.
     */
    public Item getItem(Item item, NPC npc) {
        return item;
    }

    /**
     * Gets the death item.
     *
     * @param item the item.
     * @return the item.
     */
    public Item getDeathItem(Item item) {
        return item;
    }
}

package org.gielinor.game.node.item;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.chunk.ItemUpdateFlag;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.BuildItemContext;
import org.gielinor.net.packet.out.UpdateGroundItemAmount;
import org.gielinor.parser.item.GroundItemParser.GroundSpawn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Handles ground items.
 *
 * @author Emperor
 */
public final class GroundItemManager {

    /**
     * The list of ground items.
     */
    private static final List<GroundItem> GROUND_ITEMS = new ArrayList<>();

    /**
     * Creates a ground item.
     *
     * @param item     The ground item to create.
     * @param location The location to set the ground item on.
     */
    public static GroundItem create(Item item, Location location) {
        return create(new GroundItem(item, location, null));
    }

    /**
     * Creates a ground item.
     *
     * @param item   the item.
     * @param player the player.
     * @return
     */
    public static GroundItem create(Item item, final Player player) {
        return create(new GroundItem(item, player.getLocation(), player));
    }

    /**
     * Creates a ground item.
     *
     * @param item     The ground item to create.
     * @param location The location to set the ground item on.
     * @param player   The player creating the ground item.
     */
    public static GroundItem create(Item item, Location location, Player player) {
        return create(new GroundItem(item, location, player));
    }

    /**
     * Creates a ground item.
     *
     * @param item     The ground item to create.
     * @param location The location to set the ground item on.
     * @param player   The player creating the ground item.
     */
    public static void create(Item[] item, Location location, Player player) {
        for (Item anItem : item) {
            create(new GroundItem(anItem, location, player));
        }
    }

    /**
     * Creates a ground item.
     *
     * @param item The ground item to create.
     * @return The ground item.
     */
    public static GroundItem create(GroundItem item) {
        if (!item.getDefinition().isTradeable() ||
            (item.getDropper() != null &&
                (item.getDropper().getDetails().getRights().isAdministrator()
                    || item.getDropper().isIronman()))) {
            item.setRemainPrivate(true);
        }
        if (item.getDropper() != null && item.hasItemPlugin()) {
            item.getPlugin().remove(item.getDropper(), item, ItemPlugin.DROP);
        }
        item.setRemoved(false);
        RegionManager.getRegionPlane(item.getLocation()).add(item);
        if (GROUND_ITEMS.add(item)) {
            return item;
        }
        return null;
    }

    /**
     * Destroys the ground item.
     *
     * @param item The ground item.
     */
    public static GroundItem destroy(GroundItem item) {
        if (item == null) {
            return null;
        }
        GROUND_ITEMS.remove(item);
        RegionManager.getRegionPlane(item.getLocation()).remove(item);
        if (item.isAutoSpawn()) {
            ((GroundSpawn) item).respawn();
        }
        return item;
    }

    /**
     * Destroys all current ground items.
     */
    public static void destroyAll() {
        for (GroundItem item : GROUND_ITEMS) {
            RegionManager.getRegionPlane(item.getLocation()).remove(item);
            if (item.isAutoSpawn()) {
                ((GroundSpawn) item).respawn();
            }
        }
        GROUND_ITEMS.clear();
    }

    /**
     * Gets a ground item.
     *
     * @param itemId   The item id.
     * @param location The location.
     * @param player   The player.
     * @return The ground item, or {@code null} if the ground item wasn't found.
     */
    public static GroundItem get(int itemId, Location location, Player player) {
        return RegionManager.getRegionPlane(location).getItem(itemId, location, player);
    }

    /**
     * Increases the amount of a ground item on the floor, or creates a new ground item.
     *
     * @param item     The item to drop.
     * @param location The drop location.
     * @param p        The player.
     */
    public static void increase(Item item, Location location, Player p) {
        GroundItem g = get(item.getId(), location, p);
        if (g == null || !g.droppedBy(p) || !g.isPrivate() || g.isRemoved()) {
            create(item, location, p);
            return;
        }
        int oldAmount = g.getCount();
        g.setCount(oldAmount + item.getCount());
        PacketRepository.send(UpdateGroundItemAmount.class, new BuildItemContext(p, g, oldAmount));
    }

    /**
     * Handles the ground items.
     */
    public static void pulse() {
        for (Iterator<GroundItem> it = GROUND_ITEMS.iterator(); it.hasNext(); ) {
            GroundItem item = it.next();
            if (item.isAutoSpawn()) {
                continue;
            }
            if (!item.isActive()) {
                it.remove();
                if (!item.isRemoved()) {
                    RegionManager.getRegionPlane(item.getLocation()).remove(item);
                }
            } else if (!item.isRemainPrivate() && item.getDecayTime() - World.getTicks() == 100) {
                RegionManager.getRegionChunk(item.getLocation()).flag(new ItemUpdateFlag(item, ItemUpdateFlag.CONSTRUCT_TYPE));
            }
        }
    }

    /**
     * Gets the list of ground items.
     *
     * @return The ground items.
     */
    public static List<GroundItem> getItems() {
        return GROUND_ITEMS;
    }
}

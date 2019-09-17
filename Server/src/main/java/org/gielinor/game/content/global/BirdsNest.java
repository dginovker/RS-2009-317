package org.gielinor.game.content.global;


import org.gielinor.game.node.entity.npc.drop.NPCDropTables;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents a birds nest collected from the woodcutting skill.
 *
 * @author Vexia
 */
public enum BirdsNest {

    /**
     * A bird's nest with a seed chance.
     */
    SEED(new ChanceItem(5073, 1, 65), new ChanceItem(5312, 1, NPCDropTables.DROP_RATES[0]), new ChanceItem(5283, 1, NPCDropTables.DROP_RATES[0]), new ChanceItem(5284, 1, NPCDropTables.DROP_RATES[0]), new ChanceItem(5285, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(5313, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(5286, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(5314, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(5287, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(5288, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(5289, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(5290, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(5315, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(5316, 1, NPCDropTables.DROP_RATES[3]), new ChanceItem(5317, 1, NPCDropTables.DROP_RATES[3])),

    /**
     * A bird's nest with a ring chance.
     */
    RING(new ChanceItem(5074, 1, 30), new ChanceItem(1635, 1, NPCDropTables.DROP_RATES[0]), new ChanceItem(1637, 1, NPCDropTables.DROP_RATES[0]), new ChanceItem(1639, 1, NPCDropTables.DROP_RATES[1]), new ChanceItem(1641, 1, NPCDropTables.DROP_RATES[2]), new ChanceItem(1643, 1, NPCDropTables.DROP_RATES[3])),

    /**
     * A bird's nest with a red egg chance.
     */
    RED(new ChanceItem(5070, 1, 5), new ChanceItem(5076)),

    /**
     * A bird's nest with a green egg chance.
     */
    GREEN(new ChanceItem(5071, 1, 5), new ChanceItem(5078)),

    /**
     * A bird's nest with a blue egg chance.
     */
    BLUE(new ChanceItem(5072, 1, 5), new ChanceItem(5077)),

    /**
     * A bird's nest with a raven chance.
     */
    RAVEN(new ChanceItem(11966, 1, 5), new ChanceItem(11964));

    /**
     * The random nest items.
     */
    private static final ChanceItem[] NESTS = new ChanceItem[5];

    /**
     * The empty birds nest item.
     */
    private static final Item EMPTY = new Item(5075);

    /**
     * The birds nest item.
     */
    private final ChanceItem nest;

    /**
     * The loot item from the nest.
     */
    private final ChanceItem[] loot;

    /**
     * Constructs a new {@code BirdNest} {@code Object}.
     *
     * @param nest the nest.
     * @param loot the loot.
     */
    private BirdsNest(final ChanceItem nest, final ChanceItem... loot) {
        this.nest = nest;
        this.loot = loot;
    }

    /**
     * Drops a birds nest.
     *
     * @param player the player.
     */
    public static void drop(final Player player) {
        final BirdsNest nest = getRandomNest();
        player.getAudioManager().send(1997);
        GroundItemManager.create(nest.getNest(), player);
        player.getActionSender().sendMessage("<col=FF0000>A bird's nest falls out of the tree.");
    }

    /**
     * Searches a bird nest.
     *
     * @param player the player.
     * @param item   the item searched.
     */
    public void search(final Player player, Item item) {
        if (player.getInventory().freeSlots() < 1) {
            player.getActionSender().sendMessage("You don't have enough inventory space.");
            return;
        }
        final ChanceItem loot = ordinal() > 1 ? getLoot()[0] : RandomUtil.getChanceItem(getLoot());
        final String name = loot.getName().toLowerCase();
        final String input = (TextUtils.isPlusN(name) ? "an" : "a") + " " + name;
        player.lock(1);
        player.getInventory().add(loot);
        player.getInventory().replace(EMPTY, item.getSlot());
        player.getActionSender().sendMessage("You take " + input + " out of the bird's nest.");
    }

    /**
     * Gets the random nest.
     *
     * @return the nest.
     */
    public static BirdsNest getRandomNest() {
        ChanceItem item = RandomUtil.getChanceItem(NESTS);
        for (BirdsNest n : BirdsNest.values()) {
            if (n.getNest() == item) {
                return n;
            }
        }
        return null;
    }

    /**
     * Gets the nest by the id.
     *
     * @param nest the nest.
     * @return the nest.
     */
    public static BirdsNest forNest(final Item nest) {
        for (BirdsNest n : values()) {
            if (n.getNest().getId() == nest.getId()) {
                return n;
            }
        }
        return null;
    }

    /**
     * Gets the nest.
     *
     * @return The nest.
     */
    public ChanceItem getNest() {
        return nest;
    }

    /**
     * Gets the loot.
     *
     * @return The loot.
     */
    public ChanceItem[] getLoot() {
        return loot;
    }

    /**
     * static-block to add nests.
     */
    static {
        for (int i = 0; i < NESTS.length; i++) {
            NESTS[i] = values()[i].getNest();
        }
    }
}
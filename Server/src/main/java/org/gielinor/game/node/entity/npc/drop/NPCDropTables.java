package org.gielinor.game.node.entity.npc.drop;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueLevel;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueScrollPlugin;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.config.ServerVar;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.utilities.misc.RandomUtil;
import plugin.activity.gwd.GodWarsMinionNPC;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds and handles the NPC drop tables.
 *
 * @author Emperor
 */
public final class NPCDropTables {

    /**
     * The total drops.
     */
    private static int count;

    /**
     * The drop rates (0=common, 1=uncommon, 2=rare, 3=very rare).
     */
    public static final int[] DROP_RATES = { 800, 160, 20, 12 };
    /**
     * The default drop table (holding the 100% drops).
     */
    private final List<ChanceItem> defaultTable = new ArrayList<>();

    /**
     * The charms drop table (holding the charm drops).
     */
    private final List<ChanceItem> charmTable = new ArrayList<>();

    /**
     * The main drop table (holding the main drops).
     */
    private final List<ChanceItem> mainTable = new ArrayList<>();

    /**
     * The NPC definitions.
     */
    private final NPCDefinition def;

    /**
     * The main drop table size.
     */
    private int mainTableSize;

    /**
     * Represents drops that will send a server-wide announcement.
     */
    private final static int[] NOTABLE_DROPS = new int[]{
        11335, // Dragon full helm
        11286, // Visage
        11702, 11704, 11706, 11708, // Hilts
        11724, 11726, 11728, // Bandos armour
        11718, 11720, 11722, // Armadyl armour
        10888, // Barrelchest anchor
        13746, 13750, 13752 // Sigils
    };

    /**
     * Constructs a new {@code NPCDropTables} {@code Object}.
     *
     * @param def The NPC definitions.
     */
    public NPCDropTables(NPCDefinition def) {
        this.def = def;
    }

    /**
     * Handles the dropping.
     *
     * @param npc    The NPC dropping the loot.
     * @param looter The entity gaining the loot.
     */
    public void drop(NPC npc, Entity looter) {
        drop(npc, looter, npc.getLocation());
    }

    /**
     * Handles the dropping.
     *
     * @param npc    The NPC dropping the loot.
     * @param looter The entity gaining the loot.
     * @param location The location to drop the drops.
     */
    public void drop(NPC npc, Entity looter, Location location) {
        if (npc.getDefinition().getName().toLowerCase().contains("impling")) {
            return;
        }

        Player p = looter instanceof Player ? (Player) looter : null;
        for (ChanceItem item : defaultTable) {
            int amount = RandomUtil.random(item.getMinimumAmount(), item.getMaximumAmount() + 1);
            item.setCount(amount);
            if (npc instanceof GodWarsMinionNPC && new SecureRandom().nextInt(2) == 1) {
                if (!item.getName().contains("bone") && !item.getName().contains("ash")) {
                    return;
                }
            }
            createDrop(item, p, location);
        }
        if (!charmTable.isEmpty()) {
            int slot = RandomUtil.random(1000);
            for (ChanceItem item : charmTable) {
                if ((item.getTableSlot() & 0xFFFF) <= slot && (item.getTableSlot() >> 16) > slot) {
                    int amount = RandomUtil.random(item.getMinimumAmount(), item.getMaximumAmount() + 1);
                    item.setCount(amount);
                    if (item.getCount() == 0) {
                        item.setCount(1);
                    }
                    if (npc instanceof GodWarsMinionNPC && new SecureRandom().nextInt(2) == 1) {
                        if (!item.getName().contains("bone") && !item.getName().contains("ash")) {
                            return;
                        }
                    }
                    createDrop(item, p, location);
                    break;
                }
            }
        }

        if (!mainTable.isEmpty()) {
            boolean hasWealthRing = p != null && p.getEquipment().getNew(Equipment.SLOT_RING).getId() == 2572;
            int slot = RandomUtil.random(mainTableSize);
            boolean canDrop = true;
            for (ChanceItem item : mainTable) {
                if (item.getName() != null && item.getName().equalsIgnoreCase("clue scroll")) {
                    if (p != null) {
                        if (p.getTreasureTrailManager().hasClue()) {
                            canDrop = false;
                            break;
                        }
                        if (npc.getId() == 49 && RandomUtil.random(64) != 1) {
                            canDrop = false;
                            break;
                        }
                    }
                }
                if (!canDrop) {
                    continue;
                }
                boolean isRDTSlot = item.getId() == RareDropTable.SLOT_ITEM_ID;
                if (((item.getTableSlot() & 0xFFFF) <= slot && (item.getTableSlot() >> 16) > slot) ||
                    (isRDTSlot && hasWealthRing && RandomUtil.random(8) == 1)) {
                    if (isRDTSlot) {
                        item = RareDropTable.retrieve();
                    }
                    if (item != null && p != null) {
                        int amount = RandomUtil.random(item.getMinimumAmount(), item.getMaximumAmount() + 1);
                        item.setCount((item.getMinimumAmount() == item.getMaximumAmount()) ? item.getMaximumAmount() : amount);
                        if (item.getCount() == 0) {
                            item.setCount(1);
                        }
                        createDrop(item, p, location);
                        if (isRDTSlot) {
                            p.getActionSender().sendMessage("<col=ff7000><shad=000>Your ring of wealth shines more brightly!");
                            Graphics.send(Graphics.create(671), location);
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * Creates a dropped item.
     *
     * @param item     The item to drop.
     * @param player   The player getting the loot (or null).
     * @param npc      the npc.
     * @param location The location of the NPC dropping the loot.
     */
    public void createDrop(Item item, Player player, NPC npc, Location location) {
        if (ServerVar.fetch("npc_drops_disabled", 0) == 1) {
            return;
        }
        if (item == null || item.getId() == 0 || location == null || ItemDefinition.forId(item.getId()).getName().equals("null")) { // No charm drop
            return;
        }
        if (item.hasItemPlugin() && player != null) {
            if (!item.getPlugin().createDrop(item, player, npc, location)) {
                return;
            }
            item = item.getPlugin().getItem(item, npc);
        }
        if (!(ItemDefinition.forId(item.getId()).isStackable() || !item.getDefinition().isStackable()) && item.getCount() > 1) {
            for (int i = 0; i < item.getCount(); i++) {
                GroundItemManager.create(new Item(item.getId()), location, player);
            }
            return;
        }
        if (player != null) {
            GroundItemManager.create(item, location, player);
        } else {
            GroundItemManager.create(item, location);
        }
        assert player != null;
        player.debug("Drop: [id=" + item.getId() + ", amount=" + item.getCount() + "]");
    }

    /**
     * Creates a dropped item.
     *
     * @param item   The item to drop.
     * @param player The player getting the loot (or null).
     * @param l      The location of the NPC dropping the loot.
     */
    private void createDrop(Item item, Player player, Location l) {
        if (ServerVar.fetch("npc_drops_disabled", 0) == 1) {
            return;
        }
        if (item == null || item.getId() == 0 || l == null) { //No drop
            return;
        }
        if (item.getDefinition().getName().equalsIgnoreCase("casket") ||
            item.getId() == 2732 || item.getId() == 2714) {
            return;
        }
        if (item.getName().toLowerCase().contains("charm") && item.getDefinition().isStackable()) {
            item.setCount(RandomUtil.random(6) + 1);
        }
        if (item.getName().toLowerCase().contains("essence") && item.getDefinition().isStackable()) {
            item.setCount(RandomUtil.random(60) + 1);
        }
        if (player != null && item.getName().toLowerCase().contains("clue scroll")) {
            if (player.getTreasureTrailManager().hasClue()) {
                return;
            }
            final ClueScrollPlugin clueScrollPlugin = ClueScrollPlugin.getClueScrolls().get(item.getId());
            if (clueScrollPlugin == null) {
                item = ClueScrollPlugin.getClue(ClueLevel.EASY);
            }
        }
        if (item.getId() == Item.COINS) {
            item.setCount(item.getCount() * Constants.COIN_DROP_MULTIPLIER);
        }
        notableDrop(item, player, l);
        if (item.getDefinition() != null && player != null) {
            if (item.getDefinition().getName().equals("Coins")) {
                if (Perk.COIN_COLLECTOR_II.enabled(player) && player.getBank().hasRoomFor(item)) {
                    String has = item.getCount() > 1 ? "s have" : " has";
                    player.getActionSender().sendMessage(item.getCount() + " coin" + has + " been deposited automatically to your bank.", 1);
                    player.getBank().add(item);
                    return;
                } else if (Perk.COIN_COLLECTOR.enabled(player) && player.getInventory().hasRoomFor(item)) {
                    String has = item.getCount() > 1 ? "s have" : " has";
                    player.getActionSender().sendMessage(item.getCount() + " coin" + has + " been collected automatically to your inventory.", 1);
                    player.getInventory().add(item);
                    return;
                }
            } else if (item.getDefinition().getName().equals("Bones")) {
                if (Perk.PRAY_FOR_ME.enabled(player) && player.getBank().hasRoomFor(item)) {
                    String has = item.getCount() > 1 ? "s have" : " has";
                    player.getActionSender().sendMessage(item.getCount() + " " + item.getName() + has + " been deposited automatically to your bank.", 1);
                    player.getBank().add(item);
                    return;
                }
            } else if (item.getDefinition().getName().toLowerCase().contains("bones")) {
                if (Perk.PRAY_FOR_ME_II.enabled(player) && player.getBank().hasRoomFor(item)) {
                    String has = item.getCount() > 1 ? "s have" : " has";
                    player.getActionSender().sendMessage(item.getCount() + " " + item.getName() + has + " been deposited automatically to your bank.", 1);
                    player.getBank().add(item);
                    return;
                }
            } else if ((item.getDefinition().getName().contains("charm"))) {
                if (Perk.CHARM_COLLECTOR.enabled(player) && player.getInventory().hasRoomFor(item)) {
                    String has = item.getCount() > 1 ? "s have" : " has";
                    player.getActionSender().sendMessage(item.getCount() + " charm" + has + " been collected automatically to your inventory.");
                    player.getInventory().add(item);
                    return;
                }
            }
        }
        if (!item.getDefinition().isStackable() && item.getCount() > 1) {
            for (int i = 0; i < item.getCount(); i++) {
                GroundItemManager.create(new Item(item.getId()), l, player);
            }
            return;
        }
        if (item.getCount() == 0) {
            item.setCount(1);
        }
        if (player != null) {
            GroundItemManager.create(item, l, player);
            return;
        }
        GroundItemManager.create(item, l);
    }

    public static void notableDrop(Item item, Player player, Location l) {
        for (int notable : NOTABLE_DROPS) {
            if (item.getId() == notable) {
                player.sendGlobalNewsMessage("ff8c38", " has just obtained the drop: " + item.getDefinition().getName(), 5);
                player.getActionSender().sendPositionedGraphic(671, 0, 0, l);
                if (player.getRights().isAdministrator()) {
                    player.getActionSender().sendConsoleMessage("Rare drop location: " + l.toString());
                }
            }
        }
    }

    /**
     * Prepares the tables for use in the game.
     */
    public void prepare() {
        int slot = 0;
        int index = -1;
        int amount = 0;
        for (int i = 0; i < charmTable.size(); i++) {
            ChanceItem chanceItem = charmTable.get(i);
            int rate = (int) (chanceItem.getChanceRate() * 1000);
            if (rate > amount) {
                index = i;
                amount = rate;
            }
            slot += rate;
        }
        amount = 1000 - slot;
        slot = 0;
        for (ChanceItem chanceItem : charmTable) {
            int rate = (int) (chanceItem.getChanceRate() * 1000.0);
            if (index-- == 0) {
                rate += amount;
            }
            chanceItem.setTableSlot(slot | (int) (slot += rate) << 16);
        }
        if (!charmTable.isEmpty() && slot != 1000) {
            if (slot == 0) {
                charmTable.clear();
            }
        }
        // Prepare the main drop table.
        slot = 0;
        if (!mainTable.isEmpty()) {
            double ratio = getStabilizerRatio();
            int commonAmount = 0;
            for (ChanceItem item : mainTable) {
                if (item.getDropFrequency() == DropFrequency.COMMON) {
                    commonAmount++;
                    continue;
                }
                double rate = 1.0;
                if (item.getChanceRate() > 0.0) {
                    rate = item.getChanceRate();
                }
                int rarity = (int) (DROP_RATES[item.getDropFrequency().ordinal() - 1] * rate);
                item.setTableSlot(slot | ((slot += rarity) << 16));
            }
            mainTableSize = (int) (10_000 * ratio);
            if (slot >= mainTableSize) {
                mainTableSize = (int) (slot + ((1 - (commonAmount / mainTable.size())) * mainTableSize));
            }
            if (commonAmount > 0) {
                int count = commonAmount;
                int rarity = (mainTableSize - slot) / commonAmount;
                if (rarity < DROP_RATES[0]) {
                    mainTableSize = slot + (commonAmount * DROP_RATES[0]);
                    rarity = DROP_RATES[0];
                }
                for (ChanceItem item : mainTable) {
                    if (item.getDropFrequency() == DropFrequency.COMMON) {
                        if (--count == 0) {
                            rarity = mainTableSize - slot;
                        }
                        item.setTableSlot(slot | ((slot += rarity) << 16));
                    }
                }
            }
        }
    }

    /**
     * Gets the ratio for stabilizing NPC combat difficulty & drop rates.
     *
     * @return The ratio.
     */
    public double getStabilizerRatio() {
        return (1 / (1 + def.getCombatLevel())) * 10;
    }

    /**
     * @return the defaultTable.
     */
    public List<ChanceItem> getDefaultTable() {
        return defaultTable;
    }

    /**
     * @return the charmTable.
     */
    public List<ChanceItem> getCharmTable() {
        return charmTable;
    }

    /**
     * @return the mainTable.
     */
    public List<ChanceItem> getMainTable() {
        return mainTable;
    }

    /**
     * @return the mainTableSize.
     */
    public int getMainTableSize() {
        return mainTableSize;
    }

    /**
     * @param mainTableSize the mainTableSize to set.
     */
    public void setMainTableSize(int mainTableSize) {
        this.mainTableSize = mainTableSize;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int totalCount) {
        count = totalCount;
    }
}

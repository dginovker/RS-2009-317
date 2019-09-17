package plugin.skill.herblore;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to handle the decanting of potions.
 *
 * @author 'Vexia
 */
public final class PotionDecantingPlugin extends UseWithHandler {

    /**
     * Represents the empty item vial.
     */
    public static final Item EMPTY_VIAL = new Item(229, 1);

    public static boolean isPotion(int itemId) {
        for (int potion : POTIONS) {
            if (potion == itemId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Represents the array of potions to utilitize.
     */
    public static final int[] POTIONS = new int[]{
        /*empty vial*/ 229,
        /* Strength potion(4) */113,
        /* Strength potion(3) */115,
        /* Strength potion(2) */117,
        /* Strength potion(1) */119,
        /* Attack potion(3) */121,
        /* Attack potion(2) */123,
        /* Attack potion(1) */125,
        /* Restore potion(3) */127,
        /* Restore potion(2) */129,
        /* Restore potion(1) */131,
        /* Defence potion(3) */133,
        /* Defence potion(2) */135,
        /* Defence potion(1) */137,
        /* Prayer potion(3) */139,
        /* Prayer potion(2) */141,
        /* Prayer potion(1) */143,
        /* Super attack(3) */145,
        /* Super attack(2) */147,
        /* Super attack(1) */149,
        /* Fishing potion(3) */151,
        /* Fishing potion(2) */153,
        /* Fishing potion(1) */155,
        /* Super strength(3) */157,
        /* Super strength(2) */159,
        /* Super strength(1) */161,
        /* Super defence(3) */163,
        /* Super defence(2) */165,
        /* Super defence(1) */167,
        /* Ranging potion(3) */169,
        /* Ranging potion(2) */171,
        /* Ranging potion(1) */173,
        /* Antipoison(3) */175,
        /* Antipoison(2) */177,
        /* Antipoison(1) */179,
        /* Super antipoison(3) */181,
        /* Super antipoison(2) */183,
        /* Super antipoison(1) */185,
        /* Bravery potion */739,
        /* Cadava potion */756,
        /* Magic ogre potion */2395,
        /* Attack potion(4) */2428,
        /* Restore potion(4) */2430,
        /* Defence potion(4) */2432,
        /* Prayer potion(4) */2434,
        /* Super attack(4) */2436,
        /* Fishing potion(4) */2438,
        /* Super strength(4) */2440,
        /* Super defence(4) */2442,
        /* Ranging potion(4) */2444,
        /* Antipoison(4) */2446,
        /* Super antipoison(4) */2448,
        /* Antifire potion(4) */2452,
        /* Antifire potion(3) */2454,
        /* Antifire potion(2) */2456,
        /* Antifire potion(1) */2458,
        /* Energy potion(4) */3008,
        /* Energy potion(3) */3010,
        /* Energy potion(2) */3012,
        /* Energy potion(1) */3014,
        /* Super energy(4) */3016,
        /* Super energy(3) */3018,
        /* Super energy(2) */3020,
        /* Super energy(1) */3022,
        /* Super restore(4) */3024,
        /* Super restore(3) */3026,
        /* Super restore(2) */3028,
        /* Super restore(1) */3030,
        /* Agility potion(4) */3032,
        /* Agility potion(3) */3034,
        /* Agility potion(2) */3036,
        /* Agility potion(1) */3038,
        /* Magic potion(4) */3040,
        /* Magic potion(3) */3042,
        /* Magic potion(2) */3044,
        /* Magic potion(1) */3046,
        /* Troll potion */3265,
        /* Explosive potion */4045,
        /* Super kebab */4608,
        /* Strange potion */4836,
        /* Antipoison+(4) */5943,
        /* Antipoison+(3) */5945,
        /* Antipoison+(2) */5947,
        /* Antipoison+(1) */5949,
        /* Antipoison++(4) */5952,
        /* Antipoison++(3) */5954,
        /* Antipoison++(2) */5956,
        /* Antipoison++(1) */5958,
        /* Supercompost */6034,
        /* Compost potion(4) */6470,
        /* Compost potion(3) */6472,
        /* Compost potion(2) */6474,
        /* Compost potion(1) */6476,
        /* Combat potion(4) */9739,
        /* Combat potion(3) */9741,
        /* Combat potion(2) */9743,
        /* Combat potion(1) */9745,
        /* Hunter potion(4) */9998,
        /* Hunter potion(3) */10000,
        /* Hunter potion(2) */10002,
        /* Hunter potion(1) */10004,
        /* Antipoison mix(2) */11433,
        /* Antipoison mix(1) */11435,
        /* Superattack mix(2) */11469,
        /* Superattack mix(1) */11471,
        /* Antifire mix(2) */11505,
        /* Antifire mix(1) */11507,
        /* Goblin potion (4) */11809,
        /* Goblin potion (3) */11810,
        /* Goblin potion (2) */11811,
        /* Goblin potion (1) */11812,
        /* Summoning potion(4) */12140,
        /* Summoning potion(3) */12142,
        /* Summoning potion(2) */12144,
        /* Summoning potion(1) */12146,
        /* Saradomin brew(4) */6685,
        /* Saradomin brew(3) */6687,
        /* Saradomin brew(2) */6689,
        /* Saradomin brew(1) */6691,
        /* Overload(4) */11939,
        /* Overload(3) */11940,
        /* Overload(2) */11941,
        /* Overload(1) */11942,
        /* Stamina potion(4) */14727,
        /* Stamina potion(3) */14725,
        /* Stamina potion(2) */14723,
        /* Stamina potion(1) */14721
    };
    private static final int[][] DECANT_DATA = {
        { 14721, 14723, 14725, 14727 },
        { 2458, 2458, 229, 2456 }, { 2458, 2456, 229, 2454 },
        { 2458, 2454, 229, 2452 }, { 2456, 2456, 229, 2452 }, { 2454, 2456, 2458, 2452 },
        { 2454, 2454, 2456, 2452 }, { 189, 193, 229, 2450 }, { 193, 191, 229, 189 }, { 191, 191, 229, 2450 },
        { 191, 189, 193, 2450 }, { 189, 189, 191, 2450 }, { 193, 193, 229, 191 }, { 6687, 6689, 6691, 6685 },
        { 6687, 6687, 6689, 6685 }, { 6687, 6691, 229, 6685 }, { 6691, 6689, 229, 6687 },
        { 6689, 6689, 229, 6685 }, { 6691, 6691, 229, 6689 }, { 127, 131, 229, 2430 }, { 131, 129, 229, 127 },
        { 129, 129, 229, 2430 }, { 127, 129, 131, 2430 }, { 127, 127, 129, 2430 }, { 131, 131, 229, 129 },
        { 139, 143, 229, 2434 }, { 139, 141, 143, 2434 }, { 141, 141, 229, 2434 }, { 139, 143, 229, 2434 },
        { 139, 139, 141, 2434 }, { 143, 143, 229, 141 }, { 133, 137, 229, 2432 }, { 137, 135, 229, 2432 },
        { 135, 135, 229, 2432 }, { 135, 133, 137, 2432 }, { 133, 133, 135, 2432 }, { 137, 137, 229, 135 },
        { 121, 123, 229, 121 }, { 121, 125, 229, 2428 }, { 123, 123, 229, 113 }, { 121, 123, 125, 2428 },
        { 121, 121, 123, 2428 }, { 125, 125, 229, 123 }, { 119, 117, 229, 115 }, { 119, 115, 229, 113 },
        { 117, 117, 229, 113 }, { 115, 117, 119, 113 }, { 115, 115, 117, 113 }, { 119, 119, 229, 117 },
        { 3042, 3046, 229, 3040 }, { 3046, 3044, 229, 3042 }, { 3044, 3044, 229, 3040 },
        { 3042, 3044, 3046, 3040 }, { 3042, 3042, 3044, 3040 }, { 3046, 3406, 229, 3044 },
        { 3018, 3022, 229, 3016 }, { 3022, 3020, 229, 3018 }, { 3020, 3020, 229, 3016 },
        { 3018, 3020, 3022, 3016 }, { 3018, 3018, 3020, 3016 }, { 3022, 3022, 229, 3020 },
        { 3010, 3014, 229, 3008 }, { 3014, 3012, 229, 3010 }, { 3012, 3012, 229, 3008 },
        { 3010, 3012, 3014, 3008 }, { 3010, 3010, 3012, 3008 }, { 3014, 3014, 229, 3012 }, { 163, 167, 229, 2442 },
        { 167, 165, 229, 163 }, { 165, 165, 229, 2442 }, { 165, 163, 167, 2442 }, { 163, 163, 165, 2442 },
        { 167, 167, 229, 165 }, { 3026, 3030, 229, 3024 }, { 3030, 3028, 229, 3026 }, { 3028, 3028, 229, 3024 },
        { 3028, 3026, 3030, 3024 }, { 3030, 3030, 229, 3028 }, { 3026, 3026, 3028, 3024 }, { 159, 161, 229, 157 },
        { 157, 161, 229, 2440 }, { 159, 159, 229, 2440 }, { 159, 157, 161, 2440 }, { 157, 157, 159, 2440 },
        { 161, 161, 229, 159 }, { 171, 173, 229, 169 }, { 169, 173, 229, 2444 }, { 171, 171, 229, 2444 },
        { 171, 169, 173, 2444 }, { 169, 169, 171, 2444 }, { 173, 173, 229, 171 }, { 147, 149, 229, 145 },
        { 145, 149, 229, 2436 }, { 147, 147, 229, 2436 }, { 147, 145, 229, 2436 }, { 145, 145, 147, 2436 },
        { 149, 149, 229, 147 }, { 177, 175, 179, 2446 }, { 175, 175, 177, 2446 }, { 177, 179, 229, 2446 },
        { 175, 179, 229, 175 }, { 175, 175, 229, 2446 }, { 179, 179, 229, 177 }, { 183, 181, 185, 2448 },
        { 181, 181, 183, 2448 }, { 183, 185, 229, 2448 }, { 181, 185, 229, 181 }, { 181, 181, 229, 2448 },
        { 185, 185, 229, 183 }, { 5947, 5945, 5949, 5943 }, { 5945, 5945, 5947, 5943 }, { 5947, 5949, 229, 5943 },
        { 5945, 5949, 229, 5945 }, { 5945, 5945, 229, 5943 }, { 5949, 5949, 229, 5947 },
        { 5956, 5954, 5958, 5952 }, { 5954, 5954, 5956, 5952 }, { 5956, 5958, 229, 5952 },
        { 5954, 5958, 229, 5954 }, { 5954, 5954, 229, 5952 }, { 5958, 5958, 229, 5956 },
        { 9743, 9741, 9745, 9739 }, { 9741, 9741, 9743, 9739 }, { 9743, 9745, 229, 9739 },
        { 9741, 9745, 229, 9741 }, { 9741, 9741, 229, 9739 }, { 9745, 9745, 229, 9743 },
        { 3036, 3034, 3038, 3032 }, { 3034, 3034, 3036, 3032 }, { 3036, 3038, 229, 3032 },
        { 3034, 3038, 229, 3034 }, { 3034, 3034, 229, 3032 }, { 3038, 3038, 229, 3036 }
    };

    /**
     * Constructs a new {@code PotionDecantingPlugin} {@code Object}.
     */
    public PotionDecantingPlugin() {
        super(POTIONS);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int i : POTIONS) {
            addHandler(i, ITEM_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final Item item = event.getUsedItem();
        final Item other = event.getBaseItem();
        final String itemName = formatName(item);
        final String otherName = formatName(other);
        if (!itemName.equals(otherName) && (item.getId() != 229 && other.getId() != 229)) {
            return false;
        }
        final int itemDose = getPotionDose(item);
        final int otherDose = getPotionDose(other);
        if (flagged(itemDose, otherDose)) {
            return false;
        }
        final int[] newDoses = getDoses(itemDose, otherDose);
        if (itemDose == 4 && otherDose == 0 || otherDose == 4 && itemDose == 0) {
            player.getInventory().replace(getItem(item.getName().contains("Vial") ? otherName : itemName, newDoses[1]), item.getSlot());
            player.getInventory().replace(getItem(item.getName().contains("Vial") ? otherName : itemName, newDoses[1]), other.getSlot());
            player.getActionSender().sendMessage("You decant the potion into two equal parts.", 1);
        } else {
            player.getInventory().replace(getItem(item.getName().contains("Vial") ? otherName : itemName, newDoses[0]), other.getSlot());
            player.getInventory().replace(getItem(item.getName().contains("Vial") ? otherName : itemName, newDoses[1]), item.getSlot());
            player.getActionSender().sendMessage("You have combined the liquid into " + (newDoses[0] == 0 ? newDoses[1] : newDoses[0]) + " doses.", 1);
        }
        return true;
    }

    /**
     * Method used to format a name of a potion.
     *
     * @param item the item.
     * @return the name.
     */
    public String formatName(final Item item) {
        return item.getName().replace("(1)", "").replace("(2)", "").replace("(3)", "").replace("(4)", "").trim();
    }

    /**
     * Method used to get the potion does between two potions.
     *
     * @param item the item.
     * @return the doses.
     */
    public int getPotionDose(final Item item) {
        return item.getName().contains("(1)") ? 1 : item.getName().contains("(2)") ? 2 : item.getName().contains("(3)") ? 3 : item.getName().contains("(4)") ? 4 : 0;
    }

    /**
     * Method used to check if a new potion ca be created based on the doses.
     *
     * @param itemDose  the item dose.
     * @param otherDose the other dose.
     * @return <code>True</code> if flagged.
     */
    public boolean flagged(final int itemDose, final int otherDose) {
        return (itemDose == 4 && otherDose != 0 || itemDose != 4 && otherDose == 0 || itemDose == 4 && otherDose == 3 || itemDose == 0 && otherDose != 4) || (otherDose == 4 && itemDose != 0 || otherDose != 4 && itemDose == 0 || otherDose == 4 && itemDose == 3 || otherDose == 0 && itemDose != 4);
    }

    /**
     * Method used to return the doses of the new items.
     *
     * @param itemDose  the item dose.
     * @param otherDose the other dose.
     * @return the new doses.
     */
    public int[] getDoses(final int itemDose, final int otherDose) {
        return itemDose == 1 && otherDose == 1 ? new int[]{ 2, 0 } : itemDose == 3 && otherDose == 3 ? new int[]{ 4, 2 } : itemDose == 2 && otherDose == 2 ? new int[]{ 4, 0 } : itemDose == 2 && otherDose == 3 ? new int[]{ 4, 1 } : itemDose == 1 && otherDose == 2 ? new int[]{ 0, 3 } : itemDose == 4 && otherDose == 0 ? new int[]{ 2, 2 } : itemDose == 1 && otherDose == 3 ? new int[]{ 0, 4 } : otherDose == 2 && itemDose == 3 ? new int[]{ 1, 4 } : otherDose == 1 && itemDose == 2 ? new int[]{ 0, 3 } : otherDose == 4 && itemDose == 0 ? new int[]{ 2, 2 } : otherDose == 1 && itemDose == 3 ? new int[]{ 0, 4 } : itemDose == 0 && otherDose == 4 ? new int[]{ 2, 2 } : otherDose == 4 && itemDose == 0 ? new int[]{ 2, 2 } : new int[]{ 0, 0 };
    }

    /**
     * Method used to get the new item.
     *
     * @param name the name.
     * @param dose the dose of the new item.
     * @return the new item.
     */
    public Item getItem(String name, int dose) {
        ItemDefinition def = null;
        name += "(" + dose + ")";
        if (dose == 0) {
            return EMPTY_VIAL;
        }
        for (int id : POTIONS) {
            def = ItemDefinition.forId(id);
            if (def.getName().equals(name)) {
                return new Item(id);
            }
        }
        return null;
    }

    public static int decantAll(Player player) {
        final Item[] items = player.getInventory().toArray();
        int decanted = 0;
        decanting:
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && isPotion(items[i].getId())) {
                for (int k = 0; k < items.length; k++) {
                    if (i != k && items[k] != null && isPotion(items[k].getId())) {
                        final int id1 = items[i].getId();
                        final int id2 = items[k].getId();
                        for (int[] j : DECANT_DATA) {
                            if (((id1 == j[0]) && (id2 == j[1])) || ((id1 == j[1]) && (id2 == j[0]))) {
                                player.getInventory().set(i, new Item(j[3], 1));
                                player.getInventory().set(k, new Item(j[2], 1));
                                player.getInventory().remove(new Item(Item.COINS, 300), false);
                                decanted++;
                                continue decanting;
                            }
                        }
                    }
                }
            }
        }
        player.getInventory().update(true);
        player.getInventory().refresh();
        return decanted;
    }

}

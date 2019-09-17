package org.gielinor.game.content.skill.free.crafting.jewellery;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents a useful class for jewellery crafting information.
 *
 * @author 'Vexia
 */
public class JewelleryCrafting {

    /**
     * Represents constants of useful items.
     */
    public static final int RING_MOULD = 1592, AMULET_MOULD = 1595, NECKLACE_MOULD = 1597, BRACELET_MOULD = 11065, GOLD_BAR = 2357, SAPPHIRE = 1607, EMERALD = 1605, RUBY = 1603, DIAMOND = 1601, DRAGONSTONE = 1615, ONYX = 6573;

    /**
     * Represents the anum of jewellery data.
     *
     * @author 'Vexia
     */
    public enum JewelleryItem {
        GOLD_RING(5, 15, 23344, 1635, -1, GOLD_BAR),
        SAPPIRE_RING(20, 40, 23345, 1637, 2550, SAPPHIRE, GOLD_BAR),
        EMERALD_RING(27, 55, 23346, 1639, 2552, EMERALD, GOLD_BAR),
        RUBY_RING(34, 70, 23347, 1641, 2568, RUBY, GOLD_BAR),
        DIAMOND_RING(43, 85, 23348, 1643, 2570, DIAMOND, GOLD_BAR),
        DRAGONSTONE_RING(55, 100, 23349, 1645, 2572, DRAGONSTONE, GOLD_BAR),
        ONYX_RING(67, 115, 23350, 6575, 6583, 6573, GOLD_BAR),
        GOLD_NECKLACE(6, 20, 23354, 1654, -1, 2357),
        SAPPHIRE_NECKLACE(22, 55, 23355, 1656, 3853, 1607, 2357),
        EMERALD_NECKLACE(29, 60, 23356, 1658, 5521, 1605, 2357),
        RUBY_NECKLACE(40, 75, 23357, 1660, 11194, 1603, 2357),
        DIAMOND_NECKLACE(56, 90, 23358, 1662, 11090, 1601, 2357),
        DRAGONSTONE_NECKLACE(72, 105, 23359, 1664, 11105, 1615, 2357),
        ONYX_NECKLACE(82, 120, 23360, 6577, 6737, 6573, 2357),
        GOLD_AMULET(8, 30, 23364, 1673, -1, 2357),
        SAPPHIRE_AMULET(24, 65, 23365, 1675, 1727, 1607, 2357),
        EMERALD_AMULET(31, 70, 23366, 1677, 1729, 1605, 2357),
        RUBY_AMULET(50, 85, 23367, 1679, 1725, 1603, 2357),
        DIAMOND_AMULET(70, 100, 23368, 1681, 1731, 1601, 2357),
        DRAGONSTONE_AMULET(80, 150, 23369, 1683, 1712, 1615, 2357),
        ONYX_AMULET(90, 165, 23370, 6579, 26585, 6573, 2357),
        GOLD_BRACELET(7, 25, 23374, 11069, -1, 2357),
        SAPPHIRE_BRACELET(23, 60, 23375, 11072, 11074, 1607, 2357),
        EMERALD_BRACELET(30, 65, 23376, 11076, 11079, 1605, 2357),
        RUBY_BRACELET(42, 80, 23377, 11085, 11088, 1603, 2357),
        DIAMOND_BRACELET(58, 95, 23378, 11092, 11095, 1601, 2357),
        DRAGONSTONE_BRACELET(74, 110, 23379, 11115, 11118, 1615, 2357),
        ONYX_BRACELET(84, 125, 23380, 11130, 11133, 6573, 2357);

        /**
         * Represents the item ids.
         */
        private final int[] items;

        /**
         * Represents the send item.
         */
        private final int sendItem;

        /**
         * Represents the enchanted item.
         */
        private int enchantedItem;

        /**
         * Represents the component id.
         */
        private final int componentId;

        /**
         * Represents the level.
         */
        private final int level;

        /**
         * Represesnts the experience gained.
         */
        private final double experience;

        /**
         * Constructs a new {@code Jewellery.java} {@code Object}.
         *
         * @param level      the level.
         * @param experience the experience.
         */
        JewelleryItem(int level, int experience, int componentId, int sendItem, int enchantedItem, int... items) {
            this.level = level;
            this.experience = experience;
            this.componentId = componentId;
            this.sendItem = sendItem;
            this.enchantedItem = enchantedItem;
            this.items = items;
        }

        /**
         * Gets the jewellery by the product.
         *
         * @param id the id.
         * @return the jewellery.
         */
        public static JewelleryItem forProduct(int id) {
            for (JewelleryItem d : JewelleryItem.values()) {
                if (d.getSendItem() == id) {
                    return d;
                }
            }
            return null;
        }

        /**
         * Gets the items.
         *
         * @return The items.
         */
        public int[] getItems() {
            return items;
        }

        /**
         * Gets the sendItem.
         *
         * @return The sendItem.
         */
        public int getSendItem() {
            return sendItem;
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
         * Gets the level.
         *
         * @return The level.
         */
        public int getLevel() {
            return level;
        }

        /**
         * Gets the experience.
         *
         * @return The experience.
         */
        public double getExperience() {
            return experience;
        }

        /**
         * Gets a {@link org.gielinor.game.content.skill.free.crafting.jewellery.JewelleryCrafting.JewelleryItem} by the id of the button.
         *
         * @param buttonId The id of the button.
         * @return The {@code JewelleryItem}.
         */
        public static JewelleryItem forId(int buttonId) {
            for (JewelleryItem jewelleryItem : values()) {
                if (jewelleryItem.getComponentId() == buttonId) {
                    return jewelleryItem;
                }
            }
            return null;
        }

        public int getEnchantedItem() {
            return enchantedItem;
        }
    }

    /**
     * Method used to open the jewellery crafting interface.
     *
     * @param player the player.
     */
    public static void open(final Player player) {
        Object[][] requirements = new Object[][]{
            { RING_MOULD, 23341, "ring" },
            { NECKLACE_MOULD, 23351, "necklace" },
            { AMULET_MOULD, 23361, "amulet" },
            { BRACELET_MOULD, 23371, "bracelet" }
        };
        for (Object[] requirement : requirements) {
            boolean hasMould = !player.getInventory().contains((int) requirement[0]);
            player.getInterfaceState().send(!hasMould ? 50 : 51, (int) requirement[1] + 2);
            player.getActionSender().sendString(hasMould ? "You need a" + (TextUtils.isPlusN((String) requirement[2]) ? "n " : " ") + requirement[2] + " mould to make " + requirement[2] + "s." : "", (int) requirement[1]);
            for (int childId = ((int) requirement[1] + 3); childId < ((int) requirement[1] + 10); childId++) {
                player.getActionSender().sendUpdateItem(null, childId, 0);
            }
        }
        for (JewelleryItem data : JewelleryItem.values()) {
            int length = 0;
            for (int i = 0; i < data.items.length; i++) {
                if (player.getInventory().contains(data.getItems()[i], 1)) {
                    length++;
                }
            }
            if (!player.getInventory().contains(mouldFor(data.name()), 1)) {
                length--;
            }
            if (length == data.getItems().length && player.getSkills().getLevel(Skills.CRAFTING) > data.getLevel()) {
                player.getActionSender().sendUpdateItem(data.getComponentId(), 0, data.getSendItem());
            } else {
                String name = ItemDefinition.forId(data.getSendItem()).getName().toLowerCase();
                if (name.contains("amulet") && player.getInventory().contains(mouldFor(data.name()), 1)) {
                    for (int i = 0; i < data.items.length; i++) {
                        if (!player.getInventory().contains(data.getItems()[i], 1)) {
                            player.getActionSender().sendUpdateItem(data.getComponentId(), 0, 1685);
                            break;
                        }
                    }
                }
                if (name.contains("ring") && !player.getInventory().contains(RING_MOULD, 1)) {
                    continue;
                }
                if (name.contains("necklace") && !player.getInventory().contains(NECKLACE_MOULD, 1)) {
                    continue;
                }
                if (data == JewelleryItem.DRAGONSTONE_AMULET && !player.getInventory().contains(AMULET_MOULD, 1)) {
                    continue;
                }
                if (name.contains("amulet") || name.equalsIgnoreCase("AMULET_MOULD") && !player.getInventory().contains(AMULET_MOULD, 1)) {
                    continue;
                }
                if (name.contains("bracelet") && !player.getInventory().contains(BRACELET_MOULD, 1)) {
                    continue;
                }
                player.getActionSender().sendUpdateItem(data.getComponentId(), 0, name.contains("ring") ? 1647 : name.contains("necklace") ? 1666 :
                    name.contains("amulet") || name.contains("ammy") ? 1685 : name.contains("bracelet") ? 11067
                        : -1);
            }
        }
        player.getInterfaceState().open(new Component(23335));
    }

    /**
     * Represents the making of a jewellery.
     *
     * @param player the player.
     * @param data   the data.
     * @param amount the amount.
     */
    public static void make(final Player player, JewelleryItem data, int amount) {
        int length = 0;
        int amt = 0;
        if (data.name().contains("GOLD")) {
            amt = player.getInventory().getCount(new Item(GOLD_BAR));
        } else {
            int first = player.getInventory().getCount(new Item(data.getItems()[0]));
            int second = player.getInventory().getCount(new Item(data.getItems()[1]));
            if (first == second) {
                amt = first;
            } else if (first > second) {
                amt = second;
            } else {
                amt = first;
            }
        }
        if (amount > amt) {
            amount = amt;
        }
        for (int i = 0; i < data.items.length; i++) {
            if (player.getInventory().contains(data.getItems()[i], amount)) {
                length++;
            }
        }
        if (length != data.getItems().length) {
            player.getActionSender().sendMessage("You don't have the required item's to make this item.", 1);
            return;
        }
        if (player.getSkills().getLevel(Skills.CRAFTING) < data.getLevel()) {
            player.getActionSender().sendMessage("You need a crafting level of " + data.getLevel() + " to craft this.", 1);
            return;
        }
        Item items[] = new Item[data.items.length];
        int index = 0;
        for (int i = 0; i < data.items.length; i++) {
            items[index] = new Item(data.items[i], 1 * amount);
            index++;
        }
        player.getInterfaceState().close();
        player.getPulseManager().run(new JewelleryPulse(player, null, data, amount));
    }

    /**
     * Gets the mould id for the name.
     *
     * @param name the name.
     * @return the mould id.
     */
    public static int mouldFor(String name) {
        name = name.toLowerCase();
        if (name.contains("ring")) {
            return RING_MOULD;
        }
        if (name.contains("necklace")) {
            return NECKLACE_MOULD;
        }
        if (name.contains("amulet")) {
            return AMULET_MOULD;
        }
        if (name.contains("bracelet")) {
            return BRACELET_MOULD;
        }
        return -1;
    }
}

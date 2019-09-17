package org.gielinor.game.content.skill.free.smithing;


import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.context.InterfaceColourContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.InterfaceColour;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents constants for the Smithing skill.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SmithingConstants {

    /**
     * The bar smithing interface
     */
    public static final int SMITHING_INTERFACE = 994;

    /**
     * The bar smelting interface
     */
    public static final int SMELTING_INTERFACE = 2400;

    /**
     * Holds ids for all text on the Smithing interface (994).
     */
    private static final int[] TEXT_IDS = new int[]{
        1125, 1094, 1126, 1091, 1109, 1098, 1127, 1102, 1128, 1107,
        1124, 1085, 1129, 1093, 1110, 1099, 1113, 1103, 1130, 1108,
        1116, 1087, 1118, 1083, 1111, 1100, 1114, 1104, 1131, 1106,
        1089, 1086, 1095, 1092, 1112, 1101, 1115, 1105, 1132, 1096,
        1090, 1088, 8428, 8429, 11459, 11461, 13357, 13358, 1135, 1134
    };

    /**
     * Clears the Smithing interface of all text
     *
     * @param player the player to clear it for
     */
    public static void clear(Player player) {
        for (int id : TEXT_IDS) {
            player.getActionSender().sendString(id, "");
        }
    }

    public static void buildInterface(Player player, Item item) {
        clear(player);
        Item[][] items = new Item[6][6];
        int row = -1;
        int col = 0;
        Item bolts = null;
        Item limbs = null;
        Smith smith = Smith.forId(item.getId());
        if (smith == null) {
            player.getInterfaceState().close();
            return;
        }
        for (SmithSlot[] s : smith.getSmithSlots()) {
            row += 1;
            for (SmithSlot ss : s) {
                if (ss == null) {
                    continue;
                }
                if (ss.getItem() == null) {
                    continue;
                }
                String toShow = ss.getSlot().getText() != null ? ss.getSlot().getText() : TextUtils.uppercaseFirst(ss.getSlot().toString().replaceAll("_", " "));
                if (ss.getItem() == null) {
                    toShow = "";
                }
                player.getActionSender().sendString(ss.getSlot().getItemId(), toShow);
                if (player.getSkills().getLevel(Skills.SMITHING) >= ss.getLevel() && player.getInventory().contains(new Item(smith.getBar().getId(), ss.getBars()))) {
                    PacketRepository.send(InterfaceColour.class, new InterfaceColourContext(player, ss.getSlot().getItemId(), 0xFFFFFF));
                }
                String bars = ss.getBars() + "bar" + (ss.getBars() > 1 ? "s" : "");
                if (ss.getItem() == null) {
                    bars = "";
                }
                player.getActionSender().sendString(ss.getSlot().getBarId(), bars);
                if (player.getInventory().contains(new Item(smith.getBar().getId(), ss.getBars()))) {
                    PacketRepository.send(InterfaceColour.class, new InterfaceColourContext(player, ss.getSlot().getBarId(), 4032));
                }
                items[row][col] = ss.getItem();
                if (ss.getItem().getDefinition().getName().toLowerCase().contains("bolts")) {
                    bolts = ss.getItem();
                }
                if (ss.getItem().getDefinition().getName().toLowerCase().contains("limb")) {
                    limbs = ss.getItem();
                }
                col++;
                if (col == s.length) {
                    col = 0;
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 1119 + i, items[i], false));
        }
        if (bolts != null) {
            player.getActionSender().sendUpdateItem(1123, 3, bolts);
        }
        if (limbs != null) {
            player.getActionSender().sendUpdateItem(1123, 4, limbs);
        }
        player.getInterfaceState().open(new Component(SMITHING_INTERFACE));
    }

    /**
     * Contains text slot ids for item name, bars
     *
     * These are in order from TOP LEFT to BOTTOM RIGHT
     */
    public enum Slot {

        DAGGER(1125, 1094),
        AXE(1126, 1091),
        CHAIN_BODY(1109, 1098),
        MEDIUM_HELM(1127, 1102),
        DART_TIPS(1128, 1107),
        BOLTS(1132, 1096),
        SWORD(1124, 1085),
        MACE(1129, 1093),
        PLATE_LEGS(1110, 1099),
        FULL_HELM(1113, 1103),
        ARROWTIPS(1130, 1108),
        LIMBS(1135, 1134),
        SCIMITAR(1116, 1087),
        WARHAMMER(1118, 1083),
        PLATE_SKIRT(1111, 1100),
        SQUARE_SHIELD(1114, 1104),
        THROWING_KNIVES(1131, 1106),
        GRAPPLE_TIPS("Grapple Tips", 1112, 1101),
        LONG_SWORD(1089, 1086),
        BATTLE_AXE(1095, 1092),
        PLATE_BODY(1112, 1101),
        KITE_SHIELD(1115, 1105),
        OTHER("Grapple Tip", 1132, 11461),
        TWO_HAND_SWORD("2 hand sword", 1090, 1088),
        CLAWS(8428, 8429),
        OIL_LANTERN_FRAME("Bullseye lantern", 11459, 11461),
        NAILS(13357, 13358),
        STUDS(1135, 1134);
        /**
         * The text to show on the interface
         */
        private String text;
        /**
         * The bar amount text id
         */
        private final int barId;
        /**
         * The item name text id
         */

        private final int itemId;

        /**
         * A slot via the Smithing interface
         *
         * @param text
         * @param barId
         * @param itemId
         */
        Slot(String text, int barId, int itemId) {
            this.text = text;
            this.barId = barId;
            this.itemId = itemId;
        }

        /**
         * A slot via the Smithing interface
         *
         * @param barId
         * @param barId
         */
        Slot(int barId, int itemId) {
            this.barId = barId;
            this.itemId = itemId;
        }

        /**
         * Gets text to show for the item type on the interface
         *
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * The bar amount text id
         *
         * @return the itemId
         */
        public int getBarId() {
            return barId;
        }

        /**
         * The item name text id
         *
         * @return the barId
         */
        public int getItemId() {
            return itemId;
        }

    }
}

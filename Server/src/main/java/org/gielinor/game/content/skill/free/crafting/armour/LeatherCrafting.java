package org.gielinor.game.content.skill.free.crafting.armour;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents a useful class for leather crafting related information.
 *
 * @author 'Vexia
 */
public final class LeatherCrafting {

    /**
     * Constant of a needle id.
     */
    public static final int NEEDLE = 1733;

    /**
     * Constant of a thread id.
     */
    public static final Item THREAD = new Item(1734, 1);

    /**
     * Constant of a needle id.
     */
    public static final int LEATHER = 1741;

    /**
     * Constant of a thread id.
     */
    public static final int HARD_LEATHER = 1743;

    /**
     * The constants of leather id's related to dragon hides.
     */
    public static final int GREEN_LEATHER = 1745, BLUE_LEATHER = 2505, RED_LEATHER = 2507, BLACK_LEATHER = 2509, SNAKESKIN = 6289;

    /**
     * The leather interface component.
     */
    private static final Component COMPONENT = new Component(2311);

    /**
     * Checks if its the last thrad.
     *
     * @return <code>True</code> if so.
     */
    public static boolean isLastThread(final Player player) {
        final Item thread = getThread(player);
        if (thread == null) {
            return false;
        }
        int charge = thread.getCharge();
        return charge >= 1004;
    }

    /**
     * Method used to decay thread.
     *
     * @param player The player.
     */
    public static void decayThread(final Player player) {
        final Item thread = getThread(player);
        if (thread == null) {
            return;
        }
        int charge = thread.getCharge();
        thread.setCharge(charge + 1);
    }


    /**
     * Method used to remove thread.
     *
     * @param player The player.
     */
    public static void removeThread(final Player player) {
        if (player.getInventory().remove(THREAD)) {
            player.getActionSender().sendMessage("You use a reel of your thread.", 1);
            Item thread = getThread(player);
            if (thread != null) {
                thread.setCharge(1000);
            }
        }
    }

    /**
     * Gets the thread.
     *
     * @param player the player.
     * @return the item.
     */
    public static Item getThread(final Player player) {
        return player.getInventory().get(player.getInventory().getSlot(THREAD));
    }

    /**
     * Represents the dragon hide crafting data.
     *
     * @author 'Vexia
     */
    public enum DragonHide {
        GREEN_D_HIDE_VAMBS(1745, 1, 1065, 57, 62),
        GREEN_D_HIDE_CHAPS(1745, 2, 1099, 60, 124),
        GREEN_D_HIDE_BODY(1745, 3, 1135, 63, 186),
        BLUE_D_HIDE_VAMBS(2505, 1, 2487, 66, 70),
        BLUE_D_HIDE_CHAPS(2505, 2, 2493, 68, 140),
        BLUE_D_HIDE_BODY(2505, 3, 2499, 71, 210),
        RED_D_HIDE_VAMBS(2507, 1, 2489, 73, 78),
        RED_D_HIDE_CHAPS(2507, 2, 2495, 75, 156),
        RED_D_HIDE_BODY(2507, 3, 2501, 77, 234),
        BLACK_D_HIDE_VAMBS(2509, 1, 2491, 79, 86),
        BLACK_D_HIDE_CHAPS(2509, 2, 2497, 82, 172),
        BLACK_D_HIDE_BODY(2509, 3, 2503, 84, 258),
        SNAKESKIN_BODY(6289, 15, 6322, 53, 55),
        SNAKESKIN_CHAPS(6289, 12, 6324, 51, 50),
        SNAKESKIN_VAMBRACES(6289, 8, 6330, 47, 35),
        SNAKESKIN_BANDANA(6289, 5, 6326, 48, 45),
        SNAKESKIN_BOOTS(6289, 6, 6328, 45, 30);

        /**
         * Constructs a new {@code LeatherCrafting.java} {@code Object}.
         *
         * @param leather    the leather required.
         * @param amount     the amount of leather needed.
         * @param product    the product item.
         * @param level      the level required.
         * @param experience the experience.
         */
        DragonHide(int leather, int amount, int product, int level, double experience) {
            this.leather = leather;
            this.amount = amount;
            this.product = product;
            this.level = level;
            this.experience = experience;
        }

        /**
         * The leather id.
         */
        private final int leather;

        /**
         * The leather amount required.
         */
        private final int amount;

        /**
         * The product.
         */
        private final int product;

        /**
         * The required level.
         */
        private final int level;

        /**
         * The experiences gained.
         */
        private final double experience;

        /**
         * @return the leather.
         */
        public int getLeather() {
            return leather;
        }

        /**
         * @return the amount.
         */
        public int getAmount() {
            return amount;
        }

        /**
         * @return the product.
         */
        public int getProduct() {
            return product;
        }

        /**
         * @return the level.
         */
        public int getLevel() {
            return level;
        }

        /**
         * @return the experience.
         */
        public double getExperience() {
            return experience;
        }

        /**
         * Method used to return the type for the leather.
         *
         * @param leather the leather.
         * @return the hide.
         */
        public static DragonHide forLeather(int leather) {
            for (DragonHide hide : DragonHide.values()) {
                if (hide.getLeather() == leather) {
                    return hide;
                }
            }
            return null;
        }
    }

    /**
     * Represents a {@link org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting.SoftLeather} crafting type.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public enum SoftLeather {

        ARMOUR(14, 25, new Item(1129), 8635, 8634, 8633),
        GLOVES(1, 13.8, new Item(1059), 8638, 8637, 8636),
        BOOTS(7, 16.3, new Item(1061), 8641, 8640, 8639),
        VAMBRACES(11, 22, new Item(1063), 8644, 8643, 8642),
        CHAPS(18, 27, new Item(1095), 8647, 8646, 8645),
        COIF(38, 37, new Item(1169), 8650, 8649, 8648),
        COWL(9, 18.5, new Item(1167), 8653, 8652, 8651);

        /**
         * The level required.
         */
        private final int level;

        /**
         * The experience gained.
         */
        private final double experience;

        /**
         * The product.
         */
        private final Item product;

        /**
         * The ids for buttons that apply to this leather.
         */
        private final int[] buttonIds;

        /**
         * Constructs a new {@code Soft} {@code Object}.
         *
         * @param level      the level.
         * @param experience the experience.
         * @param product    the product.
         * @param buttonIds  The ids for buttons that apply to this leather.
         */
        SoftLeather(int level, double experience, Item product, int... buttonIds) {
            this.level = level;
            this.experience = experience;
            this.product = product;
            this.buttonIds = buttonIds;
        }


        /**
         * Method used to open the crafting interface.
         *
         * @param player the player.
         */
        public static void open(final Player player) {
            player.getInterfaceState().open(COMPONENT);
        }

        /**
         * @return the level.
         */
        public int getLevel() {
            return level;
        }

        /**
         * @return the experience.
         */
        public double getExperience() {
            return experience;
        }

        /**
         * @return the product.
         */
        public Item getProduct() {
            return product;
        }

        /**
         * @return The button ids.
         */
        public int[] getButtonIds() {
            return buttonIds;
        }

        /**
         * Method used to get the amount by the id.
         *
         * @return The amount.
         */
        public static int getAmount(int button) {
            for (SoftLeather soft : SoftLeather.values()) {
                for (int index = 0; index < soft.getButtonIds().length; index++) {
                    if (soft.getButtonIds()[index] == button) {
                        return (index == 0 ? 1 : index == 1 ? 5 : index == 2 ? 10 : -1);
                    }
                }
            }
            return -1;
        }

        /**
         * Method used to get the value by the id.
         *
         * @return The soft leather.
         */
        public static SoftLeather forButton(int button) {
            for (SoftLeather soft : SoftLeather.values()) {
                for (int buttonId : soft.getButtonIds()) {
                    if (button == buttonId) {
                        return soft;
                    }
                }
            }
            return null;
        }
    }
}

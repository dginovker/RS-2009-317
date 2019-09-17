package org.gielinor.game.node.entity.player.info.login;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles a new player's starting point.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class Starter {

    /**
     * The {@link Player} this starter is for.
     */
    private final Player player;

    /**
     * Constructs a new {@code Starter}.
     */
    public Starter(Player player) {
        this.player = player;
    }

    /**
     * Begins the starter package selection.
     */
    public void start() {
        player.lock();
        player.setAttribute("STARTER_PACKAGE_SELECTION", 1);
        player.getSettings().setDataOrbs(true, true);
        player.getSettings().setRemainingXP(true, true);
        player.getSettings().setRoofRemoval(true, true);
        player.getSettings().setAreaSoundVolume(4);
        player.getSettings().setSoundEffectVolume(4);
        player.getSettings().setSplitPrivateChat(true);
        player.getSettings().setSingleMouseButtonSQL(false);
        player.getSettings().setMouseMovementSQL(true);
        player.getSettings().setMusicVolume(4);
        player.getActionSender().sendString(27259, "Select your starter package");
        int index = 27167;
        int slot = 27197;
        for (int clearId = index; clearId < 27197; clearId++) {
            player.getActionSender().sendString(clearId, "");
        }
        for (int slotId = 0; slotId < 30; slotId++) {
            player.getActionSender().sendUpdateItem(slot + slotId, 0, null);
        }
        for (Starter.StarterPackage starterPackage : Starter.StarterPackage.values()) {
            player.getActionSender().sendString(index, TextUtils.formatDisplayName(starterPackage.name().replaceAll("_", " ")) + " starter package");
            if (starterPackage.getItem() != null) {
                player.getActionSender().sendUpdateItem(slot, 0, starterPackage.getItem());
            }
            index++;
            slot++;
        }
        Component starterComponent = new Component(27135);
        player.getInterfaceState().open(starterComponent);
    }

    /**
     * Represents starter packages.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public enum StarterPackage {
        MAIN(1,
            new Item[]{
                new Item(Item.COINS, 500000),
                new Item(10943, 1),
                new Item(1725, 1),
                new Item(1323, 1),
                new Item(1153, 1),
                new Item(1115, 1),
                new Item(1067, 1),
                new Item(884, 500),
                new Item(857, 1),
                new Item(1129, 1),
                new Item(1099, 1),
                new Item(1712, 1),
                new Item(386, 100),
                new Item(579, 1),
                new Item(577, 1),
                new Item(1011, 1)
            },
            new Item[]{
                new Item(145, 20),
                new Item(157, 20),
                new Item(163, 20),
                new Item(169, 20),
                new Item(3042, 20),
                new Item(561, 2500),
                new Item(565, 2500),
                new Item(560, 2500),
                new Item(557, 2500),
                new Item(558, 2500),
                new Item(556, 2500),
                new Item(555, 2500),
                new Item(554, 2500),
                new Item(562, 2500),
                new Item(1351, 1),
                new Item(1265, 1)
            },
            1.5,
            new Item(8850)
        ) {
            @Override
            public void additional(Player player) {
                player.getSkills().updateCombatLevel();
                player.getSkills().refresh();
            }
        },
        PURE(2,
            new Item[]{
                new Item(Item.COINS, 500000),
                new Item(10943, 1),
                new Item(1725, 1),
                new Item(1323, 1),
                new Item(1153, 1),
                new Item(1115, 1),
                new Item(1067, 1),
                new Item(884, 2500),
                new Item(857, 1),
                new Item(1129, 1),
                new Item(1097, 1),
                new Item(1712, 1),
                new Item(386, 100)
            },
            new Item[]{
                new Item(145, 20),
                new Item(157, 20),
                new Item(169, 20),
                new Item(3042, 20),
                new Item(561, 2500),
                new Item(565, 2500),
                new Item(560, 2500),
                new Item(557, 2500),
                new Item(558, 2500),
                new Item(556, 2500),
                new Item(555, 2500),
                new Item(554, 2500),
                new Item(562, 2500),
                new Item(1351, 1),
                new Item(1265, 1)
            },
            1.5,
            new Item(544)
        ) {
            @Override
            public void additional(Player player) {
                player.getSkills().updateCombatLevel();
                player.getSkills().refresh();
            }
        },
        SKILLER(3,
            new Item[]{
                new Item(Item.COINS, 500000),
                new Item(10944, 1),
                new Item(6182, 1),
                new Item(6180, 1),
                new Item(6181, 1),
                new Item(2474, 1),
                new Item(1949, 1),
                new Item(1757, 1),
                new Item(1712, 1)
            },
            new Item[]{
                new Item(5291, 20),
                new Item(5292, 20),
                new Item(5293, 20),
                new Item(5294, 20),
                new Item(5096, 20),
                new Item(5097, 20),
                new Item(5098, 20),
                new Item(5099, 20),
                new Item(1511, 2500),
                new Item(946, 1),
                new Item(1755, 1)
            },
            1.7,
            new Item(5343)
        ) {
            @Override
            public void additional(Player player) {
                player.getSkills().updateCombatLevel();
                player.getSkills().refresh();
            }
        },
        IRON_MAN_MODE(4, new Item[]{
            new Item(9703),
            new Item(9704)
        },
            new Item[]{
            },
            1.0,
            new Item(Item.IRONMAN_HELM)
        ) {
            @Override
            public void additional(Player player) {
                player.getActionSender().sendMessage("As an Iron Man, you can use shops in the room East of home.");
                player.getSkills().updateCombatLevel();
                player.getSkills().refresh();
            }
        },
        ULTIMATE_IRON_MAN_MODE(5, new Item[]{
            new Item(9703),
            new Item(9704)
        },
            new Item[]{
            },
            1.0,
            new Item(Item.ULTIMATE_IRONMAN_HELM)
        ) {
            @Override
            public void additional(Player player) {
                player.getActionSender().sendMessage("As an Iron Man, you can use shops in the room East of home.");
                player.getSkills().updateCombatLevel();
                player.getSkills().refresh();
            }
        },
        HARDCORE_IRON_MAN_MODE(6, new Item[]{
            new Item(9703),
            new Item(9704)
        },
            new Item[]{
            },
            1.0,
            new Item(Item.HARDCORE_IRONMAN_HELM)
        ) {
            @Override
            public void additional(Player player) {
                player.getActionSender().sendMessage("As an Iron Man, you can use shops in the room East of home.");
                player.getSkills().updateCombatLevel();
                player.getSkills().refresh();
            }
        },;

        /**
         * The ID of this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         */
        private final int id;

        /**
         * The inventory items given to the player for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         */
        private final Item[] inventory;

        /**
         * The bank items given to the player for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         */
        private final Item[] bank;

        /**
         * The experience modifier for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         */
        private final double modifier;

        /**
         * The preview {@link Item} for this package.
         */
        private final Item item;

        /**
         * Additional tasks for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         *
         * @param player The player.
         */
        public abstract void additional(Player player);

        /**
         * Creates a new {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         *
         * @param id        The ID of this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         * @param inventory The inventory items given to the player for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         * @param bank      The bank items given to the player for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         * @param modifier  The experience modifier for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         * @param item      The preview {@link Item} for this package.
         */
        StarterPackage(int id, Item[] inventory, Item[] bank, double modifier, Item item) {
            this.id = id;
            this.inventory = inventory;
            this.bank = bank;
            this.modifier = modifier;
            this.item = item;
        }

        /**
         * Gets the ID of this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         *
         * @return The ID.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the inventory items given to the player for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         *
         * @return The item array.
         */
        public Item[] getInventory() {
            return inventory;
        }

        /**
         * Gets the bank items given to the player for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         *
         * @return The item array.
         */
        public Item[] getBank() {
            return bank;
        }

        /**
         * Gets the experience modifier for this {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         *
         * @return The modifier.
         */
        public double getModifier() {
            return modifier;
        }

        /**
         * Gets the preview {@link Item} for this package.
         *
         * @return The {@link Item}.
         */
        public Item getItem() {
            return item;
        }

        /**
         * Gets the {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage} by the ID.
         *
         * @param id The ID of the {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         * @return The {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
         */
        public static StarterPackage forId(int id) {
            for (StarterPackage starterPackage : StarterPackage.values()) {
                if (starterPackage.getId() == id) {
                    return starterPackage;
                }
            }
            return null;
        }
    }

    /**
     * Checks if the player's starter is an instance of the given {@code starter}.
     *
     * @param player         The player.
     * @param starterPackage The {@link org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage}.
     * @return <code>True</code> if they are.
     */
    public static boolean isOf(Player player, StarterPackage starterPackage) {
        return player.getSavedData().getGlobalData().getStarterPackage() == starterPackage;
    }

    /**
     * Checks whether or not the player has their starter.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasStarter() {
        return player.getSavedData().getGlobalData().getStarterPackage() != null;
    }
}

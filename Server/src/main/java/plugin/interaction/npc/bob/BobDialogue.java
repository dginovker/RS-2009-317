package plugin.interaction.npc.bob;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.RepairItem;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the bob npc who repairs items.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BobDialogue extends DialoguePlugin {

    /**
     * Represents the item id being repaired.
     */
    private int itemId = 0;

    /**
     * Represents the item being repaired.
     */
    private Item item;

    /**
     * Represents the item repairing.
     */
    private static RepairItem repairitem = null;

    /**
     * Constructs a new {@code BobDialogue} {@code Object}.
     */
    public BobDialogue() {
    }

    /**
     * Constructs a new {@code BobDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public BobDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 754:
                interpreter.sendOptions("Select an Option", "Yes, please.", "No, thank you.");
                stage = 755;
                break;
            case 755:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, please.");
                        stage = 757;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thank you.");
                        stage = 756;
                        break;
                }
                break;
            case 756:
                end();
                break;
            case 757:
                end();
                if (repairitem != null) {
                    if (!player.getInventory().contains(Item.COINS, repairitem.getCost())) {
                        end();
                        player.getActionSender().sendMessage("You don't have enough to pay him.");
                        break;
                    }
                    player.getInventory().remove(new Item(Item.COINS, repairitem.getCost()));
                    player.getInventory().remove(new Item(itemId, 1));
                    player.getInventory().add(repairitem.getProduct());
                    String cost = "free";
                    if (repairitem.getCost() != 0) {
                        cost = repairitem.getCost() + "gp";
                    }
                    player.getActionSender().sendMessage("Bob fixes your " + ItemDefinition.forId(itemId).getName().toLowerCase().replace("broken", "").trim() + " for " + cost + ".");
                }
                if (repairitem == null) {
                    String cost = "free";
                    String type = BarrowsEquipment.formatedName(itemId);
                    String single = BarrowsEquipment.getSingleName(type);
                    String equipment = BarrowsEquipment.getEquipmentType(type);
                    String newString = type.toLowerCase().replace(single, "").trim().replace("'s", "");
                    StringBuilder newEquipmentString = new StringBuilder();
                    newEquipmentString.append(newString).append(" " + equipment);
                    final BarrowsEquipment.BarrowsFullEquipment fullEquipment =
                        BarrowsEquipment.BarrowsFullEquipment.forName(newEquipmentString.toString());
                    int totalCost = BarrowsEquipment.getCost(item);
                    if (totalCost != 0) {
                        cost = String.valueOf(totalCost + "gp");
                    }
                    if (!player.getInventory().contains(Item.COINS, totalCost)) {
                        end();
                        player.getActionSender().sendMessage("You don't have enough to pay him.");
                        break;
                    }
                    player.getInventory().remove(new Item(Item.COINS, totalCost));
                    player.getInventory().remove(new Item(itemId, 1));
                    player.getInventory().add(fullEquipment.getFull());
                    player.getActionSender().sendMessage("Bob fixes your " + equipment + " for " + cost + ".");
                }
                break;
            case 678:
                end();
                break;
            case 0:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player("Give me a quest!");
                        stage = -5;
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Have you anything to sell?");
                        stage = 10;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you repair my items for me?");
                        stage = 20;
                        break;
                }
                break;
            case -5:
                interpreter.sendDialogues(npc, FacialExpression.ANGRY, "Get yer own!");
                stage = -4;
                break;
            case -4:
                end();
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Yes! I buy and sell axes! Take your pick (or axe)!");
                stage = 11;
                break;
            case 11:
                end();
                // TODO 317
                //	Shops.PICKAXE_AND_AXES_SHOP.open(player);
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Of course I'll repair it, though the materials may cost", "you. Just hand me the item and I'll have a look.");
                stage = 21;
                break;
            case 21:
                end();
                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BobDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        boolean repair = false;
        boolean wrong = false;
        if (npc.getId() == 3797 && args.length == 1) {
            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you repair my items for me?");
            stage = 20;
            return true;
        }
        if (args.length == 1) {
            interpreter.sendOptions("Select an Option", "Give me a quest!", "Have you anything to sell?.", "Can you repair my items for me?");
            stage = 0;
            return true;
        }
        if (args[1] != null) {
            repair = (boolean) args[1];
        }
        if (args[2] != null) {
            wrong = (boolean) args[2];
        }
        if (args[3] != null) {
            repairitem = RepairItem.forId((int) args[3]);
            itemId = (int) args[3];
        }
        if (args[4] != null) {
            item = (Item) args[4];
        }
        if (repair && !wrong) {
            String cost = "free";
            if (repairitem != null) {
                if (repairitem.getCost() != 0) {
                    cost = String.valueOf(repairitem.getCost() + "gp");
                }
            }
            if (repairitem == null) {
                int totalCost = BarrowsEquipment.getCost(item);
                if (totalCost != 0) {
                    cost = String.valueOf(totalCost + "gp");
                }
            }
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Quite badly damaged, but easy to repair. Would you", "like me to repair it for " + cost + "?");
            stage = 754;
            return true;
        }
        if (repair == true && wrong == true) {
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Sorry friend, but I can't do anything with that.");
            stage = 678;
            return true;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 519, 3797 };
    }

    /**
     * Barrows equipment information.
     *
     * @author 'Vexia
     */
    public static class BarrowsEquipment {

        /**
         * Represents a type of Barrows for cost issuing.
         *
         * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
         */
        enum BarrowsType {

            WEAPON(100000, 0.5051, "flail", "greataxe", "spear", "x-bow", "hammer", "hammers", "staff"),
            HELMET(60000, 0.3035, "hood", "helm", "coif"),
            BODY(90000, 0.455, "top", "platebody", "body"),
            LEGS(80000, 0.4041, "skirt", "legs", "plateskirt");

            /**
             * The initial cost to repair this item.
             */
            private final int cost;

            /**
             * The modifier for the cost when repairing this item on an armour stand.
             */
            private final double modifier;

            /**
             * The names that qualify this {@link plugin.interaction.npc.bob.BobDialogue.BarrowsEquipment.BarrowsType}.
             */
            private final String[] names;

            /**
             * Constructs a new {@link plugin.interaction.npc.bob.BobDialogue.BarrowsEquipment.BarrowsType}.
             *
             * @param cost     The initial cost to repair this item.
             * @param modifier The modifier for the cost when repairing this item on an armour stand.
             * @param names    The names that qualify this {@link plugin.interaction.npc.bob.BobDialogue.BarrowsEquipment.BarrowsType}.
             */
            BarrowsType(int cost, double modifier, String... names) {
                this.cost = cost;
                this.modifier = modifier;
                this.names = names;
            }

            /**
             * Gets the initial cost to repair this item.
             *
             * @return The cost.
             */
            public int getCost() {
                return cost;
            }

            /**
             * Gets the modifier for the cost when repairing this item on an armour stand.
             *
             * @return The modifier.
             */
            public double getModifier() {
                return modifier;
            }

            /**
             * Gets the names that qualify this {@link plugin.interaction.npc.bob.BobDialogue.BarrowsEquipment.BarrowsType}.
             *
             * @return The names.
             */
            public String[] getNames() {
                return names;
            }
        }

        /**
         * Represents the base names.
         */
        private String[] base = new String[]{ "dharok", "verac", "ahrim", "torag", "guthan" };

        /**
         * The weapon names.
         */
        private static final String[] weaponNames = new String[]{ "flail", "greataxe", "spear", "x-bow", "hammer", "hammers", "staff" };

        /**
         * The weapon body names.
         */
        private static final String[] bodyNames = new String[]{ "top", "platebody" };

        /**
         * The helm names.
         */
        private static final String[] helmetNames = new String[]{ "hood", "helm", "coif" };

        /**
         * The leg names.
         */
        private static final String[] legNames = new String[]{ "skirt", "legs", "plateskirt", "platelegs" };

        /**
         * Gets the cost to repair this {@link plugin.interaction.npc.bob.BobDialogue.BarrowsEquipment}.
         *
         * @param item          The item to repair.
         * @param armourStand   Whether or not its being repaired on an armour stand.
         * @param smithingLevel The level of the player's Smithing skill.
         * @return The cost to repair this {@link plugin.interaction.npc.bob.BobDialogue.BarrowsEquipment}.
         */
        public static int getCost(Item item, boolean armourStand, int smithingLevel) {
            int cost = 0;
            String barrowsName = item.getDefinition().getName();
            for (BarrowsType barrowsType : BarrowsType.values()) {
                for (String name : barrowsType.getNames()) {
                    if (barrowsName.toLowerCase().contains(name)) {
                        cost = barrowsType.getCost() - (item.getCharge());
                        // TODO
                        if (barrowsName.contains("100")) {
                            cost = (int) Math.ceil(cost / 100);
                        }
                        if (barrowsName.contains("75")) {
                            cost = (int) Math.ceil(cost / 7.5);
                        }
                        if (barrowsName.contains("50")) {
                            cost = (int) Math.ceil(cost / 5);
                        }
                        if (barrowsName.contains("25")) {
                            cost = (int) Math.ceil(cost / 2.5);
                        }
                        if (armourStand) {
                            cost -= barrowsType.getModifier() * smithingLevel;
                        }
                        return cost;
                    }
                }
            }
            return cost;
        }

        /**
         * Gets the cost to repair this {@link plugin.interaction.npc.bob.BobDialogue.BarrowsEquipment}.
         *
         * @param item The item to repair.
         * @return The cost to repair this {@link plugin.interaction.npc.bob.BobDialogue.BarrowsEquipment}.
         */
        public static int getCost(Item item) {
            return getCost(item, false, 0);
        }

        /**
         * Represents if an item is a barrows item.
         *
         * @param id the id.
         * @return <code>True</code> if so.
         */
        public static boolean isBarrowsItem(int id) {
            for (Object[] ITEM : ITEMS) {
                if ((int) ITEM[0] == id) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Represents the items.
         */
        private static final Object[][] ITEMS = { { 4856, "Ahrim's hood" }, { 4857, "Ahrim's hood" }, { 4858, "Ahrim's hood" }, { 4859, "Ahrim's hood" }, { 4860, "Ahrim's hood" }, { 4862, "Ahrim's staff" }, { 4863, "Ahrim's staff" }, { 4864, "Ahrim's staff" }, { 4865, "Ahrim's staff" }, { 4866, "Ahrim's staff" }, { 4868, "Ahrim's top" }, { 4869, "Ahrim's top" }, { 4870, "Ahrim's top" }, { 4871, "Ahrim's top" }, { 4872, "Ahrim's top" }, { 4874, "Ahrim's skirt" }, { 4875, "Ahrim's skirt" }, { 4876, "Ahrim's skirt" }, { 4877, "Ahrim's skirt" }, { 4878, "Ahrim's skirt" }, { 4880, "Dharok's helm" }, { 4881, "Dharok's helm" }, { 4882, "Dharok's helm" }, { 4883, "Dharok's helm" }, { 4884, "Dharok's helm" }, { 4886, "Dharok's greataxe" }, { 4887, "Dharok's greataxe" }, { 4888, "Dharok's greataxe" }, { 4889, "Dharok's greataxe" }, { 4890, "Dharok's greataxe" }, { 4892, "Dharok's platebody" }, { 4893, "Dharok's platebody" }, { 4894, "Dharok's platebody" }, { 4895, "Dharok's platebody" }, { 4896, "Dharok's platebody" }, { 4898, "Dharok's platelegs" }, { 4899, "Dharok's platelegs" }, { 4900, "Dharok's platelegs" }, { 4901, "Dharok's platelegs" }, { 4902, "Dharok's platelegs" }, { 4904, "Guthan's helm" }, { 4905, "Guthan's helm" }, { 4906, "Guthan's helm" }, { 4907, "Guthan's helm" }, { 4908, "Guthan's helm" }, { 4910, "Guthan's spear" }, { 4911, "Guthan's spear" }, { 4912, "Guthan's spear" }, { 4913, "Guthan's spear" }, { 4914, "Guthan's spear" }, { 4916, "Guthan's body" }, { 4917, "Guthan's body" }, { 4918, "Guthan's body" }, { 4919, "Guthan's body" }, { 4920, "Guthan's body" }, { 4922, "Guthan's skirt" }, { 4923, "Guthan's skirt" }, { 4924, "Guthan's skirt" }, { 4925, "Guthan's skirt" }, { 4926, "Guthan's skirt" }, { 4928, "Karil's coif" }, { 4929, "Karil's coif" }, { 4930, "Karil's coif" }, { 4931, "Karil's coif" }, { 4932, "Karil's coif" }, { 4934, "Karil's x-bow" }, { 4935, "Karil's x-bow" }, { 4936, "Karil's x-bow" }, { 4937, "Karil's x-bow" }, { 4938, "Karil's x-bow" }, { 4940, "Karil's top" }, { 4941, "Karil's top" }, { 4942, "Karil's top" }, { 4943, "Karil's top" }, { 4944, "Karil's top" }, { 4946, "Karil's skirt" }, { 4947, "Karil's skirt" }, { 4948, "Karil's skirt" }, { 4949, "Karil's skirt" }, { 4950, "Karil's skirt" }, { 4952, "Torag's helm" }, { 4953, "Torag's helm" }, { 4954, "Torag's helm" }, { 4955, "Torag's helm" }, { 4956, "Torag's helm" }, { 4958, "Torag's hammers" }, { 4959, "Torag's hammers" }, { 4960, "Torag's hammers" }, { 4961, "Torag's hammers" }, { 4962, "Torag's hammers" }, { 4964, "Torag's body" }, { 4965, "Torag's body" }, { 4966, "Torag's body" }, { 4967, "Torag's body" }, { 4968, "Torag's body" }, { 4970, "Torag's legs" }, { 4971, "Torag's legs" }, { 4972, "Torag's legs" }, { 4973, "Torag's legs" }, { 4974, "Torag's legs" }, { 4976, "Verac's helm" }, { 4977, "Verac's helm" }, { 4978, "Verac's helm" }, { 4979, "Verac's helm" }, { 4980, "Verac's helm" }, { 4982, "Verac's flail" }, { 4983, "Verac's flail" }, { 4984, "Verac's flail" }, { 4985, "Verac's flail" }, { 4986, "Verac's flail" }, { 4988, "Verac's top" }, { 4989, "Verac's top" }, { 4990, "Verac's top" }, { 4991, "Verac's top" }, { 4992, "Verac's top" }, { 4994, "Verac's skirt" }, { 4995, "Verac's skirt" }, { 4996, "Verac's skirt" }, { 4997, "Verac's skirt" }, { 4998, "Verac's skirt" } };

        /**
         * Gets the formatted name.
         *
         * @param id the id.
         * @return the name.
         */
        public static String formatedName(int id) {
            for (Object[] itemData : ITEMS) {
                if ((int) itemData[0] == id) {
                    return (String) itemData[1];
                }
            }
            return null;
        }

        /**
         * Gets the equipment type.
         *
         * @param name the name.
         * @return the type.
         */
        public static String getEquipmentType(String name) {
            name = name.toLowerCase().replace("verac's", "").replace("karil's", "").replace("dharok's", "").replace("torag's", "").replace("guthan's", "").replace("ahrim's", "").trim();
            for (int i = 0; i < weaponNames.length; i++) {
                if (weaponNames[i].contains(name)) {
                    return "weapon";
                }
            }
            for (int k = 0; k < bodyNames.length; k++) {
                if (bodyNames[k].contains(name)) {
                    return "body";
                }
            }
            for (int z = 0; z < legNames.length; z++) {
                if (legNames[z].contains(name)) {
                    return "legs";
                }
            }
            for (int q = 0; q < helmetNames.length; q++) {
                if (helmetNames[q].contains(name)) {
                    return "helm";
                }
            }
            return null;
        }

        /**
         * Method used t get its single name.
         *
         * @param name the name.
         * @return the name.
         */
        public static String getSingleName(String name) {
            name = name.toLowerCase().replace("verac's", "").replace("karil's", "").replace("dharok's", "").replace("torag's", "").replace("guthan's", "").replace("ahrim's", "").trim();
            for (int i = 0; i < weaponNames.length; i++) {
                if (weaponNames[i].contains(name)) {
                    return weaponNames[i];
                }
            }
            for (int k = 0; k < bodyNames.length; k++) {
                if (bodyNames[k].contains(name)) {
                    return bodyNames[k];
                }
            }
            for (int z = 0; z < legNames.length; z++) {
                if (legNames[z].contains(name)) {
                    return legNames[z];
                }
            }
            for (int q = 0; q < helmetNames.length; q++) {
                if (helmetNames[q].contains(name)) {
                    return helmetNames[q];
                }
            }
            return null;
        }

        /**
         * Gets the bases.
         *
         * @return the base.
         */
        public String[] getBase() {
            return base;
        }

        /**
         * Represents the multiple full barrows equipment items.
         *
         * @author 'Vexia
         * @version 1.0
         */
        public enum BarrowsFullEquipment {
            VERAC_LEGS(new Item(4759, 1)),
            VERAC_TOP(new Item(4757, 1)),
            VERAC_WEAPON(new Item(4755, 1)),
            VERAC_HELM(new Item(4753, 1)),
            TORAG_LEGS(new Item(4751, 1)),
            TORAG_TOP(new Item(4749, 1)),
            TORAG_HELM(new Item(4745, 1)),
            TORAG_WEAPON(new Item(4747, 1)),
            KARIL_HELM(new Item(4732, 1)),
            KARIL_WEAPON(new Item(4734, 1)),
            KARIL_BODY(new Item(4736, 1)),
            KARIL_LEGS(new Item(4738, 1)),
            GUTHAN_HELM(new Item(4724, 1)),
            GUTHAN_BODY(new Item(4728, 1)),
            GUTHAN_LEGS(new Item(4730, 1)),
            GUTHAN_WEAPON(new Item(4726, 1)),
            DHAROK_HELM(new Item(4716, 1)),
            DHAROK_BODY(new Item(4720, 1)),
            DHAROK_LEGS(new Item(4722, 1)),
            DHAROK_WEAPON(new Item(4718, 1)),
            AHRIM_HELM(new Item(4708, 1)),
            AHRIM_BODY(new Item(4712, 1)),
            AHRIM_LEGS(new Item(4714, 1)),
            AHRIM_WEAPON(new Item(4710, 1));

            /**
             * Constructs a new {@code BarrowsEquipment} {@code Object}.
             *
             * @param full the full item.
             */
            BarrowsFullEquipment(Item full) {
                this.full = full;
            }

            /**
             * Represents the full item.
             */
            private final Item full;

            /**
             * for name
             *
             * @param name thename.
             * @return the equipment.
             */
            public static BarrowsFullEquipment forName(String name) {
                // Custom
                if (name.toLowerCase().contains("verac body")) {
                    return VERAC_TOP;
                }
                if (name.toLowerCase().contains("guthan body")) {
                    return GUTHAN_BODY;
                }
                if (name.toLowerCase().contains("torag body")) {
                    return TORAG_TOP;
                }
                for (BarrowsFullEquipment barrow : BarrowsFullEquipment.values()) {
                    if (barrow.name().toLowerCase().replace("_", " ").trim().equalsIgnoreCase(name)) {
                        return barrow;
                    }
                }
                return null;
            }

            /**
             * Gets the full.
             *
             * @return The full.
             */
            public Item getFull() {
                return full;
            }
        }

    }

}

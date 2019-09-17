package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.action.DropItemHandler;
import org.gielinor.game.content.global.action.EquipHandler;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.impl.DarkZone;
import org.gielinor.game.world.map.zone.impl.WildernessZone;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;

/**
 * Represents the Max hood and cape plugin.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class MaxCapePlugin extends OptionHandler {

    /**
     * Represents all max cape and hood items.
     */
    public static final Item[] ITEMS = new Item[]{
        new Item(Item.MAX_CAPE),
        new Item(Item.MAX_HOOD),
        new Item(Item.FIRE_MAX_CAPE),
        new Item(Item.FIRE_MAX_HOOD),
        new Item(Item.SARADOMIN_MAX_CAPE),
        new Item(Item.SARADOMIN_MAX_HOOD),
        new Item(Item.ZAMORAK_MAX_CAPE),
        new Item(Item.ZAMORAK_MAX_HOOD),
        new Item(Item.GUTHIX_MAX_CAPE),
        new Item(Item.GUTHIX_MAX_HOOD),
        new Item(Item.AVAS_MAX_CAPE),
        new Item(Item.AVAS_MAX_HOOD)
    };

    /**
     * Checks if the player has the requirements to wear a max cape.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public static boolean hasRequirements(Player player, boolean login) {
        if (player.getRights().isAdministrator())
            return true;

        boolean hasRequirements = true;
        if (!player.getSkills().areAllAtLeast(99)) {
            if (!login) {
                player.getActionSender().sendMessage("You need to have level 99 in every skill before you can wear this.");
            }
            hasRequirements = false;
        }
        if (hasRequirements && !player.getQuestRepository().hasCompletedAll()) {
            if (!login) {
                player.getActionSender().sendMessage("You must have completed all quest diaries to wear this.");
            }
            hasRequirements = false;
        }
        if (hasRequirements && !player.getQuestRepository().hasCompletedAll()) { // TODO
            if (!login) {
                player.getActionSender().sendMessage("You must have completed all achievement diaries to wear this.");
            }
            hasRequirements = false;
        }
//        if (hasRequirements && !player.getSavedData().getActivityData().hasLearnedSlayerOption(0)) {
//            if (!login) {
//                player.getActionSender().sendMessage("You must have unlocked how to fletch broad arrows and bolts to wear this.");
//            }
//            hasRequirements = false;
//        }
//        if (hasRequirements && !player.getSavedData().getActivityData().hasLearnedSlayerOption(1)) {
//            if (!login) {
//                player.getActionSender().sendMessage("You must have unlocked how to craft rings of slaying to wear this.");
//            }
//            hasRequirements = false;
//        }
        if (hasRequirements && !player.getSavedData().getActivityData().hasLearnedSlayerOption(2)) {
            if (!login) {
                player.getActionSender().sendMessage("You must have unlocked how to craft a slayer helmet to wear this.");
            }
            hasRequirements = false;
        }
        if (!hasRequirements && login) {
            for (Item item : ITEMS) {
                if (player.getEquipment().remove(item)) {
                    player.getDialogueInterpreter().sendItemMessage(item, "As you no longer have the requirements,",
                        "your " + item.getName() + " unequips itself to your " +
                            (player.getInventory().freeSlots() < 1 ? "bank" : "inventory") + "!");
                    if (player.getInventory().freeSlots() < 1) {
                        player.getBank().add(item);
                    } else {
                        player.getInventory().add(item);
                    }
                }
            }
        }
        return hasRequirements;
    }

    /**
     * Checks if the player is wearing a Max cape.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public static boolean isWearing(Player player) {
        Item cape = player.getEquipment().get(Equipment.SLOT_CAPE);
        if (cape == null) {
            return false;
        }
        for (Item item : ITEMS) {
            if (cape.getId() == item.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player has a Max cape in their inventory.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public static boolean containsCape(Player player) {
        for (Item item : ITEMS) {
            if (item.getName().toLowerCase().contains("hood")) {
                continue;
            }
            if (player.getInventory().contains(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        // Max cape
        ItemDefinition.forId(Item.MAX_CAPE).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.MAX_CAPE).getConfigurations().put("option:teleports", this);
        ItemDefinition.forId(Item.MAX_HOOD).getConfigurations().put("option:wear", this);
        //
        ItemDefinition.forId(Item.FIRE_MAX_CAPE).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.FIRE_MAX_HOOD).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.SARADOMIN_MAX_CAPE).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.SARADOMIN_MAX_HOOD).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.ZAMORAK_MAX_CAPE).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.ZAMORAK_MAX_HOOD).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.GUTHIX_MAX_CAPE).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.GUTHIX_MAX_HOOD).getConfigurations().put("option:wear", this);
        // Ava's
        ItemDefinition.forId(Item.AVAS_MAX_CAPE).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.AVAS_MAX_CAPE).getConfigurations().put("option:commune", this);
        ItemDefinition.forId(Item.AVAS_MAX_HOOD).getConfigurations().put("option:wear", this);

        ItemDefinition.forId(Item.MAX_CAPE).getConfigurations().put("option:drop", this);
        ItemDefinition.forId(Item.FIRE_MAX_CAPE).getConfigurations().put("option:drop", this);
        ItemDefinition.forId(Item.SARADOMIN_MAX_CAPE).getConfigurations().put("option:drop", this);
        ItemDefinition.forId(Item.ZAMORAK_MAX_CAPE).getConfigurations().put("option:drop", this);
        ItemDefinition.forId(Item.GUTHIX_MAX_CAPE).getConfigurations().put("option:drop", this);
        ItemDefinition.forId(Item.AVAS_MAX_CAPE).getConfigurations().put("option:drop", this);
        PluginManager.definePlugin(new MaxCapeTeleportsDialogue());
        PluginManager.definePlugin(new MaxCapeCombinePlugin());
        PluginManager.definePlugin(new MaxCapeCombineDialogue());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "wear":
                if (!hasRequirements(player, false)) {
                    return true;
                }
                if (EquipHandler.equip(player, ((Item) node))) {
                    DarkZone.checkDarkArea(player);
//                    for (Tiara tiara : Tiara.values()) {
//                        if (tiara == null || tiara.getTalisman() == null) {
//                            continue;
//                        }
//                        MysteriousRuin mysteriousRuin = MysteriousRuin.forTalisman(tiara.getTalisman());
//                        if (mysteriousRuin == null) {
//                            continue;
//                        }
//                        player.getConfigManager().set(491,
//                                1 << ConfigFileDefinition.forId(ObjectDefinition.forId(mysteriousRuin.getObject()[0]).getConfigFileId()).getBitShift(), true);
//                    }
                    return true;
                }
                return false;
            case "teleports":
                player.getDialogueInterpreter().open("max-cape-teleports");
                return true;
            case "features":

                return true;
            case "commune":
                player.getActionSender().sendMessage("Coming soon.");
                return true;
            case "drop":
                DarkZone.checkDarkArea(player);
                return DropItemHandler.handle(player, node, option);

        }
        return false;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    /**
     * Represents the teleports dialogue for the Max cape.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class MaxCapeTeleportsDialogue extends DialoguePlugin {

        /**
         * Represents the locations to teleport to.
         */
        private final Location[] LOCATIONS = new Location[]{
            new Location(2878, 3546, 0),
            new Location(2933, 3292, 0),
            new Location(2611, 3391, 0),
            new Location(2457, 3220, 0),
            new Location(3326, 3663, 0)

        };
        /**
         * The location to teleport to.
         */
        private Location location;

        public MaxCapeTeleportsDialogue() {
        }

        public MaxCapeTeleportsDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new MaxCapeTeleportsDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            stage = 0;
            interpreter.sendOptions("Select a location", "Warriors' Guild", "Crafting Guild", "Fishing Guild", "Red Salamanders", "Black Salamanders");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    if (optionSelect == null) {
                        end();
                        return true;
                    }
                    if (optionSelect.getButtonId() > LOCATIONS.length) {
                        end();
                        return true;
                    }
                    location = LOCATIONS[optionSelect.getButtonId() - 1];
                    if (location == null) {
                        end();
                        return true;
                    }
                    if (WildernessZone.Companion.isInZone(location)) {
                        interpreter.sendPlaneMessage("<col=8A0808>Warning!", "The location you are attempting to teleport to", "is located in the wilderness.", "Are you sure you wish to teleport there?");
                        stage = 1;
                        return true;
                    }
                    // TODO Salamanders = 5x a day
                    player.getTeleporter().send(location);
                    return true;
                case 1:
                    interpreter.sendOptions("Teleport to Wilderness?", "Yes, teleport to the wilderness.", "No.");
                    stage = 2;
                    return true;
                case 2:
                    end();
                    if (location == null) {
                        break;
                    }
                    if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                        player.getTeleporter().send(location);
                    }
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("max-cape-teleports") };
        }
    }

    /**
     * Handles using another cape on a Max cape.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static final class MaxCapeCombinePlugin extends UseWithHandler {

        /**
         * Constructs a new {@link plugin.interaction.item.MaxCapePlugin.MaxCapeCombinePlugin} {@code Object}.
         */
        public MaxCapeCombinePlugin() {
            super(Item.MAX_CAPE);
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            if (event.getPlayer().getInventory().contains(event.getUsedItem()) &&
                event.getPlayer().getInventory().contains(event.getBaseItem())) {
                if (!event.getPlayer().getInventory().contains(new Item(Item.MAX_HOOD))) {
                    event.getPlayer().getActionSender().sendMessage("You need to have your max hood in your inventory to do that!");
                    return true;
                }
                Item capeItem = !event.getUsedItem().getName().toLowerCase().contains("max cape")
                    ? event.getUsedItem() : event.getBaseItem();
                Item maxCape = event.getUsedItem().getName().toLowerCase().contains("max cape")
                    ? event.getUsedItem() : event.getBaseItem();
                event.getPlayer().getDialogueInterpreter().open("max-cape-combine", capeItem, maxCape);
                return true;
            }
            return false;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            addHandler(6570, ITEM_TYPE, this);
            addHandler(2412, ITEM_TYPE, this);
            addHandler(2413, ITEM_TYPE, this);
            addHandler(2414, ITEM_TYPE, this);
            addHandler(10499, ITEM_TYPE, this);
            return this;
        }
    }

    /**
     * Represents the teleports dialogue for the Max cape.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class MaxCapeCombineDialogue extends DialoguePlugin {

        /**
         * The cape item to combine.
         */
        private Item item;
        /**
         * The max cape item.
         */
        private Item cape;

        public MaxCapeCombineDialogue() {
        }

        public MaxCapeCombineDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new MaxCapeCombineDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            stage = 0;
            item = (Item) args[0];
            cape = (Item) args[1];
            interpreter.sendPlaneMessage(false, "<col=8A0808>Warning!", "If you combine these capes, both of them will be consumed.", "Are you sure you want to do this?");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    options("Yes, combine them.", "No, I wish to keep them.");
                    stage = 1;
                    break;
                case 1:
                    if (optionSelect == OptionSelect.TWO_OPTION_TWO) {
                        end();
                        return true;
                    }
                    if (cape == null || item == null) {
                        end();
                        return true;
                    }
                    Item newCape = null;
                    switch (item.getId()) {
                        case 6570:
                            newCape = new Item(Item.FIRE_MAX_CAPE);
                            break;
                        case 2412:
                            newCape = new Item(Item.SARADOMIN_MAX_CAPE);
                            break;
                        case 2413:
                            newCape = new Item(Item.GUTHIX_MAX_CAPE);
                            break;
                        case 2414:
                            newCape = new Item(Item.ZAMORAK_MAX_CAPE);
                            break;
                        case 10499:
                            newCape = new Item(Item.AVAS_MAX_CAPE);
                            break;
                    }
                    if (newCape == null) {
                        end();
                        return true;
                    }
                    if (player.getInventory().contains(cape) && player.getInventory().contains(item) && player.getInventory().contains(new Item(Item.MAX_HOOD))) {
                        if (player.getInventory().remove(cape, item, new Item(Item.MAX_HOOD))) {
                            player.getInventory().add(new Item(newCape.getId() + 1), true);
                            player.getInventory().add(newCape, true);
                            interpreter.sendItemMessage(newCape, "You combine a " + item.getName().toLowerCase() + " to your Max cape,", "in return you receive a " + newCape.getName() + "!");
                        }
                    }
                    stage = END;
                    break;
                case END:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("max-cape-combine") };
        }
    }
}

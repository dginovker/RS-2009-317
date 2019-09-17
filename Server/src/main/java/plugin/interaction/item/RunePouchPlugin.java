package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.content.skill.free.runecrafting.Rune;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.OperationCode;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.utilities.misc.RunScript;

/**
 * The Rune Pouch item option handler.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         TODO Add listener
 */
public class RunePouchPlugin extends OptionHandler {

    /**
     * The inventory component ID.
     */
    public static final int INVENTORY_COMPONENT = 25040;
    /**
     * The morph component.
     */
    private static final Component COMPONENT = new Component(25030).setCloseEvent(new CloseEvent() {

        @Override
        public void close(Player player, Component component) {
            player.getInterfaceState().openDefaultTabs();
        }

        @Override
        public boolean canClose(Player player, Component component) {
            return true;
        }

    });

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(Item.RUNE_POUCH).getConfigurations().put("option:open", this);
        ItemDefinition.forId(Item.RUNE_POUCH).getConfigurations().put("option:empty", this);
        PluginManager.definePlugin(new RunePouchInterfacePlugin());
        PluginManager.definePlugin(new RunePouchUsePlugin());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "open":
                player.getInterfaceState().close();
                player.getInterfaceState().removeTabs(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
                player.getActionSender().sendUpdateItems(25040, player.getSavedData().getGlobalData().getRunePouch().toArray());
                player.getActionSender().sendUpdateItems(25041, player.getInventory().toArray());
                player.getInterfaceState().open(COMPONENT);
                return true;

            case "empty":
                if (player.getSavedData().getGlobalData().getRunePouch().isEmpty()) {
                    player.getActionSender().sendMessage("The pouch is empty.");
                    return true;
                }
                Container runePouch = player.getSavedData().getGlobalData().getRunePouch();
                for (Item item : runePouch.toArray()) {
                    if (item == null) {
                        continue;
                    }
                    if (player.getInventory().hasRoomFor(item) && runePouch.remove(item)) {
                        player.getInventory().add(item);
                    }
                }
                if (!runePouch.isEmpty()) {
                    player.getActionSender().sendMessage("It was not possible to empty the pouch.");
                }
                return true;
        }
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    /**
     * Handles the Rune Pouch interface.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class RunePouchInterfacePlugin extends ComponentPlugin {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ComponentDefinition.put(25030, this);
            ComponentDefinition.put(25040, this);
            return this;
        }

        public static void storeRune(Player player, int amount, Item item) {
            if (item.getId() == Item.RUNE_POUCH) {
                player.getActionSender().sendMessage("Don't be silly.");
                return;
            }
            Rune rune = Rune.forItem(item);
            if (rune == null) {
                player.getActionSender().sendMessage("You can't store that in the rune pouch."); // Item on pouch = Nothing interesting happens
                return;
            }
            Container runePouch = player.getSavedData().getGlobalData().getRunePouch();
            if (amount > 16000) {
                amount = 16000;
            }
            Item existing = runePouch.getById(item.getId());
            if (existing != null) {
                if (existing.getCount() >= 16000) {
                    player.getActionSender().sendMessage("Your pouch can't hold any more of that rune.");
                    return;
                }
                if (amount > 16000) {
                    amount = 16000 - existing.getCount();
                }
                if ((existing.getCount() + amount) > 16000) {
                    amount -= 16000 - existing.getCount();
                }
            } else {
                if (runePouch.itemCount() == 3) {
                    player.getActionSender().sendMessage("The pouch's slots are all full.");
                    return;
                }
            }
            if (amount < 1) {
                player.getActionSender().sendMessage("The pouch is currently full.");
                return;
            }
            if (player.getInventory().remove(new Item(item.getId(), amount))) {
                runePouch.add(new Item(item.getId(), amount));
                player.getActionSender().sendUpdateItems(INVENTORY_COMPONENT, player.getSavedData().getGlobalData().getRunePouch().toArray());
                player.getActionSender().sendUpdateItems(25041, player.getInventory().toArray());
            }
        }

        /**
         * Withdraws a rune from the rune pouch.
         *
         * @param player The player.
         * @param opcode The opcode.
         * @param slot   The item slot.
         */
        public void withdrawRune(Player player, int opcode, int slot) {
            if (slot < 0 || slot > 2) {
                return;
            }
            Container runePouch = player.getSavedData().getGlobalData().getRunePouch();
            Item item = runePouch.get(slot);
            if (item == null) {
                return;
            }
            final int[] amount = { 0 };
            switch (opcode) {
                case OperationCode.OPTION_OFFER_ONE:
                    amount[0] = 1;
                    break;
                case OperationCode.OPTION_OFFER_FIVE:
                    amount[0] = 5;
                    break;
                case OperationCode.OPTION_OFFER_TEN:
                    amount[0] = item.getCount();
                    break;
                case OperationCode.OPTION_OFFER_ALL:
                    player.setAttribute("runscript", new RunScript() {

                        @Override
                        public boolean handle() {
                            amount[0] = (int) value;
                            return true;
                        }
                    });
                    player.getDialogueInterpreter().sendInput(false, "Enter the amount.");
                    break;
            }
            if (amount[0] > 0) {
                Item removed = new Item(item.getId(), amount[0]);
                if (!player.getInventory().hasRoomFor(removed)) {
                    player.getActionSender().sendMessage("You don't have space to withdraw that rune.");
                    return;
                }
                if (runePouch.remove(removed)) {
                    player.getInventory().add(removed);
                }
                player.getActionSender().sendUpdateItems(INVENTORY_COMPONENT, player.getSavedData().getGlobalData().getRunePouch().toArray());
                player.getActionSender().sendUpdateItems(25041, player.getInventory().toArray());
            }
        }

        @Override
        public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
            if (component.getId() != 25030 && component.getId() != 25040) {
                return false;
            }
            if (component.getId() == 25040) {
                withdrawRune(player, opcode, slot);
                return true;
            }
            if (slot < 0 || slot > 28) {
                return false;
            }
            Item item = player.getInventory().get(slot);
            if (item == null) {
                return false;
            }
            final int[] amount = { 0 };
            switch (opcode) {
                case OperationCode.OPTION_OFFER_ONE:
                    amount[0] = 1;
                    break;
                case OperationCode.OPTION_OFFER_FIVE:
                    amount[0] = 5;
                    break;
                case OperationCode.OPTION_OFFER_TEN:
                    amount[0] = item.getCount();
                    break;
                case OperationCode.OPTION_OFFER_ALL:
                    player.setAttribute("runscript", new RunScript() {

                        @Override
                        public boolean handle() {
                            amount[0] = (int) value;
                            return true;
                        }
                    });
                    player.getDialogueInterpreter().sendInput(false, "Enter the amount.");
                    break;
            }
            if (amount[0] > 0) {
                storeRune(player, amount[0], item);
                return true;
            }
            return true;
        }
    }

    /**
     * Handles the plugin for using an item on the rune pouch.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public class RunePouchUsePlugin extends UseWithHandler {

        public RunePouchUsePlugin() {
            super(Item.RUNE_POUCH);
        }

        @Override
        public boolean handle(NodeUsageEvent nodeUsageEvent) {
            Item item = nodeUsageEvent.getUsedItem();
            if (item != null && item.getId() == Item.RUNE_POUCH) {
                item = nodeUsageEvent.getBaseItem();
            }
            if (item == null || !item.getName().toLowerCase().contains("rune")) {
                return false;
            }
            Player player = nodeUsageEvent.getPlayer();
            RunePouchInterfacePlugin.storeRune(player, item.getCount(), item);
            return true;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (Runes runes : Runes.values()) {
                addHandler(runes.getId(), ITEM_TYPE, this);
            }
            return this;
        }
    }

}

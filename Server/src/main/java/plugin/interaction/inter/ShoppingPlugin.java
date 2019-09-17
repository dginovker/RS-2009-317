package plugin.interaction.inter;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.global.shop.Shop;
import org.gielinor.game.content.global.shop.ShopViewer;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.OperationCode;
import org.gielinor.net.packet.in.ExaminePacket;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the plugin used to handle the shopping interface.
 *
 * @author 'Vexia
 * @version 1.0
 *          TODO Sell X
 */
public final class ShoppingPlugin extends ComponentPlugin {

    // temporary
    public static final int BARROWS_GLOVES_POINTS = 250;
    public static final int FIGHTER_TORSO_POINTS = 500;
    public static final int COINS_POINTS = 15;
    public static final int LAMP_POINTS = 20;
    public static final int CRYSTAL_KEY_POINTS = 10;
    public static final int DWARF_CANNON_SET = 25;
    private static final int GODSWORDS = 15;
    private static final int DARKBOWS = 10;
    private static final int HOOD = 5;
    private static final int ALE = 25;

    public static int getPriceCustomItem(int itemId) {
        switch (itemId) {
            case 7462:
                return BARROWS_GLOVES_POINTS;
            case 10551:
                return FIGHTER_TORSO_POINTS;
            case Item.COINS:
                return COINS_POINTS;
            case 989:
                return CRYSTAL_KEY_POINTS;
            case 11967:
                return DWARF_CANNON_SET;
            case 2528:
                return LAMP_POINTS;
            case 40068:
            case 40071:
            case 40074:
            case 40077:
                return GODSWORDS;
            case 32759:
            case 32763:
            case 32761:
            case 32757:
                return DARKBOWS;
            case 40116:
                return HOOD;
            case 40056:
                return ALE;
        }
        return -1;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) {
        ComponentDefinition.forId(3900).setPlugin(this);
        ComponentDefinition.forId(3823).setPlugin(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        final ShopViewer viewer = player.getExtension(ShopViewer.class);
        if (viewer == null) {
            return false;
        }
        final Container container = component.getId() == 3823 ? player.getInventory() : viewer.getShop().getContainer(player, 0);
        switch (opcode) {
            case OperationCode.OPTION_OFFER_ONE:
                value(player, viewer, container.get(slot), component.getId() == 3823);
                return true;
            case OperationCode.OPTION_OFFER_FIVE:
            case OperationCode.OPTION_OFFER_TEN:
            case OperationCode.OPTION_OFFER_ALL:
            case OperationCode.OPTION_OFFER_X:
                Item item = component.getId() == 3900 ? viewer.getShop().getItems(player)[slot] : player.getInventory().get(slot);
                if (item == null) {
                    return false;
                }
                final int amount = getAmount(player, opcode, item.getId(), component.getId() == 3823, viewer.getShop());
                if (amount == -1) {
                    return true;
                }
                if (opcode == OperationCode.OPTION_OFFER_X && amount == 0) {
                    player.setAttribute("runscript", getRunScript(viewer, slot, component.getId() == 3900));
                    player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
                    break;
                }
                if (component.getId() == 3900) {
                    viewer.buy(slot, amount);
                } else {
                    viewer.sell(slot, amount);
                }
                return true;
        }
        return false;
    }

    /**
     * Method used to value an item.
     *
     * @param player the player.
     * @param viewer the viewer.
     * @param item   the item.
     * @param sell   the sell.
     */
    private void value(final Player player, final ShopViewer viewer, final Item item, final boolean sell) {
        if (item == null) {
            return;
        }
        Shops shops = Shops.forId(viewer.getShop().getNpcs()[0]);
        if (shops != null && shops.value(player, viewer, item, sell)) {
            return;
        }
        if (sell && viewer.getShop().itemAllowed(player, item.getId())) {
            if (player.getRights().isAdministrator() && !player.ignoreRestrictions()) {
                player.getActionSender().sendMessage("You cannot sell items.");
                return;
            }

            if (viewer.getShop() == Shops.CUSTOM_FUR_CLOTHING.getShop()) {
                player.getActionSender().sendMessage("You cannot sell this item.");
                return;
            }
            if (viewer.getShop() == Shops.LOYALTY_POINT_SHOP.getShop()) {
                player.getActionSender().sendMessage("You cannot sell this item.");
                return;
            }
            if (item.getId() == viewer.getShop().getCurrency()) {
                player.getActionSender().sendMessage("You can't sell this item.");
                return;
            }
            
            final int value = viewer.getShop().getSellingValue(player, new Item(item.getId(), 1));
            String currency = ItemDefinition.forId(viewer.getShop().getCurrency()).getName().toLowerCase();
            if (value == 1 && currency.charAt(currency.length() - 1) == 's') {
                currency = currency.substring(0, currency.length() - 1);
            } else if (value != 1 && !(currency.charAt(currency.length() - 1) == 's')) {
                currency += "s";
            }
            player.getActionSender().sendMessage(item.getName() + ": shop will buy for " + value + " " + currency + ".");
        } else if (viewer.getShop().itemAllowed(player, item.getId())) {
            // Custom Fur Clothing
            if (viewer.getShop() == Shops.CUSTOM_FUR_CLOTHING.getShop()) {
                player.getActionSender().sendMessage(FurClothingInterface.getValue(item));
                return;
            }
            if (viewer.getShop() == Shops.LOYALTY_POINT_SHOP.getShop()) {
                player.getActionSender().sendMessage(item.getName() + ": currently costs " + getPriceCustomItem(item.getId()) + " Loyalty points.");
                return;
            }
            int value = item.getDefinition().getMaxValue();
            String name = ItemDefinition.forId(viewer.getShop().getCurrency()).getName().toLowerCase();
            if (value == 1 && (name.charAt(name.length() - 1) == 's')) {
                name = name.substring(0, name.length() - 1);
            } else if (value > 1 && !(name.charAt(name.length() - 1) == 's')) {
                name += "s";
            }
            player.getActionSender().sendMessage(item.getName() + ": currently costs " + value + " " + name + ".");
        } else {
            player.getActionSender().sendMessage("You cannot sell this item.");
        }
    }

    /**
     * Gets the run script for selling an item.
     *
     * @param slot the slot.
     * @return the script.
     */
    private RunScript getRunScript(final ShopViewer viewer, final int slot, boolean buy) {
        return new RunScript() {

            @Override
            public boolean handle() {
                if ((int) getValue() < 1) {
                    return true;
                }
                int totalValue = (int) getValue();
                totalValue = totalValue > 500 ? 500 : totalValue;
                if (buy) {
                    viewer.buy(slot, totalValue);
                    return true;
                }
                viewer.sell(slot, totalValue);
                return true;
            }
        };
    }

    /**
     * Gets the amount by the opcode.
     *
     * @param opcode the opcode.
     * @return the amount.
     */
    private int getAmount(Player player, int opcode, int itemId, boolean sell, Shop shop) {
        int amount = opcode == 117 ? 1 : opcode == 43 ? 5 : opcode == 129 ? 10 : opcode == 135 ? 0 : -1;
        if (sell) {
            if (opcode == 135) {
                player.getActionSender().sendDebugPacket(opcode, "ItemExamine", "ID: " + itemId);
                if (itemId < 0 || ItemDefinition.forId(itemId) == null) {
                    return -1;
                }
                player.getActionSender().sendMessage(ExaminePacket.getItemExamine(player, itemId));
                return -1;
            }
            int transferAmount = player.getInventory().getCount(new Item(itemId));
            if (amount >= transferAmount) {
                amount = transferAmount;
            }
            return amount;
        }
        if (shop != Shops.CUSTOM_FUR_CLOTHING.getShop()) {
            int space = player.getInventory().freeSlots();
            if (space < amount && !ItemDefinition.forId(itemId).isStackable()) {
                amount = space;
            }
        }
        return amount;
    }
}

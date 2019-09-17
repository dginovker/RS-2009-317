package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.OperationCode;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.config.ServerVar;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerType;
import org.gielinor.rs2.model.container.impl.PriceGuideContainer;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the Grand Exchange guide price interface plugin.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GuidePriceInterfacePlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(25677, this);
        ComponentDefinition.put(25626, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, final int slot, final int itemId) {
        if (component.getId() != 25677 && component.getId() != 25626) {
            return false;
        }
        // Functions
        if (component.getId() == 25626 && opcode == 185) {
            switch (button) {
                case 25634:
                    if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
                        player.getInterfaceState().close();
                        player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
                        return true;
                    }
                    player.getGrandExchange().openSearch();
                    return true;

                case 25640:
                    Item[] current = new Item[player.getPriceGuideContainer().itemCount()];
                    Item[] inventory = new Item[player.getInventory().itemCount()];
                    int index = 0;
                    for (Item item : player.getInventory().toArray()) {
                        if (item == null) {
                            continue;
                        }
                        if (!player.getInventory().contains(item)) {
                            continue;
                        }
                        if (item.getDefinition().hasDestroyAction() || !item.getDefinition().getConfiguration(ItemConfiguration.TRADEABLE, false)) {
                            continue;
                        }
                        inventory[index] = item;
                        index++;
                    }
                    index = 0;
                    for (Item item : player.getPriceGuideContainer().toArray()) {
                        if (item == null) {
                            continue;
                        }
                        if (!player.getPriceGuideContainer().contains(item)) {
                            continue;
                        }
                        current[index] = item;
                        index++;
                    }
                    Container copy = new Container(30, ContainerType.DEFAULT);
                    copy.add(current);
                    copy.add(inventory);
                    player.getInventory().remove(inventory);
                    player.getPriceGuideContainer().copy(copy);
                    player.getPriceGuideContainer().update(true);
                    player.getPriceGuideContainer().refresh();
                    player.getPriceGuideContainer().updateValues();
                    return true;
            }
            return true;
        }
        if (slot < 0 || slot > 30) {
            return false;
        }
        final Item item = component.getId() == 25626 ? player.getPriceGuideContainer().get(slot) : player.getInventory().get(slot);
        if (item == null) {
            return false;
        }
        switch (component.getId()) {
            /**
             * Inventory / price guide.
             */
            case 25677:
            case 25626:
                int amount = 0;
                final Container container = component.getId() == 25677 ? player.getInventory() : player.getPriceGuideContainer();
                final Container addContainer = component.getId() != 25677 ? player.getInventory() : player.getPriceGuideContainer();
                if (addContainer instanceof PriceGuideContainer && (item.getDefinition().hasDestroyAction() || !item.getDefinition().getConfiguration(ItemConfiguration.TRADEABLE, false))) {
                    player.getActionSender().sendMessage("That item cannot be traded.");
                    return true;
                }
                switch (opcode) {
                    case OperationCode.OPTION_OFFER_ONE:
                        amount = 1;
                        break;
                    case OperationCode.OPTION_OFFER_FIVE:
                        amount = 5;
                        break;
                    case OperationCode.OPTION_OFFER_TEN:
                        amount = 10;
                        break;
                    case OperationCode.OPTION_OFFER_ALL:
                        amount = container.getCount(item);
                        break;
                    case OperationCode.OPTION_OFFER_X:
                        player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                int amount = (int) value;
                                int inventoryAmount = container.getCount(item);
                                if (amount > inventoryAmount) {
                                    amount = inventoryAmount;
                                }
                                Item remove = new Item(item.getId(), amount);
                                if (!container.contains(remove)) {
                                    return true;
                                }
                                if (amount > 0 && container.remove(remove, slot, true, false)) {
                                    addContainer.add(remove);
                                    if (container instanceof PriceGuideContainer) {
                                        player.getPriceGuideContainer().shift();
                                    }
                                    player.getPriceGuideContainer().updateValues();
                                    return true;
                                }
                                return true;
                            }
                        });
                        return true;
                }
                int inventoryAmount = container.getCount(item);
                if (amount > inventoryAmount) {
                    amount = inventoryAmount;
                }
                if (amount < 1) {
                    return true;
                }
                Item remove = new Item(item.getId(), amount);
                if (!container.contains(remove)) {
                    return true;
                }
                if (container.remove(remove, slot, true, false)) {
                    addContainer.add(remove);
                    player.getPriceGuideContainer().updateValues();
                    if (container instanceof PriceGuideContainer) {
                        player.getPriceGuideContainer().shift();
                    }
                    return true;
                }
                return false;
        }
        return false;
    }
}

package plugin.interaction.inter;

import org.gielinor.cache.def.impl.CS2Mapping;
import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.eco.grandexchange.GEGuidePrice;
import org.gielinor.game.content.eco.grandexchange.GEItemSet;
import org.gielinor.game.content.eco.grandexchange.GrandExchange;
import org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeOffer;
import org.gielinor.game.content.eco.grandexchange.offer.OfferState;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.context.IntegerContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.InputStatePacket;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RunScript;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles the Grand Exchange interface options.
 *
 * @author Emperor
 * @version 1.0
 */
public class GrandExchangeInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(25531, this); /* Collection box */
        ComponentDefinition.put(24907, this); /* Main interface */
        ComponentDefinition.put(25042, this); /* Buy interface */
        ComponentDefinition.put(25679, this); /* Sell inventory */
        ComponentDefinition.put(25521, this); /* Item set inventory */
        ComponentDefinition.put(25681, this); /* Player item set inventory */
        return this;
    }

    /**
     * Increments the opened grand exchange offer amount.
     *
     * @param player The player.
     * @param amount The amount to increment.
     */
    private void setOfferAmount(Player player, GrandExchangeOffer grandExchangeOffer, int amount) {
        if (grandExchangeOffer == null || amount < 0 || grandExchangeOffer.getState() != OfferState.PENDING) {
            return;
        }
        grandExchangeOffer.setAmount(amount);
        player.getActionSender().sendString(25062, TextUtils.getFormattedNumber(grandExchangeOffer.getAmount()));
        player.getGrandExchange().updateTotalCost(grandExchangeOffer);
    }

    /**
     * Increments the opened grand exchange offer amount.
     *
     * @param player             The player.
     * @param grandExchangeOffer The {@link org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeOffer}.
     * @param value              The amount to increment.
     */
    @SuppressWarnings("deprecation")
    private void setOfferValue(Player player, GrandExchangeOffer grandExchangeOffer, int value) {
        if (grandExchangeOffer == null || value < 1 || grandExchangeOffer.getState() != OfferState.PENDING) {
            return;
        }
        grandExchangeOffer.setOfferedValue(value);
        player.getActionSender().sendString(25089, TextUtils.getFormattedNumber(grandExchangeOffer.getOfferedValue()) + " gp");
        player.getGrandExchange().updateTotalCost(grandExchangeOffer);
    }

    @Override
    public boolean handle(final Player player, final Component component, final int opcode, final int button, final int slot, final int itemId) {
        World.submit(new Pulse(1, player) {

            @Override
            public boolean pulse() {
                switch (component.getId()) {
                    case 25521:
                    case 25681: // Player inventory
                        handleItemSet(player, component, opcode, button, slot, itemId);
                        return true;
                    case 389:
                        handleSearchInterface(player, opcode, button, slot, itemId);
                        return true;
                    case 25531:
                        handleCollectionBox(player, opcode, button, slot, itemId);
                        return true;
                    case 642:
                        handleGuidePrice(player, opcode, button, slot, itemId);
                        return true;
                    case 25679:
                        handleSellingTab(player, opcode, button, slot, itemId);
                        return true;
                }
                handleMainInterface(player, opcode, button, slot, itemId);
                return true;
            }
        });
        return true;
    }

    /**
     * Handles the search interface options.
     *
     * @param player The player.
     * @param opcode The packet opcode.
     * @param button The button id.
     * @param slot   The slot.
     * @param itemId The item id.
     * @return {@code true} if the option got handled.
     */
    public boolean handleSearchInterface(final Player player, int opcode, int button, int slot, int itemId) {
        switch (button) {
            case 10:
                player.getInterfaceState().closeChatbox();
                return true;
        }
        return false;
    }

    /**
     * Handles the selling tab interface options.
     *
     * @param player The player.
     * @param opcode The packet opcode.
     * @param button The button id.
     * @param slot   The slot.
     * @param itemId The item id.
     * @return {@code true} if the option got handled.
     */
    public boolean handleSellingTab(final Player player, int opcode, int button, int slot, int itemId) {
        if (opcode != 145 || (slot < 0 || slot > 27)) {
            return false;
        }
        Item item = player.getInventory().get(slot);
        if (item == null) {
            return false;
        }
        player.getGrandExchange().constructSale(item);
        return false;
    }

    /**
     * Handles the selling tab interface options.
     *
     * @param player The player.
     * @param opcode The packet opcode.
     * @param button The button id.
     * @param slot   The slot.
     * @param itemId The item id.
     * @return {@code true} if the option got handled.
     */
    public boolean handleCollectionBox(final Player player, int opcode, int button, int slot, int itemId) {
        int index = -1;
        slot = -1;
        switch (button) {
            case 25561:
                slot = 0;
                index = 0;
                break;
            case 25564:
                slot = 1;
                index = 0;
                break;

            case 25567:
                slot = 0;
                index = 1;
                break;
            case 25570:
                slot = 1;
                index = 1;
                break;

            case 25573:
                slot = 0;
                index = 2;
                break;
            case 25576:
                slot = 1;
                index = 2;
                break;

            case 25579:
                slot = 0;
                index = 3;
                break;

            case 25582:
                slot = 1;
                index = 3;
                break;

            case 25585:
                slot = 0;
                index = 4;
                break;
            case 25588:
                slot = 1;
                index = 4;
                break;

            case 25591:
                slot = 0;
                index = 5;
                break;
            case 25594:
                slot = 1;
                index = 5;
                break;
        }
        GrandExchangeOffer grandExchangeOffer;
        if (index > -1 && (grandExchangeOffer = player.getGrandExchange().getGrandExchangeOffers()[index]) != null) {
            player.getGrandExchange().withdraw(grandExchangeOffer, slot, true, opcode == 169, opcode == 185);
        }
        player.getGrandExchange().updateCollectionBox();
        return true;
    }

    /**
     * Handles the item set.
     *
     * @param player The player.
     * @param opcode The opcode.
     * @param button The button.
     * @param slot   The slot.
     * @param itemId The item id.
     */
    private void handleItemSet(Player player, Component component, int opcode, int button, int slot, int itemId) {
        boolean inventory = component.getId() == 25681;
        if (slot < 0 || slot >= (inventory ? 28 : GEItemSet.values().length)) {
            return;
        }
        GEItemSet set = GEItemSet.values()[slot];
        Item item = inventory ? player.getInventory().get(slot) : new Item(set.getItemId());
        if (item == null) {
            return;
        }
        if (inventory && ((set = GEItemSet.forId(item.getId())) == null)) {
            player.getActionSender().sendMessage("This isn't a set item.");
            return;
        }
        switch (opcode) {
            case 129:
            case 117:
                if (inventory) {
                    if (player.getInventory().freeSlots() < set.getComponents().length - 1) {
                        player.getActionSender().sendMessage("You don't have enough inventory space for the component parts.");
                        return;
                    }
                    if (!player.getInventory().remove(item, false)) {
                        return;
                    }
                    for (int id : set.getComponents()) {
                        player.getInventory().add(new Item(id, 1));
                    }
                    player.getInventory().refresh();
                    player.getActionSender().sendMessage("You successfully traded your set for its component items!");
                } else {
                    if (!player.getInventory().containItems(set.getComponents())) {
                        player.getActionSender().sendMessage("You don't have the parts that make up this set.");
                        break;
                    }
                    for (int id : set.getComponents()) {
                        player.getInventory().remove(new Item(id, 1), false);
                    }
                    player.getInventory().add(item);
                    player.getInventory().refresh();
                    player.getActionSender().sendMessage("You successfully traded your item components for a set!");
                }
                player.getAudioManager().send(new Audio(4044, 1, 1));
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, player.getAttribute("container-key", 93), player.getInventory(), false));
                break;
            case 43:
            case 145:
                player.getActionSender().sendMessage((String) CS2Mapping.forId(1089).getMap().get(set.getItemId()));
                break;
        }
    }

    /**
     * Handles the main interface options.
     *
     * @param player The player.
     * @param opcode The packet opcode.
     * @param button The button id.
     * @param slot   The slot.
     * @param itemId The item id.
     * @return {@code true} if the option got handled.
     */
    public boolean handleMainInterface(final Player player, int opcode, int button, int slot, int itemId) {
        final GrandExchangeOffer grandExchangeOffer = player.getGrandExchange().getTemporaryOffer();
        final GrandExchangeOffer openedGrandExchangeOffer = player.getGrandExchange().getOpenedOffer();
        int amount = grandExchangeOffer == null ? 0 : grandExchangeOffer.getAmount();
        switch (button) {
            case 25482:
            case 25485:
                if (openedGrandExchangeOffer == null) {
                    return false;
                }
                player.getGrandExchange().withdraw(openedGrandExchangeOffer, button == 25482 ? 0 : 1, false, opcode == 169, opcode == 185); // Collections
                return true;
            case 25113:
                player.getGrandExchange().confirmOffer();
                return true;
            case 25466: // selection box
            case 25054:
                player.getGrandExchange().openSearch();
                return true;
            case 25478: // ABORT X when offer is in
                if (openedGrandExchangeOffer == null) {
                    return false;
                }
                player.getGrandExchange().abort(openedGrandExchangeOffer.getIndex());
                return true;
            case 24967:
            case 24971:
            case 24975:
            case 24979:
            case 24983:
            case 24987:
                if (opcode == 221) {
                    player.getGrandExchange().abort((button - 24967) >> 2);
                    return true;
                }
                player.getGrandExchange().view((button - 24967) >> 2);
                return true;
            case 24926:
            case 24933:
            case 24940:
            case 24947:
            case 24954:
            case 24961:
                player.getGrandExchange().openBuy(((button - 24926) >> 3) + (button > 24926 ? 1 : 0));
                return true;
            case 24929:
            case 24936:
            case 24943:
            case 24950:
            case 24957:
            case 24964:
                player.getGrandExchange().openSell(((button - 24929) >> 3) + (button > 24929 ? 1 : 0));
                return true;
            case 25063: //-1
                setOfferAmount(player, grandExchangeOffer, amount - 1);
                return true;
            case 25066: //+1
                setOfferAmount(player, grandExchangeOffer, amount + 1);
                return true;
            case 25069: //1
                setOfferAmount(player, grandExchangeOffer, grandExchangeOffer != null && grandExchangeOffer.isSell() ? 1 : amount + 1);
                return true;
            case 25073: //10
                setOfferAmount(player, grandExchangeOffer, grandExchangeOffer != null && grandExchangeOffer.isSell() ? 10 : amount + 10);
                return true;
            case 25077: //100
                setOfferAmount(player, grandExchangeOffer, grandExchangeOffer != null && grandExchangeOffer.isSell() ? 100 : amount + 100);
                return true;
            case 25081: //1000 / sell all
                if (grandExchangeOffer != null && grandExchangeOffer.isSell()) {
                    setOfferAmount(player, grandExchangeOffer, GrandExchange.getInventoryAmount(player, grandExchangeOffer.getItemId()));
                    return true;
                }
                setOfferAmount(player, grandExchangeOffer, amount + 1000);
                return true;
            case 25085: //value x
                player.setAttribute("runscript", new RunScript() {

                    @Override
                    public boolean handle() {
                        setOfferAmount(player, grandExchangeOffer, (int) value);
                        // player.getGrandExchange().openSearch();
                        return true;
                    }
                });
                player.getDialogueInterpreter().sendInput(false, "Enter the amount.");
                return false;
            case 25093: // med price
                if (grandExchangeOffer != null) {
                    setOfferValue(player, grandExchangeOffer, grandExchangeOffer.getEntry().getValue());
                    return true;
                }
                return false;
            case 25090: //mid - 5% value
            case 25096: //mid + 5% value
                if (grandExchangeOffer != null) {
                    double value = grandExchangeOffer.getOfferedValue();
                    value *= (button == 25090 ? 0.95 : 1.05);
                    setOfferValue(player, grandExchangeOffer, (int) Math.ceil(value));
                    return true;
                }
                return false;
            case 25102: //Decrease value by 1
            case 25105: //Increase value with 1
                if (grandExchangeOffer != null) {
                    setOfferValue(player, grandExchangeOffer, grandExchangeOffer.getOfferedValue() + (button == 25102 ? -1 : 1));
                    return true;
                }
                return false;
            case 25099: //Set value x
                if (grandExchangeOffer == null) {
                    player.getActionSender().sendMessage("Please select an offer first.");
                    return true;
                }
                player.setAttribute("runscript", new RunScript() {

                    @Override
                    public boolean handle() {
                        setOfferValue(player, grandExchangeOffer, (int) value);
                        // player.getGrandExchange().openSearch();
                        return true;
                    }
                });
                player.getDialogueInterpreter().sendInput(false, "Enter the amount.");
                return false;
            case 195:
                player.getInterfaceState().close();
                return true;
            case 25110:// go back to main
                player.getGrandExchange().setTemporaryOffer(null);
                PacketRepository.send(InputStatePacket.class, new IntegerContext(player, 0));
                player.getInterfaceState().setOpened(new Component(24907));
                player.getActionSender().sendInterface(24907);
                return true;
        }
        return false;
    }

    /**
     * Method used to handle the guide price opcode.
     *
     * @param player   the player.
     * @param opcode   the opcode.
     * @param buttonId the buttonId.
     * @param slot     the slot.
     * @param itemId   the itemId.
     */
    private void handleGuidePrice(final Player player, final int opcode, final int buttonId, final int slot, final int itemId) {
        switch (opcode) {
            case 155:
                GEGuidePrice.GuideType type = player.getAttribute("guide-price", null);
                if (type == null) {
                    return;
                }
                int subtract = 0;
                if (buttonId >= 15 && buttonId <= 23) {
                    subtract = 15;
                }
                if (buttonId >= 43 && buttonId <= 57) {
                    subtract = 43;
                }
                if (buttonId >= 89 && buttonId <= 103) {
                    subtract = 89;
                }
                if (buttonId >= 135 && buttonId <= 144) {
                    subtract = 135;
                }
                if (buttonId >= 167 && buttonId <= 182) {
                    subtract = 167;
                }
                player.getActionSender().sendMessage(ItemDefinition.forId(type.getItems()[buttonId - subtract].getItem()).getExamine());
                break;
        }
    }
}

package org.gielinor.game.content.eco.grandexchange;

import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.Locale;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.eco.grandexchange.offer.GEOfferDispatch;
import org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeEntry;
import org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeOffer;
import org.gielinor.game.content.eco.grandexchange.offer.OfferState;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.GrandExchangeOfferContext;
import org.gielinor.net.packet.context.IntegerContext;
import org.gielinor.net.packet.context.InventoryInterfaceContext;
import org.gielinor.net.packet.out.GrandExchangeOfferPacket;
import org.gielinor.net.packet.out.InputStatePacket;
import org.gielinor.net.packet.out.InventoryInterface;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.config.ServerVar;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles a player's Grand Exchange.
 *
 * @author Emperor
 */
public final class GrandExchange implements SavingModule {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The grand exchange offers.
     */
    private final GrandExchangeOffer[] grandExchangeOffers = new GrandExchangeOffer[6];

    /**
     * The offer the player is currently constructing.
     */
    private GrandExchangeOffer temporaryOffer;

    /**
     * The grand exchange offer history.
     */
    private GrandExchangeOffer[] history = new GrandExchangeOffer[5];

    /**
     * The currently opened index.
     */
    private int openedIndex = -1;

    /**
     * The sell inventory container.
     */
    private final Container sellContainer;

    /**
     * The sell inventory listener.
     */
    private final GrandExchangeListener sellExchangeListener;

    /**
     * The item set inventory container.
     */
    private final Container itemSetContainer;

    /**
     * The item set inventory listener.
     */
    private final GrandExchangeListener itemSetExchangeListener;

    /**
     * Constructs a new {@code GrandExchange} {@code Object}.
     *
     * @param player The player.
     */
    public GrandExchange(Player player) {
        this.player = player;
        sellContainer = new Container(28).register(sellExchangeListener = new GrandExchangeListener(player, 25679));
        itemSetContainer = new Container(28).register(itemSetExchangeListener = new GrandExchangeListener(player, 25681));
    }

    /**
     * Opens the Grand Exchange menu.
     */
    public void open() {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        update();
        for (GrandExchangeOffer grandExchangeOffer : grandExchangeOffers) {
            if (grandExchangeOffer == null) {
                continue;
            }
            if (grandExchangeOffer.getState() == OfferState.REMOVED) {
                remove(grandExchangeOffer.getIndex(), false);
            }
        }
        player.getInterfaceState().open(new Component(24907)).setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component c) {
                temporaryOffer = null;
                player.getInterfaceState().closeSingleTab();
                PacketRepository.send(InputStatePacket.class, new IntegerContext(player, 0));
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        });
        toMainInterface();
    }

    /**
     * Opens the collection box.
     */
    public void openCollectionBox() {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        updateCollectionBox();
        player.getInterfaceState().openComponent(25531);
    }

    /**
     * Updates the collection box.
     */
    public void updateCollectionBox() {
        int collectionBoxId = 25597;
        int index = 0;
        for (GrandExchangeOffer grandExchangeOffer : player.getGrandExchange().getGrandExchangeOffers()) {
            player.getActionSender().sendInterfaceConfig(grandExchangeOffer == null ? 50 : 51, 25537 + index);
            hide(25543 + index);
            hide(25549 + index);
            if (grandExchangeOffer != null && grandExchangeOffer.isActive()) {
                show((grandExchangeOffer.isSell() ? 25549 : 25543) + index);
            }
            player.getActionSender().sendInterfaceConfig(grandExchangeOffer == null ? 50 : 51, 25555 + index);
            if (grandExchangeOffer != null) {
                player.getActionSender().sendUpdateItem(grandExchangeOffer.getWithdraw()[0], collectionBoxId, 0);
                player.getActionSender().sendUpdateItem(grandExchangeOffer.getWithdraw()[1], collectionBoxId + 1, 0);
                player.getActionSender().sendItemZoomOnInterface(grandExchangeOffer.getItemId(), 55, 25555 + index);
            } else {
                player.getActionSender().sendUpdateItem(null, collectionBoxId, 0);
                player.getActionSender().sendUpdateItem(null, collectionBoxId + 1, 0);
                player.getActionSender().sendItemZoomOnInterface(-1, 55, 25555 + index);
            }
            collectionBoxId += 2;
            index++;
        }
    }

    /**
     * Shows a component.
     *
     * @param componentId The id of the component.
     */
    public void show(int componentId) {
        player.getActionSender().sendInterfaceConfig(51, componentId);
    }

    /**
     * Hides a component.
     *
     * @param componentId The id of the component.
     */
    public void hide(int componentId) {
        player.getActionSender().sendInterfaceConfig(50, componentId);
    }

    /**
     * Opens the history log.
     *
     * @param player The player to open it for.
     */
    public void openHistoryLog(Player player) {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        player.getInterfaceState().open(new Component(25491));
        int[] ids = { 25501, 25505, 25509, 25513, 25517 };
        //25501 = start id
        for (int index = 0; index < 5; index++) {
            GrandExchangeOffer grandExchangeOffer = history[index];
            if (grandExchangeOffer == null) {
                for (int row = 0; row < 4; row++) {
                    player.getActionSender().sendString("-", ids[index] + row);
                }
                continue;
            }
            player.getActionSender().sendString(grandExchangeOffer.isSell() ? "You sold" : "You bought", ids[index]);
            player.getActionSender().sendString(ItemDefinition.forId(grandExchangeOffer.getItemId()).getName(), ids[index] + 1);
            player.getActionSender().sendString(NumberFormat.getNumberInstance(Locale.US).format(grandExchangeOffer.getCompletedAmount()), ids[index] + 2);
            player.getActionSender().sendString(NumberFormat.getNumberInstance(Locale.US).format(grandExchangeOffer.getTotalCoinExchange()) + " gp", ids[index] + 3);
        }
    }

    /**
     * Opens the item sets interface.
     */
    public void openItemSets() {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        player.getActionSender().sendUpdateItems(25530, GEItemSet.getItemArray());
//        player.getInterfaceState().setViewedTab(3);
//        player.getActionSender().sendSidebarTab(3);
//        player.getInterfaceState().removeTabs(0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
//        // TODO
//        player.getInterfaceState().open(new Component(25521)).setCloseEvent(new CloseEvent() {
//            @Override
//            @SuppressWarnings("all")
//            public void close(Player player, Component c) {
//                PacketRepository.send(TooltipPacket.class, new TooltipContext(player, 3214, 25521, null));
//                player.getInterfaceState().openDefaultTabs();
//            }
//
//            @Override
//            public boolean canClose(Player player, Component component) {
//                return true;
//            }
//        });
        itemSetContainer.refresh();
        player.getInventory().getListeners().add(itemSetExchangeListener);
        player.getInventory().refresh();
        player.getInterfaceState().setOverlay(new Component(25681));
        player.getInterfaceState().open(new Component(25521).setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                player.getInventory().getListeners().remove(itemSetContainer.getListeners().get(0));
                player.getInterfaceState().openDefaultTabs();
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        }));
        PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(player, new Component(25521), new Component(25680)));
    }

    /**
     * Returns to the main interface.
     */
    public void toMainInterface() {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        Component component = new Component(24907);
        if (player.getInterfaceState().getOpened() == null || player.getInterfaceState().getOpened().getId() != 24907) {
            player.getInterfaceState().open(component);
        } else {
            player.getInterfaceState().setOpened(component);
            player.getActionSender().sendInterface(24907);
        }
        PacketRepository.send(InputStatePacket.class, new IntegerContext(player, 0));
        openedIndex = -1;
        hideDonorSlots();
    }

    /**
     * Hides the donor slots for regular players.
     */
    public void hideDonorSlots() {
        boolean hidden = player.getDonorManager().hasMembership();
        // todo check if slot in use
        for (int id : GrandExchangeSlot.SLOT_5.getDisableIds()) {
            player.getActionSender().sendInterfaceConfig(hidden ? 52 : 53, id);
        }
        player.getActionSender().sendInterfaceConfig(!hidden ? 51 : 50, 25474);

        for (int id : GrandExchangeSlot.SLOT_6.getDisableIds()) {
            player.getActionSender().sendInterfaceConfig(hidden ? 52 : 53, id);
        }
        player.getActionSender().sendInterfaceConfig(!hidden ? 51 : 50, 25475);
    }

    @Override
    public void save(ByteBuffer buffer) {
        for (GrandExchangeOffer grandExchangeOffer : grandExchangeOffers) {
            if (grandExchangeOffer == null) {
                continue;
            }
            buffer.put((byte) grandExchangeOffer.getIndex());
            buffer.putLong(grandExchangeOffer.getUid());
        }
        buffer.put((byte) -1);
        for (GrandExchangeOffer grandExchangeOffer : history) {
            if (grandExchangeOffer == null) {
                buffer.put((byte) -1);
                continue;
            }
            buffer.put((byte) (grandExchangeOffer.isSell() ? 1 : 0));
            buffer.putShort((short) grandExchangeOffer.getItemId());
            buffer.putInt(grandExchangeOffer.getTotalCoinExchange());
            buffer.putInt(grandExchangeOffer.getCompletedAmount());
        }
    }

    @Override
    public void parse(ByteBuffer buffer) {
        int index = -1;
        GrandExchangeOffer o;
        while ((index = buffer.get()) != -1) {
            o = grandExchangeOffers[index] = GEOfferDispatch.forUID(buffer.getLong());
            if (o != null) {
                o.setIndex(index);
            }
        }
        for (int i = 0; i < history.length; i++) {
            int s = buffer.get();
            if (s == -1) {
                continue;
            }
            o = history[i] = new GrandExchangeOffer(buffer.getShort(), s == 1);
            o.setTotalCoinExchange(buffer.getInt());
            o.setCompletedAmount(buffer.getInt());
        }
    }

    /**
     * Initializes the grand exchange.
     */
    public void init() {
        boolean updated = false;
        for (GrandExchangeOffer offer : grandExchangeOffers) {
            if (offer != null) {
                offer.setPlayer(player);
                if (!updated && (offer.getWithdraw()[0] != null || offer.getWithdraw()[1] != null)) {
                    updated = true;
                }
            }
        }
        update();
        if (updated) {
            player.getActionSender().sendMessage("You have items from the Grand Exchange waiting in your collection box.");
        }
    }

    /**
     * Updates the grand exchange data.
     */
    public void update() {
        for (GrandExchangeOffer offer : grandExchangeOffers) {
            update(offer);
        }
    }

    /**
     * Updates a grand exchange offer.
     *
     * @param grandExchangeOffer The offer to update.
     */
    public void update(GrandExchangeOffer grandExchangeOffer) {
        if (grandExchangeOffer == null) {
            return;
        }
        PacketRepository.send(GrandExchangeOfferPacket.class, new GrandExchangeOfferContext(player, grandExchangeOffer));
    }

    /**
     * Constructs a new buy offer.
     *
     * @param itemId The item id.
     */
    public void constructBuy(int itemId) {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        if (openedIndex < 0) {
            return;
        }
        temporaryOffer = new GrandExchangeOffer(itemId, false);
        if (temporaryOffer.getEntry() == null) {
            player.getActionSender().sendMessage("This item has been blacklisted from the Grand Exchange.");
            return;
        }
        temporaryOffer.setPlayer(player);
        temporaryOffer.setDefault();
        sendConfiguration(temporaryOffer, false, false);
    }

    /**
     * Constructs a new sale offer.
     *
     * @param item The item to sell.
     */
    public void constructSale(Item item) {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        if (openedIndex < 0 || grandExchangeOffers[openedIndex] != null) {
            return;
        }
        if (item.getId() == Item.COINS) {
            player.getActionSender().sendMessage("You can't offer money!");
            return;
        }
        // Disallow listing moderator tools.
        if (item.getId() == Item.BAN_HAMMER || item.getId() == Item.ROTTEN_POTATO) {
            player.getActionSender().sendMessage(item.getName() + " cannot be sold on the Grand Exchange.");
            return;
        }
        int id = item.getId();
        if (!item.getDefinition().isUnnoted()) {
            id = item.getNoteChange();
        }
        if (GrandExchangeDatabase.getDatabase().get(id) == null) {
            player.getActionSender().sendMessage("This item can't be sold on the Grand Exchange.");
            return;
        }
        temporaryOffer = new GrandExchangeOffer(id, true);
        temporaryOffer.setPlayer(player);
        temporaryOffer.setDefault();
        temporaryOffer.setAmount(item.getCount());
        sendConfiguration(temporaryOffer, true, false);
    }

    /**
     * Obtains the total amount of the item being exchanged.
     *
     * @param player The player.
     * @param itemId the item id.
     * @return The count of items in the player's inventory.
     */
    public static int getInventoryAmount(Player player, int itemId) {
        Item item = new Item(itemId);
        int note = item.getNoteChange();
        int amount = player.getInventory().getCount(item);
        if (note > -1) {
            amount += player.getInventory().getCount(new Item(note));
        }
        if (amount > 1 && item.getDefinition().isStackable()) {
            return amount / 2;
        }
        return amount;
    }

    /**
     * Confirms the current offer.
     */
    public void confirmOffer() {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        if (openedIndex < 0 || temporaryOffer == null) {
            return;
        }
        if (temporaryOffer.getAmount() > (Integer.MAX_VALUE / temporaryOffer.getOfferedValue())) {
            //   player.getActionSender().sendSound(new Audio(4039, 1, 1));
            player.getActionSender().sendMessage("You can't " + (temporaryOffer.isSell() ? "sell " : "buy ") + " this much!");
            return;
        }
        temporaryOffer.setIndex(openedIndex);
        if (temporaryOffer.isSell()) {
            int maxAmount = getInventoryAmount(player, temporaryOffer.getItemId());
            if (temporaryOffer.getAmount() > maxAmount) {
                //   player.getActionSender().sendSound(new Audio(4039, 1, 1));
                player.getActionSender().sendMessage("You do not have enough of this item in your inventory to cover the");
                player.getActionSender().sendMessage("offer.");
                return;
            }
            Item item;
            int amountLeft = temporaryOffer.getAmount() - player.getInventory().getCount(new Item(temporaryOffer.getItemId()));
            boolean remove = player.getInventory().remove(item = new Item(temporaryOffer.getItemId(), temporaryOffer.getAmount()));
            int note;
            if (amountLeft > 0) {
                if ((note = item.getNoteChange()) > 0) {
                    player.getInventory().remove(new Item(note, amountLeft));
                } else if (remove) {
                    player.getInventory().add(new Item(temporaryOffer.getItemId(), temporaryOffer.getAmount() - amountLeft));
                    return;
                }
            }
            if (GEOfferDispatch.dispatch(player, temporaryOffer)) {
                grandExchangeOffers[openedIndex] = temporaryOffer;
                GEOfferDispatch.updateOffer(temporaryOffer);
            }
        } else {
            int total = temporaryOffer.getAmount() * temporaryOffer.getOfferedValue();
            int count = player.getInventory().getCount(Item.COINS);
            if (total > count) {
                player.getActionSender().sendMessage("You do not have enough coins to cover the offer.");
                return;
            }
            if (GEOfferDispatch.dispatch(player, temporaryOffer) && player.getInventory().remove(new Item(Item.COINS, total))) {
                grandExchangeOffers[openedIndex] = temporaryOffer;
                GEOfferDispatch.updateOffer(temporaryOffer);
            }
        }
        toMainInterface();
        temporaryOffer = null;
    }

    /**
     * Aborts an offer.
     *
     * @param index The offer index.
     */
    public void abort(int index) {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        GrandExchangeOffer grandExchangeOffer = grandExchangeOffers[index];
        player.getActionSender().sendMessage("Abort request acknowledged. Please be aware that your offer may");
        player.getActionSender().sendMessage("have already been completed.");
        player.getActionSender().sendInterfaceConfig(50, 25478);
        if (grandExchangeOffer == null || !grandExchangeOffer.isActive()) {
            return;
        }
        grandExchangeOffer.setState(OfferState.ABORTED);
        if (grandExchangeOffer.isSell()) {
            grandExchangeOffer.addWithdraw(grandExchangeOffer.getItemId(), grandExchangeOffer.getAmountLeft(), true);
        } else {
            grandExchangeOffer.addWithdraw(Item.COINS, grandExchangeOffer.getAmountLeft() * grandExchangeOffer.getOfferedValue(), true);
        }
        player.getGrandExchange().update(grandExchangeOffer);
        GEOfferDispatch.setDumpDatabase(true);
    }

    /**
     * Removes an offer.
     *
     * @param index The offer index.
     */
    public boolean remove(int index, boolean collectionBox) {
        GrandExchangeOffer offer;
        if ((offer = grandExchangeOffers[index]) == null) {
            return false;
        }
        if (offer.getCompletedAmount() > 0) {
            logHistory(offer);
        }
        offer.setWithdraw(new Item[2]);
        offer.setUid(0);
        offer.setState(OfferState.REMOVED);
        grandExchangeOffers[index] = null;
        update(offer);
        if (!collectionBox) {
            toMainInterface();
        }
        return GEOfferDispatch.remove(offer.getUid());
    }

    /**
     * Adds the completed offer to the log.
     *
     * @param offer The completed offer.
     */
    public void logHistory(GrandExchangeOffer offer) {
        GrandExchangeOffer[] newHistory = new GrandExchangeOffer[5];
        newHistory[0] = offer;
        System.arraycopy(history, 0, newHistory, 1, 4);
        history = newHistory;
    }

    /**
     * Views a registered offer.
     *
     * @param index The index.
     */
    public void view(int index) {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        if (grandExchangeOffers[index] == null) {
            return;
        }
        this.openedIndex = index;
        sendConfiguration(grandExchangeOffers[index], grandExchangeOffers[index].isSell(), true);
        player.getInterfaceState().setOpened(new Component(25042));
        player.getActionSender().sendInterface(25042);
    }

    /**
     * Opens the buying screen.
     *
     * @param index The offer index.
     */
    public void openBuy(int index) {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        if (index > 3 && !player.getDonorManager().hasMembership()) {
            player.getActionSender().sendMessage("You have to be a member to unlock this slot.");
            return;
        }
        this.openedIndex = index;
        sendConfiguration(grandExchangeOffers[index], false, false);
        player.getInterfaceState().setOpened(new Component(25042));
        player.getActionSender().sendInterface(25042);
        openSearch();
    }

    /**
     * Opens the selling screen.
     */
    public void openSell(int index) {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        if (index > 3 && !player.getDonorManager().hasMembership()) {
            player.getActionSender().sendMessage("You have to be a member to unlock this slot.");
            return;
        }
        this.openedIndex = index;
        sellContainer.refresh();
        player.getInventory().getListeners().add(sellExchangeListener);
        player.getInventory().refresh();
        player.getInterfaceState().setOpened(new Component(25042).setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                player.getInventory().getListeners().remove(sellContainer.getListeners().get(0));
                player.getInterfaceState().openDefaultTabs();
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        }));
        player.getInterfaceState().setOverlay(new Component(25679));
        sendConfiguration(grandExchangeOffers[index], true, false);
        player.getActionSender().sendInterface(25042);
        PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(player, new Component(25042), new Component(25678)));
    }

    /**
     * Withdraws an item.
     *
     * @param grandExchangeOffer The offer to withdraw from.
     * @param index              The item index.
     * @param collectionBox      Whether or not we're on the collection box component.
     * @param noteItem           Whether or not to note the item on withdraw.
     * @param bankItem           Whether or not to bank the item on withdraw.
     */
    public void withdraw(GrandExchangeOffer grandExchangeOffer, int index, boolean collectionBox, boolean noteItem, boolean bankItem) {
        Item item = grandExchangeOffer.getWithdraw()[index];
        if (item == null) {
            return;
        }
        if (bankItem) {
            if (player.getBank().getMaximumAdd(item) < item.getCount()) {
                player.getActionSender().sendMessage("You do not have enough room in your bank.");
                return;
            }
            player.getBank().add(item);
        } else {
            if (player.getInventory().getMaximumAdd(item) < item.getCount() || noteItem) {
                int note = item.getNoteChange();
                if (note == -1 || player.getInventory().getMaximumAdd(new Item(note)) < item.getCount()) {
                    player.getActionSender().sendMessage("You do not have enough room in your inventory.");
                    return;
                }
                if (!noteItem) {
                    if (item.getCount() < 1) {
                        grandExchangeOffer.getWithdraw()[index] = null;
                        return;
                    }
                    int maxAdd = player.getInventory().freeSlots();
                    if (maxAdd < 1) {
                        player.getActionSender().sendMessage("You do not have enough room in your inventory.");
                        return;
                    }
                    Item maxItem = new Item(item.getId(), maxAdd);
                    if (player.getInventory().getMaximumAdd(maxItem) < maxAdd) {
                        player.getActionSender().sendMessage("You do not have enough room in your inventory.");
                        return;
                    }
                    grandExchangeOffer.getWithdraw()[index].setCount(grandExchangeOffer.getWithdraw()[index].getCount() - maxAdd);
                    player.getInventory().add(maxItem);
                    if (maxAdd == item.getCount()) {
                        grandExchangeOffer.getWithdraw()[index] = null;
                    }
                    return;
                }
                player.getInventory().add(new Item(note, item.getCount()));
            } else {
                player.getInventory().add(item);
            }
        }
        grandExchangeOffer.getWithdraw()[index] = null;
        if (!grandExchangeOffer.isActive() && grandExchangeOffer.getWithdraw()[0] == null && grandExchangeOffer.getWithdraw()[1] == null) {
            player.getGrandExchange().remove(grandExchangeOffer.getIndex(), collectionBox);
        }
        //player.getActionSender().sendSound(new Audio(4040, 1, 1));
        grandExchangeOffer.sendItems();
        GEOfferDispatch.setDumpDatabase(true);
        updateCollectionBox();
        if (!collectionBox) {
            if ((grandExchangeOffer.getWithdraw()[0] == null && grandExchangeOffer.getWithdraw()[1] == null) && !grandExchangeOffer.isActive()) {
                update();
                setTemporaryOffer(null);
                PacketRepository.send(InputStatePacket.class, new IntegerContext(player, 0));
                player.getInterfaceState().setOpened(new Component(24907));
                player.getActionSender().sendInterface(24907);
            }
        }
    }

    /**
     * Opens the search interface.
     */
    public void openSearch() {
        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
            return;
        }
        PacketRepository.send(InputStatePacket.class, new IntegerContext(player, 3));
    }

    /**
     * Toggles the offer components quantity / price buttons and view status'.
     *
     * @param enabled If the view screen is enabled.
     */
    public void toggleViewScreen(boolean enabled, int offerSlot, boolean aborted) {
        player.getActionSender().sendInterfaceConfig(332, enabled ? (offerSlot + 1) : -1);
        // Toggle offer screen
        for (int buttonId = 25068; buttonId < 25086; buttonId++) {
            player.getActionSender().sendInterfaceConfig(enabled ? 50 : 51, buttonId);
        }
        for (int buttonId = 25090; buttonId < 25100; buttonId++) {
            player.getActionSender().sendInterfaceConfig(enabled ? 50 : 51, buttonId);
        }
        for (int buttonId = 25063; buttonId < 25068; buttonId++) {
            player.getActionSender().sendInterfaceConfig(enabled ? 50 : 51, buttonId);
        }
        for (int buttonId = 25102; buttonId < 25107; buttonId++) {
            player.getActionSender().sendInterfaceConfig(enabled ? 50 : 51, buttonId);
        }
        player.getActionSender().sendInterfaceConfig(enabled ? 50 : 51, 25113);

        // Toggle view screen
        player.getActionSender().sendInterfaceConfig(enabled ? 51 : 50, 25476);
        player.getActionSender().sendInterfaceConfig(enabled ? 51 : 50, 25477);
        player.getActionSender().sendInterfaceConfig((enabled && !aborted) ? 51 : 50, 25478);
        player.getActionSender().sendInterfaceConfig(enabled ? 51 : 50, 25479);
        player.getActionSender().sendInterfaceConfig(enabled ? 51 : 50, 25481);
        player.getActionSender().sendInterfaceConfig(enabled ? 51 : 50, 25482);
        player.getActionSender().sendInterfaceConfig(enabled ? 51 : 50, 25483);
        player.getActionSender().sendInterfaceConfig(enabled ? 51 : 50, 25485);
        player.getActionSender().sendInterfaceConfig(enabled ? 51 : 50, 25486);
        player.getActionSender().sendInterfaceConfig(enabled ? 52 : 53, 25488);
        player.getActionSender().sendInterfaceConfig(enabled ? 52 : 53, 25489);
        player.getActionSender().sendInterfaceConfig(enabled ? 51 : 50, 25490);
    }

    /**
     * Sends the configuration packets for the offer.
     *
     * @param grandExchangeOffer The grand exchange offer.
     * @param sell               If it's a selling offer.
     * @param view               If we're viewing the view screen.
     */
    //hide = 50
    public void sendConfiguration(GrandExchangeOffer grandExchangeOffer, boolean sell, boolean view) {
        player.getActionSender().sendInterfaceConfig(InterfaceConfiguration.GRAND_EXCHANGE_OFFER_TYPE.getId(), sell ? 1 : 0);
        player.getActionSender().sendInterfaceConfig((sell || view) ? 51 : 50, 25469);
        player.getActionSender().sendInterfaceConfig((sell || view) ? 50 : 51, 25054);
        player.getActionSender().sendInterfaceConfig((sell || view) ? 50 : 51, 25055);
        player.getActionSender().sendInterfaceConfig((sell || view) ? 51 : 50, 25466);
        player.getActionSender().sendHideComponent(25466, (sell || view));
        player.getActionSender().sendHideComponent(25055, !(sell || view));
        player.getActionSender().sendString(25048, sell ? "Sell Offer" : "Buy Offer");
        toggleViewScreen(view, grandExchangeOffer == null ? -1 : grandExchangeOffer.getIndex(),
            grandExchangeOffer != null && grandExchangeOffer.isAborted());
        if (grandExchangeOffer == null) {
            resetOfferScreen(sell);
            return;
        }
        if (view) {
            String status = sell ? "have sold" : "have bought";
            if (grandExchangeOffer.isAborted() || grandExchangeOffer.isCompleted()) {
                status = sell ? "sold" : "bought";
            }
            if (grandExchangeOffer.isBuyingState() || grandExchangeOffer.isSellingState()) {
                status = sell ? "have sold" : "have bought";
            }
            player.getActionSender().sendString(25476, "You " + status + " a total of <col=CC9900>" +
                TextUtils.getFormattedNumber(grandExchangeOffer.getCompletedAmount()) +
                (((grandExchangeOffer.isAborted() || grandExchangeOffer.isCompleted())) ? "" : "<col=C1A875> so far"));
            player.getActionSender().sendString(25477, "for a total price of <col=CC9900>" +
                TextUtils.getFormattedNumber(grandExchangeOffer.getTotalCoinExchange())
                + "<col=C1A875> gp.");
            player.getActionSender().sendInterfaceConfig((grandExchangeOffer.isAborted() ||
                grandExchangeOffer.isCompleted()) ? 50 : 51, 25478);
        }
        PacketRepository.send(InputStatePacket.class, new IntegerContext(player, 0));
        GrandExchangeEntry grandExchangeEntry = grandExchangeOffer.getEntry();
        String itemExamine = ItemDefinition.forId(grandExchangeOffer.getItemId()).getExamine();
        int examineWidth = itemExamine.length();
        String finalExamine = "";
        if (examineWidth > 47) {
            String[] examines = itemExamine.split(" ");
            for (String examine : examines) {
                finalExamine += examine + " ";
                if (finalExamine.length() >= 46) {
                    finalExamine += "\\n";
                    break;
                }
            }
            int length = finalExamine.length();
            if (length < itemExamine.length()) {
                itemExamine = finalExamine +
                    itemExamine.substring(length - 2,
                        itemExamine.length());
            }
        }
        ItemDefinition itemDefinition = ItemDefinition.forId(grandExchangeOffer.getItemId());
        player.getActionSender().sendInterfaceConfig(329, 1);
        player.getActionSender().sendString(25050, itemDefinition.getName());
        player.getActionSender().sendString(25051, itemExamine);
        int value = grandExchangeEntry.getValue();
        player.getActionSender().sendString(25439, TextUtils.getFormattedNumber(value));
        player.getActionSender().sendString(25458, TextUtils.getFormattedNumber(value));
        double minimumValue = value * 0.95;
        double maximumValue = value * 1.05;
        // TODO Remove spaces after > ?
        player.getActionSender().sendString(25057, "<img=21> " + TextUtils.getFormattedNumber(value) + " gp");
        player.getActionSender().sendString(25052, "<img=22> " + TextUtils.getFormattedNumber((int) minimumValue) +
            " gp - " + TextUtils.getFormattedNumber((int) maximumValue) + " gp <img=23>");
        player.getActionSender().sendUpdateItem(new Item(itemDefinition.getId()), 25060, 0);
        player.getActionSender().sendString(25062, TextUtils.getFormattedNumber(grandExchangeOffer.getAmount()));
        player.getActionSender().sendString(25089, TextUtils.getFormattedNumber(grandExchangeOffer.getOfferedValue()) + " gp");
        player.getActionSender().sendString(25108, TextUtils.getFormattedNumber(grandExchangeEntry.getValue()) + " gp");
        player.getActionSender().sendInterfaceConfig(50, 25054);
        player.getActionSender().sendInterfaceConfig(51, 25466);
        player.getActionSender().sendHideComponent(25055, false);
        updateTotalCost(grandExchangeOffer);
        grandExchangeOffer.sendItems();
    }

    /**
     * Resets the offer screen.
     */
    public void resetOfferScreen(boolean sell) {
        player.getActionSender().sendString(25050, "Choose an item to exchange");
        player.getActionSender().sendString(25051, sell ? "Select an item in your inventory to sell." : "Click the icon to the left to search for items.");
        player.getActionSender().sendString(25052, "<img=22> N/A <img=23>");
        player.getActionSender().sendString(25439, "0 gp");
        player.getActionSender().sendString(25458, "0 gp");
        player.getActionSender().sendString(25057, "<img=21>N/A");
        player.getActionSender().sendString(25052, "<img=22> N/A <img=23>");
        player.getActionSender().sendUpdateItem(null, 25060, 0);
        player.getActionSender().sendString(25062, "0");
        player.getActionSender().sendString(25089, "0 gp"); // per item
        player.getActionSender().sendString(25108, "0 gp");
        player.getActionSender().sendInterfaceConfig(329, 0);
        player.getActionSender().sendUpdateItem(null, 25488, 0);
        player.getActionSender().sendUpdateItem(null, 25489, 0);
    }

    /**
     * Gets the currently opened offer.
     *
     * @return The grand exchange offer currently opened.
     */
    public GrandExchangeOffer getOpenedOffer() {
        if (openedIndex < 0) {
            return null;
        }
        return grandExchangeOffers[openedIndex];
    }

    /**
     * Checks if the player has an active offer.
     *
     * @return {@code True} if so.
     */
    public boolean hasActiveOffer() {
        for (GrandExchangeOffer offer : grandExchangeOffers) {
            if (offer != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the total value.
     *
     * @param grandExchangeOffer {@link org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeOffer}.
     */
    public void updateTotalCost(GrandExchangeOffer grandExchangeOffer) {
        if (grandExchangeOffer.getAmount() > (Integer.MAX_VALUE / grandExchangeOffer.getOfferedValue())) {
            player.getActionSender().sendString(25108, "Too high!");
            return;
        }
        int value = grandExchangeOffer.getOfferedValue() * grandExchangeOffer.getAmount();
        player.getActionSender().sendString(25108, TextUtils.getFormattedNumber(value) + " gp");
    }

    /**
     * Gets the offers.
     *
     * @return The offers.
     */
    public GrandExchangeOffer[] getGrandExchangeOffers() {
        return grandExchangeOffers;
    }

    /**
     * Gets the openedIndex.
     *
     * @return The openedIndex.
     */
    public int getOpenedIndex() {
        return openedIndex;
    }

    /**
     * Sets the openedIndex.
     *
     * @param openedIndex The openedIndex to set.
     */
    public void setOpenedIndex(int openedIndex) {
        this.openedIndex = openedIndex;
    }

    /**
     * Gets the temporaryOffer.
     *
     * @return The temporaryOffer.
     */
    public GrandExchangeOffer getTemporaryOffer() {
        return temporaryOffer;
    }

    /**
     * Sets the temporaryOffer.
     *
     * @param temporaryOffer The temporaryOffer to set.
     */
    public void setTemporaryOffer(GrandExchangeOffer temporaryOffer) {
        this.temporaryOffer = temporaryOffer;
    }

    /**
     * Gets the history.
     *
     * @return The history.
     */
    public GrandExchangeOffer[] getHistory() {
        return history;
    }

}

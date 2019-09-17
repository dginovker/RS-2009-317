package org.gielinor.rs2.model.container.impl;

import java.util.*;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Ironman;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.context.InventoryInterfaceContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.net.packet.out.InventoryInterface;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerEvent;
import org.gielinor.rs2.model.container.ContainerListener;
import org.gielinor.rs2.model.container.ContainerType;
import org.gielinor.rs2.model.container.SortType;
import org.gielinor.rs2.model.container.impl.bank.BankData;

/**
 * Represents the bank container.
 *
 * @author Emperor
 */
public final class BankContainer extends Container {

    /**
     * The base size for regular players.
     */
    public static final int BASE_SIZE = 800;
    /**
     * The bank size.
     */
    public static final int SIZE = 952;//952;
    /**
     * The player inventory interface.
     */
    public static final int PLAYER_INVENTORY_INTERFACE = 5064;
    /**
     * The bank inventory interface.
     */
    public static final int BANK_INVENTORY_INTERFACE = 5382;
    /**
     * The amount a member can have of items in their bank.
     */
    public static int MEMBER_CAPACITY = SIZE;
    /**
     * The bank listener.
     */
    private final BankListener listener;
    /**
     * The {@link org.gielinor.rs2.model.container.impl.bank.BankData}.
     */
    private final BankData bankData;
    /**
     * The player reference.
     */
    private Player player;
    /**
     * Set <code>True</code> to note items.
     */
    private boolean noteItems;
    /**
     * Set <code>True</code> to insert items.
     */
    private boolean insertItems;
    /**
     * If the bank is open.
     */
    private boolean open;

    /**
     * Construct a new {@code BankContainer} {@code Object}.
     *
     * @param player The player reference.
     */
    public BankContainer(Player player) {
        super(SIZE, ContainerType.ALWAYS_STACK, SortType.TYPE_AND_METADATA);
        super.register(listener = new BankListener(player));
        this.player = player;
        this.bankData = new BankData(player);
    }

    /**
     * Open the bank.
     */
    public void open() {
        if (!Ironman.openBank(player)) {
            return;
        }
        if (player.getBankPin().isPinBlocked()) {
            player.getBankPin().displayPinLockMessage();
            return;
        }
//		if (player.getBankPin().open(new Component(536))) {
//			return;
//		}
        if (open) {
            return;
        }

        // Send open tab
        if (Constants.BANK_TABS) {
            getBankData().setOpenTab(bankData.getOpenTab());
            if (Arrays.stream(bankData.getTabAmounts()).sum() != player.getBank().itemCount()) {
                bankData.setTabAmounts(new int[]{ itemCount(), 0, 0, 0, 0, 0, 0, 0, 0, 0 });
            }
            updateScroll();
        }
        super.refresh();
        player.getInventory().getListeners().add(listener);
        player.getInventory().refresh();
        handleCapacity();
        player.getInterfaceState().setOpened(new Component(5382).setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                BankContainer.this.close();
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        }));
        player.getInterfaceState().setOverlay(new Component(5064)); // TODO singleTab?
        PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(player, new Component(5292), new Component(5063)));
        open = true;
        shift();
    }

    public String getValue() {
        int finalValue = player.getBankNetworth();
        if (finalValue >= 1000 && finalValue < 1000000) {
            return " ~ " + (finalValue / 1000) + "K gp";
        } else if (finalValue >= 1000000) {
            return " ~ " + (finalValue / 1000000) + "M gp";
        }
        return " ~ " + finalValue + " gp";
    }

    /**
     * Updates the max scroll length of the bank.
     */
    public void updateScroll() {
        if (getBankData().getOpenTab() == 0) {
            PacketRepository.send(InterfaceMaxScrollPacket.class, new InterfaceMaxScrollContext(player, 5385, 4530));
            return;
        }
        //64
        int maxScroll = 213;
        int tabCount = player.getBank().getBankData().getTabAmount(getBankData().getOpenTab());
        if (tabCount >= 48) {
            maxScroll = ((player.getBank().getBankData().getTabAmount(getBankData().getOpenTab()) / 8) + 1) * 36;
        }
        PacketRepository.send(InterfaceMaxScrollPacket.class, new InterfaceMaxScrollContext(player, 5385, maxScroll));
    }

    /**
     * Closes the bank.
     */
    public void close() {
        open = false;
        player.getInventory().getListeners().remove(listener);
        player.getInterfaceState().closeSingleTab();
        //TODO: Add anything else for banking.
    }

    /**
     * Adds an item to the bank container.
     *
     * @param slot      The item slot.
     * @param amount    The amount.
     * @param container
     */
    public boolean addItem(int slot, int amount, Container container) {
        Container container1 = container == null ? player.getInventory() : container;
        if (slot < 0 || slot > container1.capacity() || amount < 1) {
            return false;
        }
        Item item = container1.get(slot);
        if (item == null) {
            return false;
        }

        boolean isNote = !item.getDefinition().isUnnoted();
        Item unnoted = new Item(item.getDefinition().getNoteId(), item.getCount(), item.getCharge());

        int maximum = container1.getCount(item);
        if (amount > maximum) {
            amount = maximum;
        }
        int maxCount = super.getMaximumAdd(isNote ? unnoted : item, player);
        if (amount > maxCount) {
            amount = maxCount;
            if (amount < 1) {
                player.getActionSender().sendMessage("There is not enough space left in your bank.");
                return false;
            }
        }
        if (!item.getDefinition().getConfiguration(ItemConfiguration.BANKABLE, true)) {
            player.getActionSender().sendMessage("A magical force prevents you from banking this item");
            return false;
        }
        item = new Item(item.getId(), amount, item.getCharge());
        boolean unnote = !item.getDefinition().isUnnoted();
        if (container1.remove(item, slot, true)) {
            Item add = unnote ? new Item(item.getDefinition().getNoteId(), amount, item.getCharge()) : item;
            if (unnote && !add.getDefinition().isUnnoted()) {
                add = item;
            }
            boolean contains = contains(add.getId());
            if (super.add(add)) {
                if (!contains && Constants.BANK_TABS) {
                    int bankTab = getBankData().getOpenTab();
                    getBankData().changeTabAmount(bankTab, 1, false);
                    insert(getSlot(add.getId()), bankTab == 0 ? getBankData().getTabAmount(bankTab) - 1 : getBankData().getData(bankTab, 1));
                    update(true);
                }
            }
            handleCapacity();
            return true;
        }
        return false;
    }

    /**
     * Adds a container of items to the bank container.
     *
     * @param item      The item.
     * @param container The container to remove the item from.
     */
    public boolean addFrom(Item item, Container container) {
        int amount = item.getCount();
        if (amount < 1) {
            return false;
        }
        int maximum = container.getCount(item);
        if (amount > maximum) {
            amount = maximum;
        }
        int maxCount = super.getMaximumAdd(item, player);
        if (amount > maxCount) {
            amount = maxCount;
            if (amount < 1) {
                return false;
            }
        }
        if (!item.getDefinition().getConfiguration(ItemConfiguration.BANKABLE, true)) {
            return false;
        }
        item = new Item(item.getId(), amount, item.getCharge());
        boolean unnote = !item.getDefinition().isUnnoted();
        if (container.remove(item, true)) {
            Item add = unnote ? new Item(item.getDefinition().getNoteId(), amount, item.getCharge()) : item;
            if (unnote && !add.getDefinition().isUnnoted()) {
                add = item;
            }
            boolean contains = contains(add.getId());
            if (super.add(add)) {
                if (!contains && Constants.BANK_TABS) {
                    getBankData().changeTabAmount(getBankData().getOpenTab(), 1, false);
                    insert(getSlot(add.getId()), getBankData().getOpenTab() == 0 ?
                        getBankData().getTabAmount(getBankData().getOpenTab()) - 1 : getBankData().getData(getBankData().getOpenTab(), 1));
                    update();
                    refresh();
                }
            }
            handleCapacity();
            return true;
        }
        return false;
    }

    /**
     * Deposits all items from a given container into the bank
     * @param container The container to deposit items from
     * @param itemsToAdd The items to deposit
     * @return Whether or not all items were deposited
     */
    public boolean depositAll(Container container, Item... itemsToAdd) {
        int count = 0;
        boolean notAllItemsCouldBeBanked = false;
        List<Integer> uniques = new ArrayList<>();
        List<Item> toRemove = new ArrayList<>();

        for (Item toAdd : itemsToAdd) {
            boolean contains = contains(toAdd.getId());
            int amount = container.getCount(toAdd);
            boolean isNote = !toAdd.getDefinition().isUnnoted();

            // make copies to prevent toAdd from being modified
            Item itemToBank = toAdd.copy();
            Item itemToRemove = toAdd.copy();

            // if the item is a note, convert the item to bank into the unnoted form (we dont want notes in the bank!)
            if (isNote) {
                itemToBank = new Item(toAdd.getDefinition().getNoteId(), amount, toAdd.getCharge());
            }

            int maxAdd = super.getMaximumAdd(itemToBank, player);

            // if we can't deposit all the items we want to
            if (amount > maxAdd) {
                amount = maxAdd;
                notAllItemsCouldBeBanked = true;

                // if we can't deposit anything there's no point continuing
                if (maxAdd < 1) {
                    continue;
                }
            }

            itemToBank.setCount(amount);

            // if the item definition prevents it from being banked we skip the item
            if (!toAdd.getDefinition().getConfiguration(ItemConfiguration.BANKABLE, true)) {
                notAllItemsCouldBeBanked = true;
                continue;
            }

            if (super.hasRoomFor(itemToBank)) {
                // if we haven't seen the item id before
                if (!uniques.contains(toAdd.getId())) {
                    if (!contains && Constants.BANK_TABS) {
                        count++; // increases how many we should update the amount of bank slots by
                    }
                    super.add(itemToBank, false);
                    uniques.add(itemToRemove.getId()); // add the item id to a list of items we've already seen
                }

                if ((itemToRemove.getCount() - maxAdd) > 0) {
                    itemToRemove.setCount(maxAdd);
                }

                // add the item to the list of items to remove
                toRemove.add(itemToRemove);
            }
        }

        // if there items to remove, remove them
        if (toRemove.size() > 0) {
            Item[] itemsToRemove = new Item[toRemove.size()];
            itemsToRemove = toRemove.toArray(itemsToRemove);
            container.remove(itemsToRemove);
        }

        getBankData().changeTabAmount(getBankData().getOpenTab(), count, false);
        update();
        refresh();
        handleCapacity();

        if (notAllItemsCouldBeBanked) {
            if (toRemove.size() == 0) {
                player.getActionSender().sendMessage("There is not enough space left in your bank.");
            } else {
                player.getActionSender().sendMessage("Some of your items could not be banked.");
            }
            return false;
        }

        return true;
    }

    /**
     * Adds an item to the bank container.
     *
     * @param item The item.
     */
    public boolean add(Item item) {
        int amount = item.getCount();
        if (amount < 1) {
            return false;
        }
        int maxCount = super.getMaximumAdd(item, player);
        if (amount > maxCount) {
            amount = maxCount;
            if (amount < 1) {
                return false;
            }
        }
        item = new Item(item.getId(), amount, item.getCharge());
        boolean unnote = !item.getDefinition().isUnnoted();
        Item add = unnote ? new Item(item.getDefinition().getNoteId(), amount, item.getCharge()) : item;
        if (unnote && !add.getDefinition().isUnnoted()) {
            add = item;
        }
        boolean contains = contains(add.getId());
        if (super.add(add)) {
            if (!contains && Constants.BANK_TABS) {
                getBankData().changeTabAmount(getBankData().getOpenTab(), 1, false);
                insert(getSlot(add.getId()), getBankData().getOpenTab() == 0 ?
                    getBankData().getTabAmount(getBankData().getOpenTab()) - 1 : getBankData().getData(getBankData().getOpenTab(), 1));
                update();
                refresh();
            }
        }
        handleCapacity();
        return true;
    }

    @Override
    public void swap(int to, int from) {
        if (from == to) {
            return;
        }

        int index = from;
        if (from > to) {
            while (index != to) {
                Item item = items[index - 1];
                items[index - 1] = items[index];
                items[index] = item;
                index--;
            }
        } else if (from < to) {
            while (index != to) {
                Item item = items[index + 1];
                items[index + 1] = items[index];
                items[index] = item;
                index++;
            }
        }
    }

    /**
     * Takes a item from the bank container and adds one to the inventory container.
     *
     * @param slot   The slot.
     * @param amount The amount.
     */
    public void takeItem(int slot, int amount) {
        if (amount >= Integer.MAX_VALUE) {
            amount = Integer.MAX_VALUE;
        }
        if (slot < 0 || slot > super.capacity() || amount <= 0) {
            return;
        }
        Item item = player.getBank().get(slot);
        if (item == null) {
            return;
        }
        if (amount > item.getCount()) {
            amount = item.getCount(); //It always stacks in the bank.
        }
        item = new Item(item.getId(), amount, item.getCharge());
        int noteId = item.getDefinition().getNoteId();
        Item add = noteItems && noteId > 0 ? new Item(noteId, amount, item.getCharge()) : item;
        int maxCount = player.getInventory().getMaximumAdd(add);
        if (amount > maxCount) {
            item.setCount(maxCount);
            add.setCount(maxCount);
            if (maxCount < 1) {
                player.getActionSender().sendMessage("Not enough space in your inventory.");
                return;
            }
        }
        if (noteItems && noteId < 0) {
            player.getActionSender().sendMessage("This item can't be withdrawn as a note.");
            add = item;
        }
        int bankCount = getCount(item);
        int bankSlot = getSlot(item);
        if (super.remove(item, slot, true)) {
            if (player.getInventory().add(add)) {
                if (Constants.BANK_TABS && add.getCount() == bankCount) {
                    int itemTab = getBankData().getData(bankSlot, 0);
                    if (getBankData().decreaseTabCount(itemTab, 1) <= 0) {
                        getBankData().collapse(itemTab, 0, false);
                    }
                    // if ((itemTab == 0 && bankSlot == 0) || (itemTab != 0)) {
                    shift();
                    update(true);
                    //}
                }
            }
            handleCapacity();
        }
    }

    /**
     * Takes all items out except for one.
     *
     * @param slot The slot.
     */
    public void takeAllButOne(int slot) {
        if (slot < 0 || slot > super.capacity()) {
            return;
        }
        Item item = player.getBank().get(slot);
        if (item == null) {
            return;
        }
        if (item.getCount() <= 1) {
            return;
        }
        int amount = item.getCount() - 1;
        if (amount <= 1) {
            return;
        }
        item = new Item(item.getId(), amount, item.getCharge());
        int noteId = item.getDefinition().getNoteId();
        Item add = noteItems && noteId > 0 ? new Item(noteId, amount, item.getCharge()) : item;
        int maxCount = player.getInventory().getMaximumAdd(add);
        if (amount > maxCount) {
            item.setCount(maxCount);
            add.setCount(maxCount);
            if (maxCount < 1) {
                player.getActionSender().sendMessage("Not enough space in your inventory.");
                return;
            }
        }
        if (noteItems && noteId < 0) {
            player.getActionSender().sendMessage("This item can't be withdrawn as a note.");
            add = item;
        }
        int bankCount = getCount(item);
        int bankSlot = getSlot(item);
        if (super.remove(item, slot, true)) {
            if (player.getInventory().add(add)) {
                if (Constants.BANK_TABS && add.getCount() == bankCount) {
                    int itemTab = getBankData().getData(bankSlot, 0);
                    if (getBankData().decreaseTabCount(itemTab, 1) <= 0) {
                        getBankData().collapse(itemTab, 0, false);
                    }
                    // if ((itemTab == 0 && bankSlot == 0) || (itemTab != 0)) {
                    shift();
                    update(true);
                    //}
                }
            }
            handleCapacity();
        }
    }

    private void handleCapacity() {
        int bankSpaces = player.getDonorManager().getDonorStatus().getBankSpaces();
        player.getActionSender().sendString("The Bank of " + Constants.SERVER_NAME, 5383);
        player.getActionSender().sendString(String.valueOf(itemCount()), 32895);
        player.getActionSender().sendString(String.valueOf(bankSpaces), 32897);
    }

    /**
     * Checks if the item can be added.
     *
     * @param item the item.
     * @return <code>True</code> if so.
     */
    public boolean canAdd(Item item) {
        return item.getDefinition().getConfiguration(ItemConfiguration.BANKABLE, true);
    }

    /**
     * If items have to be noted.
     *
     * @return If items have to be noted <code>True</code>.
     */
    public boolean isNoteItems() {
        return noteItems;
    }

    /**
     * Set if items have to be noted.
     *
     * @param noteItems If items have to be noted <code>True</code>.
     */
    public void setNoteItems(boolean noteItems) {
        this.noteItems = noteItems;
    }

    /**
     * If items are to be inserted instead of swapped.
     *
     * @return <code>True</code> if so.
     */
    public boolean isInsertItems() {
        return insertItems;
    }

    /**
     * Sets if items are to be inserted instead of swapped.
     *
     * @param insertItems
     */
    public void setInsertItems(boolean insertItems) {
        this.insertItems = insertItems;
    }

    /**
     * Gets if the bank is open.
     *
     * @return <code>True</code> if so.
     */
    public boolean isOpen() {
        return open;
    }

    @Override
    public void update() {
        super.update();
        updateScroll();
    }

    @Override
    public void update(boolean force) {
        super.update(force);
        updateScroll();
    }

    @Override
    public void refresh() {
        super.refresh();
        updateScroll();
    }

    /**
     * Gets the {@link org.gielinor.rs2.model.container.impl.bank.BankData}.
     *
     * @return The bank data.
     */
    public BankData getBankData() {
        return bankData;
    }

    /**
     * Listens to the bank container.
     *
     * @author Emperor
     */
    public static class BankListener implements ContainerListener {

        /**
         * The player reference.
         */
        private Player player;

        /**
         * Construct a new {@code BankListener} {@code Object}.
         *
         * @param player The player reference.
         */
        public BankListener(Player player) {
            this.player = player;
        }

        @Override
        public void update(Container c, ContainerEvent event) {
            if (c instanceof BankContainer) {
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 5382, 89, 90, event.getItems(), false, player.getBank().getBankData().getTabAmounts(), event.getSlots()));
            } else {
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 5064, 0, 0, event.getItems(), false, event.getSlots()));
            }
        }

        @Override
        public void refresh(Container c) {
            if (c instanceof BankContainer) {
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 5382, 89, 90, c.toArray(), c.capacity(), false, player.getBank().getBankData().getTabAmounts()));
            } else {
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 5064, 0, 0, c.toArray(), 28, false));
            }
        }
    }
}

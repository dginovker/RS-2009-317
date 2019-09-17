package org.gielinor.rs2.model.container.impl;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.eco.grandexchange.GrandExchangeDatabase;
import org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeEntry;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.context.InventoryInterfaceContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.net.packet.out.InventoryInterface;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerEvent;
import org.gielinor.rs2.model.container.ContainerListener;
import org.gielinor.rs2.model.container.ContainerType;
import org.gielinor.rs2.model.container.SortType;
import org.gielinor.rs2.task.impl.ItemLogoutTask;
import org.gielinor.rs2.task.impl.LogoutTask;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the price guide container.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PriceGuideContainer extends Container {

    /**
     * The listener.
     */
    private final PriceGuideListener listener;
    /**
     * The player.
     */
    private Player player;

    /**
     * Constructs a new {@code PriceGuideContainer} {@code Object}.
     *
     * @param player The player.
     */
    public PriceGuideContainer(Player player) {
        super(30, ContainerType.DEFAULT, SortType.ID);
        super.register(listener = new PriceGuideListener(player));
        this.player = player;
    }

    /**
     * Opens the component.
     */
    public void open() {
        super.refresh();
        player.getInventory().getListeners().add(listener);
        player.getInventory().refresh();
        player.getInterfaceState().open(new Component(25626).setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                PriceGuideContainer.this.close();
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        }));
        player.getInterfaceState().setOverlay(new Component(25677));
        PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(player, new Component(25626), new Component(25676)));
    }

    /**
     * Adds an item to this container.
     *
     * @param item The item.
     * @return <code>True</code> if the item got added.
     */
    @Override
    public boolean add(Item item) {
        int slot = super.addSlot(item, true, -1);
        if (slot >= 0) {
            int value1 = item.getDefinition().getValue();
            String value = TextUtils.getFormattedNumber(value1 < 0 ? 0 : value1);
            GrandExchangeEntry grandExchangeEntry = GrandExchangeDatabase.getDatabase().get(item.getDefinition().isUnnoted() ? item.getId() : item.getNoteChange());
            Item stacked = get(slot);
            if (grandExchangeEntry != null) {
                if (stacked.getCount() > 1 && (item.getDefinition().isStackable() || !item.getDefinition().isUnnoted())) {
                    long valueInt = grandExchangeEntry.getValue() * stacked.getCount();
                    value = TextUtils.getFormattedNumber(stacked.getCount()) + " x " + TextUtils.getFormattedNumber(grandExchangeEntry.getValue()) + "\\n= "
                        + ((valueInt < 0 || valueInt > Integer.MAX_VALUE) ? "Lots!" : TextUtils.getFormattedNumber((int) valueInt));
                } else {
                    value = TextUtils.getFormattedNumber(grandExchangeEntry.getValue());
                }
            } else {
                if (stacked.getCount() > 1 && (item.getDefinition().isStackable() || !item.getDefinition().isUnnoted())) {
                    long valueInt = item.getDefinition().getValue() * stacked.getCount();
                    value = TextUtils.getFormattedNumber(stacked.getCount()) + " x " + TextUtils.getFormattedNumber(item.getDefinition().getValue()) + "\\n= "
                        + ((valueInt < 0 || valueInt > Integer.MAX_VALUE) ? "Lots!" : TextUtils.getFormattedNumber((int) valueInt));
                }
            }
            player.getActionSender().sendString(25645 + slot, value); // TODO NOTED/STACK
            player.removeExtension(LogoutTask.class);
            player.addExtension(LogoutTask.class, new ItemLogoutTask(Integer.MAX_VALUE, toArray()));
        }
        long networth = getNetworth();
        player.getActionSender().sendString(25633, networth > Integer.MAX_VALUE ? "Lots!" : TextUtils.getFormattedNumber((int) networth));
        updateScroll();
        return slot != -1;
    }

    /**
     * Removes an item.
     *
     * @param item The item.
     * @return <code>True</code> if the item got removed, <code>False</code> if not.
     */
    //public boolean remove(Item item, int slot, boolean fireListener, boolean allowZero) {
    @Override
    public boolean remove(Item item, int slot, boolean fireListener, boolean allowZero) {
        boolean removed = super.remove(item, slot, true, false);
        long networth = getNetworth();
        player.getActionSender().sendString(25633, networth > Integer.MAX_VALUE ? "Lots!" : TextUtils.getFormattedNumber((int) networth));
        updateScroll();
        return removed;
    }

    /**
     * Shifts the elements in the <b>Container</b> to the appropriate position.
     */
    @Override
    public void shift() {
        final Item[] items1 = items;
        clear(true);
        int slot = 0;
        for (Item item : items1) {
            player.getActionSender().sendString(25645 + slot, "");
            slot++;
            if (item == null) {
                continue;
            }
            add(item);
        }
        updateScroll();
    }

    @Override
    public long getNetworth() {
        long networth = 0;
        for (Item item : toArray()) {
            if (item == null) {
                continue;
            }
            GrandExchangeEntry grandExchangeEntry = GrandExchangeDatabase.getDatabase().get(item.getDefinition().isUnnoted() ?
                item.getId() : item.getNoteChange());
            if (grandExchangeEntry != null) {
                if (item.getDefinition().isStackable() || !item.getDefinition().isUnnoted() && item.getCount() > 1) {
                    networth += grandExchangeEntry.getValue() * item.getCount();
                } else {
                    networth += grandExchangeEntry.getValue();
                }
            } else {
                networth += item.getDefinition().getValue();
            }
        }
        return networth;
    }


    /**
     * Updates all the slot values.
     */
    public void updateValues() {
        int slot = 0;
        for (Item item : toArray()) {
            if (item != null) {
                int value1 = item.getDefinition().getValue();
                String value = TextUtils.getFormattedNumber(value1 < 0 ? 0 : value1);
                GrandExchangeEntry grandExchangeEntry = GrandExchangeDatabase.getDatabase().get(item.getDefinition().isUnnoted() ? item.getId() : item.getNoteChange());
                Item stacked = get(slot);
                if (grandExchangeEntry != null) {
                    if (stacked.getCount() > 1 && (item.getDefinition().isStackable() || !item.getDefinition().isUnnoted())) {
                        long valueInt = grandExchangeEntry.getValue() * stacked.getCount();
                        value = TextUtils.getFormattedNumber(stacked.getCount()) + " x " + TextUtils.getFormattedNumber(grandExchangeEntry.getValue()) + "\\n= "
                            + ((valueInt < 0 || valueInt > Integer.MAX_VALUE) ? "Lots!" : TextUtils.getFormattedNumber((int) valueInt));
                    } else {
                        value = TextUtils.getFormattedNumber(grandExchangeEntry.getValue());
                    }
                } else {
                    if (stacked.getCount() > 1 && (item.getDefinition().isStackable() || !item.getDefinition().isUnnoted())) {
                        long valueInt = item.getDefinition().getValue() * stacked.getCount();
                        value = TextUtils.getFormattedNumber(stacked.getCount()) + " x " + TextUtils.getFormattedNumber(item.getDefinition().getValue()) + "\\n= "
                            + ((valueInt < 0 || valueInt > Integer.MAX_VALUE) ? "Lots!" : TextUtils.getFormattedNumber((int) valueInt));
                    }
                }
                player.getActionSender().sendString(25645 + slot, value); // TODO NOTED/STACK
                player.removeExtension(LogoutTask.class);
                player.addExtension(LogoutTask.class, new ItemLogoutTask(Integer.MAX_VALUE, toArray()));
            }
            slot++;
        }
        long networth = getNetworth();
        player.getActionSender().sendString(25633, networth > Integer.MAX_VALUE ? "Lots!" : TextUtils.getFormattedNumber((int) networth));
        updateScroll();
    }

    /**
     * Adds a grand exchange price value to the screen, or removes if item id is -1.
     *
     * @param itemId The id of the item.
     */
    public void addGrandExchangeItem(int itemId) {
//        if (ServerVar.fetch("grand_exchange_enabled", 1) == 0) {
//            player.getDialogueInterpreter().sendPlaneMessage("The Grand Exchange is currently closed...");
//            return;
//        }
        GrandExchangeEntry grandExchangeEntry = GrandExchangeDatabase.getDatabase().get(itemId);
        if (itemId == -1 || grandExchangeEntry == null) {
            player.getActionSender().sendString(25638, "");
            player.getActionSender().sendString(25639, "");
            player.getActionSender().sendUpdateItem(25637, 0, null);
            player.getActionSender().sendString(25632, "Total guide price:");
            long networth = getNetworth();
            player.getActionSender().sendString(25633, networth < 0 || networth > Integer.MAX_VALUE ? "Lots!" : TextUtils.getFormattedNumber((int) networth));
            return;
        }
        Item item = new Item(grandExchangeEntry.getItemId());
        player.getActionSender().sendString(25632, "");
        player.getActionSender().sendString(25633, "");

        player.getActionSender().sendString(25638, item.getName() + ":");
        player.getActionSender().sendString(25639, TextUtils.getFormattedNumber(grandExchangeEntry.getValue()) + " coins");
        player.getActionSender().sendUpdateItem(25637, 0, item);
    }

    /**
     * Sets the default text of the component.
     */
    public void reset() {
        clear();
        // Middle text
        player.getActionSender().sendString(25632, "Total guide price:");
        player.getActionSender().sendString(25633, "0");
        // GE search text
        player.getActionSender().sendString(25638, "");
        player.getActionSender().sendString(25639, "");
        // GE search item
        player.getActionSender().sendUpdateItem(25637, 0, null);
        // Item prices
        for (int childIndex = 25645; childIndex <= 25675; childIndex++) {
            player.getActionSender().sendString(childIndex, "");
        }
        updateScroll();
    }

    /**
     * Updates the scroll length.
     */
    public void updateScroll() {
        int scrollMax = 223;
        if (this.itemCount() > 15) {
            scrollMax += 45;
        }
        if (this.itemCount() > 20) {
            scrollMax += 67;
        }
        if (this.itemCount() > 25) {
            scrollMax += 67;
        }
        PacketRepository.send(InterfaceMaxScrollPacket.class, new InterfaceMaxScrollContext(player, 25643, scrollMax));
        addGrandExchangeItem(-1);
    }


    /**
     * Closes the price guide component.
     */
    public void close() {
        player.removeExtension(LogoutTask.class);
        player.getInventory().addAll(this);
        player.getInventory().getListeners().remove(listener);
        player.getInterfaceState().closeSingleTab();
    }

    /**
     * Listens to the price guide container.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class PriceGuideListener implements ContainerListener {

        /**
         * The player reference.
         */
        private Player player;

        /**
         * Construct a new {@code PriceGuideListener} {@code Object}.
         *
         * @param player The player reference.
         */
        public PriceGuideListener(Player player) {
            this.player = player;
        }

        @Override
        public void update(Container c, ContainerEvent event) {
            if (c instanceof PriceGuideContainer) {
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 25644, 89, 90, event.getItems(), false, event.getSlots()));
            } else {
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 25677, 0, 0, event.getItems(), false, event.getSlots()));
            }
        }

        @Override
        public void refresh(Container c) {
            if (c instanceof PriceGuideContainer) {
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 25644, 89, 90, c.toArray(), c.capacity(), false));
            } else {
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 25677, 0, 0, c.toArray(), 28, false));
            }
        }
    }
}

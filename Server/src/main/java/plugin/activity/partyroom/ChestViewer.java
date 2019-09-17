package plugin.activity.partyroom;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.context.InventoryInterfaceContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.InventoryInterface;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerEvent;
import org.gielinor.rs2.model.container.ContainerListener;
import org.gielinor.rs2.model.container.ContainerType;
import org.gielinor.rs2.model.container.SortType;

/**
 * A chest viewer.
 *
 * @author Vexia
 */
public final class ChestViewer {

    /**
     * The objects for the chest queue interface.
     */
    private static final Object[] BEING_DROPPED = new Object[]{ "", "", "", "", "", "", "", "", "", -1, 0, 39, 6, 92, 2156 << 16 | 27 };

    /**
     * The objects for the dropping interface.
     */
    private static final Object[] READY_TO_DROP = new Object[]{ "", "", "", "", "", "", "", "", "", -1, 0, 39, 6, 91, 2156 << 16 | 28 };

    /**
     * The objects for the accept interface.
     */
    private static final Object[] ACCEPT = new Object[]{ "", "", "", "", "Withdraw-X", "Withdraw-All", "Withdraw-10", "Withdraw-5", "Withdraw-1", -1, 0, 4, 10, 90, 2156 << 16 | 29 };

    /**
     * The objects for the singletab.
     */
    private final static Object[] INV_OPTIONS = new Object[]{ "", "", "", "", "Deposit-X", "Deposit-All", "Deposit-10", "Deposit-5", "Deposit", -1, 0, 7, 4, 94, 648 << 16 };

    /**
     * The player viewing the chest.
     */
    private final Player player;

    /**
     * The container.
     */
    private final DepositContainer container;

    /**
     * Constructs a new {@Code ChestViewer} {@Code Object}
     *
     * @param player the player.
     */
    public ChestViewer(Player player) {
        this.player = player;
        this.container = new DepositContainer(player);
    }

    /**
     * Views the chest.
     */
    public ChestViewer view() {
        container.open();
        player.getInventory().refresh();
        player.addExtension(ChestViewer.class, this);
        player.getInterfaceState().setOpened(new Component(2274).setCloseEvent(new ChestCloseEvent()));
        player.getInterfaceState().setOverlay(new Component(2006));
        PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(player, new Component(2156), new Component(2005)));
        player.getConfigManager().set(1135, 0);
        update(0, null);
        update(1, null);
        return this;
    }

    /**
     * Updates the chest viewer.
     *
     * @param type  the type.
     * @param event the event.
     */
    public void update(int type, ContainerEvent event) {
        if (event != null) {
            return;
        }
        switch (type) {
            case 0:
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 2273, 0, 92,
                    PartyRoomPlugin.getChestQueue().toArray(), 10, false));
                break;
            case 1:
                //  PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 2273, 0, 91,
                //      PartyRoomPlugin.getPartyChest().toArray(), 10, false));
                break;
        }
    }

    /**
     * Accepts the container.
     */
    public void accept() {
        if (PartyRoomPlugin.getChestQueue().itemCount() + getContainer().itemCount() > 215) {
            player.getActionSender().sendMessage("The chest is full.");
            return;
        }
        PartyRoomPlugin.getChestQueue().addAll(getContainer());
        getContainer().clear();
        PartyRoomPlugin.update(0, null);
        PartyRoomPlugin.update(1, null);
    }

    /**
     * Gets the container.
     *
     * @return the container
     */
    public DepositContainer getContainer() {
        return container;
    }

    /**
     * Gets the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * The close event for the chest interface.
     *
     * @author Vexia
     */
    public class ChestCloseEvent implements CloseEvent {

        /**
         * If the container has given back the items.
         */
        private boolean given = false;

        @Override
        public void close(Player player, Component c) {
            if (!given) {
                given = true;
                player.getInventory().add(container.toArray());
                container.clear();
                player.removeExtension(ChestViewer.class);
                player.getInterfaceState().closeSingleTab();
                PartyRoomPlugin.getViewers().remove(player.getName());
            }
        }

        @Override
        public boolean canClose(Player player, Component component) {
            return true;
        }

    }

    /**
     * The container used when depositng items.
     *
     * @author Vexia
     */
    public class DepositContainer extends Container {

        /**
         * The player.
         */
        private final Player player;

        /**
         * The party deposit listener.
         */
        private final PartyDepositListener listener;

        /**
         * Constructs a new {@Code DepositContainer} {@Code Object}
         *
         * @param player the player.
         */
        public DepositContainer(Player player) {
            super(8, ContainerType.DEFAULT, SortType.ID);
            super.getListeners().add(listener = new PartyDepositListener(player));
            this.player = player;
        }

        /**
         * Opens the container.
         */
        public DepositContainer open() {
            super.refresh();
            player.getInventory().getListeners().add(listener);
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 2273, 0, 90, new Item[]{}, 10, false));
            return this;
        }

        /**
         * Adds an item to the container.
         *
         * @param slot   The item slot.
         * @param amount The amount.
         */
        public void addItem(int slot, int amount) {
            if (slot < 0 || slot > player.getInventory().capacity() || amount < 1) {
                return;
            }
            Item item = player.getInventory().get(slot);
            if (item == null) {
                return;
            }
            int maximum = player.getInventory().getCount(item);
            if (amount > maximum) {
                amount = maximum;
            }
            int maxCount = super.getMaximumAdd(item);
            if (amount > maxCount) {
                amount = maxCount;
                if (amount < 1) {
                    player.getActionSender().sendMessage("You must accept the current items before adding any more.");
                    return;
                }
            }
            item = new Item(item.getId(), amount);
            if (!item.getDefinition().isTradeable()) {
                player.getActionSender().sendMessage("You can't add that item to the chest.");
                return;
            }
            if (super.add(item) && player.getInventory().remove(item)) {
                listener.update(this, null);
                player.getInventory().update();
                PartyRoomPlugin.update(0, null);
                PartyRoomPlugin.update(1, null);
            }
        }

        /**
         * Takes a item from the container and adds one to the inventory container.
         *
         * @param slot   The slot.
         * @param amount The amount.
         */
        public void takeItem(int slot, int amount) {
            if (slot < 0 || slot > super.capacity() || amount <= 0) {
                return;
            }
            Item item = super.get(slot);
            if (item == null) {
                return;
            }
            int realAmt = item.getCount();
            if (!item.getDefinition().isStackable()) {
                for (Item i : container.toArray()) {
                    if (i == null) {
                        continue;
                    }
                    if (i != item && i.getId() == item.getId()) {
                        realAmt++;
                    }
                }
            }
            if (amount > realAmt) {
                amount = realAmt;
            }
            item = new Item(item.getId(), amount);
            Item add = item;
            int maxCount = player.getInventory().getMaximumAdd(add);
            if (amount > maxCount) {
                item.setCount(maxCount);
                add.setCount(maxCount);
                if (maxCount < 1) {
                    player.getActionSender().sendMessage("Not enough space in your inventory.");
                    return;
                }
            }
            if (super.remove(item)) {
                player.getInventory().add(add);
                PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 94, player.getInventory(), false));
            }
            PartyRoomPlugin.update(0, null);
            PartyRoomPlugin.update(1, null);
        }

        /**
         * Listeners to the part deposit container.
         *
         * @author Vexia
         */
        private class PartyDepositListener implements ContainerListener {

            /**
             * The player.
             */
            private Player player;

            /**
             * Constructs a new {@code PartyDepsositContainer.java} {@code Object}.
             *
             * @param player The player.
             */
            public PartyDepositListener(Player player) {
                this.player = player;
            }

            @Override
            public void update(Container c, ContainerEvent event) {
                if (c instanceof DepositContainer) {
                    PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 2274, 89, 90, c.toArray(), 10, false));
                    PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 2006, player.getInventory().toArray(), false));
                }
            }

            @Override
            public void refresh(Container c) {
                if (c instanceof DepositContainer) {
                    PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 2274, 89, 90, c.toArray(), 10, false));
                    PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 2006, 0, 0, player.getInventory(), false));
                }

            }
        }
    }

}

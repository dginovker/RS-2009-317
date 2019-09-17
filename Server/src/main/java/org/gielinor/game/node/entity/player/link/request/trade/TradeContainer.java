package org.gielinor.game.node.entity.player.link.request.trade;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerEvent;
import org.gielinor.rs2.model.container.ContainerListener;
import org.gielinor.rs2.model.container.ContainerType;
import org.gielinor.rs2.model.container.SortType;

/**
 * Represents the container during a trade session.
 *
 * @author 'Vexia
 */
public final class TradeContainer extends Container {

    /**
     * Represents the player of this container.
     */
    private final Player player;

    /**
     * Constructs a new {@code TradeContainer} {@code Object}.
     *
     * @param player the player.
     */
    public TradeContainer(final Player player) {
        super(28, ContainerType.DEFAULT, SortType.ID);
        this.player = player;
        this.getListeners().add(new TradeListener());
    }

    /**
     * Method used to offer an item to the container.
     *
     * @param slot   the slot of the item.
     * @param amount the amount of the item.
     */
    public void offer(final int slot, int amount) {
        final Item item = getItem(slot, player.getInventory());
        if (!player.ignoreRestrictions()) {
            player.getActionSender().sendMessage("You cannot trade.");
            return;
        }
        if (!canUse()) {
            return;
        }
        if (!player.getInventory().contains(item)) {
            return;
        }
        if (!validatedItem(item, slot, amount, player.getInventory())) {
            return;
        }
        if (!player.getRights().isAdministrator() && !player.ignoreRestrictions()) {
            if (!tradeable(item)) {
                player.getActionSender().sendMessage("You can't trade this item.");
                return;
            }
        }
        Item remove = new Item(item.getId(), amount);
        remove.setCount(stabalizeAmount(remove, amount, player.getInventory()));
        if (!checkCapacity(remove, this)) {
            player.getActionSender().sendMessage("You must accept the current items before adding any more.");
            return;
        }
        if (player.getInventory().remove(remove, slot, true) && super.add(remove)) {
            if (TradeModule.getExtension(player).isAccepted() || TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).isAccepted()) {
                TradeModule.getExtension(player).setAccepted(false, true);
                TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).setAccepted(false, true);
            }
            TradeModule.getExtension(player).update();
        }
    }

    /**
     * Method used to withdraw an item from the container.
     *
     * @param slot   the slot of the item.
     * @param amount the amount of the item.
     */
    public void withdraw(final int slot, int amount) {
        Item item = getItem(slot, this);
        if (!canUse()) {
            return;
        }
        if (!validatedItem(item, slot, amount, this)) {
            return;
        }
        Item remove = new Item(item.getId(), amount);
        remove.setCount(stabalizeAmount(remove, amount, this));
        if (!checkCapacity(remove, player.getInventory())) {
            player.getActionSender().sendMessage("You don't have enough space in your inventory.");
            return;
        }
        if (super.remove(remove, slot, true) && player.getInventory().add(remove)) {
            shift();
            TradeModule.getExtension(player).setModified(true);
            if (TradeModule.getExtension(player).isAccepted() || TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).isAccepted()) {
                TradeModule.getExtension(player).setAccepted(false, true);
                TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).setAccepted(false, true);
                TradeModule.getExtension(player).update();
            } else {
                TradeModule.getExtension(player).update();
            }
        }
    }

    @Override
    public void shift() {
        super.shift();
    }

    /**
     * Method used to get the item from the slot based on container.
     *
     * @param slot      the slot to get the item from.
     * @param container the container to use.
     * @return the item from the container at the slot.
     */
    private Item getItem(int slot, final Container container) {
        return container.get(slot);
    }

    /**
     * Method used to check if an item is validated.
     *
     * @param item      the item.
     * @param slot      the slot.
     * @param amount    the amount.
     * @param container the container.
     * @return <code>True</code> if validated.
     */
    private boolean validatedItem(final Item item, final int slot, final int amount, final Container container) {
        if (item == null) {
            return false;
        }
        return !(slot < 0 || slot > player.getInventory().capacity() || amount < 1);
    }

    /**
     * Method used to stabalize the amount attempting to be withdrawn/offered.
     *
     * @param item      the item.
     * @param amount    the amount.
     * @param container the container.
     * @return the stabalized amount if over.
     */
    private int stabalizeAmount(final Item item, int amount, final Container container) {
        int maximum = container.getCount(item);
        return (amount > maximum ? maximum : amount);
    }

    /**
     * Method used to check if an item is tradeable.
     *
     * @param item the item.
     * @return <code>True</code> if tradeable.
     */
    private boolean tradeable(final Item item) {
        return ItemDefinition.forId(item.getId()).isTradeable();
    }

    /**
     * Method used to check if the player can use the container.
     *
     * @return <code>True</code> if so.
     */
    private boolean canUse() {
        return !(!player.getInterfaceState().isOpened() || TradeModule.getExtension(player) == null || TradeModule.getExtension(TradeModule.getExtension(player).getTarget()) == null || TradeModule.getExtension(player).getStage() != 0);
    }

    /**
     * Method used to check if the container has capacity for more items.
     *
     * @param item      the item to remove or add.
     * @param container the container to check capacity for.
     * @return <code>True</code> if there is enough room.
     */
    private boolean checkCapacity(final Item item, final Container container) {
        if (item.getCount() > container.getMaximumAdd(item)) {
            item.setCount(container.getMaximumAdd(item));
            if (item.getCount() < 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Represents the container listener for a players trade session.
     *
     * @author 'Vexia
     */
    public final class TradeListener implements ContainerListener {

        @Override
        public void update(Container c, ContainerEvent event) {
            if (TradeModule.getExtension(player) != null && TradeModule.getExtension(TradeModule.getExtension(player).getTarget()) != null) {
                Player otherPlayer = TradeModule.getExtension(player).getTarget();
                player.getActionSender().sendUpdateItems(3322, player.getInventory().toArray());
                player.getActionSender().sendUpdateItems(3415, c.toArray());
                player.getActionSender().sendUpdateItems(3416, TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).getContainer().toArray());
                otherPlayer.getActionSender().sendUpdateItems(3322, otherPlayer.getInventory().toArray());
                otherPlayer.getActionSender().sendUpdateItems(3415, TradeModule.getExtension(TradeModule.getExtension(player).getTarget()).getContainer().toArray());
                otherPlayer.getActionSender().sendUpdateItems(3416, c.toArray());
            }
        }

        @Override
        public void refresh(Container c) {
            // TODO 317
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 3322, 0, 0, player.getInventory(), false));
        }

    }
}

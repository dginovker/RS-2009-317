package plugin.activity.duelarena;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.model.container.*;

/**
 * Represents the {@link org.gielinor.rs2.model.container.Container} for staked
 * items in a {@link plugin.activity.duelarena.DuelSession}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class StakeContainer extends Container {

    /**
     * Represents the player of this container.
     */
    private final Player player;

    /**
     * Constructs a new {@code StakeContainer}.
     *
     * @param player
     *            the player.
     */
    public StakeContainer(final Player player) {
        super(28, ContainerType.DEFAULT, SortType.ID);
        this.player = player;
        this.getListeners().add(new StakeListener());
    }

    /**
     * Stakes an item.
     *
     * @param slot
     *            The slot of the item to stake.
     * @param amount
     *            The amount of the item to stake.
     */
    public void stake(final int slot, int amount) {
        final Item item = getItem(slot, player.getInventory());
        if (!player.ignoreRestrictions()) {
            player.getActionSender().sendMessage("You cannot stake.");
            return;
        }
        if (!player.isIronman() && player.getSavedData().getGlobalData().getTotalPlayTime() < Constants.MINIMUM_PLAY_TIME) {
            player.getActionSender().sendMessage("You must have been playing for 30 minutes to stake items.");
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
        if (item.getDefinition().hasDestroyAction() || !item.getDefinition().isTradeable()) {
            player.getActionSender().sendMessage("You can't stake this item.");
            return;
        }
        Item remove = new Item(item.getId(), amount);
        remove.setCount(stabilizeAmount(remove, amount, player.getInventory()));
        if (!checkCapacity(remove, this)) {
            player.getActionSender().sendMessage("You cannot stake anymore items.");
            return;
        }
        if (player.getInventory().remove(remove, slot, true) && super.add(remove)) {
            DuelSession playerSession = DuelSession.getExtension(player);
            DuelSession opponentSession = DuelSession.getExtension(playerSession.getOpponent());
            opponentSession.setDuelStage(DuelStage.ACCEPT_TIMEOUT);
            playerSession.setDuelStage(DuelStage.ACCEPT_TIMEOUT);
            DuelSession.getExtension(player).update();
        }
    }

    /**
     * Method used to withdraw an item from the container.
     *
     * @param slot
     *            the slot of the item.
     * @param amount
     *            the amount of the item.
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
            DuelSession playerSession = DuelSession.getExtension(player);
            DuelSession opponentSession = DuelSession.getExtension(playerSession.getOpponent());
            opponentSession.setDuelStage(DuelStage.ACCEPT_TIMEOUT);
            playerSession.setDuelStage(DuelStage.ACCEPT_TIMEOUT);
            DuelSession.getExtension(player).update();
        }
    }

    /**
     * Method used to stabalize the amount attempting to be withdrawn/offered.
     *
     * @param item
     *            the item.
     * @param amount
     *            the amount.
     * @param container
     *            the container.
     * @return the stabalized amount if over.
     */
    private int stabalizeAmount(final Item item, int amount, final Container container) {
        int maximum = container.getCount(item);
        return (amount > maximum ? maximum : amount);
    }

    /**
     * Get the item from the slot based on container.
     *
     * @param slot
     *            The slot to get the item from.
     * @param container
     *            The container to use.
     * @return The item from the container at the slot.
     */
    private Item getItem(int slot, final Container container) {
        return container.get(slot);
    }

    /**
     * Checks if the player can use the container.
     *
     * @return <code>True</code> if so.
     */
    private boolean canUse() {
        return !(DuelSession.getExtension(player).getDuelStage() != DuelStage.ACCEPT_TIMEOUT
            && DuelSession.getExtension(player).getDuelStage() != DuelStage.WAITING
            && DuelSession.getExtension(player).getDuelStage() != DuelStage.ACCEPTED_WAITING)
            && !(!player.getInterfaceState().isOpened() || DuelSession.getExtension(player) == null
            || DuelSession.getExtension(DuelSession.getExtension(player).getOpponent()) == null);
    }

    /**
     * Checks if an item is valid.
     *
     * @param item
     *            The item.
     * @param slot
     *            The slot.
     * @param amount
     *            The amount.
     * @param container
     *            The container.
     * @return <code>True</code> if validated.
     */
    private boolean validatedItem(final Item item, final int slot, final int amount, final Container container) {
        return item != null && !(slot < 0 || slot > player.getInventory().capacity() || amount < 1);
    }

    /**
     * Stabilizes the amount attempting to be withdrawn/offered.
     *
     * @param item
     *            The item.
     * @param amount
     *            The amount.
     * @param container
     *            The container.
     * @return the stabilized amount if over.
     */
    private int stabilizeAmount(final Item item, int amount, final Container container) {
        int maximum = container.getCount(item);
        return (amount > maximum ? maximum : amount);
    }

    /**
     * Checks if the container has capacity for more items.
     *
     * @param item
     *            The item to remove or add.
     * @param container
     *            The container to check capacity for.
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
     * Represents a {@link org.gielinor.rs2.model.container.ContainerListener}
     * for the {@link plugin.activity.duelarena.StakeContainer}.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public final class StakeListener implements ContainerListener {

        @Override
        public void update(Container c, ContainerEvent event) {
            if (DuelSession.getExtension(player) != null
                && DuelSession.getExtension(DuelSession.getExtension(player).getOpponent()) != null) {
                Player otherPlayer = DuelSession.getExtension(player).getOpponent();
                PacketRepository.send(ContainerPacket.class,
                    new ContainerContext(player, 6574, 0, 0, player.getInventory().toArray(), 28, false));
                Item[] opponentItems = DuelSession.getExtension(DuelSession.getExtension(player).getOpponent())
                    .getContainer().toArray();
                for (int index = 0; index < 28; index++) {
                    Item item = index > c.toArray().length ? null : c.toArray()[index];
                    player.getActionSender().sendUpdateItem(item, (48110 + index), 0);
                }
                for (int index = 0; index < 28; index++) {
                    Item item = index > opponentItems.length ? null : opponentItems[index];
                    player.getActionSender().sendUpdateItem(item, (48140 + index), 0);
                }
                PacketRepository.send(ContainerPacket.class,
                    new ContainerContext(otherPlayer, 6574, 0, 0, otherPlayer.getInventory().toArray(), 28, false));
                for (int index = 0; index < 28; index++) {
                    Item item = index > c.toArray().length ? null : c.toArray()[index];
                    otherPlayer.getActionSender().sendUpdateItem(item, (48140 + index), 0);
                }
                for (int index = 0; index < 28; index++) {
                    Item item = index > opponentItems.length ? null : opponentItems[index];
                    otherPlayer.getActionSender().sendUpdateItem(item, (48110 + index), 0);
                }
            }
        }

        @Override
        public void refresh(Container c) {
            PacketRepository.send(ContainerPacket.class,
                new ContainerContext(player, 6574, 0, 0, player.getInventory().toArray(), 28, false));
        }
    }
}

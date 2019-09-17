package org.gielinor.game.content.skill.member.summoning.familiar;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InventoryInterfaceContext;
import org.gielinor.net.packet.out.InventoryInterface;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.model.container.Container;

/**
 * Represents a beast of burden familiar.
 *
 * @author Emperor
 */
public abstract class BurdenBeast extends Familiar {

    /**
     * The container.
     */
    protected Container container;

    /**
     * Constructs a new {@code BurdenBeast} {@code Object}.
     *
     * @param owner         The owner.
     * @param id            The NPC id.
     * @param ticks         The amount of ticks.
     * @param pouchId       The pouch id.
     * @param specialCost   The special move cost.
     * @param containerSize The container size.
     */
    public BurdenBeast(Player owner, int id, int ticks, int pouchId, int specialCost, int containerSize) {
        super(owner, id, ticks, pouchId, specialCost);
        this.container = new BurdenBeastContainer(owner);
    }

    @Override
    public void dismiss() {
        if (owner.getInterfaceState().hasMainComponent(25699)) {
            owner.getInterfaceState().close();
        }
        for (Item item : container.toArray()) {
            if (item != null) {
                GroundItemManager.create(new GroundItem(item, location, 500, owner));
            }
        }
        container.clear();
        super.dismiss();
    }

    @Override
    public boolean isBurdenBeast() {
        return true;
    }

    @Override
    public boolean isPoisonImmune() {
        return true;
    }

    /**
     * Checks if the item is allowed to be stored.
     *
     * @param owner The owner.
     * @param item  The item to store.
     * @return <code>True</code> if so.
     */
    public boolean isAllowed(Player owner, Item item) {
        if (item.getValue() > 50000) {
            owner.getActionSender().sendMessage("This item is too valuable to trust to this familiar.");
            return false;
        }
        if (!item.getDefinition().isTradeable()) {
            owner.getActionSender().sendMessage("You can't trade this item, not even to your familiar.");
            return false;
        }
        if (!owner.getFamiliarManager().getFamiliar().getDefinition().getName().toLowerCase().contains("abyssal")) {
            if (item.getId() == 1436 || item.getId() == 7936 || !item.getDefinition().getConfiguration(ItemConfiguration.BANKABLE, true)) {
                owner.getActionSender().sendMessage("You can't store " + item.getName().toLowerCase() + " in this familiar.");
                return false;
            }
        }
        return true;
    }

    /**
     * Transfers an item.
     *
     * @param item     The item to store.
     * @param amount   The amount to store.
     * @param withdraw If the player is withdrawing.
     */
    public void transfer(Item item, int amount, boolean withdraw) {
        if (this instanceof Forager && !withdraw) {
            owner.getActionSender().sendMessage("You can't store your items in this familiar.");
            return;
        }
        if (!withdraw && !isAllowed(owner, new Item(item.getId(), item.getDefinition().isStackable() ? amount : 1))) {
            return;
        }
        Container to = withdraw ? owner.getInventory() : container;
        Container from = withdraw ? container : owner.getInventory();
        int fromAmount = from.getCount(item);
        if (amount > fromAmount) {
            amount = fromAmount;
        }
        int maximum = to.getMaximumAdd(item);
        if (amount > maximum) {
            amount = maximum;
        }
        if (amount < 1) {
            if (withdraw) {
                owner.getActionSender().sendMessage("Not enough space in your inventory.");
            } else {
                owner.getActionSender().sendMessage("Your familiar can't carry any more items.");
            }
            return;
        }
        if (!item.getDefinition().isStackable() && item.getSlot() > -1) {
            from.replace(null, item.getSlot());
            to.add(new Item(item.getId(), 1));
            amount--;
        }
        if (amount > 0) {
            item = new Item(item.getId(), amount);
            if (from.remove(item)) {
                to.add(item);
            }
        }
    }

    /**
     * Withdraws the full container.
     */
    public void withdrawAll() {
        for (int i = 0; i < container.capacity(); i++) {
            Item item = container.get(i);
            if (item == null) {
                continue;
            }
            int amount = owner.getInventory().getMaximumAdd(item);
            if (item.getCount() > amount) {
                item = new Item(item.getId(), amount);
                container.remove(item, false);
                owner.getInventory().add(item, false);
            } else {
                container.replace(null, i, false);
                owner.getInventory().add(item, false);
            }
        }
        container.update();
        owner.getInventory().update();
    }

    /**
     * Opens the beast of burden interface.
     */
    public void openInterface() {
        if (getContainer().itemCount() == 0 && this instanceof Forager) {
            owner.getActionSender().sendMessage("Your familiar is not carrying any items that you can withdraw.");
            return;
        }
        container.shift();
        owner.getInventory().getListeners().add(container.getListeners().get(0));
        owner.getInventory().refresh();
        owner.getInterfaceState().open(new Component(25706).setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                player.getInterfaceState().closeSingleTab();
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        }));
        owner.getInterfaceState().setOverlay(new Component(25708));
        PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(owner, new Component(25699), new Component(25707)));
        container.refresh();
    }

    /**
     * Gets the item container.
     *
     * @return The container.
     */
    public Container getContainer() {
        return container;
    }
}

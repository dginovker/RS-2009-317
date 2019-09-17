package org.gielinor.game.node.entity.player.link.request.trade;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.request.RequestModule;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.mqueue.message.impl.TradePlayerMessage;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InventoryInterfaceContext;
import org.gielinor.net.packet.out.InventoryInterface;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the module use to handle a request between trading of two players.
 *
 * @author 'Vexia
 */
public final class TradeModule implements RequestModule {

    /**
     * Represents the overlay interface component.
     */
    public static final Component OVERLAY_INTERFACE = new Component(3321);

    /**
     * Represents the main interface component.
     */
    public static final Component MAIN_INTERFACE = new Component(3323).setCloseEvent(new TradeCloseEvent());

    /**
     * Represents the accept interface component.
     */
    public static final Component ACCEPT_INTERFACE = new Component(3443).setCloseEvent(new TradeCloseEvent());

    /**
     * The player inventory interface.
     */
    public static final int PLAYER_INVENTORY_INTERFACE = 3322;
    /**
     * The bank inventory interface.
     */
    public static final int TRADE_INVENTORY_INTERFACE = 3415;

    /**
     * Represents the player instance.
     */
    private Player player;

    /**
     * Represents the target instance.
     */
    private Player target;

    /**
     * Represents the container of this session.
     */
    private TradeContainer container;

    /**
     * Represents if this session has accepted.
     */
    private boolean accepted;

    /**
     * Represents if the trade is modified.
     */
    private boolean modified = false;

    /**
     * Represents the stage of the trade(0=started, 1=second accept)
     */
    private int stage;

    /**
     * Constructs a new {@code TradeModule} {@code Object}.
     *
     * @param player the player.
     * @param target the target.
     */
    public TradeModule(final Player player, final Player target) {
        this.player = player;
        this.target = target;
        this.container = new TradeContainer(player);
    }

    /**
     * Constructs a new {@code TradeModule} {@code Object}.
     */
    public TradeModule() {
        /**
         * empty.
         */
    }

    @Override
    public void open(Player player, Player target) {
        extend(player, target);
        getExtension(player).openInterface(getInterface(stage)).display(stage);
        getExtension(target).openInterface(getInterface(stage)).display(stage);
    }

    /**
     * Method used to update the trade interfaces.
     */
    public void update() {
        display(stage);
        getExtension(target).display(stage);
    }

    /**
     * Method used to extend the module for each participant.
     *
     * @param player the player.
     * @param target the target.
     */
    public static void extend(final Player player, final Player target) {
        player.addExtension(TradeModule.class, new TradeModule(player, target));
        target.addExtension(TradeModule.class, new TradeModule(target, player));
    }

    /**
     * Method used to get the extension from the player.
     *
     * @param player the player.
     * @return the module instance.
     */
    public static TradeModule getExtension(final Player player) {
        return player.getExtension(TradeModule.class);
    }

    /**
     * Method used to open an interface at a stage.
     *
     * @param component the component.
     * @return the module instance for chaining.
     */
    private TradeModule openInterface(final Component component) {
        return component == MAIN_INTERFACE ? openMain() : openSecond();
    }

    /**
     * Gets the accepting message to display.
     *
     * @return the message.
     */
    private String getAcceptMessage() {
        boolean otherAccept = TradeModule.getExtension(target).isAccepted();
        return !otherAccept && !accepted ? "" : otherAccept ? "Other player has accepted." : "Waiting for other player...";
    }

    /**
     * Method used to display the interface display depending on stage.
     *
     * @param stage the stage.
     * @return module the module instance for chaining.
     */
    private TradeModule display(int stage) {
        switch (stage) {
            case 0:
                player.getActionSender().sendString("Trading With: " + target.getUsername(), 3417);
                player.getActionSender().sendString(getAcceptMessage(), 3431);
                break;
            case 1:
                final String acceptMessage = getAcceptMessage();
                if (acceptMessage != "") {
                    player.getActionSender().sendString(acceptMessage, 3535);
                }
                player.getInterfaceState().openDefaultTabs();
                break;
        }
        container.update(true);
        return this;
    }

    /**
     * Gets the interface for the current stage.
     *
     * @param stage the stage.
     * @return the component.
     */
    private Component getInterface(int stage) {
        return stage == 0 ? MAIN_INTERFACE : ACCEPT_INTERFACE;
    }

    /**
     * Method used to decline this offer.
     */
    public void decline() {
        player.getInterfaceState().close();
        target.getActionSender().sendMessage("<col=FF0000>Other player has declined trade!</col>");
    }

    /**
     * Method used to check if the next stage can proceed.
     */
    private void nextStage() {
        if (accepted && TradeModule.getExtension(target).isAccepted()) {
            if (stage == 0) {
                if (!hasSpace()) {
                    return;
                }
                incrementStage();
                openInterface(getInterface(stage));
                TradeModule.getExtension(target).incrementStage();
                TradeModule.getExtension(target).openInterface(getInterface(stage));
                TradeModule.getExtension(target).setAccepted(false, false);
                setAccepted(false, false);
            } else {
                giveContainers(this);
                incrementStage();
                TradeModule.getExtension(target).incrementStage();
                TradeModule.getExtension(target).setAccepted(false, false);
                setAccepted(false, false);
                player.getInterfaceState().close();
                return;
            }
        }
        update();
    }

    /**
     * Method used to open the main interface.
     *
     * @return the module instance for chaining.
     */
    private TradeModule openMain() {
        player.getInterfaceState().closeDefaultTabs();
        player.getInterfaceState().setOpened(new Component(PLAYER_INVENTORY_INTERFACE).setCloseEvent(new TradeCloseEvent()));
        player.getInterfaceState().setOverlay(new Component(TRADE_INVENTORY_INTERFACE));
        PacketRepository.send(InventoryInterface.class, new InventoryInterfaceContext(player, MAIN_INTERFACE, OVERLAY_INTERFACE));
        return this;
    }

    /**
     * Method used to open the second interface.
     *
     * @return the module instance for chaining.
     */
    private TradeModule openSecond() {
        player.getInterfaceState().open(ACCEPT_INTERFACE);
        player.getInterfaceState().closeSingleTab();
        player.getInterfaceState().openDefaultTabs();
        displayOffer(container, 3557);
        displayOffer(TradeModule.getExtension(target).getContainer(), 3558);
        return this;
    }

    /**
     * Method used to set the value of accepted.
     *
     * @param accept the accept.
     */
    public void setAccepted(final boolean accept, boolean update) {
        this.accepted = accept;
        if (update) {
            nextStage();
        }
    }

    /**
     * Method used to display an offer.
     *
     * @param container the container.
     */
    private void displayOffer(final Container container, int child) {
        player.getActionSender().sendString(3535, "Are you sure you want to accept this trade?");
        player.getActionSender().sendString(child, getDisplayMessage(container.toArray()));
    }

    /**
     * Method used to get the display message.
     *
     * @param items the items.
     * @return the message.
     */
    private String getDisplayMessage(Item[] items) {
        String message = "";
        if (items.length > 0) {
            for (Item item : items) {
                if (item == null) {
                    continue;
                }
                String custom = "";
                message = message + "<col=FF9040>" + item.getName();
                if (item.getCount() > 1) {
                    message = message + "<col=FFFFFF> x ";
                    message = message + "<col=FFFF00>" + TextUtils.getFormattedNumber(item.getCount()) + "<br>";
                } else {
                    message = message + "<br>";
                }
            }
        }
        return message.isEmpty() ? "Absolutely nothing!" : message;
    }

    /**
     * Method used to check if the players & targets have enough space.
     *
     * @return <code>True</code> if so.
     */
    private boolean hasSpace() {
        boolean hasSpace = true;
        if (!player.getInventory().hasSpaceFor(TradeModule.getExtension(target).getContainer())) {
            target.getActionSender().sendMessage("Other player doesn't have enough space in their inventory for this trade.");
            player.getActionSender().sendMessage("You don't have enough inventory space for this.");
            hasSpace = false;
        } else if (!target.getInventory().hasSpaceFor(container)) {
            player.getActionSender().sendMessage("Other player doesn't have enough space in their inventory for this trade.");
            target.getActionSender().sendMessage("You don't have enough inventory space for this.");
            hasSpace = false;
        }
        if (!hasSpace) {
            setAccepted(false, true);
            TradeModule.getExtension(target).setAccepted(false, true);
        }
        return hasSpace;
    }

    /**
     * Method used to give the containers to each participants.
     *
     * @param module the module.
     */
    private void giveContainers(final TradeModule module) {
        final Container pContainer = module.getContainer();
        final Container oContainer = TradeModule.getExtension(module.getTarget()).getContainer();
        addContainer(module.getPlayer(), oContainer);
        addContainer(module.getTarget(), pContainer);
        World.submit(new TradePlayerMessage(player, target, oContainer.toArray(), pContainer.toArray()));
        module.getTarget().getActionSender().sendMessage("Accepted trade.");
        module.getPlayer().getActionSender().sendMessage("Accepted trade.");
    }

    /**
     * Method used to add a container for a player.
     *
     * @param player    the player.
     * @param container the container.
     */
    private void addContainer(final Player player, final Container container) {
        for (Item i : container.toArray()) {
            if (i == null) {
                continue;
            }
            if (i.getCount() > player.getInventory().getMaximumAdd(i)) {
                i.setCount(player.getInventory().getMaximumAdd(i));
            }
            if (!player.getInventory().add(i)) {
                GroundItemManager.create(i, player);
            }
        }
    }

    /**
     * Gets the split list as an array.
     *
     * @param items the items.
     * @param min   the min.
     * @param max   the max.
     * @return the split item array.
     */
    private Item[] splitList(Item[] items, int min, int max) {
        List<Item> list = new ArrayList<>();
        for (int i = min; i < max; i++) {
            if (items[i] == null) {
                continue;
            }
            list.add(items[i]);
        }
        Item[] array = new Item[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                continue;
            }
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Method used to increment the stage.
     */
    public void incrementStage() {
        stage++;
    }

    /**
     * Gets the stage.
     *
     * @return the stage.
     */
    public int getStage() {
        return stage;
    }

    /**
     * Gets the accepted.
     *
     * @return The accepted.
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Gets the trade container.
     *
     * @return the container.
     */
    public TradeContainer getContainer() {
        return container;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the target.
     *
     * @return The target.
     */
    public Player getTarget() {
        return target;
    }

    /**
     * Gets the modified.
     *
     * @return The modified.
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Sets the modified.
     *
     * @param modified The modified to set.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

}

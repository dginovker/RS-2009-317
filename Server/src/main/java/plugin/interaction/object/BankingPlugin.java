package plugin.interaction.object;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.eco.grandexchange.GrandExchangeDatabase;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Ironman;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.net.packet.OperationCode;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceScrollPositionContext;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.InterfaceScrollPosition;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.impl.BankContainer;
import org.gielinor.rs2.model.container.impl.bank.BankTab;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RunScript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents the plugin used for anything related to banking.
 *
 * @author 'Vexia
 * @author Emperor
 * @version 1.0
 */
public final class BankingPlugin extends OptionHandler {

    public final static int[] BANK_OBJECTS = new int[] {
        2693,
        4483,
        6083,6084,6943,6945,6946,6947,6948,
        7409,7410,
        10083,10517,10518,10562,
        11338,
        12798,12799,12800,12801,
        14367,14368,14382,14886,
        15985,
        16642,16643,16695,16696,16700,
        18491,
        19051,
        20323,20324,20325,20326,20327,20328,
        21301,
        22819,
        24101,24347,
        25808,25937,
        26254,26707,26711,
        27254,27260,27263,27265,27267,27292,27718,27719,27720,27721,
        28429,28430,28431,28432,28433,28546,28547,28548,28549,28594,28595,28816,28861,
        29090,29103,29104,29105,29106,29108,29321,29327,
        30087,30267,30268,30796,30926,30989,
    };

    /**
     * Deposits all of the container's items into the bank.
     *
     * @param player    The player.
     * @param container The container.
     */
    public static void depositAll(Player player, Container container) {
        if (container.isEmpty()) {
            player.getActionSender().sendMessage("You have nothing to deposit.");
            return;
        }

        int itemCount = container.itemCount();
        List<Item> itemsToBank = new ArrayList<>();

        for (Item item : container.toArray()) {
            if (item == null || !item.isActive()) {
                continue;
            }
            if (!player.getBank().hasRoomFor(item)) {
                continue;
            }
            if (!item.getDefinition().getConfiguration(ItemConfiguration.BANKABLE, true)) {
                continue;
            }
            if (player.getBank().canAdd(item)) {
                itemCount--;
                itemsToBank.add(item);
            }
        }

        if (itemsToBank.size() > 0) {
            Item[] items = new Item[itemsToBank.size()];
            items = itemsToBank.toArray(items);

            player.getBank().depositAll(container, items);
            container.update();
            container.refresh();
        }

        if (player.getInterfaceState().getChatbox() != null) {
            player.getBank().getBankData().setSearchMode(false);
            player.getInterfaceState().getChatbox().close(player);
        }
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("use-quickly", this);
        ObjectDefinition.setOptionHandler("use", this);
        ObjectDefinition.setOptionHandler("bank", this);
        ObjectDefinition.setOptionHandler("exchange", this);
        ObjectDefinition.setOptionHandler("history", this);
        ObjectDefinition.setOptionHandler("sets", this);
        ObjectDefinition.setOptionHandler("deposit", this);
        ObjectDefinition.setOptionHandler("collect", this);
        ObjectDefinition.forId(3193).getConfigurations().put("option:open", this);
        new BankingInterface().newInstance(arg);
        new BankDepositInterface().newInstance(arg);
        new BankNPCPlugin().newInstance(arg);
        new BankNPC().newInstance(arg);
        new BankerDialogue().init();
        new IronmanNotePlugin().newInstance(arg);
        new IronmanUnnotePlugin().newInstance(arg);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final GameObject object = (GameObject) node;
        if (object.getLocation().inArea(2096, 3916, 2105, 3922)) {
            if (!(player.getInventory().contains(9083) || player.getEquipment().contains(9083))) {
                player.getInterfaceState().close();
                player.lock(7);
                World.submit(new Pulse(2) {

                    int count = 0;

                    @Override
                    public boolean pulse() {
                        this.setDelay(1);
                        switch (count++) {
                            case 0:
                                player.getInterfaceState().openComponent(8677);
                                break;
                            case 3:
                                player.getProperties().setTeleportLocation(Location.create(2621, 3687, 0));
                                break;
                            case 5:
                                player.unlock();
                                player.getInterfaceState().close();
                                player.getInterfaceState().openDefaultTabs();
                                PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                                player.getDialogueInterpreter().sendPlaneMessage("The banker notices you are of the Fremennik,", "they furiously cast you back to Rellekka!");
                                return true;
                        }
                        return false;
                    }
                });
                return true;
            }
        }
        if (IntStream.of(BANK_OBJECTS).anyMatch(bank_object_id -> object.getId() == bank_object_id)) {
            switch (option) {
                case "use":
                    if (object.getName().toLowerCase().contains("bank chest")) {
                        player.getBank().open();
                        return true;
                    }
                    final Location l = object.getLocation();
                    final Location p = player.getLocation();
                    final NPC npc = Repository.findNPC(l.transform(l.getX() - p.getX(), l.getY() - p.getY(), 0));
                    if (npc != null && DialogueInterpreter.contains(npc.getId())) {
                        npc.faceLocation(node.getLocation());
                        player.getDialogueInterpreter().open(npc.getId(), npc.getId());
                    } else {
                        player.getDialogueInterpreter().open(494);
                    }
                    return true;
                case "use-quickly":
                case "bank":
                case "open":
                    if (option.equalsIgnoreCase("open") && node.getId() != 3193) {
                        return false;
                    }
                    player.getBank().open();
                    return true;
                case "collect":
                    player.getGrandExchange().open();
                    //player.getGrandExchange().openCollectionBox();
                    return true;
                case "exchange":
                    if (!GrandExchangeDatabase.hasInitialized()) {
                        player.getDialogueInterpreter().sendDialogue("The Grand Exchange desk seems to be closed...");
                        break;
                    }
                    player.getGrandExchange().open();
                    break;
                case "history":
                    if (!GrandExchangeDatabase.hasInitialized()) {
                        player.getDialogueInterpreter().sendDialogue("The Grand Exchange desk seems to be closed...");
                        break;
                    }
                    player.getGrandExchange().openHistoryLog(player);
                    break;
                case "sets":
                    if (!GrandExchangeDatabase.hasInitialized()) {
                        player.getDialogueInterpreter().sendDialogue("The Grand Exchange desk seems to be closed...");
                        break;
                    }
                    player.getGrandExchange().openItemSets();
                    break;
                case "deposit":
                    openDepositBox(player);
                    return true;
            }
        }
        return true;
    }

    /**
     * Method used to open the deposit box.
     *
     * @param player the player.
     */
    private void openDepositBox(final Player player) {
        player.getInterfaceState().removeTabs(3, 4);
        player.getInterfaceState().open(new Component(4465)).setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                player.getInterfaceState().openDefaultTabs();
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        });
        player.getActionSender().sendUpdateItems(7423, player.getInventory().toArray());
    }

    /**
     * Represents the dialogue plugin used for all bankers.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class BankerDialogue extends DialoguePlugin {

        /**
         * Represents the id to use.
         */
        private int id;

        /**
         * Constructs a new {@code BankerDialogue} {@code Object}.
         */
        public BankerDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code BankerDialoguePlugin} {@code Object}.
         *
         * @param player the player.
         */
        public BankerDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new BankerDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            if (args[0] instanceof NPC) {
                setId(((NPC) args[0]).getId());
            } else {
                setId((int) args[0]);
            }
            if (id == 4519) {
                if (!(player.getInventory().contains(9083) || player.getEquipment().contains(9083))) {
                    player.getInterfaceState().close();
                    player.lock(7);
                    World.submit(new Pulse(2) {

                        int count = 0;

                        @Override
                        public boolean pulse() {
                            this.setDelay(1);
                            switch (count++) {
                                case 0:
                                    player.getInterfaceState().openComponent(8677);
                                    break;
                                case 3:
                                    player.getProperties().setTeleportLocation(Location.create(2621, 3687, 0));
                                    break;
                                case 5:
                                    player.unlock();
                                    player.getInterfaceState().close();
                                    player.getInterfaceState().openDefaultTabs();
                                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                                    player.getDialogueInterpreter().sendPlaneMessage("The banker notices you are of the Fremennik,", "they furiously cast you back to Rellekka!");
                                    return true;
                            }
                            return false;
                        }
                    });
                    return true;
                }
            }
            interpreter.sendDialogues(id, FacialExpression.NO_EXPRESSION, "Good day, how may I help you?");
            return true;
        }


        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    interpreter.sendOptions("Select an Option", "I'd like to access my bank account, please.", "I'd like to check my PIN settings.", "I'd like to collect items.", "What is this place?");
                    stage = 1;
                    break;

                case 1:
                    switch (optionSelect) {
                        case FOUR_OPTION_ONE:
                            end();
                            player.getBank().open();
                            break;

                        case FOUR_OPTION_TWO:
                            player.getActionSender().sendMessage("Coming soon...");
                            end();
                            break;

                        case FOUR_OPTION_THREE:
                            end();
                            player.getGrandExchange().openCollectionBox();
                            break;

                        case FOUR_OPTION_FOUR:
                            interpreter.sendDialogues(player, FacialExpression.NORMAL, "What is this place?");
                            stage = 2;
                            break;
                    }
                    break;

                case 2:
                    interpreter.sendDialogues(npc, FacialExpression.NORMAL, "This is a branch of the Bank of " + Constants.SERVER_NAME + ". We have", "branches in many towns.");
                    stage = 3;
                    break;

                case 3:
                    interpreter.sendOptions("Select an Option", "And what do you do?", "Didn't you used to be called the Bank of Varrock?");
                    stage = 4;
                    break;

                case 4:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            interpreter.sendDialogues(player, FacialExpression.NORMAL, "And what do you do?");
                            stage = 5;
                            break;

                        case TWO_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.NORMAL, "Didn't you used to be called the Bank of Varrock?");
                            stage = 6;
                            break;
                    }
                    break;

                case 5:
                    interpreter.sendDialogues(npc, FacialExpression.NORMAL, "We will look after your items and money for you.", "Leave your valuables with us if you want to keep them", "safe.");
                    stage = END;
                    break;

                case 6:
                    interpreter.sendDialogues(npc, FacialExpression.NORMAL, "We will look after your items and money for you.", "Yes we did, but people kept coming into our", "branches outside of Varrock and telling us that our", "signs were wrong. They acted as if we didn't know", "what town we were in or something.");
                    stage = END;
                    break;

                case END:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return BankNPC.NPC_IDS;
        }

        /**
         * Gets the id.
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }

        /**
         * Sets the id.
         *
         * @param id The id to set.
         */
        public void setId(int id) {
            this.id = id;
        }

    }

    /**
     * Represents the component plugin used to handle banking interfaces.
     *
     * @author Emperor
     * @author 'Vexia
     * @version 1.0
     */
    public static final class BankingInterface extends ComponentPlugin {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ComponentDefinition.put(BankContainer.BANK_INVENTORY_INTERFACE, this);
            ComponentDefinition.put(BankContainer.PLAYER_INVENTORY_INTERFACE, this);
            return this;
        }

        @Override
        public boolean handle(final Player player, Component component, int opcode, int button, final int slot, int itemId) {
            final Item item = component.getId() == BankContainer.BANK_INVENTORY_INTERFACE ? player.getBank().get(slot) : player.getInventory().get(slot);
            switch (component.getId()) {
                case BankContainer.BANK_INVENTORY_INTERFACE:
                    switch (opcode) {
                        case 185:
                            BankTab bankTab = BankTab.forId(button);
                            if (bankTab == null) { // Collapse tab
                                bankTab = BankTab.forId(button + 1);
                                if (bankTab != null) {
                                    if (bankTab == BankTab.TAB_ALL || player.getBank().getBankData().getTabAmount(bankTab.ordinal()) == 0) {
                                        break;
                                    }
                                    player.getBank().getBankData().collapse(bankTab.ordinal(), 0);
                                    break;
                                }
                            } else { // Switch tab
                                if (bankTab != BankTab.TAB_ALL && player.getBank().getBankData().getTabAmount(bankTab.ordinal()) == 0) {
                                    player.getActionSender().sendMessage("To create a new tab, drag items from your bank onto this tab.");
                                    break;
                                }
                                int currentTab = player.getBank().getBankData().getOpenTab();
                                player.getBank().getBankData().setOpenTab(bankTab.ordinal());
                                if (bankTab.ordinal() != currentTab) {
                                    PacketRepository.send(InterfaceScrollPosition.class, new InterfaceScrollPositionContext(player, 5385, 0));
                                }
                                break;
                            }
                            switch (button) {
                                /**
                                 * Show settings menu.
                                 */
                                case 32849:
                                    player.getInterfaceState().open(new Component(25682));
                                    return true;
                                /**
                                 * Deposit inventory / equipment.
                                 */
                                case 32847:
                                case 32848:
                                    depositAll(player, button == 32847 ? player.getInventory() : player.getEquipment());
                                    return true;
                                /**
                                 * Search mode.
                                 */
                                case 32846:
                                    if (player.getRights().isAdministrator()) {
                                        player.getBank().getBankData().setSearchMode(!player.getBank().getBankData().isSearchMode());
                                        return true;
                                    }
                                    player.getActionSender().sendMessage("Coming soon!");
                                    return true;
                                case 8130:
                                case 8131:
                                    player.getBank().setInsertItems(button == 8131);
                                    return true;
                                case 5387:
                                case 5386:
                                    player.getBank().setNoteItems(button == 5386);
                                    return true;
                            }
                            break;
                        case 180:
                            switch (button) {
                                case 105:
                                    player.getDialogueInterpreter().open("DEPOSIT_ALL");
                                    return true;
                                case 93:
                                case 92:
                                    player.getBank().setNoteItems(button == 92);
                                    return true;
                            }
                            break;

                        case OperationCode.OPTION_OFFER_ONE:
                            World.submit(new Pulse(1, player) {

                                @Override
                                public boolean pulse() {
                                    player.getBank().takeItem(slot, 1);
                                    return true;
                                }
                            });
                            return true;
                        case OperationCode.OPTION_OFFER_FIVE:
                            World.submit(new Pulse(1, player) {

                                @Override
                                public boolean pulse() {
                                    player.getBank().takeItem(slot, 5);
                                    return true;
                                }
                            });
                            return true;
                        case OperationCode.OPTION_OFFER_TEN:
                            World.submit(new Pulse(1, player) {

                                @Override
                                public boolean pulse() {
                                    player.getBank().takeItem(slot, 10);
                                    return true;
                                }
                            });
                            return true;
                        case OperationCode.OPTION_OFFER_ALL:
                            World.submit(new Pulse(1, player) {

                                @Override
                                public boolean pulse() {
                                    player.getBank().takeItem(slot, player.getBank().isNoteItems() ? player.getBank().getCount(item) : player.getInventory().getMaximumAdd(item));
                                    return true;
                                }
                            });
                            return true;
                        case OperationCode.OPTION_OFFER_X:
                            player.setAttribute("runscript", new RunScript() {

                                @Override
                                public boolean handle() {
                                    int amount = (int) value;
                                    player.getBank().takeItem(slot, amount);
                                    return false;
                                }
                            });
                            player.getDialogueInterpreter().sendInput(false, "Enter the amount.");
                            return true;
                    }
                    return true;
                case BankContainer.PLAYER_INVENTORY_INTERFACE:
                case 5292:
                    switch (opcode) {
                        case OperationCode.OPTION_OFFER_ONE:
                            player.getBank().addItem(slot, 1, null);
                            return true;
                        case OperationCode.OPTION_OFFER_FIVE:
                            player.getBank().addItem(slot, 5, null);
                            return true;
                        case OperationCode.OPTION_OFFER_TEN:
                            player.getBank().addItem(slot, 10, null);
                            return true;
                        case OperationCode.OPTION_OFFER_ALL:
                            player.getPulseManager().run(new Pulse(1, player) {

                                @Override
                                public boolean pulse() {
                                    player.getBank().addItem(slot, player.getInventory().getCount(item), null);
                                    return true;
                                }
                            });
                            return true;
                        case OperationCode.OPTION_OFFER_X:
                            player.setAttribute("runscript", new RunScript() {

                                @Override
                                public boolean handle() {
                                    int ammount = (int) value;
                                    player.getBank().addItem(slot, ammount, null);
                                    return false;
                                }
                            });
                            player.getDialogueInterpreter().sendInput(false, "Enter the amount.");
                            return true;
                    }
                    return true;
            }
            return false;
        }
    }

    /**
     * Represents the bank deposit interface handler.
     *
     * @author 'Vexia
     */
    public static final class BankDepositInterface extends ComponentPlugin {

        /**
         * Represents the deposit animation.
         */
        private static final Animation DEPOSIT_ANIMATION = new Animation(834);

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ComponentDefinition.put(4465, this);
            ComponentDefinition.put(7423, this);
            return this;
        }

        @Override
        public boolean handle(final Player player, Component component, int opcode, int button, final int slot, int itemId) {
            final Item item = player.getInventory().get(slot);
            switch (component.getId()) {
                case 4465:
                case 7423:
                    player.animate(DEPOSIT_ANIMATION);
                    switch (opcode) {

                        case OperationCode.OPTION_OFFER_ONE:
                            player.getBank().addItem(slot, 1, null);
                            break;

                        case OperationCode.OPTION_OFFER_FIVE:
                            player.getBank().addItem(slot, 5, null);
                            break;

                        case OperationCode.OPTION_OFFER_TEN:
                            player.getBank().addItem(slot, 10, null);
                            break;

                        case OperationCode.OPTION_OFFER_X:
                            player.setAttribute("runscript", new RunScript() {

                                @Override
                                public boolean handle() {
                                    final int amount = (int) value;

                                    player.getBank().addItem(slot, amount, null);
                                    player.getActionSender().sendUpdateItems(7423, player.getInventory().toArray());
                                    return false;
                                }
                            });
                            player.getDialogueInterpreter().sendInput(false, "Enter the amount.");
                            break;

                        case OperationCode.OPTION_OFFER_ALL:
                            player.getPulseManager().run(new Pulse(1, player) {

                                @Override
                                public boolean pulse() {
                                    player.getBank().addItem(slot, player.getInventory().getCount(item), null);
                                    player.getActionSender().sendUpdateItems(7423, player.getInventory().toArray());
                                    return true;
                                }
                            });
                            return true;
                        case 180://B.O.B
                            player.getFamiliarManager().dumpBob();
                            break;
                    }
                    player.getActionSender().sendUpdateItems(7423, player.getInventory().toArray());
                    return true;
            }
            return false;
        }
    }

    /**
     * Represents the plugin used to handle the banker npc.
     *
     * @author 'Vexia
     * @author Emperor
     * @version 1.01
     */
    public static final class BankNPCPlugin extends OptionHandler {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            NPCDefinition.setOptionHandler("bank", this);
            NPCDefinition.setOptionHandler("collect", this);
            return this;
        }

        @Override
        public boolean handle(final Player player, Node node, String option) {
            final NPC npc = ((NPC) node);
            npc.faceLocation(node.getLocation());
            if (node.getId() == 4519) {
                if (!(player.getInventory().contains(9083) || player.getEquipment().contains(9083))) {
                    player.getInterfaceState().close();
                    player.lock(7);
                    World.submit(new Pulse(2) {

                        int count = 0;

                        @Override
                        public boolean pulse() {
                            this.setDelay(1);
                            switch (count++) {
                                case 0:
                                    player.getInterfaceState().openComponent(8677);
                                    break;
                                case 3:
                                    player.getProperties().setTeleportLocation(Location.create(2621, 3687, 0));
                                    break;
                                case 5:
                                    player.unlock();
                                    player.getInterfaceState().close();
                                    player.getInterfaceState().openDefaultTabs();
                                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                                    player.getDialogueInterpreter().sendPlaneMessage("The banker notices you are of the Fremennik,", "they furiously cast you back to Rellekka!");
                                    return true;
                            }
                            return false;
                        }
                    });
                    return true;
                }
            }
            if (option.equals("collect")) {
                if (!GrandExchangeDatabase.hasInitialized()) {
                    player.getDialogueInterpreter().sendDialogue("The Grand Exchange desk seems to be closed...");
                    return true;
                }
                player.getGrandExchange().openCollectionBox();
                return true;
            }
            if (option.equals("bank")) {
                player.getBank().open();
            }
            return true;
        }

        @Override
        public Location getDestination(Node n, Node node) {
            NPC npc = (NPC) node;
            if (npc.getLocation().equals(2847, 5105, 0) || npc.getLocation().equals(2848, 5105, 0)) {
                return npc.getLocation().transform(0, -2, 0); // home bankers
            }
            if (npc.getAttribute("facing_booth", false)) {
                Direction dir = npc.getDirection();
                return npc.getLocation().transform(dir.getStepX() << 1, dir.getStepY() << 1, 0);
            }
            if (npc.getId() == 6533) {
                return Location.create(3167, 3490, 0);//ge bankers.
            } else if (npc.getId() == 6535) {
                return Location.create(3162, 3489, 0);
            } else if (npc.getId() == 4907) {
                return npc.getLocation().transform(0, -2, 0);
            }
            return super.getDestination(npc, node);
        }
    }

    /**
     * Represents the abstract npc of a banker.
     *
     * @author 'Vexia
     * @author Emperor
     */
    public static final class BankNPC extends AbstractNPC {

        /**
         * Represents the banker npc ids.
         */
        private static final int[] NPC_IDS = {
            4907, 44, 45, 166, 494, 495, 496, 497, 498,
            499, 902, 1036, 1360, 1702, 2163, 2164, 2354,
            2355, 2568, 2569, 2570, 2619, 3046, 3198, 3199,
            4296, 4519, 5257, 5258, 5259, 5260, 5383, 5488,
            5776, 5777, 5898, 5912, 5913, 6200, 6362, 6532,
            6533, 6534, 6535, 6538, 7049, 7050, 7445, 7446,
            7605
        };

        /**
         * Constructs a new {@code BankNPC} {@code Object}.
         */
        public BankNPC() {
            super(0, null);
        }

        /**
         * Constructs a new {@code BankNPC} {@code Object}.
         *
         * @param id       The NPC id.
         * @param location The location.
         */
        private BankNPC(int id, Location location) {
            super(id, location);
        }

        @Override
        public void init() {
            super.init();
            for (int i = 0; i < 4; i++) {
                Direction dir = Direction.get(i);
                Location loc = getLocation().transform(dir.getStepX(), dir.getStepY(), 0);
                GameObject bank = RegionManager.getObject(loc);
                if (bank != null && bank.getName().equals("Bank booth")) {
                    setDirection(dir);
                    setAttribute("facing_booth", true);
                    super.setWalks(false);
                    break;
                }
            }
        }

        @Override
        public AbstractNPC construct(int id, Location location, Object... objects) {
            return new BankNPC(id, location);
        }

        @Override
        public int[] getIds() {
            return NPC_IDS;
        }
    }

    /**
     */
    public static final class IronmanNotePlugin extends UseWithHandler {

        /**
         * Bank booth ids.
         */
        private static final int[] BOOTH_IDS = new int[]{ 2213, 3045, 5276, 6084, 10517, 11338, 11402, 11758, 12798, 14367, 16700, 18491, 19230, 20325, 22819, 24914, 25808, 26972, 29085, 30016, 34752, 35647, 36786, 37474 };

        /**
         *
         */
        public IronmanNotePlugin() {
            super(getItems());
        }

        /**
         * Gets the products.
         *
         * @return the ids.
         */
        public static int[] getItems() {
            final List<Integer> ids = new ArrayList<>();
            for (ItemDefinition itemDefinition : ItemDefinition.getDefinitions().values()) {
                if (!itemDefinition.isUnnoted()) {
                    continue;
                }
                if (itemDefinition.getNoteId() == -1) {
                    continue;
                }
                ids.add(itemDefinition.getId());
            }
            final int[] array = new int[ids.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = ids.get(i);
            }
            return array;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (int boothId : BOOTH_IDS) {
                addHandler(boothId, OBJECT_TYPE, this);
            }
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            if (!Ironman.isUltimateIronman(event.getPlayer())) {
                return false;
            }
            if (!event.getUsedItem().getDefinition().isUnnoted()) {
                return false;
            }
            if (event.getUsedItem().getDefinition().getNoteId() == -1) {
                return false;
            }
            int amount = event.getPlayer().getInventory().getCount(event.getUsedItem());
            if (event.getPlayer().getInventory().remove(new Item(event.getUsedItem().getId(), amount))) {
                event.getPlayer().getInventory().add(new Item(event.getUsedItem().getDefinition().getNoteId(), amount));
            }
            event.getPlayer().getDialogueInterpreter().sendItemMessage(event.getUsedItem(), "The banker exchanges your items for banknotes.");
            return true;
        }
    }

    /**
     */
    public static final class IronmanUnnotePlugin extends UseWithHandler {

        /**
         * Bank booth ids.
         */
        private static final int[] BOOTH_IDS = new int[]{ 2213, 3045, 5276, 6084, 10517, 11338, 11402, 11758, 12798, 14367, 16700, 18491, 19230, 20325, 22819, 24914, 25808, 26972, 29085, 30016, 34752, 35647, 36786, 37474 };

        /**
         *
         */
        public IronmanUnnotePlugin() {
            super(getItems());
        }

        /**
         * Gets the products.
         *
         * @return the ids.
         */
        public static int[] getItems() {
            final List<Integer> ids = new ArrayList<>();
            for (ItemDefinition itemDefinition : ItemDefinition.getDefinitions().values()) {
                if (itemDefinition.isUnnoted()) {
                    continue;
                }
                ids.add(itemDefinition.getId());
            }
            final int[] array = new int[ids.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = ids.get(i);
            }
            return array;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (int boothId : BOOTH_IDS) {
                addHandler(boothId, OBJECT_TYPE, this);
            }
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            Player player = event.getPlayer();
            if (event.getUsedItem().getDefinition().isUnnoted()) {
                return false;
            }
            if (player.getInventory().freeSlots() == 0) {
                player.getActionSender().sendMessage("Not enough space in inventory.");
                return true;
            }
            int itemCount = player.getInventory().getCount(event.getUsedItem());
            // How many do we unnote?
            int unnoteCount = itemCount;
            if (itemCount > player.getInventory().freeSlots()) {
                unnoteCount -= (itemCount - player.getInventory().freeSlots());
            }
            if (player.getInventory().remove(new Item(event.getUsedItem().getId(), unnoteCount))) {
                for (int index = 0; index < unnoteCount; index++) {
                    player.getInventory().add(new Item(event.getUsedItem().getNoteChange(), 1), true);
                }
            }
            player.getDialogueInterpreter().sendItemMessage(event.getUsedItem().getNoteChange(), "The banker exchanges your banknotes for items.");
            return true;
        }
    }
}

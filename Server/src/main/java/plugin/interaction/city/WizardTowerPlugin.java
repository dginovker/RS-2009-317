package plugin.interaction.city;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.Skillcape;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.content.global.travel.EssenceTeleport;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatSpell;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ChildPositionContext;
import org.gielinor.net.packet.out.RepositionChild;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.misc.RunScript;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the plugins used related to the wizard tower.
 *
 * @author 'Vexia
 *         TODO 317 BARK MAKING, AND TEST DIALOGUE
 */
public final class WizardTowerPlugin extends OptionHandler {

    /**
     * Represents the location of the wizard tower.
     */
    private static final Location BASEMENT = new Location(3104, 9576, 0);

    /**
     * Represents the location of the ground floor.
     */
    private static final Location GROUND_FLOOR = new Location(3105, 3162, 0);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(12540).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(12539).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(2147).getConfigurations().put("option:climb-down", this);
        ObjectDefinition.forId(32015).getConfigurations().put("option:climb-up", this); //Lumbridge basement uses this object too!
        NPCDefinition.forId(300).getConfigurations().put("option:teleport", this);
        ObjectDefinition.forId(11993).getConfigurations().put("option:open", this);
        PluginManager.definePlugin(new WizardtowerWizardNPC());
        PluginManager.definePlugin(new WizardTowerDialogue());
        PluginManager.definePlugin(new WizardGrayzagDialogue());
        PluginManager.definePlugin(new WizardDialogue());
        PluginManager.definePlugin(new SedridorDialogue());
        PluginManager.definePlugin(new AuburyDialoguePlugin());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "teleport":
                EssenceTeleport.teleport(((NPC) node), player);
                break;
            case "climb-down":
                player.getProperties().setTeleportLocation(BASEMENT);
                break;
            case "climb-up":
                if (player.getLocation().withinDistance(Location.create(3117, 9753, 0))) {
                    ClimbActionHandler.climb(player, new Animation(828), new Location(3092, 3361, 0));
                    return true;
                }
                if (!Location.create(3103, 9576, 0).equals(((GameObject) node).getLocation())) {
                    return ObjectDefinition.getOptionHandler(2147, "climb-up").handle(player, node, option);
                }
                player.getProperties().setTeleportLocation(GROUND_FLOOR);
                break;
            case "search":
                player.getDialogueInterpreter().open(458543948);
                break;
            case "open":
                if (node.getLocation().equals(new Location(3107, 3162, 0))) {
                    DoorActionHandler.handleAutowalkDoor(player, (GameObject) node, player.getLocation().getX() >= 3107 ? Location.create(3106, 3161, 0) : Location.create(3108, 3163, 0));
                } else {
                    DoorActionHandler.handleDoor(player, (GameObject) node);
                }
                return true;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof GameObject) {
            final GameObject object = (GameObject) n;
            if (object.getId() == 11993 && object.getLocation().equals(new Location(3107, 3162, 0))) {
                return node.getLocation().getX() >= 3107 ? Location.create(3108, 3163, 0) : Location.create(3106, 3161, 0);
            }
        }
        return null;
    }

    /**
     * Represents a wizard at the wizard tower.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class WizardtowerWizardNPC extends AbstractNPC {

        /**
         * The NPC ids of NPCs using this plugin.
         */
        private static final int[] ID = { 3097 };

        /**
         * Constructs a new {@code WizardtowerWizardNPC} {@code Object}.
         */
        public WizardtowerWizardNPC() {
            super(0, null);
        }

        /**
         * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
         *
         * @param id       The NPC id.
         * @param location The location.
         */
        private WizardtowerWizardNPC(int id, Location location) {
            super(id, location);
        }

        @Override
        public AbstractNPC construct(int id, Location location, Object... objects) {
            return new WizardtowerWizardNPC(id, location);
        }

        @Override
        public void init() {
            super.init();
            getProperties().getCombatPulse().setStyle(CombatStyle.MAGIC);
            getProperties().setAutocastSpell((CombatSpell) SpellBook.MODERN.getSpell(8));
        }

        @Override
        public void finalizeDeath(Entity killer) {
            super.finalizeDeath(killer);
            GroundItemManager.create(new Item(526), getLocation(), (Player) killer);
        }

        @Override
        public int[] getIds() {
            return ID;
        }

    }

    /**
     * Represents the dialogue used in the wizard tower.
     *
     * @author 'Vexia
     */
    public final class WizardTowerDialogue extends DialoguePlugin {

        /**
         * Represents the book name to use.
         */
        private String bookName = "";

        /**
         * Represents the book names.
         */
        private final String[] books = new String[]{ "Living with a Wizard Husband - a Housewife's Story", "Wind Strike for Beginners", "So you think you're a Mage? Volume 28", "101 Ways to Impress your Mates with Magic", "The Life & Times of a Thingummywut by Traiborn the Wizard", "How to become the Ulitmate Wizard of the Universe", "The Dark Arts of Magical Wands" };

        /**
         * Constructs a new {@code WizardTowerDialogue.java} {@code Object}.
         */
        public WizardTowerDialogue() {
        }

        /**
         * Constructs a new {@code WizardTowerDialogue.java} {@code Object}.
         *
         * @param player the player.
         */
        public WizardTowerDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new WizardTowerDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            interpreter.sendPlaneMessage("There's a large selection of books, the majority of which look fairly", "old. Some very strange names... You pick one at random :");
            bookName = books[RandomUtil.random(books.length)];
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    interpreter.sendPlaneMessage("'" + bookName + "'");
                    stage = 1;
                    break;
                case 1:
                    interpreter.sendPlaneMessage("Interesting...");
                    stage = 2;
                    break;
                case 2:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 458543948 };
        }
    }

    /**
     * Handles the WizardGrayzagDialogue dialogue.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class WizardGrayzagDialogue extends DialoguePlugin {

        /**
         * Constructs a new {@code WizardGrayzagDialogue} {@code Object}.
         */
        public WizardGrayzagDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code WizardGrayzagDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public WizardGrayzagDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new WizardGrayzagDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Not now, I'm trying to concentrate on a very difficult", "spell!");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 5006 };
        }
    }

    /**
     * Handles the WizardDialogue dialogue.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class WizardDialogue extends DialoguePlugin {

        /**
         * Represents the bark item used to make split bark equipment.
         */
        private static final Item BARK = new Item(3239);

        /**
         * Constructs a new {@code WizardDialogue} {@code Object}.
         */
        public WizardDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code WizardDialogue.java} {@code Object}.
         *
         * @param player the player.
         */
        public WizardDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new WizardDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello there, can I help you?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    interpreter.sendOptions("Select an Option", "What do you do here?", "What's that you're wearing?", "Can you make me some armour please?", "No thanks.");
                    stage = 1;
                    break;
                case 1:
                    switch (optionSelect) {
                        case FOUR_OPTION_ONE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What do you do here?");
                            stage = 10;
                            break;
                        case FOUR_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What's that you're wearing?");
                            stage = 20;
                            break;
                        case FOUR_OPTION_THREE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you make me some armour please?");
                            stage = 30;
                            break;
                        case FOUR_OPTION_FOUR:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thanks.");
                            stage = 40;
                            break;
                    }
                    break;
                case 30:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Certainly, what would like to me to make?");
                    stage = 31;
                    break;
                case 31:
                    player.getInterfaceState().open(new Component(306));
                    int count = 2;
                    int shift = 75;
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 26, 10, shift));
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 22, 88, shift));
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 18, 288, shift));
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 14, 188, shift + 5));
                    PacketRepository.send(RepositionChild.class, new ChildPositionContext(player, 306, 10, 380, shift));
                    int indexes[] = new int[]{ 26, 22, 14, 18, 10 };
                    for (SplitBark bark : SplitBark.values()) {
                        player.getActionSender().sendItemZoomOnInterface(bark.itemId, 170, 306, count);
                        player.getActionSender().sendString498(TextUtils.formatDisplayName(bark.name()), 306, indexes[bark.ordinal()]);
                        count++;
                    }
                    stage = 32;
                    break;
                case 32:
                    final SplitBark bark = SplitBark.forButton(optionSelect);
                    if (bark == null) {
                        end();
                        return true;
                    }

                    if (!player.getInventory().contains(BARK.getId(), bark.getAmt())) {
                        String name = bark == SplitBark.HELM ? "a splitbark helm" : bark == SplitBark.BODY ? "a splitbark body" : bark == SplitBark.BOOTS ? "splitbark boots" : bark == SplitBark.GAUNTLETS ? "splitbark gauntlets" : "splitbark legs";
                        interpreter.sendDialogues(npc, null, "You need " + bark.getAmt() + " pieces of bark for " + name + ".");
                        return true;
                    }
                    final int amount = optionSelect.getAmount(optionSelect.getId()); // TODO HERE
                    if (amount == -1) {//rscript.
                        player.getDialogueInterpreter().sendInput(false, "Enter amount:");
                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                final int amt = (int) getValue();
                                make(bark, amt);
                                return true;
                            }
                        });
                        return true;
                    }
                    make(bark, amount);
                    break;
                case 33:
                    end();
                    break;
                case 20:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Split-bark armour is special armour for mages, it's much", "more resistant to physical attacks than normal robes.", "It's actually very easy for me to make, but I've been", "having trouble getting hold of the pieces.");
                    stage = 14;
                    break;
                case 10:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I've been studying the practice of making split-bark", "armour.");
                    stage = 11;
                    break;
                case 11:
                    interpreter.sendOptions("Select an Option", "Split-bark armour, what's that?", "Can you make me some?");
                    stage = 12;
                    break;
                case 12:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Split-bark armour, what's that?");
                            stage = 13;
                            break;
                        case TWO_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you make me some?");
                            stage = 9000;
                            break;
                    }
                    break;
                case 13:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Split-bark armour is special armour for mages, it's much", "more resistant to physical attacks than normal robes.", "It's actually very easy for me to make, but I've been", "having trouble getting hold of the pieces.");
                    stage = 14;
                    break;
                case 14:
                    interpreter.sendOptions("Select an Option", "Well good luck with that.", "Can you make me some?");
                    stage = 15;
                    break;
                case 15:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Well good luck with that.");
                            stage = 16;
                            break;
                        case TWO_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you make me some?");
                            stage = 9000;
                            break;
                    }
                    break;
                case 16:
                    end();
                    break;
                case 9000:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I need bark from a hollow tree, and some fine cloth.", "Unfortunately both these items can be found in", "Morytania, especially the cloth which is found in the", "tombs of shades.");
                    stage = 9001;
                    break;
                case 9001:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Of course I'd happily sell you some at a discounted", "price if you bring me those items.");
                    stage = 9002;
                    break;
                case 9002:
                    interpreter.sendOptions("Select an Option", "Ok, guess I'll go looking then!", "Ok, how much do I need?");
                    stage = 9003;
                    break;
                case 9003:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Ok, guess I'll go looking then!");
                            stage = 9004;
                            break;
                        case TWO_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Ok, how much do I need?");
                            stage = 9005;
                            break;
                    }
                    break;
                case 9004:
                    end();
                    break;
                case 9005:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "1 need 1 piece of each for either gloves or boots,", "2 pieces of each for a hat,", "3 pieces of each for leggings,", "and 4 pieces of each for a top.");
                    stage = 9006;
                    break;
                case 9006:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'll charge you 1,000 coins for either gloves or boots,", "6,000 coins for a hat", "32,000 coins for leggings,", "and 37,000 for a top.");
                    stage = 9007;
                    break;
                case 9007:
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Ok, guess I'll go looking then!");
                    stage = 9008;
                    break;
                case 9008:
                    end();
                    break;
                case 40:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 1263 };
        }

        /**
         * Method used to make split bark.
         *
         * @param bark   the bark.
         * @param amount the amount.
         */
        public final void make(final SplitBark bark, int amount) {
            final int barkAmt = player.getInventory().getCount(BARK);
            if (barkAmt < amount * bark.getAmt()) {
                interpreter.sendDialogues(npc, null, "You don't have enough bark to make that many.");
                stage = 33;
                return;
            }
            if (amount < 1) {
                end();
                return;
            }
            if (player.getInventory().freeSlots() < amount) {
                interpreter.sendDialogues(player, null, "Sorry, I don't seem to have enough inventory space.");
                stage = 33;
                return;
            }
            if (!player.getInventory().contains(Item.COINS, bark.getCost() * amount)) {
                interpreter.sendDialogues(player, null, "Sorry, I don't seem to have enough coins.");
                stage = 33;
                return;
            }
            final Item money = new Item(Item.COINS, bark.getCost() * amount);
            final Item barkRemove = new Item(BARK.getId(), amount * bark.getAmt());
            if (player.getInventory().remove(money) && player.getInventory().remove(barkRemove)) {
                player.getInventory().add(new Item(bark.getItemId(), amount));
                interpreter.sendDialogues(npc, null, "There you go, enjoy your new armour!");
                return;
            }
            end();
        }

        /**
         * Represents the split bark item info.
         *
         * @author 'Vexia
         * @date 21/11/2013
         */
        public enum SplitBark {
            HELM(3385, 6000, 2, 9),
            BODY(3387, 37000, 4, 13),
            LEGS(3389, 32000, 3, 17),
            GAUNTLETS(3391, 1000, 1, 21),
            BOOTS(3393, 1000, 1, 25);

            /**
             * Constructs a new {@code WizardDialogue.java} {@code Object}.
             *
             * @param itemId   the item id.
             * @param cost     the cost.
             * @param buttonId the button id.
             */
            SplitBark(final int itemId, final int cost, final int amt, final int buttonId) {
                this.itemId = itemId;
                this.cost = cost;
                this.amt = amt;
                this.buttonId = buttonId;
            }

            /**
             * Represents the item to give.
             */
            private final int itemId;

            /**
             * Represents the cost of the item.
             */
            private final int cost;

            /**
             * Represents the needed amt.
             */
            private final int amt;

            /**
             * Represents the button id.
             */
            private final int buttonId;

            /**
             * Gets the itemId.
             *
             * @return The itemId.
             */
            public int getItemId() {
                return itemId;
            }

            /**
             * Gets the cost.
             *
             * @return The cost.
             */
            public int getCost() {
                return cost;
            }

            /**
             * Gets the buttonId.
             *
             * @return The buttonId.
             */
            public int getButtonId() {
                return buttonId;
            }

            /**
             * Method used to get the splitbark armour.
             *
             * @param optionSelect The option select.
             * @return The split bark.
             */
            public static SplitBark forButton(final OptionSelect optionSelect) {
                SplitBark bark = null;
                // TODO HERE
//                if (buttonId >= 7 && buttonId <= 10) {
//                    bark = HELM;
//                }
//                if (buttonId >= 11 && buttonId <= 14) {
//                    bark = BODY;
//                }
//                if (buttonId >= 15 && buttonId <= 18) {
//                    bark = LEGS;
//                }
//                if (buttonId >= 19 && buttonId <= 22) {
//                    bark = GAUNTLETS;
//                }
//                if (buttonId >= 23 && buttonId <= 26) {
//                    bark = BOOTS;
//                }
                return bark;
            }

            /**
             * Gets the amt.
             *
             * @return The amt.
             */
            public int getAmt() {
                return amt;
            }
        }
    }

    /**
     * Handles the SedridorDialogue dialogue.
     *
     * @author 'Vexia
     */
    public final static class SedridorDialogue extends DialoguePlugin {

        /**
         * Represents the talisman item.
         */
        private static final Item TALISMAN = new Item(1438);

        /**
         * Represents the package item.
         */
        private static final Item PACKAGE = new Item(290);

        /**
         * Represents the notes item.
         */
        private static final Item NOTES = new Item(291);

        /**
         * Represents the graphic to use.
         */
        private static final Graphics GRAPHIC = new Graphics(6);

        /**
         * Constructs a new {@code SedridorDialogue} {@code Object}.
         */
        public SedridorDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code SedridorDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public SedridorDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new SedridorDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Welcome adventurer, to the world renowned", "Wizards' Tower. How may I help you?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    interpreter.sendDialogues(player, null, "Hello there.");
                    stage = 400;
                    break;
                case 400:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello again " + player.getUsername() + ". What can I do for you?");
                    stage = 401;
                    break;
                case 401:
                    interpreter.sendOptions("Select an Option", "Nothing thanks, I'm just looking around.", "Can you teleport me to the Rune Essence?");
                    stage = 402;
                    break;
                case 402:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Nothing thanks, I'm just looking around.");
                            stage = 403;
                            break;
                        case TWO_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you teleport me to the Rune Essence?");
                            stage = 405;
                            break;
                    }
                    break;
                case 403:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Well, take care adventurer. You stand on the", "ruins of the old destroyed Wizards' Tower.", "Strange and powerful magicks lurk here,");
                    stage = 404;
                    break;
                case 404:
                    end();
                    break;
                case 405:
                    end();
                    EssenceTeleport.teleport(npc, player);
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 300 };
        }
    }

    /**
     * Represents the dialogue plugin used for aubury.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class AuburyDialoguePlugin extends DialoguePlugin {

        /**
         * Represents the package item.
         */
        private static final Item PACKAGE = new Item(290);

        /**
         * Represents the package item.
         */
        private static final Item NOTES = new Item(291);

        /**
         * The NPC ids that use this dialogue plugin.
         */
        private static final int[] NPC_IDS = { 553 };

        /**
         * Constructs a new {@code AuburyDialoguePlugin} {@code Object}.
         */
        public AuburyDialoguePlugin() {
            /*
             * empty.
             */
        }

        public AuburyDialoguePlugin(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new AuburyDialoguePlugin(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            if (Skillcape.isMaster(player, Skills.MAGIC)) {
                options("Can I buy a Skillcape of Runecrafting?", "Something else");
                stage = 450;
            } else {
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Do you want to buy some runes?");
            }
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    interpreter.sendOptions("Choose an option:", "Yes, please.", "No thanks.", "Can you teleport me to the rune essence?");
                    stage = 1;
                    break;
                case 1:
                    switch (optionSelect) {
                        case THREE_OPTION_ONE: // TODO 317
                            Shops.AMAZING_MAGIC.open(player);
                            end();
                            break;
                        case THREE_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thanks.");
                            stage = 10;
                            break;
                        case THREE_OPTION_THREE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you teleport me to the rune essence?");
                            stage = 11;
                            break;
                    }
                    break;
                case 10:
                    end();
                    break;
                case 11:
                    EssenceTeleport.teleport(npc, player);
                    end();
                    break;
                case 450:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            player("Can I buy a Skillcape of Runecrafting?");
                            stage = 2;
                            break;
                        case TWO_OPTION_TWO:
                            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Do you want to buy some runes?");
                            stage = 0;
                            break;
                    }
                    break;
                case 2:
                    npc("Certainly! Right when you give me 99000 coins.");
                    stage = 3;
                    break;
                case 3:
                    options("Okay, here you go.", "No");
                    stage = 4;
                    break;
                case 4:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            player("Okay, here you go.");
                            stage = 5;
                            break;
                        case TWO_OPTION_TWO:
                            end();
                            break;
                    }
                    break;
                case 5:
                    if (Skillcape.purchase(player, Skills.RUNECRAFTING)) {
                        npc("There you go! Enjoy.");
                    }
                    stage = 6;
                    break;
                case 6:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return NPC_IDS;
        }
    }

}

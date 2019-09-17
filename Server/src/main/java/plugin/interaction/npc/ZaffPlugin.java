package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the plugin used for buying a battle staff from zeke.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ZaffPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.setOptionHandler("buy-battlestaves", this);
        new ZaffDialogue().init();
        new ZaffStaveDialogue().init();
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().open(9679);
        return true;
    }

    /**
     * Represents the dialogue plugin used for the zaff npc.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class ZaffDialogue extends DialoguePlugin {

        /**
         * Represents the staff item.
         */
        private static final Item STAFF = new Item(11014, 1);

        /**
         * Constructs a new {@code ZaffDialogue} {@code Object}.
         */
        public ZaffDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code ZaffDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public ZaffDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new ZaffDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Would you like to buy or sell some staves or is there", "something else you need?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {

            switch (stage) {
                case 0:
                    interpreter.sendOptions("Select an Option", "Yes, please.", "No, thank you.");
                    stage = 1;
                    break;
                case 1:

                    switch (optionSelect) {
                        case THREE_OPTION_ONE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, please.");
                            stage = 10;
                            break;
                        case THREE_OPTION_TWO:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thank you.");
                            stage = 20;
                            break;
                        case THREE_OPTION_THREE:
                            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can I have another ring?");
                            stage = 50;
                            break;
                    }

                    break;
                case 10:
                    end();
                    Shops.STAFF_SHOP1.open(player);
                    break;
                case 20:
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Well, 'stick' your head in again if you change your mind.");
                    stage = 21;
                    break;
                case 21:
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Huh, terrible pun. You just can't get the 'staff' these", "days!");
                    stage = 22;
                    break;
                case 22:
                    end();
                    break;
                case 50:
                    if (player.getInventory().contains(11014, 1)) {
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Go and get the one that's in your inventory " + player.getUsername() + "!");
                    } else if (player.getBank().contains(11014, 1)) {
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Go and get the one that's in your bank" + player.getUsername() + "!");
                    } else if (player.getEquipment().contains(11014, 1)) {
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Go and get the one that's on your finger " + player.getUsername() + "!");
                    } else {
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Of course you can! Here you go " + player.getUsername() + "!");
                        player.getInventory().add(STAFF);
                    }
                    stage = 51;
                    break;
                case 51:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 532 };
        }
    }

    /**
     * Represents the dialogue used to buy staves from zaff.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class ZaffStaveDialogue extends DialoguePlugin {

        /**
         * Thee ammount of battlestaves.
         */
        private int ammount = 8;

        /**
         * Constructs a new {@code ZaffBuyStavesDialogue} {@code Object}.
         */
        public ZaffStaveDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code ZaffBuyStavesDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public ZaffStaveDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new ZaffStaveDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Do you have any battlestaves?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    ammount = player.getSavedData().getGlobalData().getZaffAmount();
                    if (player.getSavedData().getGlobalData().getZaffTime() > System.currentTimeMillis() && ammount <= 0) {
                        interpreter.sendDialogues(532, FacialExpression.NO_EXPRESSION, "I'm very sorry! I seem to be out of battlestaves at the", "moment! I expect I'll get some more in by tomorrow,", "though.");
                        stage = 2;
                        break;
                    }
                    if (player.getSavedData().getGlobalData().getZaffTime() < System.currentTimeMillis() && ammount == 0) {
                        player.getGameAttributes().removeAttribute("zaf-amt");
                        ammount = 8;
                    }
                    interpreter.sendDialogues(532, FacialExpression.NO_EXPRESSION, "Battlestaves cost 8,000 gold pieces each. I have " + ammount + " left.", "How many would you like to buy?");
                    stage = 1;
                    break;
                case 1:
                    end();
                    end();
                    player.setAttribute("runscript", new RunScript() {

                        @Override
                        public boolean handle() {
                            int amt = (int) value;
                            /** the ammount to buy. */
                            if (amt > ammount) {
                                amt = ammount;
                            }
                            if (amt > player.getInventory().freeSlots()) {
                                amt = player.getInventory().freeSlots();
                            }
                            if (amt > player.getSavedData().getGlobalData().getZaffAmount()) {
                                amt = player.getSavedData().getGlobalData().getZaffAmount();
                            }
                            if (amt == 0) {
                                return true;
                            }
                            if (7000 * amt > player.getInventory().getCount(new Item(Item.COINS))) {
                                player.getActionSender().sendMessage("You don't have enough money to buy that many.");
                                return true;
                            } else {
                                Item remove = new Item(Item.COINS, 7000 * amt);
                                if (!player.getInventory().containsItem(remove)) {
                                    end();
                                    return true;
                                }
                                player.getInventory().remove(remove);
                                if (player.getInventory().add(new Item(1391, amt))) {
                                    player.getSavedData().getGlobalData().setZaffTime(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
                                    if (player.getSavedData().getGlobalData().getZaffAmount() == 0) {
                                        player.getSavedData().getGlobalData().setZaffAmount(8 - amt);
                                        return true;
                                    }
                                    if (player.getSavedData().getGlobalData().getZaffAmount() > amt) {
                                        player.getSavedData().getGlobalData().setZaffAmount(player.getSavedData().getGlobalData().getZaffAmount() - amt);
                                    } else {
                                        player.getSavedData().getGlobalData().setZaffAmount(amt - player.getSavedData().getGlobalData().getZaffAmount());
                                    }
                                }
                            }
                            return true;
                        }
                    });
                    interpreter.sendInput(false, "Zaff has " + ammount + " battlestaves...");
                    break;
                case 2:
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Oh, okay then. I'll try again another time.");
                    stage = 3;
                    break;
                case 3:
                    end();
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 9679 };
        }
    }
}

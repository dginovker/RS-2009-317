package plugin.dialogue;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.game.node.item.Item;

/**
 * Represents the thessalia dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ThessaliaDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code ThessaliaDialogue} {@code Object}.
     */
    public ThessaliaDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code ThessaliaDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public ThessaliaDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ThessaliaDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        if (args.length == 2) {
            interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "Woah! Fabulous! You look absolutely great!");
            stage = 600;
            return true;
        } else if (args.length == 3) {
            interpreter.sendOptions("Select an Option", "I'd like to change my top please.", "I'd like to change my legwear please.", "I'd like to buy some clothes.", "No, thank you.");
            stage = 55;
            return true;
        }
        interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "Would you like to buy any fine clothes?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Choose an option:", "What do you have?", "No, thank you.");
                stage = 1;
                break;
            case 1:

                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What do you have?");
                        stage = 10;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thank you.");
                        stage = 202;
                        break;
                }
                break;
            case 202:
                interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "Well, please return if you change your mind.");
                stage = 203;
                break;
            case 203:
                end();
                break;
            case 2:
                break;
            case 10:
                interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "I have a number of fine pieces of clothing on sale or,", "if you prefer, I can offer you an exclusive, total clothing", "makeover?");
                stage = 11;
                break;
            case 11:
                interpreter.sendOptions("Select an Option", "Tell me more about this makeover.", "I'd just like to buy some clothes.");
                stage = 12;
                break;
            case 12:

                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Tell me more about this makeover.");
                        stage = 50;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        Shops.THESSALIAS_FINE_CLOTHES.open(player);
                        break;
                }
                break;
            case 50:
                interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "Certainly!");
                stage = 51;
                break;
            case 51:
                interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "Here at Thessalia's fine clothing boutique, we offer a", "unique service where we will totally revamp your outfit", "to your choosing, for... wait for it...");
                stage = 52;
                break;
            case 52:
                interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "A fee of only 500 gold coins! Tired of always wearing", "the same old outfit, day in, day out? This is the service", "for you!");
                stage = 53;
                break;
            case 53:
                interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "So what do you say? Interested? We can change either", "your top, or your legwear for only 500 gold a item!");
                stage = 54;
                break;
            case 54:
                interpreter.sendOptions("Select an Option", "I'd like to change my top please.", "I'd like to change my legwear please.", "I'd like to buy some clothes.", "No, thank you.");
                stage = 55;
                break;
            case 55:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'd like to change my top please.");
                        stage = 100;
                        break;
                    case FOUR_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'd like to change my legwear please.");
                        stage = 110;
                        break;
                    case FOUR_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'd just like to buy some clothes.");
                        stage = 120;
                        break;
                    case FOUR_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thank you.");
                        stage = 130;
                        break;
                }
                break;
            case 120:
                end();
                Shops.THESSALIAS_FINE_CLOTHES.open(player);
                break;
            case 130:
                end();
                break;
            case 110:
                interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "Just select what style and colour you would like from", "this catalogue, and then give me the 500 gold when", "you've picked.");
                stage = 111;
                break;
            case 111:
                if (!player.getInventory().contains(Item.COINS, 500)) {
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I don't have 500 gold on me...");
                    stage = 105;
                    break;
                } else {
                    if (player.getAppearance().getGender() == Gender.FEMALE) {
                        end();
                        player.getInterfaceState().open(new Component(4731));
                    } else {
                        end();
                        player.getInterfaceState().open(new Component(0));
                    }
                }
                break;
            case 100:
                interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "Just select what style and colour you would like from", "this catalogue, and then give me the 500 gold when", "you've picked.");
                stage = 101;
                break;
            case 101:
                if (!player.getInventory().contains(Item.COINS, 500)) {
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I don't have 500 gold on me...");
                    stage = 105;
                    break;
                } else {
                    if (player.getAppearance().getGender() == Gender.FEMALE) {
                        end();
                        player.getInterfaceState().open(new Component(3038));
                    } else {
                        end();
                        player.getInterfaceState().open(new Component(2851));
                    }
                }
                break;
            case 105:
                interpreter.sendDialogues(548, FacialExpression.NO_EXPRESSION, "That's ok! Just come back when you do have it!");
                stage = 106;
                break;
            case 106:
                end();
                break;
            case 600:
                end();
                player.getInterfaceState().close();
                break;
        }
        return true;
    }


    @Override
    public int[] getIds() {
        return new int[]{ 548 };
    }
}

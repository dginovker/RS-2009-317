package plugin.interaction.npc;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Handles the RoavarDialogue dialogue.
 *
 * @author 'Vexia
 */
public class RoavarDialogue extends DialoguePlugin {

    public RoavarDialogue() {

    }

    public RoavarDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new RoavarDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello there!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Greeting traveller. Welcome to 'The Hair Of The Dog'", "Tavern. What can I do you for?");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Select an Option", "Can I buy a beer?", "Can I hear some gossipp?", "Can I hear a story?", "Nothing thanks.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can I buy a beer?");
                        stage = 10;
                        break;
                    case FOUR_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can I hear some gossip?");
                        stage = 20;
                        break;
                    case FOUR_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can I hear a story?");
                        stage = 30;
                        break;
                    case FOUR_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Nothing thanks.");
                        stage = 40;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Well that's my speciality! The local brew's named", "'Moonlight Mead' and will set you back 5 gold.", "Waddya say? Fancy a pint?");
                stage = 11;
                break;
            case 11:
                interpreter.sendOptions("Select an Option", "Yes please.", "Actually, no thanks.");
                stage = 12;
                break;
            case 12:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes please.");
                        stage = 15;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Actually, no thanks.");
                        stage = 14;
                        break;

                }
                break;
            case 14:
                end();
                break;
            case 15:
                if (player.getInventory().contains(Item.COINS, 5)) {
                    if (player.getInventory().remove(new Item(Item.COINS, 5))) {
                        player.getInventory().add(new Item(2955, 1));
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Here ya go pal. Enjoy!");
                        stage = 16;
                    }
                } else {
                    end();
                    player.getActionSender().sendMessage("You need 5 gold coins to buy a pint of beer.");
                }
                break;
            case 16:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I am not one to gossip!");
                stage = 21;
                break;
            case 21:
                end();
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I don't have any stories worth telling.");
                stage = 31;
                break;
            case 31:
                end();
                break;
            case 40:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "...I don't know why you talked to me if you don't want", "anything then...");
                stage = 41;
                break;
            case 41:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6527 };
    }
}

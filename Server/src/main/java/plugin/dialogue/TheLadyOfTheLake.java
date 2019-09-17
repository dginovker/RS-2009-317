package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Handles the Toolleprechaun dialogue.
 *
 * @author 'Vexia
 */
public class TheLadyOfTheLake extends DialoguePlugin {

    public TheLadyOfTheLake() {

    }

    public TheLadyOfTheLake(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 250 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("What would you like to say?", "Who are you?", "I seek the sword Excalibur.", "Good day.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Who are you?");
                        stage = 100;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I seek the sword Excalibur.");
                        stage = 161;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Good day.");
                        stage = 703;
                        break;

                }
                break;
            case 100:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I am the Lady of the Lake.");
                stage = 102;
                break;
            case 102:
                interpreter.sendOptions("What would you like to say?", "I seek the sword Excalivur.", "Good day.");
                stage = 150;
                break;
            case 150:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I seek the sword Excalibur.");
                        stage = 161;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Good day.");
                        stage = 703;
                        break;

                }
                break;
            case 161:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I recall you have already proven yourself to be worthy of", "wielding it. I shall return it to you if you can prove", "yourself to still be worthy.");
                stage = 162;
                break;
            case 162:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "And how can I do that?");
                stage = 163;
                break;
            case 163:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Why, by proving youself to be above material goods.");
                stage = 164;
                break;
            case 164:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "... And how can I do that?");
                stage = 165;
                break;
            case 165:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "500 coins ought to do it.");
                stage = 166;
                break;
            case 166:
                interpreter.sendOptions("What would you like to say?", "Okay, here you go.", "500 coins? No thanks!");
                stage = 167;
                break;
            case 167:
                switch (optionSelect.getButtonId()) {
                    case 1:

                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Okay, here you go.");
                        stage = 168;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "500 coins? No thanks.");
                        stage = 456;
                        break;
                }
                break;
            case 168:
                if (player.getInventory().freeSlots() == 0) {
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, I don't seem to have enough inventory space.");
                    stage = 169;
                } else if (player.getInventory().contains(Item.COINS, 500)) {
                    player.getInventory().remove(new Item(Item.COINS, 500));
                    player.getInventory().add(new Item(35, 1));
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Thank's for the cash!", "I'm saving up for a new dress.");
                    stage = 170;
                } else {
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, I don't seem to have enough conins.");
                    stage = 169;
                }
                break;
            case 169:
                end();
                break;
            case 170:
                interpreter.sendPlaneMessage("The lady of the Lake hands you Excalibur.");
                stage = 171;
                break;
            case 171:
                end();
                break;
            case 699:
                interpreter.sendOptions("What would you like to say?", "Who are you?", "Good day.");
                stage = 700;
                break;
            case 700:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Who are you?");
                        stage = 720;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Good day.");
                        stage = 703;
                        break;
                }
                break;
            case 720:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I am the Lady of the Lake.");
                stage = 721;
                break;
            case 721:
                end();
                break;
            case 456:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Your materialistic attitude saddens me.");
                stage = 457;
                break;
            case 457:
                end();
                break;
            case 703:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new TheLadyOfTheLake(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (!player.getInventory().contains(35, 1) && !player.getBank().contains(35, 1) && !player.getEquipment().contains(35, 1)) {
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Good day to you, sir.");
            stage = 0;
        } else {
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Good day to you, sir.");
            stage = 699;
        }
        return true;
    }
}

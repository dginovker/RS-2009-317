package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Handles the PortSarimBartenderDialogue dialogue.
 *
 * @author 'Vexia
 */
public class PortSarimBartenderDialogue extends DialoguePlugin {

    public PortSarimBartenderDialogue() {

    }

    public PortSarimBartenderDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 734 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {

        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello there!");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Choose an option:", "Could I buy a beer, please.", "Bye, then.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Could I buy a beer, please?");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Bye, then.");
                        stage = 20;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Sure, that will be two gold coins, please.");
                stage = 11;
                break;
            case 11:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Okay, here you go.");
                stage = 12;
                break;
            case 12:
                if (player.getInventory().contains(Item.COINS, 2)) {
                    interpreter.sendPlaneMessage("You buy a pint of beer.");
                    player.getInventory().remove(new Item(Item.COINS, 2));
                    player.getInventory().add(new Item(1917, 1));
                    stage = 13;
                } else {
                    player.getActionSender().sendMessage("You need 2 gold coins to buy beer.");
                    end();
                }
                break;
            case 13:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Come back soon!");
                stage = 21;
                break;
            case 21:
                end();
                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new PortSarimBartenderDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Good day to you!");
        stage = 0;
        return true;
    }
}

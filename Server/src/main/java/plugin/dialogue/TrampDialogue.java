package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Handles the TrampDialogue dialogue.
 *
 * @author 'Vexia
 */
public class TrampDialogue extends DialoguePlugin {

    public TrampDialogue() {

    }

    public TrampDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 11 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {

        switch (stage) {
            case 0:
                interpreter.sendOptions("What would you like to say?", "Yes, I can spare a little money.", "Sorry, you'll have to earn it yourself.");
                stage = 10;
                break;
            case 10:

                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, I can spare a little money.");
                        stage = 100;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.ANGRY, "Sorry, you'll have to earn it yourself, just like I did.");
                        stage = 50;
                        break;
                }
                break;
            case 100:
                if (player.getInventory().contains(Item.COINS, 1)) {
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Thank's mate!");
                    stage = 101;
                } else {
                    end();
                    player.getActionSender().sendMessage("You only need one coin to give to this tramp.");
                }
                break;
            case 101:
                end();
                break;
            case 50:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Please yourself.");
                stage = 51;
                break;
            case 51:
                end();
                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new TrampDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Got any spare change, mate?");
        stage = 0;
        return true;
    }
}

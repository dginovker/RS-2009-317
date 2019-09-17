package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the RatBugiss dialogue.
 *
 * @author 'Vexia
 */
public class RatBugiss extends DialoguePlugin {

    public RatBugiss() {

    }

    public RatBugiss(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5833 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Oh, hello. I'd love to chat right now, but I'm a bit busy.", "Perhaps you could come back and chat another time?");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Of course. Sorry to bother you!");
                stage = 2;
                break;
            case 2:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "No problem! Say, have you been to see the wizard's", "tower in the south yet? It's an amazing sight! you", "should go and see it!");
                stage = 3;
                break;
            case 3:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Thanks! I will!");
                stage = 4;
                break;
            case 4:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new RatBugiss(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello!");
        stage = 0;
        return true;
    }
}

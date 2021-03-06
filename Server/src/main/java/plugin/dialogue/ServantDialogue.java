package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the ServantDialogue dialogue.
 *
 * @author 'Vexia
 */
public class ServantDialogue extends DialoguePlugin {

    public ServantDialogue() {

    }

    public ServantDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new ServantDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hi!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hi!");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Look, I'd better not talk. I'll get in trouble.");
                stage = 2;
                break;
            case 2:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "If you want someone to show you round the castle ask", "Eohric, the Head Servant.");
                stage = 3;
                break;
            case 3:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1081 };
    }
}

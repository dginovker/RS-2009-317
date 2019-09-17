package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the PhilopDialogue dialogue.
 *
 * @author 'Vexia
 */
public class PhilopDialogue extends DialoguePlugin {

    public PhilopDialogue() {

    }

    public PhilopDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 782 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {

        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Gwwrr!");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Err, hello there. What's that you have there?");
                stage = 2;
                break;
            case 2:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Gwwwrrr! Dwa-gon Gwwwwrrrr!");
                stage = 3;
                break;
            case 3:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Enjoy playing with your dragon, then.");
                stage = 4;
                break;
            case 4:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Gwwwrrr!");
                stage = 5;
                break;
            case 5:
                end();
                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new PhilopDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello, what's your name?");
        stage = 0;
        return true;
    }
}

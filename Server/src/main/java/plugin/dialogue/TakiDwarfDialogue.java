package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the TakiDwarfDialogue dialogue.
 *
 * @author 'Vexia
 */
public class TakiDwarfDialogue extends DialoguePlugin {

    public TakiDwarfDialogue() {

    }

    public TakiDwarfDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7115 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {

        switch (stage) {
            case 0:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hi little fellow.");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "What did you just say to me!?");
                stage = 2;
                break;
            case 2:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Arrr! nothing, nothing at all..");
                stage = 3;
                break;
            case 3:
                end();
                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new TakiDwarfDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Arrr!");
        stage = 0;
        return true;
    }
}

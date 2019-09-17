package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the VarrockGateGuardDialogue dialogue.
 *
 * @author 'Vexia
 */
public class VarrockGateGuardDialogue extends DialoguePlugin {

    public VarrockGateGuardDialogue() {

    }

    public VarrockGateGuardDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 368 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {

        switch (stage) {
            case 0:
                end();
                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new VarrockGateGuardDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Please don't disturb me, I've got to keep an eye out for", "suspicious indiduals.");
        stage = 0;
        return true;
    }
}

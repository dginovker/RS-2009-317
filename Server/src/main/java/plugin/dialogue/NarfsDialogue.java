package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the NarfsDialogue dialogue.
 *
 * @author 'Vexia
 */
public class NarfsDialogue extends DialoguePlugin {

    public NarfsDialogue() {

    }

    public NarfsDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 3238 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 1:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "'Narf'? You think that's funny?", "At least I Don't call myself '" + player.getDetails().getUsername() + "' ", "Where did you get a name like that?");
                stage = 100;
                break;
            case 100:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "It seemed like a good idea at the time!");
                stage = 101;
                break;
            case 101:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Bah!");
                stage = 102;
                break;
            case 102:
                end();
                break;

        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new NarfsDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "That's a funny name you've got.");
        stage = 1;
        return true;
    }
}

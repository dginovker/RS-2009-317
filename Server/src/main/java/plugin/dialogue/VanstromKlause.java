package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the VanstromKlause dialogue.
 *
 * @author 'Vexia
 */
public class VanstromKlause extends DialoguePlugin {

    public VanstromKlause() {

    }

    public VanstromKlause(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new VanstromKlause(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello there, how goes it stranger?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Quite well thanks for asking, how about you?");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Quite well my self.");
                stage = 2;
                break;
            case 2:
                end();// todo real dial at this part.
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2020 };
    }
}

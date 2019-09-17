package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue for Patrick Bateman (pickaxe seller).
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PatrickBateman extends DialoguePlugin {

    public PatrickBateman() {

    }

    public PatrickBateman(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new PatrickBateman(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ask me a question.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "So, what do you do?");
                stage = 1;
                break;
            case 1:
                npc("I'm into, uh, well, murders and executions, mostly.");
                stage = 2;
                break;
            case 2:
                player("Do you like it?");
                stage = 3;
                break;
            case 3:
                npc("Well, it depends. Why?");
                stage = 4;
                break;
            case 4:
                player("Well, most guys I know who are in Mergers", "and Acquisitions really don't like it.");
                stage = 5;
                break;
            case 5:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5880 };
    }
}

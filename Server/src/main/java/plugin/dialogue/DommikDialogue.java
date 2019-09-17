package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the DommikDialogue dialogue.
 *
 * @author 'Vexia
 */
public class DommikDialogue extends DialoguePlugin {

    public DommikDialogue() {

    }

    public DommikDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 545 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "No, thanks, I've got all the Crafting equipment I need.", "Let's see what you've got, then.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thanks, I've got all the Crafting equipment I need.");
                        stage = 10;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        // TODO 317
                        //	Shops.DOMMIKS_CRAFTING_STORE.open(player);
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Okay. Fare well on your travels.");
                stage = 11;
                break;
            case 11:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new DommikDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Would you like to buy some Crafting equipment?");
        stage = 0;
        return true;
    }
}

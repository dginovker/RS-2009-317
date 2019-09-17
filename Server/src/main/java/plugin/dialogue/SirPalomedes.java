package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the SirPalomedes dialogue.
 *
 * @author 'Vexia
 */
public class SirPalomedes extends DialoguePlugin {

    public SirPalomedes() {

    }

    public SirPalomedes(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 3787 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "After your help freeing Merlin, my he;p is the least", "I can offer as a man of honour.");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Nothing right now, but I'll bear it in mind. Thanks.");
                stage = 10;
                break;
            case 10:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new SirPalomedes(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello there adventurer, what do you want of me?");
        stage = 0;
        return true;
    }
}

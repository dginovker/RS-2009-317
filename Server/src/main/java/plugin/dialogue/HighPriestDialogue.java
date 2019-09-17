package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the high priest dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class HighPriestDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code HighPriestDialogue} {@code Object}.
     */
    public HighPriestDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code HighPriestDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public HighPriestDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new HighPriestDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Many greetings. Welcome to our fair island.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Enjoy our stay here. May it be spiritually uplifting!");
                stage = 1;
                break;
            case 1:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 216 };
    }
}

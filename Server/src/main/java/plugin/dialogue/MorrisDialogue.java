package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the morris dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MorrisDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code MorrisDialogue} {@code Object}.
     */
    public MorrisDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code MorrisDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public MorrisDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MorrisDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What are you sitting around here for?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'm making sure only those with a competition pass enter", "the fishing contest.");
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
        return new int[]{ 227 };
    }
}

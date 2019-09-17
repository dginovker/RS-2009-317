package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the Boris npc.
 *
 * @author 'Vexia
 */
public final class BorisDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code BorisDialogue} {@code Object}.
     */
    public BorisDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BorisDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public BorisDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BorisDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Out of my way, punk");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 1:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6026, 6032 };
    }
}

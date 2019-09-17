package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the gregory npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GregoryDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code GregoryDialogue} {@code Object}.
     */
    public GregoryDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code GregoryDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public GregoryDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GregoryDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "If I were as ugly as you I would not dare to show my", "face in public!");
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
        return new int[]{ 6033, 6027, 6043, 6036, 6040, 6038, 6045 };
    }
}

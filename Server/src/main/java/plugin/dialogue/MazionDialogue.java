package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue used for the mazion npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MazionDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code MazionDialogue} {@code Object}.
     */
    public MazionDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code MazionDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public MazionDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MazionDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Please leave me alone, a parrot stole my banana.");
        stage = 0;
        return true;
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
    public int[] getIds() {
        return new int[]{ 3114 };
    }
}

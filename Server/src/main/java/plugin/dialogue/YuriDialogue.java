package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue used for the yuri npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class YuriDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code YuriDialogue} {@code Object}.
     */
    public YuriDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code YuriDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public YuriDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new YuriDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hmm... you smell strange...");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        end();
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6028 };
    }
}

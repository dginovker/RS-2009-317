package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the yadviga dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class YadvigaDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code YadvigaDialogue} {@code Object}.
     */
    public YadvigaDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code YadvigaDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public YadvigaDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new YadvigaDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Get lost!");
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
        return new int[]{ 6041 };
    }
}

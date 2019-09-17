package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the burthorpe soldier dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BurthorpeSoldierDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code BurthorpeSoldierDialogue} {@code Object}.
     */
    public BurthorpeSoldierDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BurthorpeSoldierDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public BurthorpeSoldierDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BurthorpeSoldierDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hi!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Vacca foeda.");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Er...");
                stage = 2;
                break;
            case 2:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1065 };
    }
}

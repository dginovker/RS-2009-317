package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue used for sir tiffy.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class SirTiffyCashienDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code SirTiffyCashienDialogue} {@code Object}.
     */
    public SirTiffyCashienDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code SirTiffyCashienDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public SirTiffyCashienDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SirTiffyCashienDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "What ho, sirrag.", "Spiffing day for a walk in the park, what?");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Spiffing?");
                stage = 2;
                break;
            case 2:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Absolutely, top-hole!", "Well, can't stay and chat all day, dontchaknow!", "Ta-ta for now!");
                stage = 3;
                break;
            case 3:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Erm...goodbye.");
                stage = 4;
                break;
            case 4:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2290 };
    }
}

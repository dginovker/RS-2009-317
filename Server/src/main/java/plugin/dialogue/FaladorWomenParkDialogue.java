package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the falador women park dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FaladorWomenParkDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code FaladorWomenParkDialogue} {@code Object}.
     */
    public FaladorWomenParkDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code FaladorWomenParkDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public FaladorWomenParkDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new FaladorWomenParkDialogue(player);
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
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Grettings! Have you come to gaze in rapture at the", "natural beauty of Falador's parkland?");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Um, yes, very nice. Lots of.... trees and stuff.");
                stage = 2;
                break;
            case 2:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Trees! I do so love trees! And flowers! And squirrels.");
                stage = 3;
                break;
            case 3:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, I have a strange urge to be somewhere else.");
                stage = 4;
                break;
            case 4:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Come back to me soon and we can talk again about trees!");
                stage = 5;
                break;
            case 5:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "...");
                stage = 6;
                break;
            case 6:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 3226 };
    }
}

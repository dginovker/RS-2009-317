package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the man dialogues.
 *
 * @author 'Vexia
 */
public class ManDialoguePlugin extends DialoguePlugin {

    /**
     * The NPC ids that use this dialogue plugin.
     */
    private static final int[] NPC_IDS = { 1, 2, 3, 4, 5, 6, 16, 24, 25, 170, 351, 352, 353, 354, 359, 360, 361, 362, 363, 663, 726, 727, 728, 729, 730, 1086, 2675, 2776, 3224, 3225, 3227, 5923, 5924, };

    public ManDialoguePlugin() {

    }

    public ManDialoguePlugin(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return NPC_IDS;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'm very well thank you.");
                stage = 6969;
                break;
            case 6969:
                end();
                break;
            case 2:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Who are you?");
                stage = 20;
                break;
            case 3:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'm fine, how are you?");
                stage = 30;
                break;
            case 4:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "No, I don't want to buy anything!");
                stage = 40;
                break;
            case 5:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I think we need a new king. The one we've got isn't", "very good.");
                stage = 22;
                break;
            case 20:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'm a bold adventurer.");
                stage = 21;
                break;
            case 21:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ah, a very noble profession.");
                stage = 22;
                break;
            case 22:
                end();
                break;
            case 30:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Very well thank you.");
                stage = 31;
                break;
            case 31:
                end();
                break;
            case 40:
                end();
                break;
            case 50:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ManDialoguePlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (npc == null) {
            return false;
        }
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello, how's it going?");
        stage = RandomUtil.random(0, 5);
        if (stage == 1) {
            stage = 0;
        }
        return true;
    }

}

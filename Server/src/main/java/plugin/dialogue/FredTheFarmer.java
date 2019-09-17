package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for fred the farmer npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FredTheFarmer extends DialoguePlugin {

    /**
     * Constructs a new {@code FredTheFarmer} {@code Object}.
     */
    public FredTheFarmer() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code FredTheFarmer} {@code Object}.
     *
     * @param player the player.
     */
    public FredTheFarmer(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new FredTheFarmer(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "What are you doing on my land? You're not the one", "who keeps leaving all my gates open and letting out all", "my sheep are you?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "I'm looking for something to kill.", "I'm lost.");
                stage = 346;
                break;
            case 346:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'm looking for something to kill.");
                        stage = 20;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'm lost.");
                        stage = 30;
                        break;
                }
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "How can you be lost? Just follow the road east and", "south. You'll end up in Lumbridge fairly quickly.");
                stage = 31;
                break;
            case 31:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "What, on my land? Leave my livestock alone you", "scoundrel!");
                stage = 21;
                break;
            case 21:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 758 };
    }
}

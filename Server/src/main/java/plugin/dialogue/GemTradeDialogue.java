package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the gem trade dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GemTradeDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code GemTradeDialogue} {@code Object}.
     */
    public GemTradeDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code GemTradeDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public GemTradeDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GemTradeDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello adventurer! Can I interest you in any of my gems?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes!", "No, thanks you.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes!");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thanks though.");
                        stage = 20;
                        break;

                }
                break;
            case 10:
                end();
                // TODO 317
                //	Shops.GEM_TRADER.open(player);
                break;
            case 20:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 540 };
    }
}

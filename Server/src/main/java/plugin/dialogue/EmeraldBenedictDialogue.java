package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the emerald benedict dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class EmeraldBenedictDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code EmeraldBenedictDialogue} {@code Object}.
     */
    public EmeraldBenedictDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code EmeraldBenedictDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public EmeraldBenedictDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new EmeraldBenedictDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Got anything you don't want to lose?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes actually, can you help?", "Yes, but can you show me my PIN settings?", "Yes thanks, and I'll keep hold of it too.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        end();
                        player.getBank().open();
                        break;
                    case THREE_OPTION_TWO:
                        end();
                        player.getBankPin().openSettings();
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Yes thanks, and I'll keep hold of it too.");
                        stage = 99;
                        break;
                }
                break;
            case 99:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2271 };
    }
}

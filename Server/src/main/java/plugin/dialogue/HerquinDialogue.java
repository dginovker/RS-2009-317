package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the herquin dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class HerquinDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code HerquinDialogue} {@code Object}.
     */
    public HerquinDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code HerquinDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public HerquinDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new HerquinDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendOptions("Select an Option", "Do you wish to trade?", "Sorry, I don't want to talk to you, actually.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Do you wish to trade?");
                stage = 10;
                break;
            case 2:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, I don't want to talk to you, actually.");
                stage = 3;
                break;
            case 3:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Huh, charming.");
                stage = 4;
                break;
            case 4:
                end();
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Why, yes, this is a jewel shop after all.");
                stage = 11;
                break;
            case 11:
                end();
                // TODO 317
                //	Shops.HERQUINS_GEMS.open(player);
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 584 };
    }
}

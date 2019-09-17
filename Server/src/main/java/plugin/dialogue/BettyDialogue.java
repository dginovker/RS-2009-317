package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin for the betty npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BettyDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code BettyDialogue} {@code Object}.
     */
    public BettyDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BettyDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public BettyDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BettyDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Welcome to the magic emporium.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Can I see your wares?", "Sorry, I'm not into Magic.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        // TODO 317
                        //	Shops.BETTYS_MAGIC_EMPORIUM.open(player);
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, I'm not into Magic.");
                        stage = 20;
                        break;
                }
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Well, if you see ayone who is into Magic, please send", "them my way.");
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
        return new int[]{ 583 };
    }
}

package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin for the npc alfonse the waiter.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class AlfonseWaiterDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code AlfonseWaiterDialogue} {@code Object}.
     */
    public AlfonseWaiterDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code AlfonseWaiterDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public AlfonseWaiterDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new AlfonseWaiterDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Welcome to the Shrimps and Parrot.", "Would you like to order, sir?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes, please.", "No, thank you.", "Where do you get your Karambwan from?");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, please.");
                        stage = 10;
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thank you.");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Where do you get your Karambwan from?");
                        stage = 30;
                        break;
                }
                break;
            case 10:
                end();
                Shops.THE_SHRIMP_AND_PARROT.open(player);
                break;
            case 20:
                end();
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "We buy directly off Lubufu, a local fisherman. He", "seems to have a monopoly over Karambwan sale.");
                stage = 31;
                break;
            case 31:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 793 };
    }
}
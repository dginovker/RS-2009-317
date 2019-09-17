package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.config.Constants;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for Lanthus.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class LanthusDialogue extends DialoguePlugin {

    /**
     * Constructs a new <code>LanthusDialogue</code>.
     */
    public LanthusDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new <code>LanthusDialogue</code>.
     *
     * @param player the player.
     */
    public LanthusDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new LanthusDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello! What can I help you with?");
                stage = 1;
                break;
            case 1:
                options("What are you doing here?", "How do I donate?", "How do I claim my reward?", "I want to see your shop.", "Nothing.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        player("What are you doing here?");
                        stage = 3;
                        break;
                    case FIVE_OPTION_TWO:
                        player("How do I donate?");
                        stage = 4;
                        break;
                    case FIVE_OPTION_THREE:
                        player("How do I claim my reward?");
                        stage = 6;
                        break;
                    case FIVE_OPTION_FOUR:
                        player("I want to see your shop, please.");
                        stage = 7;
                        break;
                    case FIVE_OPTION_FIVE:
                        player("Nothing.");
                        stage = END;
                        break;
                }
                break;
            case 3:
                npc("I'm here to sell specialty items to those who have", "Gielinor tokens from donating.");
                stage = 1;
                break;
            case 4:
                npc("You can donate by visiting the " + Constants.SERVER_NAME + " website", "at www.Gielinor.org, and clicking the \"Donate\" button", "located under the logo.");
                stage = 5;
                break;
            case 5:
                npc("You can find a list of things you can buy with", "the Gielinor tokens rewarded on that page.", "You can also click the \"FAQ\" button, to see more", "details about donating.");
                stage = 6;
                break;
            case 6:
                npc("After you have donated, the server will check to", "automatically give you the reward for what", "you have donated. If not, you can type ::claim", "and you will receive your Gielinor tokens.");
                stage = 1;
                break;
            case 7:
                end();
                Shops.DONOR_SHOP.open(player);
                break;
            case END:
                end();
                return true;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1526 };
    }
}

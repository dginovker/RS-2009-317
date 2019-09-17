package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the RimmingtonShopKeeperDialogue dialogue.
 *
 * @author 'Vexia
 */
public class RimmingtonShopKeeperDialogue extends DialoguePlugin {

    public RimmingtonShopKeeperDialogue() {

    }

    public RimmingtonShopKeeperDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 531, 530 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes, please. What are you selling?", "How should I use your shop?", "No, thanks.");
                stage = 1;
                break;
            case 1:

                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        end();
                        // TODO 317
                        //	Shops.GENERAL_STORE.open(player);
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'm glad you asked! You can buy as many of the items", "stocked as you wish. You can also sell most items to the", "shop.");
                        stage = 10;
                        break;
                    case THREE_OPTION_THREE:
                        end();
                        break;
                }

                break;
            case 10:
                end();
                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new RimmingtonShopKeeperDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Can I help you at all?");
        stage = 0;
        return true;
    }
}

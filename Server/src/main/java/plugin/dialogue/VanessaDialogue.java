package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the VanessaDialogue dialogue.
 *
 * @author 'Vexia
 */
public class VanessaDialogue extends DialoguePlugin {

    public VanessaDialogue() {

    }

    public VanessaDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2305 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "What are you selling?", "Can you give me any Farming advice?", "I'm okay, thank you.");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Select an Option", "What are you selling?", "Can you give me any Farming advice?", "I'm okay, thank you.");
                stage = 10;
                break;
            case 10:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        end();
                        Shops.VANESSAS_FARMING_SHOP.open(player);
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you give me any Farming advice?");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'm okay, thank you.");
                        stage = 30;
                        break;

                }
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Yes - ask a gardener.");
                stage = 21;
                break;
            case 21:
                end();
                break;
            case 30:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new VanessaDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello. How can I help you?");
        stage = 0;
        return true;
    }
}

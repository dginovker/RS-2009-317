package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the TeaSellerDialogue dialogue.
 *
 * @author 'Vexia
 */
public class TeaSellerDialogue extends DialoguePlugin {

    public TeaSellerDialogue() {

    }

    public TeaSellerDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 595 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {

        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes, please.", "No, thanks.", "What are you selling?");
                stage = 1;
                break;
            case 1:

                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, please.");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thanks.");
                        stage = 20;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What are you selling?");
                        stage = 30;
                        break;
                }

                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Only the most delicious infusion of the leaves of the tea", "plant. Grown in the exotic regions of this world. Buy", "yourself a cup.");
                stage = 31;
                break;
            case 31:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Well, if you're sure. You know where to come if you do.");
                stage = 21;
                break;
            case 21:
                end();
                break;
            case 10:
                end();
                Shops.TEA_SHOP.open(player);
                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new TeaSellerDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Greetings! Are you in need of refreshment?");
        stage = 0;
        return true;
    }
}

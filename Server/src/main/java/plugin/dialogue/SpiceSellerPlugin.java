package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the SpiceSellerPlugin dialogue.
 *
 * @author 'Vexia
 */
public class SpiceSellerPlugin extends DialoguePlugin {

    public SpiceSellerPlugin() {

    }

    public SpiceSellerPlugin(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 572 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes.", "No.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        end();
                        // TODO 317
                        //	Shops.ARDOUGNE_SPICE_STALL.open(player);
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thanks.");
                        stage = 20;
                        break;

                }
                break;
            case 20:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new SpiceSellerPlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Are you interested in buying or selling spice?");
        stage = 0;
        return true;
    }
}

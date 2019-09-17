package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the SilverMechantPlugin dialogue.
 *
 * @author 'Vexia
 */
public class SilverMechantPlugin extends DialoguePlugin {

    public SilverMechantPlugin() {

    }

    public SilverMechantPlugin(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 569 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes please.", "No, thank you.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        end();
                        Shops.ARDOUGNE_SILVER_STALL.open(player);
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thank you.");
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

        return new SilverMechantPlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Silver! Silver! Best prices for buying and selling in all", "Kandarin!");
        stage = 0;
        return true;
    }
}

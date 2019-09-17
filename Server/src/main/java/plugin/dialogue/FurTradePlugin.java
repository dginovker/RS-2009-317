package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the plugin used for the fur trader.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FurTradePlugin extends DialoguePlugin {

    /**
     * Constructs a new {@code FurTradePlugin} {@code Object}.
     */
    public FurTradePlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code FurTradePlugin} {@code Object}.
     *
     * @param player the player.
     */
    public FurTradePlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new FurTradePlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Would you like to trade in fur?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes.", "No.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        // TODO 317
                        //	Shops.ARDOUGNE_FUR_TRADER.open(player);
                        break;
                    case TWO_OPTION_TWO:
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
    public int[] getIds() {
        return new int[]{ 573 };
    }
}

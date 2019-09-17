package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used to handle the candle maker npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CandleMakerDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code CandleMakerDialogue} {@code Object}.
     */
    public CandleMakerDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code CandleMakerDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public CandleMakerDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CandleMakerDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ah, you again. You're quite a trend setter. Can't believe", "the number of black candle requests I've had since you", "came. I couldn't pass up a business opportunity like that,", "bad luck or no. So I'm selling them now. Would you be");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 1:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "interested in some of my fine candles?");
                stage = 2;
                break;
            case 2:
                interpreter.sendOptions("Select an Option", "Yes Please.", "No thank you.");
                stage = 3;
                break;
            case 3:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        Shops.CANDLE_SHOP_TYPE.open(player);
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thank you.");
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
        return new int[]{ 562 };
    }
}

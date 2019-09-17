package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the gem merchant dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GemMerchantDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code GemMerchantDialogue} {@code Object}.
     */
    public GemMerchantDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code GemMerchantDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public GemMerchantDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GemMerchantDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Here, look at my lovely gems.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                end();
                // TODO 317
                //	Shops.GEM_MERCHANT.open(player);
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 570 };
    }
}

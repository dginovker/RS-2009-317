package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the cassie npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CassieDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code CassieDialogue} {@code Object}.
     */
    public CassieDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code CassieDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public CassieDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CassieDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I buy and sell shields; do you want to trade?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes, please.", "No, thank you.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        // TODO 317
                        //	Shops.SHIELD_SHOP.open(player);
                        break;
                    case TWO_OPTION_TWO:
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
    public int[] getIds() {
        return new int[]{ 577 };
    }
}

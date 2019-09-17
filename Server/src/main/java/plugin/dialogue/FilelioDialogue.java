package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the filelio dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FilelioDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code FilelioDialogue} {@code Object}.
     */
    public FilelioDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code FilelioDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public FilelioDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new FilelioDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello there.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "H-hello. You l-look like a s-stranger to these p-parts.", "Would you l-ike to buy something? I h-have some s-", "special offers at the m-minute...some s-sample bottles for", "s-storing s-snail slime.");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Select an Option", "Yes, please.", "No, thanks.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, please.");
                        stage = 10;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thanks.");
                        stage = 20;
                        break;

                }
                break;
            case 20:
                end();
                break;
            case 10:
                end();
                Shops.GENERAL_STOREC_CANIFIS_.open(player);
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1040 };
    }
}

package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the frinco npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FrincosDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code FrincosDialogue} {@code Object}.
     */
    public FrincosDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code FrincosDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public FrincosDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new FrincosDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello, how can I help you?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "What are you selling?", "You can't; I'm beyond help.", "I'm okay, thank you.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What are you selling?");
                        stage = 10;
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "You can't; I'm beyond help.");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'm okay, thank you.");
                        stage = 30;
                        break;
                }
                break;
            case 10:
                end();
                // TODO 317
                //	org.gielinor.game.content.global.shop.Shops.FRINCOSS_FABULOUS_HERB_STORE.open(player);
                break;
            case 20:
                end();
                break;
            case 30:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 578 };
    }
}

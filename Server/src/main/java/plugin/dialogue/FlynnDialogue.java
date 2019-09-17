package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the flynn npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FlynnDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code FlynnDialogue} {@code Object}.
     */
    public FlynnDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code FlynnDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public FlynnDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new FlynnDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello. Do you want to buy or sell any maces?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "No, thanks.", "Well, I'll have a look, at least.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, thanks.");
                        stage = 10;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        Shops.MACE_SHOP.open(player);
                        break;

                }
                break;
            case 10:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 580 };
    }
}

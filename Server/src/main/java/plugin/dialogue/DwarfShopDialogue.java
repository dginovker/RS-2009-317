package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialouge plugin used for the dward shop.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class DwarfShopDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code DwarfShopDialogue} {@code Object}.
     */
    public DwarfShopDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code DwarfShopDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public DwarfShopDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DwarfShopDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Can I help you at all?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes please, what are you selling?", "No thanks.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        Shops.DWARVEN_SHOPPING_STORE.open(player);
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 582 };
    }
}

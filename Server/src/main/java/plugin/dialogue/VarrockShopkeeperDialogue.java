package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the VarrockShopkeeperDialogue dialogue.
 *
 * @author 'Vexia
 */
public class VarrockShopkeeperDialogue extends DialoguePlugin {

    public VarrockShopkeeperDialogue() {

    }

    public VarrockShopkeeperDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 523, 522, 520, 521 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {

        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes, please. What are you selling?", "No, thanks.");
                stage = 1;
                break;
            case 1:

                switch (optionSelect.getButtonId()) {
                    case 1:
                        end();
                        if (npc.getId() == 520 || npc.getId() == 521) {
                            Shops.LUMBRIDGE_GENERAL_STORE.open(player);
                        } else {
                            Shops.VARROCK_GENERAL_STORE.open(player);
                        }
                        break;
                    case 2:
                        end();
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
    public DialoguePlugin newInstance(Player player) {
        return new VarrockShopkeeperDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Can i help you at all?");
        stage = 0;
        return true;
    }
}

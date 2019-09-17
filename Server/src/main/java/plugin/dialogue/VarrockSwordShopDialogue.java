package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the VarrockSwordShopDialogue dialogue.
 *
 * @author 'Vexia
 */
public class VarrockSwordShopDialogue extends DialoguePlugin {

    public VarrockSwordShopDialogue() {

    }

    public VarrockSwordShopDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 551, 552 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {

        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes, please.", "No, I'm okay for swords right now.");
                stage = 1;
                break;
            case 1:

                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes, please.");
                        stage = 2;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No, I'm okay for swords right now.");
                        stage = 10;
                        break;
                }

                break;
            case 2:
                end();
                // TODO 317
                //	Shops.VARROCK_SWORD_SHOP.open(player);
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Come back if you need any.");
                stage = 11;
                break;
            case 11:
                end();
                break;
        }

        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new VarrockSwordShopDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello, adventurer. Can I interest you in some swords?");
        stage = 0;
        return true;
    }
}

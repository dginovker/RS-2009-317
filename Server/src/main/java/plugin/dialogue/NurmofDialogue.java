package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the NurmofDialogue dialogue.
 *
 * @author 'Vexia
 */
public class NurmofDialogue extends DialoguePlugin {

    public NurmofDialogue() {

    }

    public NurmofDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new NurmofDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Greetings and welcome to my pickaxe shop. Do you", "want to buy my premium quality pickaxes?");
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
                switch (optionSelect.getButtonId()) {
                    case 1:
                        end();
                        Shops.NURMOFS_PICKAXE_SHOP.open(player);
                        break;
                    case 2:
                        end();
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 594 };
    }
}

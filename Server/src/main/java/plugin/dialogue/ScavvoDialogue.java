package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the ScavvoDialogue dialogue.
 *
 * @author 'Vexia
 */
public class ScavvoDialogue extends DialoguePlugin {

    public ScavvoDialogue() {

    }

    public ScavvoDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new ScavvoDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "'Ello matey! D'ya wanna buy some exiting new toys?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Let's have a look then.");
                stage = 1;
                break;
            case 1:
                end();
                Shops.SCAVIO.open(player);
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 537 };
    }
}

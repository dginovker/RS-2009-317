package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the plugin used for the grandpa jack npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GrandpaJackPlguin extends DialoguePlugin {

    /**
     * Constructs a new {@code GrandpaJackPlguin} {@code Object}.
     */
    public GrandpaJackPlguin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code GrandpaJackPlugin} {@code Object}.
     *
     * @param player the player.
     */
    public GrandpaJackPlguin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GrandpaJackPlguin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello young man!", "Come to visit old Grandpa Jack? I can tell ye stories", "for sure. I used to be the best fisherman these parts", "have seen!");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, just passing through.");
                stage = 2;
                break;
            case 2:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 230 };
    }
}

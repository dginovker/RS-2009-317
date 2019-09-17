package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the elly the camel dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class EllyTheCamelDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code EllyTheCamelDialogue} {@code Object}.
     */
    public EllyTheCamelDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code EllyTheCamelDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public EllyTheCamelDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                end();
                player.getActionSender().sendMessage("The camel tries to stomp on your foot, but you pull it back quickly.");
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new EllyTheCamelDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        int rand = RandomUtil.random(1, 3);
        switch (rand) {
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "If I go near that camel, it'll probably", "bite my hand off.");
                stage = 0;
                break;
            case 2:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I wonder if that camel has fleas...");
                stage = 0;
                break;
            case 3:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I wonder if that camel has fleas...");
                stage = 0;
                break;
        }
        return true;
    }


    @Override
    public int[] getIds() {
        return new int[]{ 2810, 2812 };
    }
}

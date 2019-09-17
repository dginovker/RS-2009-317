package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the SimpleEntityMessage dialogue.
 *
 * @author 'Vexia
 */
public class SimpleEntityMessage extends DialoguePlugin {

    public SimpleEntityMessage() {

    }

    public SimpleEntityMessage(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 8000 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        end();
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new SimpleEntityMessage(player);
    }

    @Override
    public boolean open(Object... args) {
        String[] messages = new String[args.length];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = (String) args[i];
        }
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, messages);
        return true;
    }
}

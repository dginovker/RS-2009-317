package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.member.summoning.pet.Pet;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used to dismiss a follower.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class DismissDialoguePlugin extends DialoguePlugin {

    /**
     * Constructs a new {@code DismissDialoguePlugin} {@code Object}.
     */
    public DismissDialoguePlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code DismissDialoguePlugin} {@code Object}.
     *
     * @param player the player.
     */
    public DismissDialoguePlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DismissDialoguePlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        if (player.getFamiliarManager().getFamiliar() instanceof Pet) {
            interpreter.sendOptions("Free pet", "Yes", "No");
        } else {
            interpreter.sendOptions("Dismiss Familiar", "Yes", "No");
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (player.getFamiliarManager().getFamiliar() instanceof Pet) {
                            interpreter.sendDialogues(player, null, "Run along; I'm setting you free.");
                        } else {
                            end();
                        }
                        player.getFamiliarManager().dismiss(false);
                        stage = 1;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 1:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("dismiss_dial") };
    }
}

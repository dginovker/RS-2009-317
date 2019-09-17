package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the {@link DialoguePlugin} for the Ring of Slaying.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public class RingOfSlayingDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@link RingOfSlayingDialogue} {@link DialoguePlugin}.
     */
    public RingOfSlayingDialogue() {
    }

    /**
     * Constructs a new {@link RingOfSlayingDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public RingOfSlayingDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new RingOfSlayingDialogue(player);
    }

    @Override
    public boolean open(Object... args) {

        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("RingOfSlaying") };
    }
}

package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.node.entity.player.Player;

/**
 * Created by Stan van der Bend on 09/01/2018.
 *
 * todo: figure out piles dialogue. (located in resource area)
 *
 * project: GielinorGS
 * package: plugin.dialogue
 */
public final class PilesDialogue extends DialoguePlugin {

    private int stage;

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new PilesDialogue();
    }

    @Override
    public boolean open(Object... args) {

        return true;
    }

    @Override
    public int[] getIds() {
        return new int[0];
    }
}

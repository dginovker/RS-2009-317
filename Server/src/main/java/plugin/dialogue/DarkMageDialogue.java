package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dark mage dialogues.
 *
 * @author 'Vexia
 * @version 1.0
 * @note todo for abbyss.
 */
public final class DarkMageDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code DarkMageDialogue} {@code Object}.
     */
    public DarkMageDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code DarkMageDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public DarkMageDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DarkMageDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];

        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {

        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2262 };
    }
}

package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for bandits.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class BanditDialogue extends DialoguePlugin {

    /**
     * Constructs a new <code>BanditDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public BanditDialogue() {
    }

    /**
     * Constructs a new <code>BanditDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public BanditDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BanditDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Get out of this village.", "You are not welcome here.");
        stage = END;
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
        return new int[]{ 1926, 1925, 1927, 1928, 1929, 1930, 1931 };
    }
}

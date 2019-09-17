package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the dialogue plugin used for the joshua npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class JoshuaDialoguePlugin extends DialoguePlugin {

    /**
     * Constructs a new {@code JoshuaDialoguePlugin} {@code Object}.
     */
    public JoshuaDialoguePlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code JoshuaDialoguePlugin} {@code Object}.
     *
     * @param player the player.
     */
    public JoshuaDialoguePlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new JoshuaDialoguePlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        end();
        player.getActionSender().sendMessage("He doesn't seem interested in talking to you.");
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
        return new int[]{ 229 };
    }
}

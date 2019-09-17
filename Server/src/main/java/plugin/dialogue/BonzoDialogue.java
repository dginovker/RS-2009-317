package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Rerpesents the dialogue plugin used for the bonzo npc.
 *
 * @author 'Vexia
 * @version 1.0
 * @note finish with fishing contests.
 */
public final class BonzoDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code BonzoDialogue} {@code Object}.
     */
    public BonzoDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BonzoDialogue} {@code Object}.
     *
     * @param player
     */
    public BonzoDialogue(Player player) {
        super(player);
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
    public DialoguePlugin newInstance(Player player) {

        return new BonzoDialogue(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 225 };
    }
}

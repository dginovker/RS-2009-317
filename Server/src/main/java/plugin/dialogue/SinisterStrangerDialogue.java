package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the SinisterStrangerDialogue dialogue.
 *
 * @author 'Vexia
 */
public class SinisterStrangerDialogue extends DialoguePlugin {

    public SinisterStrangerDialogue() {

    }

    public SinisterStrangerDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 3677 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {

        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {

        return new SinisterStrangerDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        end();
        player.getActionSender().sendMessage("He doesn't seem interested in talking to you.");
        return true;
    }
}

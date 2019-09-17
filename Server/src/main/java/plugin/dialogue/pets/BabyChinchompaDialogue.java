package plugin.dialogue.pets;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * @author Erik
 */
public class BabyChinchompaDialogue extends DialoguePlugin {

    public BabyChinchompaDialogue() {
    }

    public BabyChinchompaDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BabyChinchompaDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];

        if (npc instanceof Familiar) {
            Familiar familiar = (Familiar) npc;
            if (familiar.getOwner() != player) {
                player.getActionSender().sendMessage("It doesn't seem interested in talking to you.");
                return true;
            }
        } else {
            player.getActionSender().sendMessage("It doesn't seem interested in talking to you.");
            return true;
        }

        if (npc.getId() == 26759) {
            npc("Squeaka squeaka!");
        } else {
            npc("Squeak squeak!");
        }

        return false;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect option) {
        end();
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 26756, 26757, 26758, 26759 };
    }

}

package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.voting.VoteReward;

import plugin.npc.PenanceNPC.BarbarianReward;

/**
 * Represents the interface for voting rewards.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class VotingRewardInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(27135, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 27135) {
            return false;
        }
        if (player.getAttribute("STARTER_PACKAGE_SELECTION") != null) {
            StarterPackage starterPackage = null;
            int index = 0;
            for (StarterPackage starterPackage1 : StarterPackage.values()) {
                if ((27137 + index) == button) {
                    starterPackage = starterPackage1;
                    break;
                }
                index++;
            }
            if (starterPackage == null) {
                return true;
            }
            player.getInterfaceState().close();
            player.getDialogueInterpreter().open("StarterDialogue", starterPackage);
            return true;
        }
        if (player.getAttribute("BARBARIAN_ASSAULT_REWARD") != null) {
            player.removeAttribute("BARBARIAN_ASSAULT_REWARD");
            BarbarianReward barbarianReward = null;
            int index = 0;
            for (BarbarianReward barbarianReward1 : BarbarianReward.values()) {
                if ((27137 + index) == button) {
                    barbarianReward = barbarianReward1;
                    break;
                }
                index++;
            }
            if (barbarianReward == null) {
                return true;
            }
            player.getInterfaceState().close();
            player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("BarbarianReward"), barbarianReward);
            return true;
        }
        VoteReward voteReward = null;
        int index = 0;
        for (VoteReward voteReward1 : VoteReward.values()) {
            if ((27137 + index) == button) {
                voteReward = voteReward1;
                break;
            }
            index++;
        }
        if (voteReward == null) {
            player.getActionSender().sendMessage("That reward is currently unavailable.");
            return true;
        }
        player.getInterfaceState().close();
        player.getDialogueInterpreter().open(4362, Repository.findNPC(4362), voteReward);
        return true;
    }
}
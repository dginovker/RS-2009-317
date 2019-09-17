package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for the Donor fountain.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DonorFountainPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(880).getConfigurations().put("option:drink-from", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        // TODO donor fountain
        /*if (player.sendDonorNotification("to use this fountain.", DonorStatus.SUPER_DONOR)) {
            return true;
        }*/
        player.lock(2);
        player.getPrayer().reset();
        player.getSettings().setSpecialEnergy(100);
        player.getSettings().updateRunEnergy(-100);
        for (int skillId = 0; skillId < 24; skillId++) {
            int staticLevel = player.getSkills().getStaticLevel(skillId);
            int currentLevel = player.getSkills().getLevel(skillId);
            if (skillId == Skills.HITPOINTS) {
                player.getSkills().heal(staticLevel);
            }
            if (currentLevel < staticLevel) {
                player.getSkills().setLevel(skillId, staticLevel);
            }
        }
        player.getSkills().rechargePrayerPoints();
        player.getSkills().refresh();
        player.getActionSender().sendMessage("You feel fully refreshed.");
        return true;
    }

}

package plugin.skill.summoning;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.summoning.SummoningCreator;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the option used on the summoning obelisk.
 *
 * @author 'Vexia
 * @author Emperor
 */
public final class ObeliskOptionPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "go-down":
                ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, new Location(2209, 5348, 0));
                return true;
            case "climb":
                ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, new Location(2848, 5111, 0));
                return true;
            case "infuse-pouch":
                SummoningCreator.open(player, true);
                return true;
            case "renew-points":
                if (player.getSkills().getLevel(Skills.SUMMONING) == player.getSkills().getStaticLevel(Skills.SUMMONING)) {
                    player.getActionSender().sendMessage("You already have full summoning points.");
                    return true;
                }
                player.visualize(Animation.create(8502), Graphics.create(1308));
                player.getSkills().setLevel(Skills.SUMMONING, player.getSkills().getStaticLevel(Skills.SUMMONING));
                player.getActionSender().sendMessage("You renew your summoning points.");
                return true;
        }
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("infuse-pouch", this);
        ObjectDefinition.setOptionHandler("renew-points", this);
        ObjectDefinition.forId(12263).getConfigurations().put("option:go-down", this);
        ObjectDefinition.forId(28714).getConfigurations().put("option:climb", this);
        return this;
    }

}

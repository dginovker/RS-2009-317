package plugin.skill.agility.shortcuts;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles shortcuts in Zanaris.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ZanarisShortcuts extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(12127).getConfigurations().put("option:squeeze-past", this);
        return this;
    }


    @Override
    public boolean handle(final Player player, Node node, String option) {
        Direction dir = null;
        switch (node.getId()) {
            case 12127:
                int level = (node.getLocation().getX() == 2400 && node.getLocation().getY() == 4403) ? 66 : 46;
                if (player.getSkills().getStaticLevel(Skills.AGILITY) < level) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least " + level + " to do that.");
                    return true;
                }
                if (node.getLocation().getX() == 2400 && node.getLocation().getY() == 4403) {
                    player.faceLocation(Location.create(2408, player.getLocation().getY() == 4402 ? 4404 : 4402));
                    dir = player.getLocation().getY() == 4402 ? Direction.NORTH : Direction.SOUTH;
                    player.getActionSender().sendMessage("You squeeze past the wall.", 1);
                    AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX() * 2, dir.getStepY() * 2, 0), Animation.create(player.getLocation().getY() == 4402 ? 754 : 756), 0, null);
                    return true;
                }
                if (player.getLocation().getY() >= 4400) {
                    player.faceLocation(Location.create(2408, player.getLocation().getY() == 4401 ? 4403 : 4401));
                    dir = player.getLocation().getY() == 4401 ? Direction.NORTH : Direction.SOUTH;
                    if (player.getLocation().getY() == 4403) {
                        AchievementDiary.finalize(player, AchievementTask.JUTTING_WALL);
                    }
                    player.getActionSender().sendMessage("You squeeze past the wall.", 1);
                    AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX() * 2, dir.getStepY() * 2, 0), Animation.create(player.getLocation().getY() == 4401 ? 754 : 756), 0, null);
                    return true;
                }
                if (player.getLocation().getY() < 4400) {
                    player.faceLocation(Location.create(2408, player.getLocation().getY() == 4394 ? 4396 : 4394));
                    dir = player.getLocation().getY() == 4394 ? Direction.NORTH : Direction.SOUTH;
                    player.getActionSender().sendMessage("You squeeze past the wall.", 1);
                    AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX() * 2, dir.getStepY() * 2, 0), Animation.create(player.getLocation().getY() == 4394 ? 754 : 756), 0, null);
                    return true;
                }
                return true;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof GameObject) {
            switch (n.getId()) {
                case 9293:
                    return node.getLocation().getX() < n.getLocation().getX() ? Location.create(2886, 9799, 0) : Location.create(2892, 9799, 0);
            }
        }
        return null;
    }

}
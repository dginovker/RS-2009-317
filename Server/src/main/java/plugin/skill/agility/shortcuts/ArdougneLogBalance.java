package plugin.skill.agility.shortcuts;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The Ardougne log balance shortcut.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ArdougneLogBalance extends OptionHandler {

    @Override
    public boolean handle(final Player player, Node node, String option) {
        final Direction dir = Direction.getLogicalDirection(player.getLocation(), node.getLocation());
        AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX() * 4, dir.getStepY() * 4, 0), Animation.create(762), 3, null);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(35999).getConfigurations().put("option:walk-across", this);
        ObjectDefinition.forId(35997).getConfigurations().put("option:walk-across", this);
        return this;
    }
}

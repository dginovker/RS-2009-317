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
 * Represents the plugin used to handle the tunnel shortcut in Piscatoris.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class PiscatorisTunnel extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(14922).getConfigurations().put("option:enter", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        final Direction dir = node.getLocation().getY() != 3654 ? Direction.NORTH : Direction.SOUTH;
        AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX() * 5, dir.getStepY() * 5, 0), Animation.create(2590), 0, null);
        return true;
    }

}

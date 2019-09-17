package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.action.DropItemHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the drop / destroy option.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class DropItemPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("drop", this);
        ItemDefinition.setOptionHandler("destroy", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        return DropItemHandler.handle(player, node, option);
    }

    @Override
    public Location getDestination(Node node, Node item) {
        return null;
    }
}
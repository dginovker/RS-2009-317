package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.action.PickupHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Ironman;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the option handler used for ground items.
 *
 * @author 'Vexia
 * @author Emperor
 */
public final class PickupPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("take", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        GroundItem groundItem = (GroundItem) node;
        if (!Ironman.canPickup(player, groundItem)) {
            return true;
        }
        return PickupHandler.take(player, groundItem);
    }

    @Override
    public Location getDestination(Node node, Node item) {
        return null;
    }

}
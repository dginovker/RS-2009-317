package plugin.interaction.object;


import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the port phasmaty's energy barrier.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class EnergyBarrierPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(5259).getConfigurations().put("option:pass", this);
        ObjectDefinition.forId(5259).getConfigurations().put("option:pay-toll(2-ecto)", this);
        return this;
    }


    @Override
    public boolean handle(final Player player, Node node, final String option) {
        DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
        return true;
    }

    @Override
    public Location getDestination(Node n, Node object) {
        return null;
    }
}

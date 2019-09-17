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
 * Plugin used for handling the opening/closing of (double)
 * doors/gates/fences/...
 *
 * @author Emperor
 */
public final class DoorManagingPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("open", this);
        ObjectDefinition.setOptionHandler("close", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        GameObject object = (GameObject) node;
        if (object.getType() != 9 && !player.getLocation().equals(node.getLocation()) &&
            !player.getLocation().isNextTo(object.getLocation())) {
            return true;
        }
        if (object.getId() == 1589 || object.getId() == 1590 && object.getLocation().getRegionId() == 12697) {
            player.getActionSender().sendMessage("The gate is locked.");
            return true;
        }
        if (object.getId() == 16113) {
            return false;
        }
        String name = object.getName().toLowerCase();
        if (name.contains("trapdoor")) {
            /**
             * Custom trapdoors.
             */
            if (object.getLocation().getX() == 3495 && object.getLocation().getY() == 3465) {
                player.getActionSender().sendMessage("This trapdoor seems to be locked.");
                return true;
            }
            if (object.getLocation().getX() == 2542 && object.getLocation().getY() == 3327) {
                player.setTeleportTarget(Location.create(2907, 9717, 0));
                return true;
            }
            player.setTeleportTarget(object.getLocation().transform(0, 6400, 0));
            return true;
        }
        if (object.getId() == 2882 || object.getId() == 2883) {
            return false;
        }
        if (!name.contains("door") && !name.contains("gate") && !name.contains("fence") &&
            !name.contains("wall") && !name.contains("exit") && !name.contains("entrance")) {
            return false;
        }
        /**
         * Custom doors.
         */
        if (object.getId() == 30265) {
            return true;
        }
        DoorActionHandler.handleDoor(player, object);
        return true;
    }

    @Override
    public Location getDestination(Node n, Node node) {
        GameObject o = (GameObject) node;
        if (o.getType() < 4 || o.getType() == 9) {
            return DoorActionHandler.getDestination((Player) n, o);
        }
        return null;
    }

}

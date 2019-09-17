package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used for the door near wydin.
 * @author 'Vexia
 * @version 1.0
 */
public final class WydinDoorPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2069).getConfigurations().put("option:open", this);
        return null;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!player.getEquipment().contains(1005, 1) && player.getLocation().getX() >= 3011 && player.getLocation().getX() <= 3018) {
            player.getDialogueInterpreter().open(557, true, true);
            return true;
        } else {
            final GameObject object = (GameObject) node;
            DoorActionHandler.handleAutowalkDoor(player, object);
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        return DoorActionHandler.getDestination(((Entity) node), ((GameObject) n));
    }
}

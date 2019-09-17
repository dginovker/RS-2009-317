package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the shed door for Lost City.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class LostCityShedDoorPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2406).getConfigurations().put("option:open", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (!(node instanceof GameObject)) {
            return false;
        }
        if (node.getLocation().getX() != 3202 && node.getLocation().getY() != 3169) {
            return false;
        }
        DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);//, Location.create(3202, 3169));
        if (player.getEquipment().contains(new Item(772))) {
            player.lock(2);
            player.getActionSender().sendMessage("The world starts to shimmer...");
            World.submit(new Pulse(2, player) {

                @Override
                public boolean pulse() {
                    if (player.getLocation().equals(Location.create(3202, 3169))) {
                        player.setTeleportTarget(Location.create(2452, 4473, 0));
                    }
                    return true;
                }
            });
        }
        return true;
    }
}

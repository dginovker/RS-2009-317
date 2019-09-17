package plugin.interaction.item.withobject;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using items on the ectofuntus.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class EctophileFillPlugin extends UseWithHandler {

    public EctophileFillPlugin() {
        super(4252);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(16648, OBJECT_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        if (!player.getInventory().remove(new Item(4252))) {
            return false;
        }
        player.lock(2);
        player.faceLocation(event.getUsedWith().getCenterLocation());
        player.getInventory().add(new Item(4251));
        player.getActionSender().sendMessage("You refill the ectophial from the Ectofuntus.");
        return true;
    }

    @Override
    public Location getDestination(Player player, Node with) {
        return null;
    }
}
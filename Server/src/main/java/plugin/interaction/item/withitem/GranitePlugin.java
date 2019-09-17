package plugin.interaction.item.withitem;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using a chisel on granite for splitting.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GranitePlugin extends UseWithHandler {

    public GranitePlugin() {
        super(6983, 6981);
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Item granite = event.getBaseItem().getId() == 1755 ? event.getUsedItem() : event.getBaseItem();
        if (!player.getInventory().contains(granite)) {
            return true;
        }
        if (player.getInventory().remove(granite)) {
            player.getInventory().add(new Item(granite.getId() == 6983 ? 6981 : 6979, 2), player);
            player.getActionSender().sendMessage("You split the granite.");
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(1755, ITEM_TYPE, this);
        return this;
    }
}

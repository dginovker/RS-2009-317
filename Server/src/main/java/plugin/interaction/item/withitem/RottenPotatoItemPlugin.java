package plugin.interaction.item.withitem;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using a rotten potato on an item.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RottenPotatoItemPlugin extends UseWithHandler {
    
    /**
     * Constructs a new {@code RottenPotatoItemPlugin}.
     */
    public RottenPotatoItemPlugin() {
        super(ALL_NODES_ALLOWED);
    }
    
    private final int ROTTEN_POTATO = Item.ROTTEN_POTATO;
    
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(ROTTEN_POTATO, ITEM_TYPE, this);
        return this;
    }
    
    @Override
    public boolean handle(NodeUsageEvent event) {
        if (event.getUsedItem() == null || event.getBaseItem() == null) {
            return false;
        }
        if (event.getUsedItem().getId() != ROTTEN_POTATO) {
            return false;
        }
    
        Player player = event.getPlayer();
        
        if (event.getBaseItem().getId() == ROTTEN_POTATO) {
            if (!player.getInventory().add(event.getUsedItem())) {
                player.getActionSender().sendMessage("You don't have enough inventory space.");
            }
        } else {
            if (!player.getInventory().remove(event.getBaseItem())) {
                player.getActionSender().sendMessage("Could not remove item");
            }
        }
        return true;
    }
    
    @Override
    public boolean isWalks() {
        return false;
    }
}

package plugin.interaction.item.withitem;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to make soft clay.
 * @author 'Vexia
 * @date 1/14/14
 */
public final class SoftclayPlugin extends UseWithHandler {

    /**
     * Represents the bucket of water item.
     */
    private static final Item BUCKET_OF_WATER = new Item(1929);

    /**
     * Represents the jug of water item.
     */
    private static final Item JUG_OF_WATER = new Item(1937);

    /**
     * Represents the jug item.
     */
    private static final Item JUG = new Item(1935);

    /**
     * Represents the clay item.
     */
    private static final Item CLAY = new Item(434);

    /**
     * Represents the soft clay item.
     */
    private static final Item SOFT_CLAY = new Item(1761);

    /**
     * Represents the empty bucket item.
     */
    private static final Item BUCKET = new Item(1925);

    /**
     * Constructs a new {@code SoftclayPlugin} {@code Object}.
     */
    public SoftclayPlugin() {
        super(434);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(1929, ITEM_TYPE, this);
        addHandler(1937, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        boolean bucket = event.getUsedItem().getId() == 1929 || event.getBaseItem().getId() == 1929;
        if ((!bucket ? player.getInventory().remove(JUG_OF_WATER) : player.getInventory().remove(BUCKET_OF_WATER)) && player.getInventory().remove(CLAY)) {
            player.getInventory().add(SOFT_CLAY);
            player.getInventory().add(bucket ? BUCKET : JUG);
            player.getActionSender().sendMessage("You mix the clay and water. You now have some soft, workable clay.");
        }
        return true;
    }

}

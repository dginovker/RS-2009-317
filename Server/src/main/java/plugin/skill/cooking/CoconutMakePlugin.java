package plugin.skill.cooking;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to make a coconut.
 * @author 'Vexia
 * @version 1.0
 */
public final class CoconutMakePlugin extends UseWithHandler {

    /**
     * Represents the items related to coconut making plugin.
     */
    private static final Item[] ITEMS = new Item[]{ new Item(5974, 1), new Item(5976, 1), new Item(229), new Item(5935, 1), new Item(5978) };

    /**
     * Constructs a new {@code CoconutMakePlugin} {@code Object}.
     */
    public CoconutMakePlugin() {
        super(5974, 5976);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(Item.HAMMER, ITEM_TYPE, this);
        addHandler(229, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final Item usedWith = (Item) event.getUsedWith();
        if (usedWith.getId() == 5974 && event.getUsedItem().getId() == Item.HAMMER || usedWith.getId() == Item.HAMMER && event.getUsedItem().getId() == 5974) {
            player.getInventory().remove(ITEMS[0]);
            player.getInventory().add(ITEMS[1]);
            player.getActionSender().sendMessage("You crush the coconut with a hammer.", 1);
        }
        if (usedWith.getId() == 5976 && event.getUsedItem().getId() == 229 || usedWith.getId() == 229 && event.getUsedItem().getId() == 5976) {
            player.getInventory().remove(ITEMS[1]);
            player.getInventory().remove(ITEMS[2]);
            player.getInventory().add(ITEMS[3]);
            player.getInventory().add(ITEMS[4]);
            player.getActionSender().sendMessage("You overturn the coconut into a vial.", 1);
        }
        return true;
    }

}

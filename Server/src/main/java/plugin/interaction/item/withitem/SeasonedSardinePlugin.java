package plugin.interaction.item.withitem;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin to make seasoned sardines.
 * @author 'Vexia
 * @date Oct 6, 2013
 */
public class SeasonedSardinePlugin extends UseWithHandler {

    /**
     * Represents the seasoned sardine.
     */
    private final Item SEASONED_SARDINE = new Item(1552);

    /**
     * Constructs a new {@code SeasonedSardinePlugina} {@code Object}.
     */
    public SeasonedSardinePlugin() {
        super(327);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(1573, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        if (player.getInventory().remove(event.getUsedItem()) && player.getInventory().remove(event.getBaseItem())) {
            player.getDialogueInterpreter().sendPlaneMessage("You rub the doogle leaves over the sardine.");
            player.getInventory().add(SEASONED_SARDINE);
        }
        return true;
    }

}

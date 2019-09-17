package plugin.interaction.item.withitem;

import org.gielinor.game.content.eco.grandexchange.GEItemSet;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

import java.util.Arrays;

/**
 * @author Corey
 */
public class OpenItemSetPlugin extends UseWithHandler {

    /**
     * Represents the chisel item.
     */
    private static final Item CHISEL = new Item(1755);

    /**
     * Constructs a new {@code OpenItemSetPlugin} {@code Object}.
     */
    public OpenItemSetPlugin() {
        super(CHISEL.getId());
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        // automatically configure all sets found within the enum
        Arrays.stream(GEItemSet.values()).forEach(set -> addHandler(set.getItemId(), ITEM_TYPE, this));
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final GEItemSet set = GEItemSet.forId(event.getUsedItem().getId());
        final int[] components = set.getComponents();
        Item itemSet = new Item(set.getItemId());

        if (!Perk.READY_SET_GO.enabled(player)) {
            player.getActionSender().sendMessage("You require the <col=255>" + Perk.READY_SET_GO.getFormattedName() + "</col> perk to do this.");
            return true;
        }

        if (player.getInventory().freeSlots() < components.length - 1) { // -1 because the set container will get removed
            player.getActionSender().sendMessage("You do not have enough space in your inventory.");
            return true;
        }

        if (!player.getInventory().remove(set.getItemId())) {
            player.getActionSender().sendMessage("An error occured, report to staff! item id: " + set.getItemId());
            return true;
        }

        Item[] items = Arrays.stream(components).mapToObj(Item::new).toArray(Item[]::new);
        player.getInventory().add(items);
        player.getActionSender().sendMessage("You unpack the " + itemSet.getName() + ".");

        return true;
    }

}


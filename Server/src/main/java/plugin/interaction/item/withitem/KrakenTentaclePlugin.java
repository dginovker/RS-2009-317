package plugin.interaction.item.withitem;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using a Kraken tentacle on an Abyssal whip.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KrakenTentaclePlugin extends UseWithHandler {

    /**
     * Represents the Abyssal whip item.
     */
    public static final Item ABYSSAL_WHIP = new Item(4151, 1);

    /**
     * Represents the Kraken tentacle item.
     */
    public static final Item KRAKEN_TENTACLE = new Item(12004, 1);

    /**
     * Constructs a new {@code KrakenTentaclePlugin} {@code Object}.
     */
    public KrakenTentaclePlugin() {
        super(KRAKEN_TENTACLE.getId());
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        player.getDialogueInterpreter().open("AbyssalTentacle", event.getBaseItem().getId() == 4151 ? event.getBaseItem() : event.getUsedItem(), event.getBaseItem().getId() == 4151 ? event.getUsedItem() : event.getBaseItem());
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(ABYSSAL_WHIP.getId(), ITEM_TYPE, this);
        return this;
    }

}

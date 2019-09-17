package plugin.interaction.item.withitem;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.interaction.UseWithHandler} for Spirit shields.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SpiritShieldPlugin extends UseWithHandler {

    /**
     * Constructs a new {@code SpiritShieldPlugin} {@code Object}.
     */
    public SpiritShieldPlugin() {
        super(Item.SPIRIT_SHIELD, Item.ELYSIAN_SIGIL, Item.SPECTRAL_SIGIL, Item.ARCANE_SIGIL);
    }

    @Override
    public boolean handle(NodeUsageEvent nodeUsageEvent) {
        final Player player = nodeUsageEvent.getPlayer();
        if (nodeUsageEvent.getUsedItem().getId() == Item.HOLY_ELIXIR && nodeUsageEvent.getBaseItem().getId() == Item.SPIRIT_SHIELD) {
            player.getActionSender().sendMessage("The shield must be blessed at an altar.");
            return true;
        }
        if (nodeUsageEvent.getBaseItem().getId() == Item.BLESSED_SPIRIT_SHIELD &&
            (nodeUsageEvent.getUsedItem().getId() == Item.ELYSIAN_SIGIL || nodeUsageEvent.getUsedItem().getId() == Item.SPECTRAL_SIGIL ||
                nodeUsageEvent.getUsedItem().getId() == Item.ARCANE_SIGIL)) {
            player.getActionSender().sendMessage("You can only attach that at an anvil.");
            return true;
        }
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(Item.BLESSED_SPIRIT_SHIELD, ITEM_TYPE, this);
        addHandler(Item.HOLY_ELIXIR, ITEM_TYPE, this);
        return this;
    }

}

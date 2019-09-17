package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the item plugin for the Ring of Wealth.
 *
 * @author <a href="http://Gielinor.org/">Gielinor</a>
 */
public class RingOfWealthPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(2572).getConfigurations().put("option:boss log", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getSavedData().getBossKillLog().sendInterface(player);
        return true;
    }

}
package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the buy crate option plugin for the seers village city.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BuyCrateOptionPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        Shops.PELTERS_VEG_STALL.open(player);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(6839).getConfigurations().put("option:buy", this);
        return this;
    }

}

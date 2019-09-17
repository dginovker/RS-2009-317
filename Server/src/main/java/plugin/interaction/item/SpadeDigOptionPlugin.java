package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.action.DigSpadeHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to handle the dig option on a spade.
 *
 * @author 'Vexia
 * @author Emperor
 */
public class SpadeDigOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(952).getConfigurations().put("option:dig", this);
        return null;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        return DigSpadeHandler.dig(player);
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}
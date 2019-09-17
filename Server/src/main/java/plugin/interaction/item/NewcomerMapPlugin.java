package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for the Newcomer map.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class NewcomerMapPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getConfigManager().force(106, ((player.getLocation().getX() / 64) - 46) + (((player.getLocation().getY() / 64) - 49) * 6), false);
        player.getInterfaceState().open(new Component(5392));
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(550).getConfigurations().put("option:read", this);
        return this;
    }

}
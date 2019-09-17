package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used for an experience lamp.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LampPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(2528).getConfigurations().put("option:rub", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getInterfaceState().open(new Component(2808));
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

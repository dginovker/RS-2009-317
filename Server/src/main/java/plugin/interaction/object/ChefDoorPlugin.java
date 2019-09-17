package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used for the chef door.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ChefDoorPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(3018).getConfigurations().put("option:open", this);
        return this;
    }

}

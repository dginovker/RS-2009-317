package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used for the museum gate plugin.
 * @author 'Vexia
 * @version 1.0
 */
public final class MuseumGatePlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(24536).getConfigurations().put("option:open", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.getLocation().getY() >= 3447) {
            player.getDialogueInterpreter().open(5941);
        } else {
            DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
            return true;
        }
        return true;
    }
}

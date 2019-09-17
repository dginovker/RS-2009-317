package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the guidor door plugin.
 * @author 'Vexia
 * @version 1.0
 */
public final class GuidorDoorPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().open(342, true, true);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2032).getConfigurations().put("option:open", this);
        return this;
    }

}

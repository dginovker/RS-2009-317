package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * @author 'Vexia
 */
public class SlayerDangerSignPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(5127).getConfigurations().put("option:read", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().sendPlaneMessage("<col=FFF0000>WARNING!", "</col>This area contains very dangerous creatures!", "Do not pass unless properly prepared!");
        return true;
    }

}

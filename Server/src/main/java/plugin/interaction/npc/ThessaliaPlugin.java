package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 *
 * @author 'Vexia.
 *
 */
public class ThessaliaPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().open(548, true, true, true);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(548).getConfigurations().put("option:change-clothes", this);
        return this;
    }

}

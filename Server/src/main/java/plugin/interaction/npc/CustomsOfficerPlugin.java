package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the customs officer plugin.
 * @author 'Vexia
 * @version 1.0
 */
public final class CustomsOfficerPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(380).getConfigurations().put("option:pay-fare", this);
        NPCDefinition.forId(381).getConfigurations().put("option:pay-fare", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().open(((NPC) node).getId(), ((NPC) node), true);
        return true;
    }

}

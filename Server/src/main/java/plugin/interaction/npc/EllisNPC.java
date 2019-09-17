package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

import plugin.interaction.inter.TanningInterface;

/**
 * Handles the Ellis NPC interaction.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class EllisNPC extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(2824).getConfigurations().put("option:tan-hides", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        TanningInterface.openInterface(player);
        return true;
    }

}

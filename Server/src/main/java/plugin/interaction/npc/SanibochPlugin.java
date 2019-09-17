package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles Saniboch's options.
 *
 * @author Emperor
 */
public final class SanibochPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(2345).getConfigurations().put("option:pay", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().open(2345, null, 35);
        return false;
    }

}
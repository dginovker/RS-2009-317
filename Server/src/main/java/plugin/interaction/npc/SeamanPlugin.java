package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents a plugin used to handle the pay fare option.
 * @author 'Vexia
 */
public class SeamanPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.setOptionHandler("pay-fare", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final NPC npc = ((NPC) node);
        player.getDialogueInterpreter().open(npc.getId(), npc, true);
        return true;
    }

}

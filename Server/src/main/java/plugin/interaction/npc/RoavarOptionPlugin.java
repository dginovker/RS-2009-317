package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

public class RoavarOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(6527).getConfigurations().put("option:trade", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().open(((NPC) node).getId(), ((NPC) node));
        return true;
    }

}
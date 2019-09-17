package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for Iron Men npcs Adam and Paul.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class IronMenPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(NPCDefinition.ADAM).getConfigurations().put("option:armour", this);
        NPCDefinition.forId(NPCDefinition.PAUL).getConfigurations().put("option:armour", this);
        NPCDefinition.forId(NPCDefinition.JUAN).getConfigurations().put("option:armour", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "armour":
                ((NPC) node).faceLocation(player.getLocation());
                player.getDialogueInterpreter().open(node.getId(), node, true);
                return true;
        }
        return false;
    }

}
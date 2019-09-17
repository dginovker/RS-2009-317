package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the runescape instructor plugin.
 * @author 'Vexia
 * @date 20.11.2013
 */
public class RunescapeInstructorPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(4707).getConfigurations().put("option:claim", this);
        NPCDefinition.forId(1861).getConfigurations().put("option:claim", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "claim":
                player.getDialogueInterpreter().open(node.getId(), node, true);
                break;
        }
        return true;
    }

}

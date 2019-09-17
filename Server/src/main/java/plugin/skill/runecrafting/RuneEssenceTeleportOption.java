package plugin.skill.runecrafting;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.global.travel.EssenceTeleport;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the option handler used to teleport into an essence mine.
 * @author 'Vexia
 * @version 1.0
 */
public class RuneEssenceTeleportOption extends OptionHandler {

    @Override
    public boolean handle(final Player player, Node node, String option) {
        EssenceTeleport.teleport(((NPC) node), player);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(553).getConfigurations().put("option:teleport", this);
        return this;
    }

}

package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.IdleAbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the disturb option.
 *
 * @author Emperor
 */
public final class NPCDisturbPlugin extends OptionHandler {

    public NPCDisturbPlugin() {
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.setOptionHandler("disturb", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (node instanceof IdleAbstractNPC) {
            IdleAbstractNPC npc = (IdleAbstractNPC) node;
            if (npc.canDisturb(player)) {
                npc.disturb(player);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isWalk(final Player player, final Node node) {
        if (node instanceof IdleAbstractNPC) {
            IdleAbstractNPC npc = (IdleAbstractNPC) node;
            return !npc.inDisturbingRange(player);
        }
        return false;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

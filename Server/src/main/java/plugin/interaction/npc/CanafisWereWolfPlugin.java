package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin used to handle the attacking of a werwolf.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CanafisWereWolfPlugin extends OptionHandler {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(6543);

    /**
     * Represents the id to transform into.
     */
    private static final int TRANSFORM_ID = 6006;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int i = 6026; i < 6046; i++) {
            NPCDefinition.forId(i).getConfigurations().put("option:attack", this);
        }
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (!player.getEquipment().contains(2952, 1)) {
            player.lock(2);
            final NPC n = (NPC) node;
            final NPC newN = NPC.create(TRANSFORM_ID, n.getLocation());
            newN.init();
            newN.animate(ANIMATION);
            n.clear();
            newN.lock(2);
            newN.setRespawn(false);
            newN.setAttribute("original", n.getId());
            newN.setAttribute("loc", n.getProperties().getSpawnLocation());
            World.submit(new Pulse(2) {

                @Override
                public boolean pulse() {
                    newN.getProperties().getCombatPulse().attack(player);
                    return true;
                }
            });
        } else {
            player.getPulseManager().clear("interaction:attack:" + node.hashCode());
            player.getProperties().getCombatPulse().attack(node);
        }
        return true;
    }

}

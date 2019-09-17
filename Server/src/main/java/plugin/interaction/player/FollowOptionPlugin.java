package plugin.interaction.player;

import org.gielinor.game.interaction.DestinationFlag;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.out.ClearMinimapFlag;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.impl.MovementPulse;

import plugin.activity.duelarena.DuelRule;

/**
 * Represents the plugin used to start following a node.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FollowOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Option._P_FOLLOW.setHandler(this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        final Player target = ((Player) node);
        if (DuelRule.NO_MOVEMENT.enforce(player, false)) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return true;
        }
        player.getPulseManager().run(new MovementPulse(player, target, DestinationFlag.FOLLOW_ENTITY) {

            @Override
            public boolean pulse() {
                player.face(target);
                return false;
            }

            @Override
            public void stop() {
                super.stop();
                mover.face(null);
            }
        }, "movement");
        return true;
    }
}

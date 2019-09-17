package plugin.interaction.object;


import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.Animator;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Point;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin for the Donator Zone Benches
 *
 * @author Corey
 */
public final class DonatorBenchPlugin extends OptionHandler {

    public static Animation SIT_ON_BENCH_ANIM = new Animation(9872);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(8772).getConfigurations().put("option:sit", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, final String option) {
        final Location sitLocation = node.getLocation();
        final Direction dir = node.getDirection();
        final Point dirPoint = Direction.getWalkPoint(dir);
        final Location faceLocation = new Location(dirPoint.getX(), dirPoint.getY());

        player.faceLocation(faceLocation);
        player.setTeleportTarget(sitLocation);
        player.playAnimation(SIT_ON_BENCH_ANIM);

        player.getPulseManager().run(new Pulse(6) {

            @Override
            public boolean pulse() {
                if (!player.getLocation().equals(sitLocation)) {
                    player.unlock();
                    return true;
                }
                player.animate(SIT_ON_BENCH_ANIM);
                return false;
            }

            @Override
            public void stop() {
                super.stop();
                player.animate(new Animation(-1, Animator.Priority.HIGH));
                player.unlock();
            }
        });

        return true;
    }

}

package plugin.skill.agility.shortcuts;

import org.gielinor.game.content.skill.member.agility.AgilityShortcut;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the basalt rocks south of the Lighthouse.
 *
 * @author Torchic
 */
public class LighthouseRocksShortcut extends AgilityShortcut {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(741);

    /**
     * The start and ending location.
     */
    private Location start;

    /**
     * The ending and start location.
     */
    private Location end;

    /**
     * Constructs a new {@Code LighthouseRocksShortcut} {@Code Object}
     *
     * @param ids        The object ids.
     * @param level      The level required.
     * @param experience The experience given.
     * @param start      The start location.
     * @param end        The end location.
     * @param options    The option string.
     */
    public LighthouseRocksShortcut(int[] ids, int level, double experience, Location start, Location end, String... options) {
        super(ids, level, experience, options);
        this.start = start;
        this.end = end;
    }

    /**
     * Constructs a new {@Code LighthouseRocksShortcut} {@Code Object}
     */
    public LighthouseRocksShortcut() {
        super(new int[]{}, 0, 0.0);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) {
        configure(new LighthouseRocksShortcut(new int[]{ 4559 }, 1, 0.0, Location.create(2514, 3619, 0), Location.create(2514, 3617, 0), "jump-to"));
        configure(new LighthouseRocksShortcut(new int[]{ 4558 }, 1, 0.0, Location.create(2514, 3617, 0), Location.create(2514, 3619, 0), "jump-across"));
        configure(new LighthouseRocksShortcut(new int[]{ 4557 }, 1, 0.0, Location.create(2514, 3615, 0), Location.create(2514, 3613, 0), "jump-across"));
        configure(new LighthouseRocksShortcut(new int[]{ 4556 }, 1, 0.0, Location.create(2514, 3613, 0), Location.create(2514, 3615, 0), "jump-across"));
        configure(new LighthouseRocksShortcut(new int[]{ 4555 }, 1, 0.0, Location.create(2516, 3611, 0), Location.create(2518, 3611, 0), "jump-across"));
        configure(new LighthouseRocksShortcut(new int[]{ 4554 }, 1, 0.0, Location.create(2518, 3611, 0), Location.create(2516, 3611, 0), "jump-across"));
        configure(new LighthouseRocksShortcut(new int[]{ 4553 }, 1, 0.0, Location.create(2522, 3602, 0), Location.create(2522, 3600, 0), "jump-across"));
        configure(new LighthouseRocksShortcut(new int[]{ 4552 }, 1, 0.0, Location.create(2522, 3600, 0), Location.create(2522, 3602, 0), "jump-across"));
        configure(new LighthouseRocksShortcut(new int[]{ 4551 }, 1, 0.0, Location.create(2522, 3597, 0), Location.create(2522, 3595, 0), "jump-across"));
        configure(new LighthouseRocksShortcut(new int[]{ 4550 }, 1, 0.0, Location.create(2522, 3595, 0), Location.create(2522, 3597, 0), "jump-to"));
        return this;
    }

    @Override
    public void run(final Player player, GameObject object, String option, boolean failed) {
        final Location destination = end;
        if (player.getLocation().getDistance(start) < player.getLocation().getDistance(end)) {
            //destination = end;
        }
        player.faceLocation(end);
        if (player.getLocation() != start) {
            ForceMovement.run(player, player.getLocation(), start);
        }
        World.submit(new Pulse(3) {

            @Override
            public boolean pulse() {
                ForceMovement.run(player, player.getLocation(), destination, ANIMATION, 20);
                this.stop();
                return false;
            }

        });
    }

}

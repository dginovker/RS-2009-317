package plugin.skill.agility.shortcuts;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin used to handle the tunnel shortcut in Yanille.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class YanilleTunnel extends OptionHandler {

    /**
     * Represents the level needed to pass.
     */
    private static final int LEVEL = 16;

    /**
     * The climbing down animation.
     */
    private static final Animation CLIMB_DOWN = Animation.create(2589);

    /**
     * The crawling through animation.
     */
    private static final Animation CRAWL_THROUGH = Animation.create(2590);

    /**
     * The climbing up animation.
     */
    private static final Animation CLIMB_UP = Animation.create(2591);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(9301).getConfigurations().put("option:climb-under", this);
        ObjectDefinition.forId(9302).getConfigurations().put("option:climb-into", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (player.getSkills().getLevel(Skills.AGILITY) < LEVEL) {
            player.getActionSender().sendMessage("You need an Agility level of 16 to negotiate this tunnel.");
            return true;
        }
        player.lock(5);
        final GameObject o = (GameObject) node;
        ForceMovement.run(player, o.getId() == 9302 ? Location.create(2575, 3112, 0) : Location.create(2575, 3107, 0), o.getLocation(), CLIMB_DOWN, 8);
        World.submit(new Pulse(1, player) {

            int count;

            @Override
            public boolean pulse() {
                switch (++count) {
                    case 2:
                        player.animate(CRAWL_THROUGH);
                        player.getProperties().setTeleportLocation(Location.create(2575, 3109, 0));
                        break;
                    case 3:
                        player.animate(CRAWL_THROUGH);
                        player.getProperties().setTeleportLocation(Location.create(2575, 3108, 0));
                        break;
                    case 5:
                        ForceMovement.run(player, o.getId() == 9302 ? Location.create(2575, 3110, 0) : Location.create(2575, 3110, 0), o.getId() == 9302 ? Location.create(2575, 3107, 0) : Location.create(2575, 3112, 0), CLIMB_UP, 19);
                        break;
                    case 6:
                        player.animate(ForceMovement.WALK_ANIMATION);
                        return true;
                }
                return false;
            }
        });
        return true;
    }

}

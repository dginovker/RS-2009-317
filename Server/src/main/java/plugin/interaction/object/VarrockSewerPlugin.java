package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin used to handle the varrock sewer.
 * @author 'Vexia
 * @version 1.0
 */
public final class VarrockSewerPlugin extends OptionHandler {

    /**
     * Represents the location cache data for the monkey bars.
     */
    private static final Location[][] MBAR_LOCATIONS = new Location[][]{ { new Location(3120, 9969, 0), Location.create(3121, 9969, 0) }, { new Location(3119, 9969, 0), Location.create(3120, 9969, 0) }, { new Location(3120, 9964, 0), Location.create(3121, 9964, 0) }, { Location.create(3120, 9963, 0), Location.create(3120, 9964, 0) } };

    /**
     * Represents the jump up animation
     */
    @SuppressWarnings("unused")
    private static final Animation JUMP_UP = new Animation(742);

    /**
     * Represents jump down animation.
     */
    @SuppressWarnings("unused")
    private static final Animation JUMP_DOWN = new Animation(743);

    /**
     * Represents the swing animation.
     */
    @SuppressWarnings("unused")
    private static final Animation SWINGING = new Animation(744);

    /**
     * Represents the climb out animation.
     */
    private static final Animation CLIMB_OUT = new Animation(748);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(9295).getConfigurations().put("option:squeeze-through", this);
        ObjectDefinition.forId(29375).getConfigurations().put("option:swing across", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        int id = ((GameObject) node).getId();
        switch (id) {
            case 29375:
                //TODO:
                break;
            case 9295:
                handlePipe(player);
                break;
        }
        return true;
    }

    /**
     * Method used to handle the sewer pipe.
     * @param player the player.
     */
    private void handlePipe(final Player player) {
        if (player.getSkills().getLevel(Skills.AGILITY) < 51) {
            player.getActionSender().sendMessage("You need a 51 agility to squeeze through the pipe.");
            return;
        }
        Location destination = player.getLocation().getX() == 3149 ? Location.create(3151, 9906, 0) : Location.create(3153, 9906, 0);
        final Location secondDestination = player.getLocation().getX() == 3149 ? Location.create(3155, 9906, 0) : Location.create(3149, 9906, 0);
        ForceMovement movement = new ForceMovement(player, player.getLocation(), destination, new Animation(749, Priority.HIGH));
        movement.run(player, 2);
        final Location destt = destination;
        World.submit(new Pulse(1, player) {

            int counter = 0;

            @Override
            public boolean pulse() {
                switch (counter++) {
                    case 4:
                        ForceMovement.run(player, destt, secondDestination, new Animation(8939));
                        break;
                    case 8:
                        player.animate(CLIMB_OUT);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof GameObject) {
            int id = ((GameObject) n).getId();
            if (id == 24428) {
                return Location.create(3258, 3451, 0);
            } else if (id == 29375) {
                for (Location[] locations : MBAR_LOCATIONS) {
                    if (n.getLocation().equals(locations[0])) {
                        return locations[1];
                    }
                }
            }
        }
        return null;
    }
}

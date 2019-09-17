package plugin.skill.agility.shortcuts;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles shortcuts in Taverley dungeon.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TaverleyDungeon extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(16509).getConfigurations().put("option:squeeze-through", this);
        return this;
    }

    /**
     * Gets the pipe transformation.
     *
     * @param player the player.
     * @return the transform.
     */
    private int getPipeTransform(final Player player) {
        return (player.getLocation().getX() > 2890 ? -6 : 6);
    }

    /**
     * Handles the squeezing through of a pipe.
     *
     * @param player the player.
     * @param object the object.
     */
    private void handlePipe(final Player player, final GameObject object) {
        if (player.getSkills().getStaticLevel(Skills.AGILITY) < 70) {
            player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 70 to enter.");
            return;
        }
        AgilityHandler.forceWalk(player, 0, player.getLocation(), player.getLocation().transform(getPipeTransform(player), 0, 0), Animation.create(746), 25, 0, null, 1);
        World.submit(new Pulse(2, player) {

            @Override
            public boolean pulse() {
                player.faceLocation(object.getLocation());
                return true;
            }
        });
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        switch (node.getId()) {
            case 16509:
                handlePipe(player, (GameObject) node);
                return true;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof GameObject) {
            switch (n.getId()) {
                case 16509:
                    return node.getLocation().getX() < n.getLocation().getX() ? Location.create(2886, 9799, 0) : Location.create(2892, 9799, 0);
            }
        }
        return null;
    }

}

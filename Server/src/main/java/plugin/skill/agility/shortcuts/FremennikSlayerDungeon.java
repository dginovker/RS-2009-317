package plugin.skill.agility.shortcuts;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles shortcuts in the Fremennik Slayer dungeon.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class FremennikSlayerDungeon extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(9326).getConfigurations().put("option:jump-over", this);
        ObjectDefinition.forId(9321).getConfigurations().put("option:squeeze-through", this);

        return this;
    }


    @Override
    public boolean handle(final Player player, Node node, String option) {
        Direction dir = null;
        switch (node.getId()) {
            case 9321:
                if (player.getSkills().getStaticLevel(Skills.AGILITY) < 62) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 62 to do that.");
                    return true;
                }
                dir = player.getLocation().getX() != 2735 ? Direction.EAST : Direction.WEST;
                player.getActionSender().sendMessage("You squeeze through the crevice.", 1);
                AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX() * 5, dir.getStepY() * 5, 0), Animation.create(2240), 0, null);
                return true;
            case 9326:
                if (player.getSkills().getLevel(Skills.AGILITY) < 81) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 81 in order to do this.");
                    return true;
                }
                World.submit(new Pulse(2, player) {

                    @Override
                    public boolean pulse() {
                        player.animate(new Animation(1603));
                        return true;
                    }
                });
                Location start = player.getLocation().getX() == 2775 ? player.getLocation().transform(1, 0, 0) : player.getLocation().transform(-1, 0, 0);
                Location destination = player.getLocation().getX() == 2775 ? player.getLocation().transform(-2, 0, 0) : player.getLocation().transform(2, 0, 0);
                if (node.getLocation().getY() == 10002) {
                    start = player.getLocation().getX() == 2770 ? player.getLocation().transform(1, 0, 0) : player.getLocation().transform(-1, 0, 0);
                    destination = player.getLocation().getX() == 2770 ? player.getLocation().transform(-2, 0, 0) : player.getLocation().transform(2, 0, 0);
                }
                ForceMovement.run(player, start, destination, new Animation(1995), 13);
                return true;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof GameObject) {
            switch (n.getId()) {
                case 9293:
                    return node.getLocation().getX() < n.getLocation().getX() ? Location.create(2886, 9799, 0) : Location.create(2892, 9799, 0);
            }
        }
        return null;
    }

}

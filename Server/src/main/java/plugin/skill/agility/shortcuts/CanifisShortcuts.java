package plugin.skill.agility.shortcuts;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the Canifis shortcuts.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CanifisShortcuts extends OptionHandler {

    @Override
    public boolean handle(final Player player, Node node, String option) {
        Direction dir = null;
        switch (node.getId()) {
            case 9334:
                if (player.getSkills().getStaticLevel(Skills.AGILITY) < 65) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 65 to do that.");
                    return true;
                }
                dir = player.getLocation().getX() == 3423 ? Direction.EAST : Direction.WEST;
                player.getActionSender().sendMessage("You squeeze through the railing.", 1);
                AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX(), dir.getStepY(), 0), Animation.create(2240), 0, null);
                return true;
            case 9335:
                if (player.getLocation().getX() == 3424) {
                    AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(2, 0, 0), Animation.create(740), 0, "You climb down the rocks.");
                    World.submit(new Pulse(2, player) {

                        @Override
                        public boolean pulse() {
                            player.getAnimator().reset();
                            return true;
                        }
                    });
                    return true;
                }
                if (player.getLocation().getX() == 3426) {
                    AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(-2, 0, 0), Animation.create(740), 0, "You climb up the rocks.");
                    World.submit(new Pulse(2, player) {

                        @Override
                        public boolean pulse() {
                            player.getAnimator().reset();
                            return true;
                        }
                    });
                    return true;
                }
                return true;
            case 9336:
                if (player.getLocation().getX() == 3426 && player.getLocation().getY() == 3476) {
                    AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(1, 1, 0), Animation.create(740), 0, "You climb down the rocks.");
                    World.submit(new Pulse(2, player) {

                        @Override
                        public boolean pulse() {
                            player.getAnimator().reset();
                            return true;
                        }
                    });
                    return true;
                }
                if (player.getLocation().getX() == 3427 && player.getLocation().getY() == 3477) {
                    AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(-1, -1, 0), Animation.create(740), 0, "You climb up the rocks.");
                    World.submit(new Pulse(2, player) {

                        @Override
                        public boolean pulse() {
                            player.getAnimator().reset();
                            return true;
                        }
                    });
                    return true;
                }
                return true;
            case 9337:
                if (player.getSkills().getStaticLevel(Skills.AGILITY) < 65) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 65 to do that.");
                    return true;
                }
                dir = player.getLocation().getY() == 3482 ? Direction.NORTH : Direction.SOUTH;
                player.getActionSender().sendMessage("You squeeze through the railing.", 1);
                AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX(), dir.getStepY(), 0), Animation.create(2240), 0, null);
                return true;
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(9334).getConfigurations().put("option:squeeze-through", this); // railing
        ObjectDefinition.forId(9335).getConfigurations().put("option:climb", this); // rocks
        ObjectDefinition.forId(9336).getConfigurations().put("option:climb", this); // rocks
        ObjectDefinition.forId(9337).getConfigurations().put("option:squeeze-through", this); // railing
        return this;
    }
}

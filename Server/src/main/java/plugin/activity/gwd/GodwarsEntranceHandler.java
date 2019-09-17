package plugin.activity.gwd;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the entrance hole to the godwars dungeon.
 *
 * @author Emperor
 */
public final class GodwarsEntranceHandler extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(26340).getConfigurations().put("option:tie-rope", this);
        ObjectDefinition.forId(26341).getConfigurations().put("option:climb-down", this);
        ObjectDefinition.forId(26338).getConfigurations().put("option:move", this);
        ObjectDefinition.forId(26305).getConfigurations().put("option:crawl-through", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        GameObject object = (GameObject) node;
        switch (object.getId()) {
            case 26340:
                if (!player.getInventory().remove(new Item(954))) {
                    player.getActionSender().sendMessage("You don't have a rope to tie around the pillar.");
                    return true;
                }
                int value = player.getConfigManager().get(1048) | 0x1;
                player.getConfigManager().set(1048, value, true);
                return true;
            case 26341:
                if (player.getSkills().getStaticLevel(Skills.AGILITY) < 15) {
                    player.getActionSender().sendMessage("You need an Agility level of 15 to enter this.");
                    return true;
                }
                if ((player.getConfigManager().get(1048) & 16) == 0) {
                    player.getDialogueInterpreter().sendDialogues(6201, FacialExpression.NO_EXPRESSION, "Cough... Hey, over here.");
                    return true;
                }
                player.lock(2);
                player.getActionSender().sendMessage("You climb down the rope.");
                player.animate(Animation.create(828));
                World.submit(new Pulse(1, player) {

                    @Override
                    public boolean pulse() {
                        player.getProperties().setTeleportLocation(Location.create(2882, 5311, 2));
                        return true;
                    }
                });
                return true;
            case 26338:
                if (player.getSkills().getStaticLevel(Skills.STRENGTH) < 60) {
                    player.getActionSender().sendMessage("You need a Strength level of 60 to move this boulder.");
                    return true;
                }
                player.getActionSender().sendObjectAnimation(object, Animation.create(6980));
                if (player.getLocation().getY() < 3716) {
                    ForceMovement.run(player, Location.create(2898, 3715, 0), Location.create(2898, 3719, 0), new Animation(6978), 3);
                } else {
                    ForceMovement.run(player, Location.create(2898, 3719, 0), Location.create(2898, 3715, 0), new Animation(6979), 3);
                }
                World.submit(new Pulse(12, player) {

                    @Override
                    public boolean pulse() {
                        player.getActionSender().sendObjectAnimation(RegionManager.getObject(0, 2898, 3716), Animation.create(6981));
                        return true;
                    }
                });
                return true;
            case 26305:
                if (player.getSkills().getStaticLevel(Skills.AGILITY) < 60) {
                    player.getActionSender().sendMessage("You need an Agility level of 60 to squeeze through the crack.");
                    return true;
                }
                if (object.getLocation().equals(Location.create(2900, 3713, 0))) {
                    player.getProperties().setTeleportLocation(Location.create(2904, 3720, 0));
                } else {
                    player.getProperties().setTeleportLocation(Location.create(2899, 3713, 0));
                }
                return true;
        }
        return false;
    }

}

package plugin.interaction.city;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the node option handler for lumbridge.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LumbridgeNodePlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(3500).getConfigurations().put("option:play", this);
        ObjectDefinition.forId(37335).getConfigurations().put("option:raise", this);
        ObjectDefinition.forId(36976).getConfigurations().put("option:ring", this);
        ObjectDefinition.forId(6898).getConfigurations().put("option:squeeze-through", this);
        ObjectDefinition.forId(6905).getConfigurations().put("option:squeeze-through", this);
        ObjectDefinition.forId(9299).getConfigurations().put("option:squeeze-through", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        int id = node.getId();
        switch (id) {
            case 3500:
                player.getActionSender().sendMessage("There are no notes. You can't play without notes!");
                //player.lock();
                //ActivityManager.start(player, "organ cutscene", false);
                return true;
            case 9299:
                AgilityHandler.walk(player, 0, player.getLocation(),
                    node.getLocation().transform(0, player.getLocation().getY() == 3190 ? 1 : -1, 0), new Animation(2240), 0, null);
                return true;
            case 6898:
            case 6905:
                AgilityHandler.walk(player, 0, player.getLocation(),
                    node.getLocation().transform(player.getLocation().getX() <= 3219 ? 3 : -2, 0, 0), new Animation(2240), 0, null);
                return true;
            case 37335:
                Animation.create(9977).getDefinition();
                player.getActionSender().sendObjectAnimation(((GameObject) node), new Animation(9977));
                break;
            case 36976:
                player.getActionSender().sendMessage("The towns people wouldn't appreciate you ringing their bell.");
                break;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof GameObject) {
            final GameObject object = (GameObject) n;
            if (object.getId() == 36976) {
                return Location.create(3243, 3205, 2);
            } else if (object.getId() == 37095) {
                return n.getLocation().transform(5, 0, 0);
            }
        }
        return null;
    }
}

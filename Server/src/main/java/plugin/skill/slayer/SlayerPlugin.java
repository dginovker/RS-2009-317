package plugin.skill.slayer;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SlayerPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(5008).getConfigurations().put("option:enter", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (node.getId()) {
            case 5008:
                player.setTeleportTarget(new Location(2807, 10002, 0));
                break;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n.getId() == 23158 || n.getId() == 23157) {
            return new Location(2690, 10124, 0);
        }
        if (n.getId() == 96) {
            return new Location(2641, 9763, 0);
        }
        return null;
    }

}

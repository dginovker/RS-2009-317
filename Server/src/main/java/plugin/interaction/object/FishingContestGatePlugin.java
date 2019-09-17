package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the fishing contest gate plugin.
 * @author 'Vexia
 * @version 1.0
 */
public final class FishingContestGatePlugin extends OptionHandler {

    /**
     * Represents the location to be near.
     */
    private static final Location LOCATION = Location.create(2643, 3441, 0);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(47).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(48).getConfigurations().put("option:open", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.getLocation().withinDistance(LOCATION, 50)) {
            player.getActionSender().sendMessage("You need a fishing contest pass to go through here.");
            return true;
        }
        return true;
    }

}

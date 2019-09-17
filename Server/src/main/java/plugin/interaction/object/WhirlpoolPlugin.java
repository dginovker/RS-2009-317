package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the ancient cavern whirlpool.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class WhirlpoolPlugin extends OptionHandler {

    /**
     * Handles the interaction option.
     *
     * @param player The player who used the option.
     * @param node   The node the player selected an option on.
     * @param option The option selected.
     * @return <code>True</code> if successful.
     */
    @Override
    public boolean handle(Player player, Node node, String option) {
        // TODO proper entrance
        player.setTeleportTarget(Location.create(1763, 5365, 1));
        return true;
    }

    /**
     * Creates a new instance.
     *
     * @param arg The argument.
     * @return The plugin instance created.
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(25274).getConfigurations().put("option:dive in", this);
        return this;
    }
}

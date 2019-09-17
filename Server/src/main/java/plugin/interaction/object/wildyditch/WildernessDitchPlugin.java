package plugin.interaction.object.wildyditch;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Represents the plugin to handle the crossing.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class WildernessDitchPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(23271).getConfigurations().put("option:cross", this);
        PluginManager.definePlugin(new WildernessInterfacePlugin());
        return this;
    }

    @Override
    public boolean handle(final Player player, final Node node, String option) {
        if (player.getLocation().getDistance(node.getLocation()) < 3) {
            handleDitch(player, node);
        } else {
            player.getPulseManager().run(new MovementPulse(player, node) {

                @Override
                public boolean pulse() {
                    handleDitch(player, node);
                    return true;
                }
            }, "movement");
        }
        return true;
    }

    /**
     * Handles the wilderness ditch jumping.
     *
     * @param player The player.
     * @param node   The ditch object.
     */
    public void handleDitch(final Player player, Node node) {
        player.faceLocation(node.getLocation());
        GameObject ditch = (GameObject) node;
        player.setAttribute("wildy_ditch", ditch);
        if (player.getInterfaceState().get(InterfaceConfiguration.WILDERNESS_WARNING) == 0) {
            if (ditch.getRotation() % 2 == 0) {
                if (player.getLocation().getY() <= node.getLocation().getY()) {
                    player.getInterfaceState().open(new Component(46900));
                    return;
                }
            } else {
                if (player.getLocation().getX() > node.getLocation().getX()) {
                    player.getInterfaceState().open(new Component(46900));
                    return;
                }
            }
        }
        WildernessInterfacePlugin.handleDitch(player);
    }

    @Override
    public boolean isWalk() {
        return true;
    }
}

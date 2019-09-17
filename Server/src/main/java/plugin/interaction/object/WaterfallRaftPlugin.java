package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the waterfall log raft boarding option.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class WaterfallRaftPlugin extends OptionHandler {

    /**
     * Handles the interaction option.
     *
     * @param player The player who used the option.
     * @param node   The node the player selected an option on.
     * @param option The option selected.
     * @return <code>True</code> if successful.
     */
    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (!player.getSavedData().getGlobalData().hasPaidWaterfall()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need to talk to Almera before boarding this raft.");
            return true;
        }
        player.getActionSender().sendMessage("You board the raft...");
        player.getInterfaceState().open(new Component(8677));
        player.getActionSender().sendMinimapState(2);
        World.submit(new Pulse(7, player) {

            @Override
            public boolean pulse() {
                player.getDialogueInterpreter().sendPlaneMessage("The raft breaks as you begin to go down the waterfall", "and you end up on a ledge.");
                player.setLocation(Location.create(2511, 3463, 0));
                player.getInterfaceState().close();
                player.getActionSender().sendMinimapState(0);
                return true;
            }
        });
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
        ObjectDefinition.forId(1987).getConfigurations().put("option:board", this);
        return this;
    }
}

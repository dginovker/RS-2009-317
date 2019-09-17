package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The {@link org.gielinor.game.interaction.OptionHandler} for the entrances for Skeletal Wyverns.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SkeletalWyvernTunnels extends OptionHandler {

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
        if (node.getLocation().getX() == 3055 && node.getLocation().getY() == 9560) {
            player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("SkeletalWyvernWarning"));
            return true;
        }
        if (node.getLocation().getX() == 3055 && node.getLocation().getY() == 9556) {
            player.faceLocation(Location.create(3056, 9563));
            player.getProperties().setTeleportLocation(Location.create(3056, 9562, 0));
            return true;
        }
        return false;
    }

    /**
     * Creates a new instance.
     *
     * @param arg The argument.
     * @return The plugin instance created.
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(10595).getConfigurations().put("option:enter", this);
        ObjectDefinition.forId(10596).getConfigurations().put("option:enter", this);
        return this;
    }
}

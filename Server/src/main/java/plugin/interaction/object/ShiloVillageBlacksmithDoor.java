package plugin.interaction.object;


import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the blacksmith door in Shilo village.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ShiloVillageBlacksmithDoor extends OptionHandler {

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
        if (!(node instanceof GameObject)) {
            return false;
        }
        GameObject gameObject = (GameObject) node;
        if (gameObject.getLocation().getX() != 2856 && gameObject.getLocation().getY() != 2963) {
            return false;
        }
        if (!player.getSavedData().getGlobalData().hasPaidBlacksmith() && player.getLocation().getY() == 2963) {
            player.getDialogueInterpreter().open(513, Repository.findNPC(513));
            return true;
        }
        player.getSavedData().getGlobalData().resetBlacksmithPaid();
        DoorActionHandler.handleAutowalkDoor(player, gameObject);
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
        ObjectDefinition.forId(2266).getConfigurations().put("option:open", this);
        return this;
    }
}

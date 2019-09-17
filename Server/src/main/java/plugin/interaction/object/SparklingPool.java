package plugin.interaction.object;


import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin for the Sparkling pool in the Mage Bank.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SparklingPool extends OptionHandler {

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
        return player.getDialogueInterpreter().open("SparklingPool", (GameObject) node);
    }

    /**
     * Creates a new instance.
     *
     * @param arg The argument.
     * @return The plugin instance created.
     */
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2878).getConfigurations().put("option:step-into", this);
        ObjectDefinition.forId(2879).getConfigurations().put("option:step-into", this);
        return this;
    }
}

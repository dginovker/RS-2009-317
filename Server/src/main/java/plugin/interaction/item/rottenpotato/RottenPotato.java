package plugin.interaction.item.rottenpotato;

import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles actions for the rotten potato item.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RottenPotato extends OptionHandler {

    /**
     * Represents the sliced potato options.
     */
    private static final String[] OPTIONS = new String[]{ "eat", "slice", "peel", "mash", "drop" };

    @Override
    public boolean handle(Player player, Node node, String option) {
        // player.getDialogueInterpreter().open("RottenPotatoDialoguePlugin", option);
        return false;//true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (String option : OPTIONS) {
            //ItemDefinition.forId(5733).getConfigurations().put("option:" + option, this);
        }
        return this;
    }
}

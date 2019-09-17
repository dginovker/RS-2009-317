package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;
import java.util.concurrent.TimeUnit;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for the Dark Portal to Bork.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DarkPortalPlugin extends OptionHandler {

    // TODO Figure out whether we want Bork - it's RS2 and doesn't exist in OSRS.

    private static final long COOLDOWN_IN_MILLISECONDS = TimeUnit.HOURS.toMillis(12);

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.getSkills().getStaticLevel(Skills.SLAYER) < 70) {
            player.getActionSender().sendMessage("You must have a Slayer level of at least 70 to use that.");
            return true;
        }
        // Bork can only be fought
        long timePassed = System.currentTimeMillis() - player.getSavedData().getActivityData().getLastBorkBattle();
        if (timePassed < COOLDOWN_IN_MILLISECONDS) {
            player.getActionSender().sendMessage("The portal's magic is too weak to teleport you right now.");
            return true;
        }
        player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("BorkDialoguePlugin"));
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) {
        ObjectDefinition.forId(12355).getConfigurations().put("option:enter", this);
        ObjectDefinition.forId(12355).getConfigurations().put("option:use", this);
        return this;
    }

}

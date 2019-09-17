package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

import plugin.activity.duelarena.DuelRule;
import plugin.activity.duelarena.DuelSession;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for the Duel Arena forfeit trapdoor.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ForfeitTrapdoorPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (DuelSession.getExtension(player) == null) {
            player.getActionSender().sendMessage("Nothing interesting happens.");
            return true;
        }
        if (DuelRule.NO_FORFEIT.enforce(player, true)) {
            return true;
        }
        DuelSession.decline(player);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(3203).getConfigurations().put("option:forfeit", this);
        return this;
    }
}

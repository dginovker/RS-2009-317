package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the witchs potion plugin.
 *
 * @author 'Vexia
 */
public final class WitchsPotionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2024).getConfigurations().put("option:drink from", this);
        ObjectDefinition.forId(2024).getConfigurations().put("option:Drink From", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getDialogueInterpreter().sendDialogues(player, null, "As nice as that looks I think I'll give it a miss for now.");
        return true;
    }

}

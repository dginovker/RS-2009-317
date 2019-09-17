package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to unlock the sheers cage.
 * @author 'Vexia
 * @versio 1.0
 */
public final class SeersCageUnlockPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getActionSender().sendMessage("You can't unlock the pillory, you'll let all the prisoners out!");
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(6836).getConfigurations().put("option:unlock", this);
        return this;
    }

}

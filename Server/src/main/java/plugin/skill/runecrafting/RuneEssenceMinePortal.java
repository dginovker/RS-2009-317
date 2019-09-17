package plugin.skill.runecrafting;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.travel.EssenceTeleport;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used for the rune essence mine.
 * @author 'Vexia
 * @version 1.0
 */
public final class RuneEssenceMinePortal extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2492).getConfigurations().put("option:use", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        EssenceTeleport.home(player);
        return true;
    }

}

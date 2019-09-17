package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

import plugin.dialogue.HonestJimmyDialogue;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} plugin for Honest Jimmy.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class HonestJimmyPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        HonestJimmyDialogue.openRewardScreen(player);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(4362).getConfigurations().put("option:trade-points", this);
        return this;
    }

}

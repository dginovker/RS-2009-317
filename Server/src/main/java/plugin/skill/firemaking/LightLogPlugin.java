package plugin.skill.firemaking;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.skill.free.firemaking.FiremakingPulse;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to light a log.
 * @author 'Vexia
 * @version 1.0
 */
public final class LightLogPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getPulseManager().run(new FiremakingPulse(player, ((Item) node), ((GroundItem) node)));
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("light", this);
        return this;
    }

}

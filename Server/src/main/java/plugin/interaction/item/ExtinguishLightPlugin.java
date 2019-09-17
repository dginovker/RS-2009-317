package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.LightSource;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.zone.impl.DarkZone;
import org.gielinor.rs2.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the extinguish light plugin.
 * @author 'Vexia
 * @version 1.0
 */
public final class ExtinguishLightPlugin extends OptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExtinguishLightPlugin.class);

    @Override
    public Plugin<Object> newInstance(Object arg) {
        ItemDefinition.setOptionHandler("extinguish", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final LightSource source = LightSource.forProductId(node.getId());
        if (source == null) {
            log.warn("Unable to find extinguish light source plugin for [{}].", node.getId());
            return true;
        }
        player.getInventory().remove(source.getProduct());
        player.getInventory().add(source.getRaw());
        DarkZone.checkDarkArea(player);
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }
}

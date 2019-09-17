package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.action.EquipHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using the "wield"/"wear" option.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class WieldItemPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("equip", this);
        ItemDefinition.setOptionHandler("wield", this);
        ItemDefinition.setOptionHandler("wear", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        return EquipHandler.equip(player, ((Item) node));
    }

    @Override
    public Location getDestination(Node node, Node item) {
        return null;
    }
}

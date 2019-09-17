package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the doogle leaf plugin for this object.
 * @author 'Vexia
 * @version 1.0
 */
public class DoogleLeafPlugin extends OptionHandler {

    /**
     * Represents the leaf item.
     */
    private static final Item LEAF = new Item(1573, 1);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(31155).getConfigurations().put("option:pick-leaf", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!player.getInventory().add(LEAF)) {
            player.getActionSender().sendMessage("You don't have have enough space in your inventory.");
        } else {
            player.getActionSender().sendMessage("You pick some doogle leaves.");
        }
        return true;
    }

}

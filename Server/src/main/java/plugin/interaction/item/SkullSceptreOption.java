package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.travel.Teleport;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the skull sceptre options.
 *
 * @author 'Vexia
 *
 */
public class SkullSceptreOption extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        Item item = (Item) node;
        switch (option) {
            case "invoke":
                if (player.getTeleporter().send(Location.create(3081, 3421, 0), Teleport.TeleportType.NORMAL, 1)) {
                    item.setCharge(item.getCharge() - 200);
                    if (item.getCharge() < 1) {
                        player.getInventory().remove(item);
                        player.getActionSender().sendMessage("Your staff crumbles to dust as you use its last charge.");
                    }
                }
                break;
            case "divine":
                if (item.getCharge() < 1) {
                    player.getActionSender().sendMessage("You don't have enough charges left.");
                    return true;
                }
                player.getActionSender().sendMessage("Concentrating deeply, you divine that the sceptre has " + (item.getCharge() / 200) + " charges left.");
                break;
        }
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(9013).getConfigurations().put("option:invoke", this);
        ItemDefinition.forId(9013).getConfigurations().put("option:divine", this);
        return this;
    }
}

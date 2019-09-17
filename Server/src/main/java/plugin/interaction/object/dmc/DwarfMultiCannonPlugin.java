package plugin.interaction.object.dmc;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the Dwarf multi-cannon.
 *
 * @author Emperor
 */
public final class DwarfMultiCannonPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) {
        ItemDefinition.forId(6).getConfigurations().put("option:set-up", this);
        ObjectDefinition.forId(6).getConfigurations().put("option:fire", this);
        ObjectDefinition.forId(6).getConfigurations().put("option:pick-up", this);
        ObjectDefinition.forId(7).getConfigurations().put("option:pick-up", this);
        ObjectDefinition.forId(8).getConfigurations().put("option:pick-up", this);
        ObjectDefinition.forId(9).getConfigurations().put("option:pick-up", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        // if (!player.getQuestRepository().isComplete(DwarfCannon.NAME) &&
        // player.getDetails().getRights() != Rights.ADMINISTRATOR) {
        // player.getActionSender().sendMessage("You have to complete the Dwarf
        // Cannon to know how to use this.");
        // return true;
        // }
        if (node instanceof Item) {
            if (!player.getInventory().containItems(6, 8, 10, 12)) {
                player.getActionSender().sendMessage("You don't have all the cannon components!");
                return true;
            }
            if (player.getLocation().getRegionId() == 11343) {
                player.getActionSender().sendMessage("You cannot place a cannon at home!");
                return true;
            }
            if (player.getAttribute("dmc") != null) {
                player.getActionSender().sendMessage("You cannot construct more than one cannon at a time.");
                player.getActionSender()
                    .sendMessage("If you have lost your cannon, go and see the dwarf cannon engineer.");
                return true;
            }
            DMCHandler.construct(player);
            return true;
        }
        switch (option) {
            case "fire":
                DMCHandler handler = player.getAttribute("dmc");
                if (handler != null && handler.getCannon() == node) {
                    handler.startFiring();
                    return true;
                }
                player.getActionSender().sendMessage("This is not your cannon.");
                return true;
            case "pick-up":
                handler = player.getAttribute("dmc");
                if (handler != null && handler.getCannon() == node) {
                    if (player.getInventory().freeSlots() < 4) {
                        player.getActionSender().sendMessage("Not enough space in inventory.");
                        return true;
                    }
                    player.getActionSender().sendMessage("You pick up the cannon.");
                    handler.clear(true);
                    return true;
                }
                if (player.getDetails().getRights() == Rights.GIELINOR_MODERATOR) {
                    ObjectBuilder.remove((GameObject) node);
                    player.removeAttribute("dmc");
                    for (int i = 3; i >= 0; i--) {
                        player.getInventory().add(new Item(6 + (i * 2)));
                    }
                    return true;
                }
                player.getActionSender().sendMessage("This is not your cannon.");
                return true;
        }
        return false;
    }

}

package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.plugin.Plugin;

public class TollGateOptionPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (option.equals("pay-toll(10gp)")) {
//			if (player.getQuestRepository().getQuest("Prince Ali Rescue").getStage() > 50) {
//				player.getActionSender().sendMessage("The guards let you through for free.");
//				DoorActionHandler.handleDoor(player, (GameObject) node);
//			} else {
            if (player.getInventory().contains(Item.COINS, 10)) {
                player.getInventory().remove(new Item(Item.COINS, 10));
                player.getActionSender().sendMessage("You quickly pay the 10 gold toll and go through the gates.");
                DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
                return true;
            } else {
                player.getActionSender().sendMessage("You need 10 gold to pass through the gates.");
            }
            //}
        } else {
            player.getDialogueInterpreter().open(925, Repository.findNPC(925), node);
            return true;
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2883).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(2883).getConfigurations().put("option:pay-toll(10gp)", this);
        ObjectDefinition.forId(2882).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(2882).getConfigurations().put("option:pay-toll(10gp)", this);
        return this;
    }

}

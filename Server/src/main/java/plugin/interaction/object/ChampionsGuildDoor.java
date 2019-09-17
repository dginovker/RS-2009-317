package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to handle the interaction with the champions guild door.
 * @author 'Vexia
 * @version 1.0
 */
public final class ChampionsGuildDoor extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
//		if (player.getLocation().getY() > 3362 &&  player.getQuestRepository().getPoints() < 32) {
//			player.getDialogueInterpreter().open(70099, "You have not proved yourself worthy to enter here yet.");
//			player.getActionSender().sendMessage("The door won't open - you need at least 32 Quest Points.");
//		} else {
        if (player.getLocation().getX() == 3191 && player.getLocation().getY() == 3363) {
            player.getDialogueInterpreter().sendDialogues(198, null, "Greetings bold adventurer. Welcome to the guild of", "Champions.");
        }
        DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
        return true;
        //}
        //return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(1805).getConfigurations().put("option:open", this);
        return this;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        return DoorActionHandler.getDestination(((Player) node), ((GameObject) n));
    }
}

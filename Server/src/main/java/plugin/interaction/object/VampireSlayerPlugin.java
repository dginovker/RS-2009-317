package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin to handle vampire slayer node handling.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class VampireSlayerPlugin extends OptionHandler {

    /**
     * Represents the basement location.
     */
    private static final Location BASEMENT = Location.create(3077, 9770, 0);

    /**
     * Represents the ground floor location.
     */
    private static final Location GROUND_FLOOR = Location.create(3115, 3356, 0);

    /**
     * Represents the stake item
     */
    private static final Item STAKE = new Item(1549);

    /**
     * Represents the hammer item.
     */
    private static final Item HAMMER = new Item(Item.HAMMER);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(33502).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(2614).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(32835).getConfigurations().put("option:walk-down", this);
        ObjectDefinition.forId(32836).getConfigurations().put("option:walk-up", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "open":
                int id = ((GameObject) node).getId();
                switch (id) {
                    case 2614:
                        player.getDialogueInterpreter().sendPlaneMessage("There's only a pillow in here..");
                        break;
                    case 33502:
                        player.getActionSender().sendMessage("The cupboard contains garlic. You take a clove.");
                        if (!player.getInventory().add(new Item(1550))) {
                            player.getActionSender().sendMessage("Not enough inventory space.");
                        }
                        break;
                }
                break;
            case "walk-down":
                player.getProperties().setTeleportLocation(BASEMENT);
                player.getActionSender().sendMessage("You walk down the stairs...");
                break;
            case "walk-up":
                player.getProperties().setTeleportLocation(GROUND_FLOOR);
                break;
        }
        return true;
    }
}

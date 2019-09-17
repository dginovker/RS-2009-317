package plugin.interaction.object;


import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to handle picking from a red berry bush.
 * @author 'Vexia
 *
 */
public class RedberryBushPlugin extends OptionHandler {

    /**
     * Represents the red berries item.
     */
    private final Item RED_BERRIES = new Item(1951);

    /**
     * Represents the picking berries animation.
     */
    private final Animation ANIMATION = new Animation(2282);

    /**
     * Represents the counter.
     */
    private int counter = 0;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(23628).getConfigurations().put("option:pick-from", this);
        ObjectDefinition.forId(23629).getConfigurations().put("option:pick-from", this);
        ObjectDefinition.forId(23630).getConfigurations().put("option:pick-from", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (((GameObject) node).getId() == 23630) {
            player.getActionSender().sendMessage("There are no berries left on this bush.");
            player.getActionSender().sendMessage("More berries will grow soon.");
            return true;
        }
        if (!player.getInventory().add(RED_BERRIES)) {
            player.getActionSender().sendMessage("Your inventory is too full to pick the berries from the bush.");
            return true;
        }
        player.lock(4);
        player.animate(ANIMATION);
        if (counter == 2) {
            ObjectBuilder.replace(((GameObject) node), new GameObject(23630, node.getLocation()), 30);
            counter = 0;
            return true;
        }
        counter++;
        return true;
    }

}

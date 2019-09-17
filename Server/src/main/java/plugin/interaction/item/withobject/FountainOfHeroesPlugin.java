package plugin.interaction.item.withobject;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin for the Fountain of Heroes.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class FountainOfHeroesPlugin extends UseWithHandler {

    /**
     * The animation.
     */
    private static final Animation ANIMATION = Animation.create(832);

    public FountainOfHeroesPlugin() {
        super(1710, 1708, 1706, 1704, 10356, 10358, 10360, 10362);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(2638, OBJECT_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        Player player = event.getPlayer();
        if (!player.getInventory().contains(event.getUsedItem())) {
            return true;
        }
        player.getInventory().remove(event.getUsedItem());
        player.playAnimation(ANIMATION);
        player.getInventory().add((event.getUsedItem().getId() >= 1704 && event.getUsedItem().getId() <= 1710) ? new Item(1712, 1) : new Item(10354, 1));
        return true;
    }
}

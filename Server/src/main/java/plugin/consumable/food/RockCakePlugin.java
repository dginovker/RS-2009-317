package plugin.consumable.food;

import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Food;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles eating a Rock cake.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 * TODO 317 - "Eat" and "Guzzle" option - see https://www.youtube.com/watch?v=OFhy0xZ7URE
 * Use 7510
 */
public class RockCakePlugin extends Food {

    /**
     * Constructs a new {@code RockCakePlugin} {@code Object}.
     */
    public RockCakePlugin() {
        super(7509, 0);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Consumables.add(new RockCakePlugin());
        return this;
    }

    @Override
    public void consume(final Item item, final Player player) {
        player.sendChat("Ow! I nearly broke a tooth!");
        player.getImpactHandler().manualHit(player, 1, ImpactHandler.HitsplatType.NORMAL);
        player.getActionSender().sendMessage("You eat the rock cake.");
    }
}

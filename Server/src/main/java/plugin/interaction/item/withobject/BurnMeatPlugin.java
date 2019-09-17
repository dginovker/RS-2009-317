package plugin.interaction.item.withobject;

import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Food;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to burn a fire.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BurnMeatPlugin extends UseWithHandler {

    /**
     * Represents the fire ids.
     */
    private static final int[] FIRES = new int[]{
        3769, 3775, 4265, 4266, 5249, 5499, 5981, 9735,
        10433, 10660, 12796, 13337, 13881, 14169, 15156,
        20000, 20001, 21620, 23046, 25155, 25156, 25465,
        26185, 26186, 26575, 26576, 26577, 26578
    };

    /**
     * Constructs a new {@code BurnMeatPlugin} {@code Object}.
     */
    public BurnMeatPlugin() {
        super(2140, 1861, 3228, 7521, 3151, 325, 319, 347, 355, 333, 339, 351, 329, 3381, 361, 10136, 5003, 379, 365, 373, 2149, 7946, 385, 397, 391, 3369, 3371, 3373, 1893, 1895, 1897, 1899, 1901, 1963, 2102, 2120, 2108, 5972, 5504, 1982, 1965, 1957, 5988, 1781, 6701, 6703, 6705, 7056, 7058, 7060, 1933, 1783, 1927, 1929, 6032, 6034, 2011, 227, 1921, 1937, 7934, 7086, 4239, 7068, 7086, 2003, 7078, 7072, 4239, 7062, 7064, 7084, 1871, 7082, 2309, 1891, 1967, 1971, 2142, 9436, 2142, 2142, 2325, 2333, 2327, 2331, 7170, 2323, 2335, 7178, 7180, 7188, 7190, 7198, 7200, 7208, 7210, 7218, 7220, 2289, 2291, 2293, 2295, 2297, 2299, 2301, 2303, 315, 9988, 2878, 7568, 9980, 7223, 5982, 5984);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int id : FIRES) {
            addHandler(id, OBJECT_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Consumables.findFood(event.getUsedItem()).ifPresent(food -> {
            if (player.getInventory().remove(event.getUsedItem())) {
                player.lock(3);
                player.animate(Animation.create(897));
                player.getInventory().add(food.getBurnt());
                player.getActionSender().sendMessage("You deliberately burn the perfectly good piece of meat.");
            }
        });

        return true;
    }

}

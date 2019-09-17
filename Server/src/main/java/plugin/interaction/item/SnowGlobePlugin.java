package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * The plugin for a snow globe.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SnowGlobePlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(11949).getConfigurations().put("option:shake", this);
        return null;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (!(node instanceof Item)) {
            return false;
        }
        if (player.getAttribute("SNOW_GLOBE_SHAKE") != null) {
            return true;
        }
        player.setAttribute("SNOW_GLOBE_SHAKE", 1);
        player.lock(8);
        player.visualize(Animation.create(7528), Graphics.create(1284));
        player.getInventory().add(new Item(11951, player.getInventory().freeSlots()));
        World.submit(new Pulse(10, player) {

            @Override
            public boolean pulse() {
                player.removeAttribute("SNOW_GLOBE_SHAKE");
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

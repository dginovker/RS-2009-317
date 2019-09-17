package plugin.interaction.object;

import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin for using a silver bar on a furnace.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SilverCastingInterfacePlugin extends UseWithHandler {

    public SilverCastingInterfacePlugin() {
        super(2355);
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        //7637 = rod
        event.getPlayer().getInterfaceState().open(new Component(13782));
        player.getActionSender().sendUpdateItem(new Item(1718), 15445, 0);
        player.getActionSender().sendUpdateItem(new Item(2961), 15459, 0);
        player.getActionSender().sendUpdateItem(new Item(5525), 15473, 0);
        player.getActionSender().sendUpdateItem(new Item(1724), 15481, 0);
        player.getActionSender().sendString(15482, "Make\\nZamorak\\nSymbol");
        player.getActionSender().sendString(15467, "");
        player.getActionSender().sendString(15453, "");
        player.getActionSender().sendString(15485, "");
        player.getActionSender().sendString(18521, "");
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(2966, OBJECT_TYPE, this);
        addHandler(3044, OBJECT_TYPE, this);
        addHandler(3294, OBJECT_TYPE, this);
        addHandler(4304, OBJECT_TYPE, this);
        addHandler(6189, OBJECT_TYPE, this);
        addHandler(11009, OBJECT_TYPE, this);
        addHandler(11010, OBJECT_TYPE, this);
        addHandler(11666, OBJECT_TYPE, this);
        addHandler(12100, OBJECT_TYPE, this);
        addHandler(12809, OBJECT_TYPE, this);
        addHandler(18497, OBJECT_TYPE, this);
        addHandler(18525, OBJECT_TYPE, this);
        addHandler(18526, OBJECT_TYPE, this);
        addHandler(21879, OBJECT_TYPE, this);
        addHandler(22721, OBJECT_TYPE, this);
        addHandler(26814, OBJECT_TYPE, this);
        addHandler(28433, OBJECT_TYPE, this);
        addHandler(28434, OBJECT_TYPE, this);
        addHandler(30021, OBJECT_TYPE, this);
        addHandler(30510, OBJECT_TYPE, this);
        addHandler(36956, OBJECT_TYPE, this);
        addHandler(37651, OBJECT_TYPE, this);
        return null;
    }

}

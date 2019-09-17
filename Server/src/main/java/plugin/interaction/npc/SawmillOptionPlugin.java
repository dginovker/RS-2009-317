package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin to handle the options for saw mill man.
 * @author 'Vexia
 * @date Oct 8, 2013
 */
public class SawmillOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(4250).getConfigurations().put("option:buy-plank", this);
        NPCDefinition.forId(4250).getConfigurations().put("option:talk-to", this);
        NPCDefinition.forId(4250).getConfigurations().put("option:trade", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "buy-plank":
                player.getInterfaceState().open(new Component(403));
                return true;
            case "talk-to":
                player.getDialogueInterpreter().open(4250, node);
                return true;
            case "trade":
                Shops.forId(4250).getShop().open(player);
                return true;
        }
        return true;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        return Location.create(3302, 3491, 0);
    }
}

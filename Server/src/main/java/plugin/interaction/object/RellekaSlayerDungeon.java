package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

public class RellekaSlayerDungeon extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2123).getConfigurations().put("option:enter", this);
        ObjectDefinition.forId(2141).getConfigurations().put("option:enter", this);
        return this;
    }


    @Override
    public boolean handle(final Player player, Node node, String option) {
        switch (node.getId()) {
            case 2123:
                player.teleport(new Location(2808, 10002));
                break;
            case 2141:
                player.teleport(new Location(2796, 3615));
                break;
        }
        return true;
    }

}
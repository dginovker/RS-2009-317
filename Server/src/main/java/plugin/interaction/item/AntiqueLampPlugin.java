package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for Antique lamps.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AntiqueLampPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(11137).getConfigurations().put("option:rub", this);
        ItemDefinition.forId(11139).getConfigurations().put("option:rub", this);
        ItemDefinition.forId(11141).getConfigurations().put("option:rub", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.setAttribute("ANTIQUE_LAMP_ID", node.getId());
        player.setAttribute("ANTIQUE_LAMP_LEVEL", (node.getId() == 11137 ? 30 :
            node.getId() == 11139 ? 40 : node.getId() == 11141 ? 50 : 5));
        player.setAttribute("ANTIQUE_LAMP_EXP", (node.getId() == 11137 ? 30000 :
            node.getId() == 11139 ? 60000 : node.getId() == 11141 ? 120000 : -1));
        player.getInterfaceState().open(new Component(2808));
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

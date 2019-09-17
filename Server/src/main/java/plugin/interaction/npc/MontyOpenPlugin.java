package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the "Open" {@link org.gielinor.game.interaction.OptionHandler} for Monty the Collector.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class MontyOpenPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(3092).getConfigurations().put("option:open", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "open":
                player.getInterfaceState().open(new Component(23035));
                return true;
        }
        return false;
    }

    @Override
    public boolean isWalk() {
        return true;
    }

}

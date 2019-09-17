package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the Check / Dissolve option on an Abyssal tentacle.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AbyssalTentaclePlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(12006).getConfigurations().put("option:check", this);
        ItemDefinition.forId(12006).getConfigurations().put("option:operate", this);
        ItemDefinition.forId(12006).getConfigurations().put("option:dissolve", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "check":
            case "operate":
                int charge = ((Item) node).getCharge();
                player.getActionSender().sendMessage("Your abyssal tentacle can perform " + charge + " more attack" + (charge == 1 ? "" : "s") + ".");
                return true;
            case "dissolve":
                player.getDialogueInterpreter().open("AbyssalTentacle", (Item) node);
                return true;
        }
        return false;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

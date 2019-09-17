package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles claiming experience from an XP Token.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class XPTokenPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int id = 10942; id < 10945; id++) {
            ItemDefinition.forId(id).getConfigurations().put("option:claim", this);
            ItemDefinition.forId(id).getConfigurations().put("option:check", this);
        }
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        Item token = (Item) node;
        if (!player.getInventory().contains(token)) {
            return false;
        }
        if (!token.isCharged()) {
            player.getActionSender().sendMessage("Your XP token does not have any experience left.");
            return true;
        }
        if (option.equals("check")) {
            int exp = token.getCharge() * 1000;
            player.getActionSender().sendMessage("Your XP token has " + TextUtils.getFormattedNumber(exp) + " experience left.");
            return true;
        }
        return player.getDialogueInterpreter().open("XPTokenDialogue", token);
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

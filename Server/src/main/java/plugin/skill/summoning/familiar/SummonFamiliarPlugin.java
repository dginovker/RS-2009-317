package plugin.skill.summoning.familiar;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles summoning a familiar.
 *
 * @author Emperor
 */
public final class SummonFamiliarPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("summon", this);
        return null;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        Item item = (Item) node;
//		if (item.getId() == 12047 && player.getAttribute("in-cutscene", null) != null) {
//			player.getActionSender().sendInterface(0, player.getInterfaceManager().isResizable() ? 746 : 548, player.getInterfaceManager().isResizable() ? 76 : 80, 662);
//		}
//		if (!player.getQuestRepository().isComplete("Wolf Whistle") && player.getAttribute("in-cutscene", null) == null) {
//			player.getActionSender().sendMessage("You have to complete Wolf Whistle before you can summon a familiar.");
//			return true;
//		}
        player.getFamiliarManager().summon(item, false, true); // TODO Animate player?
        return true;
    }

}

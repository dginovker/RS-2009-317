package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the "Polish" option on buttons.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PolishButtonsPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(688).getConfigurations().put("option:polish", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        player.lock(1);
        if (player.getSkills().getLevel(Skills.CRAFTING) < 3) {
            player.getActionSender().sendMessage("You need a Crafting level of at least 3 to do that.");
            return true;
        }
        if (player.getInventory().remove((Item) node)) {
            player.getInventory().add(new Item(10496));
            player.getSkills().addExperience(Skills.CRAFTING, 5);
            player.getActionSender().sendMessage("You rub the buttons on your clothes and they become more shiny.");
            return true;
        }
        return false;
    }

}

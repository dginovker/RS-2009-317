package plugin.skill.herblore;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.herblore.Herbs;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the cleaning of a dirty herb.
 *
 * @author Vexia
 * @version 1.0
 */
public final class HerbCleanPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("clean", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final Herbs herb = Herbs.forItem((Item) node);
        if (player.getSkills().getLevel(Skills.HERBLORE) < herb.getLevel()) {
            player.getActionSender().sendMessage("You need a herblore level " + herb.getLevel() + " to clean this herb.");
            return true;
        }
        if (player.getInventory().replace(herb.getProduct(), ((Item) node).getSlot()) != null) {
            player.getSkills().addExperience(Skills.HERBLORE, herb.getExperience());
            player.getActionSender().sendMessage("You clean the dirt from the " + herb.getProduct().getName().toLowerCase().replace("clean", "").trim() + " leaf.", 1);
        }
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

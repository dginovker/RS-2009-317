package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.action.EquipHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin for wearing the Quest point and Achievement diary items.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class DiaryCapePlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(9813).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(9814).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.ACHIEVEMENT_DIARY_CAPE_T).getConfigurations().put("option:wear", this);
        ItemDefinition.forId(Item.ACHIEVEMENT_DIARY_HOOD).getConfigurations().put("option:wear", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.getRights().isAdministrator()) {
            return EquipHandler.equip(player, ((Item) node));
        }
        if (node.getId() == 9813 || node.getId() == 9814) {
            if (!player.getQuestRepository().hasCompletedAll()) {
                player.getActionSender().sendMessage("You must have completed all achievement diaries to wear this.");
                return true;
            }
        }
        if (node.getId() == Item.ACHIEVEMENT_DIARY_CAPE_T || node.getId() == Item.ACHIEVEMENT_DIARY_HOOD) {
            if (!(player.getQuestRepository().hasCompletedAll() &&
                player.getAchievementRepository().hasCompletedAll())) {
                player.getActionSender().sendMessage("You must have completed all quest and achievement diaries to wear this.");
                return true;
            }
        }
        return EquipHandler.equip(player, ((Item) node));
    }

    @Override
    public boolean isWalk() {
        return false;
    }
}

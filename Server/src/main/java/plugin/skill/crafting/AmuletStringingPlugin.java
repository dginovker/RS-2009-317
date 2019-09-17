package plugin.skill.crafting;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.crafting.jewellery.JewelleryCrafting;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to string an amulet.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class AmuletStringingPlugin extends UseWithHandler {

    /**
     * Constructs a new {@code AmuletStringingPlugin} {@code Object}.
     */
    public AmuletStringingPlugin() {
        super(1673, 1675, 1677, 1679, 1681, 1683, 6579);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(1759, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final JewelleryCrafting.JewelleryItem data = JewelleryCrafting.JewelleryItem.forProduct(event.getUsedItem().getId() == 6579 ? 6579 : ((Item) event.getUsedWith()).getId());
        if (data == null) {
            return true;
        }
        if (player.getSkills().getLevel(Skills.CRAFTING) < data.getLevel()) {
            player.getActionSender().sendMessage("You need a Crafting level of at least " + data.getLevel() + " to do that.");
            return true;
        }
        if (player.getInventory().remove(event.getUsedItem(), event.getBaseItem())) {
            if (data == JewelleryCrafting.JewelleryItem.DIAMOND_AMULET) {
                if ((player.getLocation().getRegionId() == 12850 || player.getLocation().getRegionId() == 12851) &&
                    player.getAchievementRepository().isCount(AchievementTask.CRAFT_AMULET_POWER, 1)) {
                    AchievementDiary.decrease(player, AchievementTask.CRAFT_AMULET_POWER, 1);
                }
            }
            player.getInventory().add(new Item(data == JewelleryCrafting.JewelleryItem.ONYX_AMULET ? 26581 : data.getSendItem() + 19));
        }
        return true;
    }

}

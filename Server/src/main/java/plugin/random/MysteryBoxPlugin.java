package plugin.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueLevel;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the mystery box item.
 *
 * @author Vexia
 */
public final class MysteryBoxPlugin extends OptionHandler {

    /**
     * Gets a reward item.
     *
     * @return the item.
     */
    public static Item getReward(List<ChanceItem> items) {
        Collections.shuffle(items);
        return RandomUtil.getChanceItem(items.toArray(new ChanceItem[items.size()])).getRandomItem();
    }

    /**
     * Gets the rewards.
     *
     * @return the rewards.
     */
    public static List<Item> getLoot(Player player) {
        List<ChanceItem> items = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        items.addAll(Arrays.asList(ClueLevel.MEDIUM.getRewards()));
        List<Item> rewards = new ArrayList<>();
        int size = RandomUtil.random(1, 4);
        Item item = null;
        for (int i = 0; i < size; i++) {
            item = getReward(items);
            if (ids.contains(item.getId())) {
                continue;
            }
            ids.add(item.getId());
            rewards.add(item);
        }
        if ((RandomUtil.getRandom(2) == 1 && RandomUtil.random(5000) == 11) || player.specialDetails()) {
            rewards.remove(0);
            rewards.add(RandomUtil.getChanceItem(ClueLevel.SUPER_RARE).getRandomItem());
        }
        return rewards;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(6199).getConfigurations().put("option:open", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (player.getInventory().freeSlots() < 4) {
            player.getActionSender().sendMessage("You need at least 3 free inventory spaces to open that!");
            return true;
        }
        final List<Item> rewards = getLoot(player);
        final Item box = (Item) node;
        player.getActionSender().sendMessage("You open the mystery box...");
        if (player.getInventory().remove(box, box.getSlot(), true)) {
            player.lock(1);
            for (Item item : rewards) {
                player.getInventory().add(item, player);
                if (ClueLevel.isSuperRare(item)) {
                    player.sendGlobalNewsMessage("ff8c38", " was rewarded " + item.getName() + " from a mystery box!", 5);
                }
            }
            player.getActionSender().sendMessage("Inside you find " + (rewards.size() == 1 ? "a" : "some") + " prize" + (rewards.size() == 1 ? "" : "s") + "!");
        }
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

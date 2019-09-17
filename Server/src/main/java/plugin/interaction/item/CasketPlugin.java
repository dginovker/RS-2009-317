package plugin.interaction.item;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the casket handling plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CasketPlugin extends OptionHandler {

    /**
     * Represents the casket rewards.
     */
    private static final ChanceItem[] CASKET_REWARD = new ChanceItem[]{
        new ChanceItem(Item.COINS, 8, 3000, 30),
        new ChanceItem(1623, 1, 30),
        new ChanceItem(1621, 1, 70),
        new ChanceItem(1619, 1, 70),
        new ChanceItem(1617, 1, 97),
        new ChanceItem(987, 1, 97),
        new ChanceItem(985, 1, 97),
        new ChanceItem(1454, 1, 30),
        new ChanceItem(1452, 1, 70),
        new ChanceItem(1462, 1, 97)
    };

    /**
     * Represents the slayer casket rewards.
     */
    private static final ChanceItem[] SLAYER_CASKET_REWARD = new ChanceItem[]{
        new ChanceItem(Item.COINS, 5000, 10000, 30),
        new ChanceItem(565, 100, 1250, 30),
        new ChanceItem(563, 100, 1250, 30),
        new ChanceItem(555, 100, 1500, 30),
        new ChanceItem(560, 100, 1250, 30),
        new ChanceItem(145, 4, 11, 30), // Super Attack (3)
        new ChanceItem(157, 4, 11, 30), // Super Strength (3)
        new ChanceItem(163, 4, 11, 30), // Super Defense (3)
        new ChanceItem(139, 4, 9, 30), // Prayer potion (3)
        new ChanceItem(1215, 1, 70), // Dragon dagger
        new ChanceItem(1305, 1, 70), // Dragon longsword
        new ChanceItem(1163, 1, 70), // Rune full helm
        new ChanceItem(1113, 1, 70), // Rune chainbody
        new ChanceItem(2503, 1, 70), // Black d'hide body
        new ChanceItem(532, 5, 30, 70), // Big Bones
        new ChanceItem(985, 1, 82), // Loop half
        new ChanceItem(987, 1, 82), // Key half
        new ChanceItem(1631, 2, 82), // Uncut dragonstone
        new ChanceItem(1645, 1, 86), // Dragonstone ring
        new ChanceItem(1703, 1, 82), // Dragonstone amulet
        new ChanceItem(6571, 1, 94), // Uncut onyx
    };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(405).getConfigurations().put("option:open", this); // Normal
        ItemDefinition.forId(2734).getConfigurations().put("option:open", this); // Slayer
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        boolean slayerCasket = node.getId() == 2734;
        final ChanceItem reward = slayerCasket ? getChanceItem(SLAYER_CASKET_REWARD) : getChanceItem(CASKET_REWARD);
        final ChanceItem reward2 = getChanceItem(SLAYER_CASKET_REWARD);
        player.getInventory().remove((Item) node);
        String amount = reward.getCount() > 1 ? "some" : (TextUtils.isPlusN(reward.getName()) ? "an" : "a");
        String amount2 = reward2.getCount() > 1 ? "some" : (TextUtils.isPlusN(reward2.getName()) ? "an" : "a");
        String message = "You open the casket. Inside you find " + amount + " " + determineName(reward.getName(), reward) + (slayerCasket ? (" and " + amount2 + " " + determineName(reward2.getName(), reward2) + "!") : "!");
        if (slayerCasket) {
            player.getDialogueInterpreter().sendDoubleItemMessage(reward.getId(), reward2.getId(), "\\n\\n" + message);
        } else {
            player.getDialogueInterpreter().sendItemMessage(reward, message);
        }
        if (!player.getInventory().add(determineReward(reward))) {
            GroundItemManager.create(determineReward(reward), player);
        }
        if (!player.getInventory().add(determineReward(reward2)) && slayerCasket) {
            GroundItemManager.create(determineReward(reward2), player);
        }
        return true;
    }

    /**
     * Determines the item to give the player.
     *
     * @param reward The reward.
     * @return
     */
    private Item determineReward(ChanceItem reward) {
        int rewardId = determineNotedId(reward) ? reward.getId() + 1 : reward.getId();
        return reward.getCount() == 1 ? new Item(rewardId) : new Item(rewardId, RandomUtil.random(reward.getMinimumAmount(), reward.getMaximumAmount()));
    }

    /**
     * Determines if the item should be noted.
     *
     * @param reward The reward item.
     * @return
     */
    private boolean determineNotedId(ChanceItem reward) {
        if (reward.getId() == 145 || reward.getId() == 157 || reward.getId() == 163 || reward.getId() == 139 || reward.getId() == 532) {
            return true;
        }
        return false;
    }

    /**
     * Determines the formatted name for the reward.
     *
     * @param name   The name.
     * @param reward The reward.
     * @return
     */
    private String determineName(String name, ChanceItem reward) {
        if (reward.getId() == 145 || reward.getId() == 157 || reward.getId() == 163 || reward.getId() == 139) {
            return reward.getName().toLowerCase().replace("(3)", "s");
        }
        if (reward.getId() == 565 || reward.getId() == 563 || reward.getId() == 555 || reward.getId() == 560) {
            return reward.getName().toLowerCase() + "s";
        } else {
            return reward.getName().toLowerCase();
        }
    }

    /**
     * Gets the chance item from the array.
     *
     * @param items the items.
     * @return the chance item.
     */
    private ChanceItem getChanceItem(ChanceItem[] items) {
        final int chance = RandomUtil.random(100);
        final List<ChanceItem> chances = new ArrayList<>();
        for (ChanceItem c : items) {
            if (chance > c.getChanceRate()) {
                chances.add(c);
            }
        }
        return chances.size() == 0 ? items[0] : chances.get(RandomUtil.random(chances.size()));
    }

    @Override
    public boolean isWalk() {
        return false;
    }
}

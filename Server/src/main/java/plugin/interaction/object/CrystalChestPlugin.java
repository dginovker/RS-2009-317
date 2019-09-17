package plugin.interaction.object;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the plugin used for the crystal chest.
 *
 * @author 'Vexia
 * @version 1.0
 */

/**
 * Represents the plugin used for the crystal chest.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CrystalChestPlugin extends UseWithHandler {


    /**
     * Represents the cyrstal key.
     */
    private static final Item KEY = new Item(989);

    /**
     * Constructs a new {@code CrystalChestPlugin} {@code Object}.
     */
    public CrystalChestPlugin() {
        super(989);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(172, OBJECT_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        if (!player.getInventory().contains(989, 1)) {
            player.getActionSender().sendMessage("This chest is securely locked shut.");
            return true;
        }
        if (player.getInventory().freeSlots() < 2) {
            player.getActionSender().sendMessage("You need at least 2 free inventory slots to do this.");
            return true;
        }
        // @todo open then close chest
        if (player.getInventory().remove(KEY)) {
            final Reward[] rewards = Reward.values();
            Item item = null;
            while (item == null) {
                Reward reward = rewards[RandomUtil.random(rewards.length)];
                if (RandomUtil.random(100) < reward.getChance()) {
                    item = reward.getItems()[RandomUtil.random(reward.getItems().length)];
                }
            }
            player.playAnimation(Animation.create(535));
            player.getInventory().add(new Item(1631, 1));
            player.getInventory().add(item);
            player.getActionSender().sendMessage("You unlock the chest with your key.");
            player.getActionSender().sendMessage("You find some treasure in the chest!");
        }
        return true;
    }

    /**
     * Represents a crystal ches reward.
     *
     * @author 'Vexia
     */
    public enum Reward {

        FIRST(39.69, new Item(1969, 1), new Item(Item.COINS, 2000)),
        SECOND(10.57, new Item(372, 5), new Item(Item.COINS, 1000)),
        THIRD(7.73, new Item(559, 50), new Item(556, 50), new Item(558, 50), new Item(557, 50), new Item(554, 50), new Item(555, 50), new Item(562, 10), new Item(564, 10), new Item(560, 10), new Item(561, 10), new Item(563, 10)),
        FOURTH(6.55, new Item(454, 100)),
        FIFTH(4.23, new Item(1603, 2), new Item(1601, 1)),
        SIXTH(3.67, new Item(Item.COINS, 700), new Item(985, 1)),
        SEVENTH(3.51, new Item(2363, 3)),
        EIGHTH(3.26, new Item(Item.COINS, 750), new Item(987)),
        NINTH(2.57, new Item(441, 150)),
        TENTH(1.06, new Item(1183, 1)),
        ELEVENTH(0.26, new Item(1079, 1)),
        TWELFTH(0.26, new Item(1093, 1));

        /**
         * Represents the item rewards.
         */
        private final Item[] items;

        /**
         * Represents the chance of getting the item.
         */
        private final double chance;

        /**
         * Constructs a new {@code CrystalChestPlugin} {@code Object}.
         *
         * @param chance the chance.
         * @param items  the item.
         */
        Reward(double chance, Item... items) {
            this.chance = chance;
            this.items = items;
        }

        /**
         * Gets the items.
         *
         * @return The items.
         */
        public Item[] getItems() {
            return items;
        }

        /**
         * Gets the chance.
         *
         * @return The chance.
         */
        public double getChance() {
            return chance;
        }

    }
}

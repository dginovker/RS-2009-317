package plugin.interaction.item.withitem;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the fruit cutting plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FruitCuttingPlugin extends UseWithHandler {

    /**
     * Represents the cutting banana animation.
     */
    private static final Animation ANIMATION = new Animation(1192);

    /**
     * Constructs a new {@code FruitCuttingPlugin} {@code Object}.
     */
    public FruitCuttingPlugin() {
        super(946);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (Fruit fruit : Fruit.values()) {
            addHandler(fruit.getBase().getId(), ITEM_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final Fruit fruit = Fruit.forBase(event.getUsedItem());
        if (fruit == Fruit.BANANA) {
            if (player.getInventory().remove(fruit.getBase())) {
                player.lock(2);
                player.animate(ANIMATION);
                player.getInventory().add(fruit.getSliced());
                player.getActionSender().sendMessage("You deftly chop the bananas into slices.");
            }
            return true;
        }
        new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.TWO_OPTION, fruit.getSliced(), fruit.getDiced()) {

            @Override
            public void create(int amount, int index) {
                cut(player, fruit, amount, (index == 0));
            }
        }.open();
        return true;
    }

    /**
     * Method used to cut a fruit.
     *
     * @param player the player.
     * @param amount the amount.
     * @param slice  if its sliced.
     */
    private void cut(final Player player, Fruit fruit, int amount, boolean slice) {
        if (amount > player.getInventory().getCount(fruit.getBase())) {
            amount = player.getInventory().getCount(fruit.getBase());
        }
        final Item remove = new Item(fruit.getBase().getId(), amount);
        if (!player.getInventory().containsItem(remove)) {
            return;
        }
        if (player.getInventory().remove(remove)) {
            player.getActionSender().sendMessage("You cut the " + fruit.name().toLowerCase() + " into " + (slice ? fruit == FruitCuttingPlugin.Fruit.PINEAPPLE ? "rings" : "slices" : "chunks") + ".");
            for (int i = 0; i < amount; i++) {
                player.getInventory().add(slice ? fruit.getSliced() : fruit.getDiced());
            }
        }
    }

    /**
     * Represents the a fruit to cut.
     *
     * @author 'Vexia
     * @date 30/11/2013
     */
    public enum Fruit {
        PINEAPPLE(new Item(2114), new Item(2116), new Item(2118, 4)),
        BANANA(new Item(1963), null, new Item(3162)),
        ORANGE(new Item(2108), new Item(2110), new Item(2112));

        /**
         * Constructs a new {@code FruitCuttingPlugin.java} {@code Object}.
         *
         * @param base   the base item.
         * @param diced  the diced item.
         * @param sliced the sliced item.
         */
        Fruit(Item base, Item diced, Item sliced) {
            this.base = base;
            this.diced = diced;
            this.sliced = sliced;
        }

        /**
         * Represents the base item.
         */
        private final Item base;

        /**
         * Represents the diced item.
         */
        private final Item diced;

        /**
         * Represents the sliced item.
         */
        private final Item sliced;

        /**
         * Gets the base.
         *
         * @return The base.
         */
        public Item getBase() {
            return base;
        }

        /**
         * Gets the diced.
         *
         * @return The diced.
         */
        public Item getDiced() {
            return diced;
        }

        /**
         * Gets the sliced.
         *
         * @return The sliced.
         */
        public Item getSliced() {
            return sliced;
        }

        /**
         * Method used to get the fruit for the base item.
         *
         * @param item the item.
         * @return the fruit.
         */
        public static Fruit forBase(final Item item) {
            for (Fruit fruit : values()) {
                if (fruit.getBase().getId() == item.getId()) {
                    return fruit;
                }
            }
            return null;
        }
    }
}

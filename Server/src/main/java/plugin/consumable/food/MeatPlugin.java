package plugin.consumable.food;

import org.gielinor.game.content.global.consumable.ConsumableProperties;
import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.CookingProperties;
import org.gielinor.game.content.global.consumable.Food;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the meat consumable food.
 *
 * @author 'Vexia
 */
public final class MeatPlugin extends Food {

    /**
     * Represents the cooking dialogue id.
     */
    private static final int DIALOGUE_ID = 43989;

    /**
     * Constructs a new {@code MeatPlugin} {@code Object}.
     */
    public MeatPlugin() {
        /**
         * empty.
         */
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Consumables.add(new MeatPlugin(2142, 2132, 2146, new ConsumableProperties(3), new MeatProperty(1, 30, 30, "You cook a piece of beef.", "You accidentally burn the meat.")));
        Consumables.add(new MeatPlugin(9436, 2132, 2146, null, new MeatProperty(1, 30, 30, "You dry a piece of beef and extract the sinew.", "You accidentally burn the meat.")));
        Consumables.add(new MeatPlugin(2142, 2134, 2146, new ConsumableProperties(3), new MeatProperty(1, 30, 30, "You cook a piece of rat meat.", "You accidentally burn the meat.")));
        Consumables.add(new MeatPlugin(2142, 2136, 2146, new ConsumableProperties(3), new MeatProperty(1, 30, 30, "You cook a piece of bear meat.", "You accidentally burn the meat.")));
        return this;
    }

    /**
     * Constructs a new {@code Meat} {@code Object}.
     *
     * @param item              the item.
     * @param raw               the raw item.
     * @param burnt             the burnt item.
     * @param foodProperties    the food properties.
     * @param cookingProperties the cooking properties.
     */
    public MeatPlugin(int item, int raw, int burnt, ConsumableProperties foodProperties, CookingProperties cookingProperties) {
        super(item, raw, burnt, foodProperties, cookingProperties);
    }

    @Override
    public boolean interact(final Player player, final Node node) {
        if (this.getRaw().getId() == 2132) {
            player.getDialogueInterpreter().open(DIALOGUE_ID, this, node, true);
        } else {
            player.getDialogueInterpreter().open(DIALOGUE_ID, this, node);
        }
        return false;
    }

    /**
     * Represents the meat properties used to override for sinew.
     *
     * @author 'Vexia
     */
    public static class MeatProperty extends CookingProperties {

        /**
         * Constructs a new {@code Meat} {@code Object}.
         *
         * @param level      the level.
         * @param experience the experience.
         * @param burnLevel  the burn level.
         * @param messages   the messages.
         */
        public MeatProperty(int level, double experience, int burnLevel, String... messages) {
            super(level, experience, burnLevel, true, messages);
        }

        @Override
        public boolean cook(final Food food, final Player player, final GameObject object) {
            return super.cook(food, player, object);
        }
    }
}
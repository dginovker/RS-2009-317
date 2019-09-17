package plugin.consumable.food;


import org.gielinor.game.content.global.consumable.ConsumableProperties;
import org.gielinor.game.content.global.consumable.CookingProperties;
import org.gielinor.game.content.global.consumable.Food;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;

/**
 * Represents the shrimp food represented in it's own class because of the details it contains for tutorial island
 * this is an example class on how this consumable system works. We can do our conditions in this class instead
 * of cluttering the {@code CookPulse#reward} method.
 *
 * @author 'Vexia
 * @date 22/12/2013
 */
public class ShrimpsPlugin extends Food {

    /**
     * Represents the shrimp properties.
     */
    private static final ShrimpProperties PROPERTIES = new ShrimpProperties();

    /**
     * Constructs a new {@code Shrimps} {@code Object}.
     */
    public ShrimpsPlugin() {
        super(315, 317, 7954, new ConsumableProperties(3), PROPERTIES);
    }

    @Override
    public boolean interact(final Player player, final Node node) {
        return true;
    }

    /**
     * Represents the cooking properties of shirmps.
     *
     * @author 'Vexia
     * @date 22/12/2013
     */
    public static class ShrimpProperties extends CookingProperties {

        /**
         * Constructs a new {@code Shrimps} {@code Object}.
         */
        public ShrimpProperties() {
            super(1, 45, 35, "You successfully cook some shrimps.", CookingProperties.FAIL_MESSAGE);
        }

        @Override
        public boolean cook(final Food food, final Player player, final GameObject object) {
            return super.cook(food, player, object);
        }
    }
}

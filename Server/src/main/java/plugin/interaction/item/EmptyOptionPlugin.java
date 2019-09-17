package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.consumable.Consumable;
import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Drink;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to handle the emptying of an item.
 *
 * @author 'Vexia
 */
public final class EmptyOptionPlugin extends OptionHandler {

    /**
     * Represents the vial consumable.
     */
    private static final VialConsumable VIAL_CONSUMABLE = new VialConsumable();

    @Override
    public boolean handle(Player player, Node node, String option) {
        Consumables.find(((Item) node))
            .orElse(node.getName().contains("potion")
                || node.getName().contains("+")
                || node.getName().contains("mix")
                || node.getName().toLowerCase().equals("plant cure")
                ? VIAL_CONSUMABLE
                : new Drink(((Item) node), null)
            ).interact(player, node, option);
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("empty", this);
        return this;
    }

    /**
     * Represents a vial consumable.
     *
     * @author 'Vexia
     */
    public static class VialConsumable extends Consumable {

        /**
         * Constructs a new {@code VialConsumable} {@code Object}.
         */
        public VialConsumable() {
            super(null, null);
        }

        public String getEmptyMessage(Item item) {
            return "You empty the vial.";
        }

        @Override
        public Item getEmptyItem() {
            return VIAL;
        }
    }
}

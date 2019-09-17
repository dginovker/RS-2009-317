package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.consumable.ConsumableProperties;
import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Drink;
import org.gielinor.game.content.global.consumable.Food;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to consume a consumable item.
 * @author 'Vexia
 * @author Emperor
 * @version 1.0
 */
public final class ConsumableOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) {
        ItemDefinition.setOptionHandler("eat", this);
        ItemDefinition.setOptionHandler("drink", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, final Node node, final String option) {

        if (player.getLocks().isLocked(option))
            return true;

        if(player.getAttribute("reverse_gun_game", false)){
            player.getActionSender().sendMessage("You cannot consume resources in a gun game.");
            return false;
        }

        final boolean isEdible = option.equals("eat");

        player.getLocks().lock(option, 3);

        if (!isEdible)
            player.getLocks().lock("eat", 2);

        final Item item = (Item) node;

        if (player.getInventory().get(item.getSlot()) != item)
            return false;

        if(isEdible) Consumables
                .findFood(item)
                .orElse( new Food(item.getId(), new ConsumableProperties(1)))
                .consume(node.asItem(), player);
         else Consumables.findDrink(item)
                .orElse(new Drink(item.getId(), new ConsumableProperties(1)))
                .consume(node.asItem(), player);

        if (isEdible)
            player.getProperties().getCombatPulse().delayNextAttack(3);

        return true;
    }
}

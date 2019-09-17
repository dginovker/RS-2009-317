package plugin.consumable;

import org.gielinor.game.content.global.consumable.ConsumableProperties;
import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.global.consumable.Drink;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the cup of tea plugin.
 * @author 'Veixa
 * @version 1.0
 */
public final class CupofTeaPlugin extends Drink {

    /**
     * Represents the force chat to use.
     */
    private static final String CHAT = "Aaah, nothing like a nice cuppa tea!";

    @Override
    public CupofTeaPlugin newInstance(Object object) {
        Consumables.add(this);
        return this;
    }

    /**
     * Constructs a new {@code CupofTeaPlugin} {@code Object}.
     */
    public CupofTeaPlugin() {
        super(712, new ConsumableProperties(2, 1980));
    }

    @Override
    public void consume(final Item item, final Player player) {
        player.sendChat(CHAT);
        super.healAndRemove(player, item);
    }

}

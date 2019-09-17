package plugin.interaction.object;

import java.security.SecureRandom;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for the chest in the KBD lair.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KBDChestPlugin extends OptionHandler {

    /**
     * The {@link java.security.SecureRandom} instance.
     */
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (node.getLocation().getRegionId() != 9033) {
            return false;
        }
        if (player.getInventory().contains(1543)) {
            if (player.getInventory().freeSlots() < 4) {
                player.getActionSender().sendMessage("You do not have enough space in your inventory.");
                return true;
            }
            if (player.getInventory().remove(new Item(1543))) {
                // TODO Dragon pickaxe
                boolean dragonFullHelm = (secureRandom.nextInt(7000) == 1);
                player.getInventory().add(new Item(Item.COINS, RandomUtil.random(40000, 400000)));
                player.getInventory().add(new Item(1632, RandomUtil.random(2, 8)));
                player.getInventory().add(new Item(537, RandomUtil.random(4, 14)));
                if (dragonFullHelm) {
                    player.getInventory().add(new Item(11335, 1));
                    player.sendGlobalNewsMessage("FF8C38", " has just received a Dragon full helm from the KBD chest!", 5);
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(375).getConfigurations().put("option:open", this);
        return this;
    }
}

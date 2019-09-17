package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import plugin.interaction.item.withitem.SlayerHelmCreatePlugin.SlayerHelmAssembly;

import java.util.Arrays;

/**
 * Handles disassembling a Slayer helmet.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 * @author Corey
 */
public class SlayerHelmetPlugin extends OptionHandler {

    /**
     * Represents Black mask items.
     */
    public static final Item[] BLACK_MASKS = new Item[]{
        new Item(8901),
        new Item(8903),
        new Item(8905),
        new Item(8907),
        new Item(8909),
        new Item(8911),
        new Item(8913),
        new Item(8915),
        new Item(8917),
        new Item(8919),
        new Item(8921)
    };

    public static final Item ABYSSAL_HEAD = new Item(27979);
    public static final Item KBD_HEAD = new Item(27980);
    public static final Item KQ_HEAD = new Item(27981);
    public static final Item DARK_CLAW = new Item(41275);

    public static final Item SLAYER_HELMET = new Item(Item.SLAYER_HELMET);
    public static final Item BLACK_SLAYER_HELMET = new Item(Item.BLACK_SLAYER_HELMET);
    public static final Item GREEN_SLAYER_HELMET = new Item(Item.GREEN_SLAYER_HELMET);
    public static final Item RED_SLAYER_HELMET = new Item(Item.RED_SLAYER_HELMET);
    public static final Item PURPLE_SLAYER_HELMET = new Item(Item.PURPLE_SLAYER_HELMET);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Arrays.stream(SlayerHelmAssembly.values())
            .forEach(helm -> ItemDefinition.forId(helm.getProduct().getId()).getConfigurations().put("option:disassemble", this));

        Arrays.stream(SlayerHelmAssembly.values())
            .forEach(helm -> ItemDefinition.forId(helm.getProduct().getId()).getConfigurations().put("option:check", this));

        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        SlayerHelmAssembly helmet = SlayerHelmAssembly.forHelmetId(node.getId());
        switch (option.toLowerCase().trim()) {
            case "disassemble":
                if (player.getInventory().freeSlots() < helmet.getIngredients().length - 1) { // -1 because we remove the helm itself
                    player.getActionSender().sendMessage("Not enough space in inventory.");
                    return true;
                }
                if (player.getInventory().remove(helmet.getProduct())) {
                    player.getInventory().add(helmet.getIngredients());
                    return true;
                }
                break;
            case "check":
                player.getSlayer().informTaskProgress();
                return true;
        }
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

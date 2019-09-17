package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for the Slayer ring.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SlayerRingPlugin extends OptionHandler {

    /**
     * Represents IDs of Slayer rings (8 through 1 charge)
     */
    public static final int[] IDS = new int[]{ Item.SLAYER_RING_8, Item.SLAYER_RING_7, Item.SLAYER_RING_6, Item.SLAYER_RING_5, Item.SLAYER_RING_4, Item.SLAYER_RING_3, Item.SLAYER_RING_2, Item.SLAYER_RING_1 };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int id : IDS) {
            ItemDefinition.forId(id).getConfigurations().put("option:rub", this);
            ItemDefinition.forId(id).getConfigurations().put("option:check", this);
        }
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!player.getSlayer().hasStarted()) {
            player.getActionSender().sendMessage("You try to activate the ring...");
            return true;
        }
        switch (option.toLowerCase()) {
            case "check":
                player.getSlayer().informTaskProgress();
                return true;
            case "rub":
                final EnchantedJewelleryPlugin.EnchantedJewellery jewellery = EnchantedJewelleryPlugin.EnchantedJewellery.forItem((Item) node);
                player.getActionSender().sendMessage("You rub the Slayer ring...");
                player.getDialogueInterpreter().open(EnchantedJewelleryPlugin.JewelleryDialogue.ID, node, jewellery, option.equals("operate"));
                return true;
        }
        return false;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

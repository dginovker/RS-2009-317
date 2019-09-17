package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.perk.PerkManagement;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class LanthusPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(1526).getConfigurations().put("option:perks", this);
        NPCDefinition.forId(1526).getConfigurations().put("option:shop", this);
        PluginManager.definePlugin(new CreditShopInterface());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "shop":
                player.getDonorManager().openStore();
                return true;
            case "perks":
                PerkManagement.openPage(player, 0, true);
                return true;
        }
        return false;
    }

    /**
     * Represents the credit shop interface for claiming items.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public class CreditShopInterface extends ComponentPlugin {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ComponentDefinition.put(25709, this);
            return this;
        }

        @Override
        public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
            if (component.getId() != 25709 && opcode != 145) {
                return false;
            }
            Container container = player.getDonorManager().getCreditShopContainer();
            Item item = container.get(slot);
            if (item == null || item.getId() != itemId) {
                return false;
            }
            if (item.getCount() < 1) {
                return false;
            }
            if (!player.getInventory().hasRoomFor(item)) {
                player.getActionSender().sendMessage("You do not have enough space in your inventory to claim that.");
                return true;
            }
            player.getDonorManager().claimItem(item);
            return true;
        }
    }

}
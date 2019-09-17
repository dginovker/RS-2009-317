package plugin.interaction.item.withitem;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles using a compost potion on a bucket of compost.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CompostPotion extends UseWithHandler {

    public CompostPotion() {
        super(6032);
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Item compostPotion = event.getBaseItem().getId() == 6032 ? event.getUsedItem() : event.getBaseItem();
        if (player.getInventory().remove(event.getBaseItem(), event.getUsedItem())) {
            int toAdd = 229;
            switch (compostPotion.getId()) {
                case 6474:
                    toAdd = 6476;
                    break;
                case 6472:
                    toAdd = 6474;
                    break;
                case 6470:
                    toAdd = 6472;
                    break;
            }
            player.getInventory().add(new Item(toAdd, 1));
            player.getInventory().add(new Item(6034, 1));
            player.getActionSender().sendMessage("You add the compost potion to the compost and get supercompost.");
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(6476, ITEM_TYPE, this);
        addHandler(6474, ITEM_TYPE, this);
        addHandler(6472, ITEM_TYPE, this);
        addHandler(6470, ITEM_TYPE, this);
        return this;
    }

}

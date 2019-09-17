package plugin.interaction.item.ticket;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Created by Stan van der Bend on 05/02/2018.
 *
 * project: Gielinor-Server
 * package: plugin.interaction.item.ticket
 */
public abstract class TicketPlugin extends OptionHandler {

    private int itemID;

    public TicketPlugin(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(itemID).getConfigurations().put("option:claim", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final Item ticket = (Item) node;

        if (!player.getInventory().contains(ticket))
            return false;

        if (!canClaim(player)) {
            player.getActionSender().sendMessage("You did not meet the requirements to claim this ticket.");
            return true;
        }

        if (option.equals("claim"))
            onClaim(player);

        if(deleteAfterClaim(player))
            player.getInventory().remove(ticket);

        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    protected abstract void onClaim(Player player);
    protected abstract boolean canClaim(Player player);
    protected abstract boolean deleteAfterClaim(Player player);

}

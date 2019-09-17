package plugin.interaction.item.ticket.vote;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import plugin.interaction.item.ticket.TicketPlugin;

/**
 * Created by Stan van der Bend on 05/02/2018.
 *
 * TODO: map an ID to this.
 *
 * task: https://www.meistertask.com/app/task/cOWITe9h/vote-tickets
 * project: Gielinor-Server
 * package: plugin.interaction.item.ticket.vote
 */
public class GoldenTicket extends TicketPlugin{

    public GoldenTicket(int itemID) {
        super(itemID);
    }

    @Override
    protected void onClaim(Player player) {
        player.getInventory().add(new Item(995, 1_000_000));
    }

    @Override
    protected boolean canClaim(Player player) {
        if(player.getInventory().freeSlots() < 1 && !player.getInventory().contains(995)){
            player.getActionSender().sendMessage("You do no not have enough free inventory slots to do this,");
            return false;
        }
        return true;
    }

    @Override
    protected boolean deleteAfterClaim(Player player) {
        return true;
    }
}

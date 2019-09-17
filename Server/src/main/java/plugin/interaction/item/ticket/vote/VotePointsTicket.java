package plugin.interaction.item.ticket.vote;

import org.gielinor.game.node.entity.player.Player;
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
public class VotePointsTicket extends TicketPlugin {

    public VotePointsTicket(int itemID) {
        super(itemID);
    }

    @Override
    protected void onClaim(Player player) {
        player.getSavedData().getGlobalData().increaseVotingPoints(2);
    }

    @Override
    protected boolean canClaim(Player player) {
        return true;
    }

    @Override
    protected boolean deleteAfterClaim(Player player) {
        return true;
    }
}

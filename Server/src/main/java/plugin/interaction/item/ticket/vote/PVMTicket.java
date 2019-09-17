package plugin.interaction.item.ticket.vote;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.GlobalData;
import plugin.interaction.item.ticket.TicketPlugin;

import java.util.stream.IntStream;

/**
 * Created by Stan van der Bend on 05/02/2018.
 *
 * TODO: map an ID to this.
 *
 * task: https://www.meistertask.com/app/task/cOWITe9h/vote-tickets
 * project: Gielinor-Server
 * package: plugin.interaction.item.ticket.vote
 */
public class PVMTicket extends TicketPlugin{

    private final static double
        COMBAT_MODIFIER = 1.15D,
        DROP_RATE_MODIFIER = 1.10D;

    private final static int
        INCREASED_COMBAT_DURATION_IN_MINUTES = 60,
        INCREASED_DROP_RATE_DURATION_IN_MINUTES = 30;

    public PVMTicket(int itemID) {
        super(itemID);
    }

    @Override
    protected void onClaim(Player player) {
        final GlobalData globalData = player.getSavedData().getGlobalData();

        globalData.setAccuracyModifier(COMBAT_MODIFIER);
        globalData.setStrengthModifier(COMBAT_MODIFIER);
        globalData.setDropModifier(DROP_RATE_MODIFIER);

        globalData.setAccuracyModifierTime(INCREASED_COMBAT_DURATION_IN_MINUTES);
        globalData.setStrengthModifierTime(INCREASED_COMBAT_DURATION_IN_MINUTES);
        globalData.setDropModifierTime(INCREASED_DROP_RATE_DURATION_IN_MINUTES);

        player.getDialogueInterpreter().sendDialogue(
            "You have been granted "+ INCREASED_COMBAT_DURATION_IN_MINUTES +" minutes of increased accuracy and strength.",
            "Also your drop rate has been slightly increased for "+INCREASED_DROP_RATE_DURATION_IN_MINUTES+".",
            "("+COMBAT_MODIFIER+"x increased accuracy and strength for PVM combat)",
            "("+DROP_RATE_MODIFIER+"x increased drop rate.)");
    }

    @Override
    protected boolean canClaim(Player player) {

        final GlobalData globalData = player.getSavedData().getGlobalData();

        final int
            dropTimeLeft = globalData.getDropModifierTime(),
            accuracyTimeLeft = globalData.getAccuracyModifierTime(),
            strengthTimeLeft = globalData.getStrengthModifierTime();

        boolean canClaim = IntStream.of(dropTimeLeft, accuracyTimeLeft, strengthTimeLeft).allMatch(time -> time < 1);

        if(!canClaim) {

            if (dropTimeLeft > 0)
                player.getActionSender().sendMessage("You still have " + dropTimeLeft + " left of increased drop rate.");

            if (accuracyTimeLeft > 0)
                player.getActionSender().sendMessage("You still have " + accuracyTimeLeft + " minutes left of increased accuracy.");

            if(strengthTimeLeft > 0)
                player.getActionSender().sendMessage("You still have " + strengthTimeLeft + " minutes left of increased strength.");

        }

        return canClaim;
    }

    @Override
    protected boolean deleteAfterClaim(Player player) {
        return true;
    }
}

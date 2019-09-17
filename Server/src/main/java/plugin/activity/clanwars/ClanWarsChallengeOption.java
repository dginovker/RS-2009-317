package plugin.activity.clanwars;

import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.request.RequestType;
import org.gielinor.game.system.communication.ClanRank;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the challenge option within the Clan Wars area.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ClanWarsChallengeOption extends OptionHandler {

    /**
     * The challenge option.
     */
    public static final Option OPTION = new Option("Challenge", 0);

    /**
     * The request type.
     */
    private static final RequestType REQUEST_TYPE = new RequestType("Sending Clan Wars challenge...", ":clanreq:", new ClanRequestModule(), "challenge") {

        @Override
        public boolean canRequest(Player player, Player target) {
            if (player.getCommunication().getClan() == null) {
                player.getActionSender().sendMessage("You don't appear to be in a clan. This is Clan Wars, mmkay?");
                return false;
            }
            if (player.getCommunication().getClan().getRank(player).ordinal() < ClanRank.CAPTAIN.ordinal() && player.getCommunication().getClan().getRank(player) != ClanRank.OWNER) {
                player.getActionSender().sendMessage("Your clan rank is not high enough to challenge other clans.");
                return false;
            }
            if (player.getCommunication().getClan().getClanWar() != null) {
                player.getActionSender().sendMessage("Your clan is already in a war.");
                return false;
            }
            if (target.getCommunication().getClan() == null) {
                player.getActionSender().sendMessage(target.getUsername() + " doesn't appear to be in a clan.");
                return false;
            }
            if (target.getCommunication().getClan().getRank(target).ordinal() < ClanRank.CAPTAIN.ordinal() && target.getCommunication().getClan().getRank(target) != ClanRank.OWNER) {
                player.getActionSender().sendMessage("This player's clan rank is not high enough to accept challenges.");
                return false;
            }
            if (target.getCommunication().getClan().getClanWar() != null) {
                player.getActionSender().sendMessage("This player's clan is already in a war.");
                return false;
            }
            if (target.getCommunication().getClan() == player.getCommunication().getClan()) {
                player.getActionSender().sendMessage("You can't challenge someone from your own clan.");
                return false;
            }
            return true;
        }
    };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        OPTION.setHandler(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getRequestManager().request((Player) node, REQUEST_TYPE);
        return true;
    }

}
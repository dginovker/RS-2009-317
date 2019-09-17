package plugin.interaction.item;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.referral.Referred;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.DateUtils;
import org.gielinor.utilities.misc.RunScript;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ReferralTicketPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(14752).getConfigurations().put("option:referrals", this);
        ItemDefinition.forId(14752).getConfigurations().put("option:refer player", this);
        ItemDefinition.forId(14752).getConfigurations().put("option:requests", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        switch (option) {
            case "referrals":
                return openReferrals(player);

            case "refer player":
                return openReferralDialogue(player);

            case "requests":
                return openReferralRequests(player);
        }
        return true;
    }

    /**
     * Opens the player's referrals list.
     *
     * @param player
     *            The player.
     * @return <code>True</code> if handled.
     */
    public static boolean openReferrals(Player player) {
        player.getQuestMenuManager().clear();
        player.getQuestMenuManager().setTitle(player.getUsername() + " - Referrals");
        List<String> lines = new ArrayList<>();
        lines.add("Username   -   Date Sent   -   Date Accepted");
        lines.add("");
        List<Referred> refersList = player.getReferralManager().getReferrals();
        if (player.getReferralManager().getSentReferralCount() == 0) {
            lines.add("You do not have any referrals to display.");
            player.getQuestMenuManager().setLines(lines).send();
            return true;
        }
        Referred[] referrals = refersList.toArray(new Referred[refersList.size()]);
        for (Referred referred : referrals) {
            if (referred.isRequested()) {
                continue;
            }
            lines.add(referred.getUsername() + " - " + DateUtils.getTime(referred.getReferredRequestTime(), false)
                + (referred.getReferredTime() > 0 ? " - " + DateUtils.getTime(referred.getReferredTime(), false)
                : " - <shad=1><col=B03232>Pending"));
        }
        player.getQuestMenuManager().setLines(lines).send();
        return true;
    }

    /**
     * Opens a referral dialogue.
     *
     * @param player
     *            The player.
     * @return <code>True</code> if handled.
     */
    public static boolean openReferralDialogue(Player player) {
        if (!player.getRights().isAdministrator()
            && player.getSavedData().getGlobalData().getTotalPlayTime() < Constants.MINIMUM_PLAY_TIME) {
            player.getDialogueInterpreter()
                .sendPlaneMessage("You must have been playing for at least 30 minutes to do this.");
            return true;
        }
        if (player.getSkills().getTotalLevel() < 300) {
            player.getDialogueInterpreter().sendPlaneMessage("You must have a total level of at least 300 to do this.");
            return true;
        }
        player.setAttribute("runscript", new RunScript() {

            @Override
            public boolean handle() {
                String username = (String) getValue();
                if (username == null || username.isEmpty() || username.length() < 1
                    || username.equalsIgnoreCase(player.getName())) {
                    player.getDialogueInterpreter().sendPlaneMessage("Please provide a valid username.");
                    // return true;
                }
                Player referral = Repository.getPlayerByName(username);
                if (referral == null) {
                    player.getDialogueInterpreter().sendPlaneMessage("The player " + username + " is not online.");
                    return true;
                }
                if (referral.getReferralManager().isReferred()) {
                    player.getDialogueInterpreter().sendPlaneMessage("That player has already been referred.");
                    return true;
                }
                if (player.getReferralManager().hasReferred(username)) {
                    player.getDialogueInterpreter()
                        .sendPlaneMessage("You have already sent a referral to this player.");
                    return true;
                }
                if (referral.getSavedData().getGlobalData().getTotalPlayTime() < Constants.MINIMUM_PLAY_TIME) {
                    player.getDialogueInterpreter()
                        .sendPlaneMessage("They must have been playing for at least 2 hours to be referred.");
                    return true;
                }
                if (referral.getSkills().getTotalLevel() < 300) {
                    player.getDialogueInterpreter()
                        .sendPlaneMessage("They must have a total level of at least 300 to be referred.");
                    return true;
                }
                player.getReferralManager().refer(referral);
                return true;
            }
        });
        player.getDialogueInterpreter().sendInput(true, "Enter username:");
        return true;
    }

    /**
     * Opens the referral requests list.
     *
     * @param player
     *            The player.
     * @return <code>True</code> if handled.
     */
    public static boolean openReferralRequests(Player player) {
        for (int childId = 27703; childId < 27803; childId++) {
            player.getActionSender().sendString(childId, "");
            player.getActionSender().sendHideComponent(childId, true);
        }
        PacketRepository.send(InterfaceMaxScrollPacket.class, new InterfaceMaxScrollContext(player, 27702, 0));
        player.setAttribute("OPTION_INTERFACE", "REFERRALS");
        player.getInterfaceState().open(new Component(27700).setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                player.removeAttribute("OPTION_INTERFACE");
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        }));
        player.getActionSender().sendString(27803, player.getUsername() + " - Referral requests");
        if (player.getReferralManager().isReferred()) {
            player.getActionSender().sendString(27703, "<col=8A0808>You were already referred by another player.");
            return true;
        }
        if (player.getReferralManager().getReferralRequests() == 0) {
            player.getActionSender().sendString(27703, "You do not have any referral requests to display.");
            return true;
        }
        player.getActionSender().sendString(27703,
            "<col=8A0808>To accept a referral request, type ::accept <lt>username> or click the");
        player.getActionSender().sendString(27704,
            "<col=8A0808>username below. Once accepted, you can <u>NOT</u> change your referrer.");
        player.getActionSender().sendString(27705, "");
        int childId = 27706;
        int scrollLength = 0;
        List<Referred> refersList = player.getReferralManager().getReferrals();
        Referred[] referrals = refersList.toArray(new Referred[refersList.size()]);
        for (Referred referred : referrals) {
            if (!referred.isRequested()) {
                continue;
            }
            player.getActionSender().sendString(childId,
                referred.getUsername() + " - " + DateUtils.getTime(referred.getReferredRequestTime(), false));
            player.getActionSender().sendHideComponent(childId, false);
            childId++;
            scrollLength += 20;
        }
        scrollLength += 56;
        PacketRepository.send(InterfaceMaxScrollPacket.class,
            new InterfaceMaxScrollContext(player, 27702, scrollLength));
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

}

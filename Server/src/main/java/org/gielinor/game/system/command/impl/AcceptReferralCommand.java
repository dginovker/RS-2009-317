package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Accepts a referral.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AcceptReferralCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "accept", "acceptreferral", "decline", "declinereferral" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("accept", "Accepts a referral request", getRights(), "::accept <lt>username><br>Example<br>::accept Gielinor"));
        CommandDescription.add(new CommandDescription("acceptreferral", "Accepts a referral request", getRights(), "::acceptreferral <lt>username><br>Example<br>::accept Gielinor"));
        CommandDescription.add(new CommandDescription("decline", "Declines a referral request", getRights(), "::decline <lt>username><br>Example<br>::decline Gielinor"));
        CommandDescription.add(new CommandDescription("declinereferral", "Declines a referral request", getRights(), "::declinereferral <lt>username><br>Example<br>::decline Gielinor"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>username>");
            return;
        }
        String username = toString(args, 1);
        if (username == null || username.isEmpty()) {
            player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>username>");
            return;
        }
        if (args[0].toLowerCase().startsWith("accept")) {
            if (player.getReferralManager().isReferred()) {
                player.getActionSender().sendMessage("You were already referred by another player.");
                return;
            }
            player.getReferralManager().accept(username);
        } else {
            player.getReferralManager().decline(username);
        }
    }
}

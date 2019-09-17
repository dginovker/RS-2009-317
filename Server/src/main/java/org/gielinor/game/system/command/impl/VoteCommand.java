package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.config.ServerVar;

/**
 * Opens the voting web-page for the player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class VoteCommand extends Command {

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
        return new String[]{ "vote", "voteurl" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("vote", "Launches the voting website"));
        CommandDescription.add(new CommandDescription("voteurl", "Displays the voting URL", getRights(), null));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args == null || args.length == 1 && args[0].equalsIgnoreCase("voteurl")) {
            player.getActionSender().sendURL(ServerVar.fetch("vote_url"), "You can vote for us at", Constants.VOTE_URL);
            return;
        }
        player.getActionSender().sendURL(ServerVar.fetch("vote_url"));
    }
}

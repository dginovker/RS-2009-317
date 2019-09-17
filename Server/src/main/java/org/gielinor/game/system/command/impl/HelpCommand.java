package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.rs2.config.Constants;

/**
 * An in-game help command for players.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class HelpCommand extends Command {

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
        return new String[]{ "help" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("help", "Opens the help menu", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        player.getActionSender().sendQuestInterface(Constants.SERVER_NAME + " Help", "<col=000000>Welcome to Gielinor.",
            "",
            "<col=000000>Report any issues found, or suggestions on the forums",
            "<col=000000>at <col=8B0000>" + Constants.FORUMS_URL + "</col> under the ",
            "<col=8B0000>Development Diaries</col> <gt> <col=8B0000>Bug Reports</col> board.",
            "",
            "<col=000000>If you require moderator or administrator assistance",
            "<col=000000>you can use <col=8B0000>::modyyell <lt>message></col> and",
            "<col=8B0000>::adminyell <lt>message></col> to request the assistance.",
            "",
            "<col=000000>For additional help, guides, or to talk to other players",
            "<col=000000>you can visit the forums (<col=8B0000>" + Constants.FORUMS_URL + "</col>)",
            "<col=000000>and login with your forum account.");
    }
}

package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Gives the given player voting points.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GiveVotePointsCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "givevotingpoints", "givevotepoints" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("givevotepoints", "Gives a player vote points", getRights(),
            "::givevotepoints <lt>amount> <lt>player name>"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 3) {
            player.getActionSender().sendMessage("Use as ::givevotepoints <lt>amount> <lt>player name>");
            return;
        }
        if (!StringUtils.isNumeric(args[1])) {
            player.getActionSender().sendMessage("Use as ::givevotepoints <lt>amount> <lt>player name>");
            return;
        }
        int points = Integer.parseInt(args[1]);
        String playerName = toString(args, 2).toLowerCase();
        Player otherPlayer = Repository.getPlayerByName(playerName);
        if (otherPlayer == null) {
            player.getActionSender().sendMessage("No player found for \"" + playerName + "\".");
            return;
        }
        otherPlayer.getActionSender().sendMessage(
            "<col=8A0808>You have been rewarded " + points + " voting point" + (points > 1 ? "s" : "") + ".");
        otherPlayer.getActionSender().sendMessage(
            "<col=8A0808>Spend them by talking to <col=08088A>Honest Jimmy</col><col=8A0808> in <col=08088A>Edgeville</col><col=8A0808>.");
        otherPlayer.getSavedData().getGlobalData().increaseVotingPoints(points);
    }
}

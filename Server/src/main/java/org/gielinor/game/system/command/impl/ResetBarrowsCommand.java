package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.utilities.string.TextUtils;

/**
 * Resets a players barrows settings.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ResetBarrowsCommand extends Command {

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
        return new String[]{ "resetbarrows" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("resetbarrows", "Resets a player's Barrows minigame status",
            getRights(), "::resetbarrows <lt>player_name>"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::resetbarrows <lt>player_name>");
            return;
        }
        Player otherPlayer = Repository.getPlayerByName(toString(args, 1));
        if (otherPlayer == null) {
            player.getActionSender().sendMessage("Player \"" + toString(args, 1) + "\" could not be found.");
            return;
        }
        otherPlayer.getSavedData().getActivityData().resetBarrowsKillCount();
        otherPlayer.getSavedData().getActivityData().resetBarrowsKilled();
        otherPlayer.getSavedData().getActivityData().setBarrowsChestStatus(0);
        player.getActionSender().sendMessage(
            "Reset Barrows minigame for player " + TextUtils.formatDisplayName(otherPlayer.getName()) + ".");
        otherPlayer.getActionSender().sendMessage("Your Barrows minigame has been reset.");
    }
}

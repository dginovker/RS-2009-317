package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Unlocks a player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class UnlockPlayerCommand extends Command {

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
        return new String[]{ "unlock" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("unlock", "Unlocks a player's actions", getRights(),
            "::unlock <lt>player_name>"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::unlock <lt>player name>");
            return;
        }
        Player otherPlayer = Repository.getPlayerByName(toString(args, 1));
        if (otherPlayer == null) {
            player.getActionSender().sendMessage("Player \"" + toString(args, 1) + "\" could not be found.");
            return;
        }
        otherPlayer.lock();
        player.getActionSender().sendMessage(otherPlayer.getUsername() + " has been unlocked.");
    }
}

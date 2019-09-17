package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.utilities.misc.PlayerLoader;

/**
 * A command to compare IPs between two player accounts.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CompareIPsCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return false;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "compareips" };
    }

    @Override
    public void init() {
        // CommandDescription.add(new CommandDescription("compareips", "Compares
        // the IP addresses of two players", getRights(), "::compareips
        // <lt>player one>-<lt>player two><br>Example:<br>::compareips
        // Gielinor-iceage"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::compareips <lt>player one>-<lt>player two>");
            return;
        }
        String[] params = args[1].split("-");
        if (params.length < 2) {
            player.getActionSender().sendMessage("Use as ::compareips <lt>player one>-<lt>player two>");
            return;
        }
        String playerOne = params[0];
        String playerTwo = params[1];
        Player target = Repository.getPlayerByName(playerOne);
        if (target == null) {
            target = PlayerLoader.getPlayerFile(playerOne);
            if (target == null) {
                player.getActionSender().sendMessage("The player \"" + playerOne + "\" could not be found.");
                return;
            }
        }
        if (target == null) {
            player.getActionSender().sendMessage("The player \"" + playerOne + "\" could not be found.");
            return;
        }
        Player secondTarget = Repository.getPlayerByName(playerTwo);
        if (secondTarget == null) {
            secondTarget = PlayerLoader.getPlayerFile(playerTwo);
            if (secondTarget == null) {
                player.getActionSender().sendMessage("The player \"" + playerTwo + "\" could not be found.");
                return;
            }
        }
        if (secondTarget == null) {
            player.getActionSender().sendMessage("The player \"" + playerTwo + "\" could not be found.");
            return;
        }
        if (target.getDetails().getIp().equals(secondTarget.getDetails().getIp())) {
            player.getActionSender().sendMessage("Current IPs match.");
        }
        if (target.getDetails().getLastIp() != null && secondTarget.getDetails().getLastIp() != null) {
            if (target.getDetails().getLastIp().equals(secondTarget.getDetails().getLastIp())) {
                player.getActionSender().sendMessage("Last IPs match.");
            }
            if (target.getDetails().getLastIp().equals(secondTarget.getDetails().getIp())) {
                player.getActionSender().sendMessage("First players last IP matches second players current.");
            }
            if (secondTarget.getDetails().getLastIp().equals(target.getDetails().getIp())) {
                player.getActionSender().sendMessage("Second players last IP matches first players current.");
            }
        }
    }
}

package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.world.repository.Repository;

/**
 * Gives the given player Gielinor tokens.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GiveGielinorTokensCommand extends Command {

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
        return new String[]{ "givealutokens" };
    }

    @Override
    public void init() {
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 3) {
            player.getActionSender().sendMessage("Use as ::givealutokens <lt>amount> <lt>player name>");
            return;
        }
        if (!StringUtils.isNumeric(args[1])) {
            player.getActionSender().sendMessage("Use as ::givealutokens <lt>amount> <lt>player name>");
            return;
        }
        int tokens = Integer.parseInt(args[1]);
        String playerName = toString(args, 2).toLowerCase();
        Player otherPlayer = Repository.getPlayerByName(playerName);
        if (otherPlayer == null) {
            player.getActionSender().sendMessage("No player found for \"" + playerName + "\".");
            return;
        }
        otherPlayer.getActionSender()
            .sendMessage("<col=FFFFFF><shad=255>Thank you for donating! You have been credited " + tokens
                + " Gielinor token" + (tokens > 1 ? "s" : "") + "!");
        otherPlayer.getDonorManager().increaseGielinorTokens(tokens);
    }
}

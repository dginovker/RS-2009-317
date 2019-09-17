package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Claims a donation.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ClaimCommand extends Command {

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
        return new String[]{ "claim" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("claim", "Checks if you have donated recently,<br>and claims the reward.", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.getDonorManager().claim()) {
            player.getActionSender().sendMessage("You have not donated recently.");
        }
    }
}

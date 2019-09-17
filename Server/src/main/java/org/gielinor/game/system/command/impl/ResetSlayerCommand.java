package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Resets a players Slayer task.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ResetSlayerCommand extends Command {

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
        return new String[]{ "resetslayer" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("resetslayer", "Resets your Slayer task", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        player.getSlayer().clear();
        player.getActionSender().sendMessage("Your Slayer assignment has been cleared.");
    }
}

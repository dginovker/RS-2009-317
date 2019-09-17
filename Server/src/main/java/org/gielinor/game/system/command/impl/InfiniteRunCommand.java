package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * A command that gives an administrator infinite run energy.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class InfiniteRunCommand extends Command {

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
        return new String[]{ "infrun", "infiniterun" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("infrun", "Toggles infinite run on or off", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (player.getAttribute("INFINITE_RUN") != null) {
            player.removeAttribute("INFINITE_RUN");
            player.getActionSender().sendMessage("You no longer have infinite run.");
        } else {
            player.setAttribute("INFINITE_RUN", true);
            player.getActionSender().sendMessage("You now have infinite run.");
        }
    }
}

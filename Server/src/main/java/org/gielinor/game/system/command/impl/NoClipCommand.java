package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Toggles noclipping for the player server-side.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class NoClipCommand extends Command {

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
        return new String[]{ "noclip" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("noclip", "Toggles noclip on or off", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        player.setAttribute("noclip", !player.getAttribute("noclip", false));
    }
}

package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;

/**
 * Prints out buttons clicked.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ButtonsCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public void init() {
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "buttons" };
    }

    @Override
    public void execute(final Player player, String[] args) {
        player.removeAttribute("BUTTON_TYPE");
        if (args.length != 2) {
            return;
        }
        player.setAttribute("BUTTON_TYPE", args[1].toLowerCase());
    }
}

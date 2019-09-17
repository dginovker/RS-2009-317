package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * A command that gives an administrator infinite special.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class InfiniteSpecialCommand extends Command {

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
        return new String[]{ "spec", "infspec", "infinitespec", "tspec" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("infspec", "Toggles infinite special on or off", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("tspec")) {
            player.getSettings().toggleSpecialBar();
        }
        if (player.getAttribute("INF_SPEC") == null) {
            player.setAttribute("INF_SPEC", 1);
        } else {
            player.removeAttribute("INF_SPEC");
        }
        player.getActionSender().sendMessage("You" + (player.getAttribute("INF_SPEC") != null ? " now " : " no longer ")
            + "have infinite special attack.");
    }
}

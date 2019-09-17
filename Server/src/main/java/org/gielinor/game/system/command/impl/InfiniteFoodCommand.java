package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * A command that gives an administrator infinite food.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class InfiniteFoodCommand extends Command {

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
        return new String[]{ "inffood", "infinitefood" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("inffood", "Toggles infinite food on or off", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (player.getAttribute("INFINITE_FOOD") != null) {
            player.removeAttribute("INFINITE_FOOD");
            player.getActionSender().sendMessage("You no longer have infinite food.");
        } else {
            player.setAttribute("INFINITE_FOOD", true);
            player.getActionSender().sendMessage("You now have infinite food.");
        }
    }
}

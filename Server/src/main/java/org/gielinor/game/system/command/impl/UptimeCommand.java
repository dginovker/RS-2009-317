package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.World;

/**
 * Displays the uptime of the server.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class UptimeCommand extends Command {

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
        return new String[]{ "uptime" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("uptime", "Shows the server's current uptime", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        long days = (World.getTicks() / 86400);
        long hours = (World.getTicks() / 3600) - (days * 24);
        long minutes = (World.getTicks() / 60) - (days * 1440) - (hours * 60);
        long seconds = World.getTicks() - (days * 86400) - (hours * 3600) - (minutes * 60);
        player.getActionSender().sendMessage("The server has been online for " + days + " days, " + hours + " hours, " + minutes + " minutes, and " + seconds + " seconds.");
    }
}

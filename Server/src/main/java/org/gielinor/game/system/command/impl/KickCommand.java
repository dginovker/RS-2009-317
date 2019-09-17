package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Kicks a player from the server.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KickCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.PLAYER_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "kick" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("kick", "Kicks an online player from the server", getRights(),
            "::kick <lt>player_name>"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length < 2) {
            p.getActionSender().sendMessage("Use as ::kick <lt>player name>");
            return;
        }
        String other = toString(args, 1);
        Player kick = Repository.getPlayerByName(other);
        if (kick == null) {
            p.getActionSender().sendMessage("Player " + other + " is not online at the moment.");
            return;
        }
        if (kick.specialDetails() && !p.specialDetails()) {
            p.getActionSender().sendMessage("That doesn't seem appropriate!");
            return;
        }
        if (kick.getRights().ordinal() >= p.getRights().ordinal() && p.getRights() != Rights.GIELINOR_MODERATOR) {
            if (!kick.getUsername().equalsIgnoreCase(p.getUsername())) {
                p.getActionSender()
                    .sendMessage(kick.getUsername() + " is "
                        + ((kick.getRights().name().toLowerCase().length() > 0
                        && kick.getRights().name().toLowerCase().charAt(0) == 'a'
                        || kick.getRights().name().toLowerCase().length() > 0
                        && kick.getRights().name().toLowerCase().charAt(0) == 'o') ? "an" : "a")
                        + " " + kick.getRights().name().toLowerCase() + " and cannot be kicked.");
                return;
            }
        }
        kick.setAttribute("FORCE_KICK", true);
        kick.getActionSender().sendLogout();
        p.getActionSender().sendMessage(kick.getUsername() + " has been kicked from the server.");
        if (p.getRights().isAdministrator()) {
            kick.clear();
        }
    }
}

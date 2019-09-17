package org.gielinor.game.system.command.impl;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

public class TeleportToMeCommand extends Command {

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
        return new String[]{ "teletome", "telealltome", "alltome" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("teletome",
            "Sends a teleport request to the given<br>player name", getRights(), "::teletome <lt>player_name>"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("alltome") || args[0].equalsIgnoreCase("telealltome")) {
            int requests = 0;
            for (Player otherPlayer : Repository.getPlayers()) {
                if (!otherPlayer.isActive() || otherPlayer.getLocks().isTeleportLocked()) {
                    continue;
                }
                otherPlayer.setAttribute("t-o_location", player.getLocation());
                otherPlayer.getActionSender().sendString(player.getUsername(), 12558);
                otherPlayer.getActionSender().sendString("to them", 12560);
                otherPlayer.getInterfaceState().open(new Component(12468));
                requests++;
            }
            player.getActionSender().sendMessage("Sent " + requests + " teleport requests to players.");
            return;
        }
        if (args.length < 1) {
            player.getActionSender().sendMessage("Use as ::teletome <lt>player name>");
            return;
        }
        String other = toString(args, 1);
        for (Player otherPlayer : Repository.getPlayers()) {
            if (otherPlayer.getUsername().equalsIgnoreCase(other)) {
                if (!otherPlayer.isActive() || otherPlayer.getLocks().isTeleportLocked()) {
                    player.getActionSender().sendMessage("The other player is currently busy.");
                    continue;
                }
                player.getActionSender().sendMessage("Sending teleport request to " + otherPlayer.getUsername());
                otherPlayer.setAttribute("t-o_location", player.getLocation());
                otherPlayer.getActionSender().sendString(player.getUsername(), 12558);
                otherPlayer.getActionSender().sendString("to them", 12560);
                otherPlayer.getInterfaceState().open(new Component(12468));
                break;
            }
        }
    }
}

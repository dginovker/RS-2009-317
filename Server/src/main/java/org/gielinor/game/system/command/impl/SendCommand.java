package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.repository.Repository;

/**
 * Sends another player to given coords.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SendCommand extends Command {

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
        return new String[]{ "send" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("send", "Sends a player to given coordinates", getRights(),
            "::send <lt>player_name> <lt>x> <lt>y> <lt>z> - Sends a player to the coordinates typed<br>::send <lt>player_name> <lt>location> - Sends a player to the location chosen"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 3) {
            player.getActionSender().sendMessage(
                "Invalid usage; use as ::send <lt>player_name> <lt>location> or ::send <lt>player_name> <lt>x> <lt>y> <lt>z>");
            return;
        }
        String playerName = args[1].replaceAll("_", " ");
        Player otherPlayer = Repository.getPlayerByName(playerName);
        if (otherPlayer == null) {
            player.getActionSender().sendMessage("Cound not find player by name '" + playerName + "'.");
            return;
        }
        // To coordinates
        if (args.length >= 4 && StringUtils.isNumeric(args[2]) && StringUtils.isNumeric(args[3])) {
            int toX = Integer.parseInt(args[2]);
            int toY = Integer.parseInt(args[3]);
            int toZ = (args.length > 4 && StringUtils.isNumeric(args[4])) ? Integer.parseInt(args[4]) : 0;
            otherPlayer.setTeleportTarget(new Location(toX, toY, toZ));
            return;
        }
        // To location
        if (args.length == 3) {
            Location location = TeleportCommand.getDestination(toString(args, 2));
            if (location == null) {
                player.getActionSender().sendMessage("No teleport location found for '" + toString(args, 2) + "'.");
                return;
            }
            otherPlayer.setTeleportTarget(location);
        }
    }
}

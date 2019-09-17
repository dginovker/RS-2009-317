package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;

/**
 * Sets the player's coordinate / plane by the given argument.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GoCommand extends Command {

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
        return new String[]{ "go", "goup", "godown" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("go", "Teleports in the direction given", getRights(),
            "::go <lt>direction> <lt>[ distance]<br>Possible directions:<br>north<br>south<br>east<br>west<br>northeast<br>northwest<br>southeast<br>southwest"));
        CommandDescription.add(new CommandDescription("goup", "Raises height by 1", getRights(), null));
        CommandDescription.add(new CommandDescription("godown", "Decreases height by 1", getRights(), null));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender().sendMessage(
                "Use as ::go <lt>command> <lt>[distance]> - command can be north, east, up, down, etc");
            return;
        }
        String command = args[1].toLowerCase();
        int distance = args.length == 3 ? Integer.parseInt(args[2]) : 1;
        Location location = player.getLocation();
        switch (command) {
            case "north":
            case "n":
                location = location.getNorth(distance);
                break;
            case "south":
            case "s":
                location = location.getSouth(distance);
                break;
            case "east":
            case "e":
                location = location.getEast(distance);
                break;
            case "west":
            case "w":
                location = location.getWest(distance);
                break;
            case "northeast":
            case "ne":
                location = location.getNorthEast(distance);
                break;
            case "northwest":
            case "nw":
                location = location.getNorthWest(distance);
                break;
            case "southeast":
            case "se":
                location = location.getSouthEast(distance);
                break;
            case "southwest":
            case "sw":
                location = location.getSouthWest(distance);
                break;
            case "goup":
            case "up":
                location = location.getUp();
                break;
            case "godown":
            case "down":
                location = location.getDown();
                break;
        }
        if (location.equals(player.getLocation())) {
            player.getActionSender().sendMessage("Invalid command provided.");
            return;
        }
        player.setTeleportTarget(location);
    }
}

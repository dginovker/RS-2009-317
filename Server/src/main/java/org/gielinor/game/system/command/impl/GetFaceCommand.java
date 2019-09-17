package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Direction;

/**
 * Gets the ordinal for the face string.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GetFaceCommand extends Command {

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
        return new String[]{ "getface" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("getface", "Gets the integer for a direction string", getRights(),
            "::getface <lt>direction><br>Directions:<br>NORTH_WEST<br>NORTH<br>NORTH_EAST<br>WEST<br>EAST<br>SOUTH_WEST<br>SOUTH<br>SOUTH_EAST"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.getActionSender().sendMessage("Use as ::getface <lt>direction>");
            return;
        }
        String face = args[1];
        int ordinal = -100;
        for (Direction direction : Direction.values()) {
            if (direction.name().equalsIgnoreCase(face)) {
                ordinal = direction.ordinal();
            }
        }
        if (ordinal == -100) {
            player.getActionSender().sendMessage("Invalid face direction, possibles are:");
            int index = 0;
            String types = "";
            for (Direction direction : Direction.values()) {
                types += direction.name().toLowerCase() + ", ";
                if (index % 5 == 4) {
                    player.getActionSender().sendMessage(types);
                    types = "";
                }
                index += 1;
            }
            return;
        }
        player.getActionSender().sendMessage("Face for " + face + ": " + ordinal);
    }
}

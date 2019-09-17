package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;

/**
 * Sets a player's height.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class HeightCommand extends Command {

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
        return new String[]{ "height", "plane" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("height", "Sets your height on the world map", getRights(),
            "::height <lt>plane><br>Plane must be more than or equal to 0 and<br>less than 4"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args.length != 2) {
            player.getActionSender().sendMessage("Wrong syntax; use as ::" + args[0] + " <lt>plane>");
            return;
        }
        int plane = Integer.parseInt(args[1]);
        if (plane < 0 || plane > 3) {
            player.getActionSender().sendMessage("Wrong syntax; use as ::" + args[0] + " <lt>plane>");
            player.getActionSender().sendMessage("Where <lt>plane> must be 0, 1, 2, or 3.");
            return;
        }
        player.setTeleportTarget(Location.create(player.getLocation().getX(), player.getLocation().getY(), plane));
    }
}

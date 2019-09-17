package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;

/**
 * Teleports the player to Varrock wilderness.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PKCommand extends Command {

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
        return new String[]{ "pk" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("pk", "Teleports to the edge of Varrock wilderness", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        player.setTeleportTarget(Location.create(3243, 3515, 0));
    }
}

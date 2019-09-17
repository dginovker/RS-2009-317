package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;

/**
 * The <b>::shops</b> command.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ShopsCommand extends Command {

    /**
     * The player's shop location.
     */
    private final Location LOCATION = new Location(2835, 5098, 0);
    /**
     * The Iron Men player's shop location.
     */
    private final Location IRON_MEN_LOCATION = new Location(2860, 5090, 0);

    @Override
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("shops", "Teleports you to the shops location.", getRights(), null));
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "shops", "shopping" };
    }

    @Override
    public void execute(final Player player, String[] args) {
        player.getTeleporter().send(player.isIronman() ? IRON_MEN_LOCATION : LOCATION);
    }
}

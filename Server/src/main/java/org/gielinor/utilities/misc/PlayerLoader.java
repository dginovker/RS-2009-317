package org.gielinor.utilities.misc;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.PlayerDetails;
import org.gielinor.game.world.World;

/**
 * Represents a class that is used to load a player, or details of it.
 *
 * @author 'Vexia
 */
public final class PlayerLoader {

    /**
     * Method used to load the player file.
     *
     * @param name the name.
     * @return the player.
     */
    public static Player getPlayerFile(String name) {
        final PlayerDetails playerDetails = new PlayerDetails(name, "", null);
        playerDetails.parse();
        final Player player = new Player(playerDetails);
        World.getWorld().getAccountService().loadPlayer(player);
        return player;
    }

    /**
     * Method used to load the player details file.
     *
     * @param name the name.
     * @return the details
     */
    public static PlayerDetails getPlayerDetailFile(String name) {
        final PlayerDetails playerDetails = new PlayerDetails(name, "", null);
        playerDetails.parse();
        return playerDetails;
    }
}

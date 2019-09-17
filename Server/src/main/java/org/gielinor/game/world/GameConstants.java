package org.gielinor.game.world;

import org.gielinor.game.world.map.Location;

/**
 * A class holding constant fields that are constant with the server, un related to {@link ServerContext}.
 *
 * @author Emperor
 * @author 'Vexia
 */
public final class GameConstants {

    /**
     * The maximum ammount of players.
     */
    public static final int MAX_PLAYERS = (1 << 11) - 1;

    /**
     * The maximum ammount of NPCs.
     */
    public static final int MAX_NPCS = (1 << 15) - 1;

    /**
     * The {@code Location} start location of the player.
     */
    public static final Location START_LOCATION = new Location(2846, 5092, 0);

    /**
     * The {@code Location} home location.
     */
    public static final Location HOME_LOCATION = new Location(2848, 5101, 0);

    /**
     * If we are debugging.
     */
    public static boolean DEBUG = false;

    /**
     * Gets the path.
     *
     * @param path the path.
     * @return the path.
     */
    public static String getPath(String path) {
        return path;
    }
}

package org.gielinor.game.content.bot;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Handles {@link Bot} walking.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class Walking {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The {@link Method} instance.
     */
    private final Method method;

    /**
     * The locations for walking.
     */
    private Location[] locations;

    /**
     * The original locations.
     */
    private Location[] originalLocations;

    /**
     * Whether or not to end walking.
     */
    private boolean end;

    /**
     * Creates the {@link Bot} walking class.
     *
     * @param player The player.
     */
    public Walking(Player player) {
        this.player = player;
        this.method = new Method(player);
    }

    /**
     * Walks to the given location paths.
     *
     * @param locations The locations.
     */
    public void traverse(Location... locations) {
        this.locations = locations;
        Location next = getNext();
        if (next == null) {
            System.out.println("Next null");
            return;
        }
        System.out.println(next.toString());
        player.getPulseManager().run(new MovementPulse(player, next) {

            @Override
            public boolean pulse() {
                if (player.getWalkingQueue().isMoving()) {
                    return false;
                }
                if (player.getLocation().getDistance(next) < 3) {
                    traverse(locations);
                    return true;
                }
                return true;
            }
        }, "movement");
    }

    /**
     * Reverses this path.
     *
     * @return This path.
     */
    public Location[] reverse(Location[] locations) {
        Location[] reversed = new Location[locations.length];
        for (int i = 0; i < originalLocations.length; ++i) {
            reversed[i] = originalLocations[locations.length - 1 - i];
        }
        originalLocations = reversed;
        reversed = new Location[locations.length];
        for (int i = 0; i < locations.length; ++i) {
            reversed[i] = locations[locations.length - 1 - i];
        }
        locations = reversed;
        return locations;
    }

    /**
     * Gets the next location to walk to.
     *
     * @return The location.
     */
    public Location getNext() {
        for (int i = locations.length - 1; i >= 0; --i) {
            if (player.getLocation().getDistance(locations[i]) < 15) {
                return locations[i];
            }
        }
        return null;
    }

    /**
     * Gets the ending location.
     *
     * @return The ending location.
     */
    public Location getEnd() {
        return locations[locations.length - 1];
    }

}

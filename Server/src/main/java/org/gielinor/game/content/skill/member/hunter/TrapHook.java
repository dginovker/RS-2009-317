package org.gielinor.game.content.skill.member.hunter;

import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * A trap location hook.
 *
 * @author Vexia
 */
public class TrapHook {

    /**
     * The wrapper of the hook.
     */
    private final TrapWrapper wrapper;

    /**
     * The locations for the trap to trigger.
     */
    private final Location[] locations;

    /**
     * Constructs a new {@code TrapHook} {@code Object}.
     *
     * @param wrapper   the wrapper.
     * @param locations the locations.
     */
    public TrapHook(TrapWrapper wrapper, Location[] locations) {
        this.wrapper = wrapper;
        this.locations = locations;
    }

    /**
     * Gets a location by chance for the npc to go to.
     *
     * @return the location.
     */
    public Location getChanceLocation() {
        final double chance = wrapper.getChanceRate();
        final int roll = RandomUtil.random(99 - 15);//(wrapper.getPlayer().getDetails().getShop().hasPerk(Perks.PROWLER) ? 34 : 0));
        final double successChance = (World.getConfiguration().isDevelopmentEnabled() ? 100 : 55.0) + chance;
        if (successChance > roll) {
            return RandomUtil.getRandomElement(locations);
        }
        return null;
    }

    /**
     * Checks if the trap is hooked.
     *
     * @param location the location.
     * @return <code>True</code> if hooked.
     */
    public boolean isHooked(Location location) {
        for (Location l : locations) {
            if (l.equals(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the wrapper.
     *
     * @return The wrapper.
     */
    public TrapWrapper getWrapper() {
        return wrapper;
    }

    /**
     * Gets the locations.
     *
     * @return The locations.
     */
    public Location[] getLocations() {
        return locations;
    }

}

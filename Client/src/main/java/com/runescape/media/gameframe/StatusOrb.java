package com.runescape.media.gameframe;

import com.runescape.Game;
import com.runescape.cache.media.RSComponent;

/**
 * Handles status orbs.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class StatusOrb {

    /**
     * Gets the colour of the status text.
     *
     * @param level        The level.
     * @param maximumLevel The maximum level.
     * @return The colour.
     */
    public static int getColour(int level, int maximumLevel) {
        int percentage = (int) (((double) level / (double) maximumLevel) * 100D);
        return (percentage >= 75 ? 65280
                : percentage < 75 && percentage >= 50 ? 0xFFFF00
                : percentage < 50 && percentage >= 25 ? 0xFCA607 : 0xF50D0D);
    }

    /**
     * Gets the fill percentage of an orb.
     *
     * @param level        The level.
     * @param maximumLevel The maximum level.
     * @param height       The height of the sprite.
     * @return The percentage.
     */
    public static int getFillPercentage(int level, int maximumLevel, int height) {
        int percentage = (int) (((double) level / (double) maximumLevel) * 100D);
        return height - (int) ((double) height * ((double) percentage / 100D));
    }

    /**
     * Gets the level.
     *
     * @param rsComponent The {@link com.runescape.cache.media.RSComponent}.
     * @return The level.
     */
    public static int getLevel(RSComponent rsComponent) {
        return rsComponent.id == 149 ? Game.INSTANCE.getRunEnergy() : Game.INSTANCE.executeCS1Script(rsComponent, 0);
    }

    /**
     * Gets the maximum level.
     *
     * @param rsComponent The {@link com.runescape.cache.media.RSComponent}.
     * @return The level.
     */
    public static int getMaximumLevel(RSComponent rsComponent) {
        return rsComponent.id == 149 ? 100 : Game.INSTANCE.executeCS1Script(rsComponent, 1);
    }

    /**
     * Checks if an orb's status is flashing.
     *
     * @param level        The level.
     * @param maximumLevel The maximum level.
     * @return <code>True</code> if the status is below 25 percent.
     */
    public static boolean isFlashing(int level, int maximumLevel) {
        int percentage = (int) (((double) level / (double) maximumLevel) * 100D);
        return percentage < 25;
    }
}

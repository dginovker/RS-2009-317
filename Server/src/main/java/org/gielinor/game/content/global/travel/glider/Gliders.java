package org.gielinor.game.content.global.travel.glider;

import org.gielinor.game.world.map.Location;

/**
 * Represents an enum of glider locations.
 *
 * @author 'Vexia
 */
public enum Gliders {
    TA_QUIR_PRIW(825, Location.create(2465, 3501, 3), 9),
    SINDARPOS(826, Location.create(2848, 3497, 0), 1),
    LEMANTO_ADRA(827, Location.create(3321, 3427, 0), 10),
    KAR_HEWO(828, Location.create(3278, 3212, 0), 4),
    LEMANTOLLY_UNDRI(12342, Location.create(2544, 2970, 0), 3),
    GANDIUS(824, Location.create(2894, 2730, 0), 8);

    /**
     * The button of the location.
     */
    private final int button;

    /**
     * The location to fly to.
     */
    private final Location location;

    /**
     * the config value.
     */
    private final int config;

    /**
     * Constructs a new {@code Gliders.java} {@Code Object}
     *
     * @param button   the button.
     * @param location the location.
     */
    Gliders(int button, Location location, int config) {
        this.button = button;
        this.location = location;
        this.config = config;
    }

    /**
     * Gets the button.
     *
     * @return The button.
     */
    public int getButton() {
        return button;
    }

    /**
     * Gets the location.
     *
     * @return The location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the config.
     *
     * @return The config.
     */
    public int getConfig() {
        return config;
    }

    /**
     * Gets the flider value for the button id.
     *
     * @param id the id.
     * @return the value.
     */
    public static Gliders forId(int id) {
        for (Gliders i : Gliders.values()) {
            if (i.getButton() == id) {
                return i;
            }
        }
        return null;
    }

}

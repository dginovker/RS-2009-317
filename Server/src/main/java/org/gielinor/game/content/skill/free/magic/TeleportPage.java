package org.gielinor.game.content.skill.free.magic;

import org.gielinor.game.world.map.Location;

/**
 * Represents a teleport "page" for dialogues.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TeleportPage {

    /**
     * The teleport locations names.
     */
    private final String[] names;
    /**
     * The teleport location.
     */
    private final Location[] locations;
    /**
     * The warning messages (if any).
     */
    private String[] warningMessages;

    /**
     * Constructs a new {@code TeleportPage}.
     *
     * @param names     The teleport locations names.
     * @param locations The teleport locations.
     */
    public TeleportPage(String[] names, Location... locations) {
        this.names = names;
        this.locations = locations;
    }

    /**
     * Constructs a new {@code TeleportPage}.
     *
     * @param names     The teleport locations names.
     * @param locations The teleport locations.
     */
    public TeleportPage(String[] names, String[] warningMessages, Location... locations) {
        this.names = names;
        this.warningMessages = warningMessages;
        this.locations = locations;
    }

    /**
     * Gets the teleport locations names.
     *
     * @return The names.
     */
    public String[] getNames() {
        return names;
    }

    /**
     * Gets the teleport locations.
     *
     * @return The locations.
     */
    public Location[] getLocations() {
        return locations;
    }

    /**
     * Gets the warning messages.
     *
     * @return The warning messages.
     */
    public String[] getWarningMessages() {
        return warningMessages;
    }
}
package com.runescape.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a world.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */

/**
 * Represents a world's definition.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class WorldDefinition {

    /**
     * A {@link java.awt.List} of current world definitions.
     */
    private static final List<WorldDefinition> WORLD_LIST = new ArrayList<>();
    /**
     * The variable we're currently sorting by.
     */
    public static String SORT_VARIABLE = "WORLD";
    /**
     * The direction we're currently sorting by.
     */
    public static String SORT_DIRECTION = "DESC";
    /**
     * The id of this world.
     */
    private final int id;

    /**
     * The activity of this world.
     */
    private final String activity;

    /**
     * The country flag of this world.
     */
    private final int country;

    /**
     * The flag of this world.
     */
    private final int flag;

    /**
     * The address of this world.
     */
    private final String address;

    /**
     * The port of this world.
     */
    private final int port;

    /**
     * The region this world is in.
     */
    private final String region;

    /**
     * The player count of this world.
     */
    private int players;

    /**
     * @param id       The id of this world.
     * @param activity The activity of this world.
     * @param country  The country flag of this world.
     * @param flag     The flag of this world.
     * @param address  The address of this world.
     * @param port     The port of this world.
     * @param region   The region this world is in.
     */
    public WorldDefinition(int id, String activity, int country, int flag, String address, int port, String region) {
        this.id = id;
        this.activity = activity;
        this.country = country;
        this.flag = flag;
        this.address = address;
        this.port = port;
        this.region = region;
    }

    /**
     * Gets the {@link java.util.List} of the current world definitions.
     *
     * @return The list.
     */
    public static List<WorldDefinition> getWorldList() {
        return WORLD_LIST;
    }

    /**
     * Gets the <code>WorldDefinition</code> for the world id given.
     *
     * @param worldId The id of the world.
     * @return The world definition.
     */
    public static WorldDefinition forId(int worldId) {
        for (WorldDefinition worldDefinition : WORLD_LIST) {
            if (worldDefinition.getId() == worldId) {
                return worldDefinition;
            }
        }
        return WORLD_LIST.get(0);
    }

    /**
     * Adds a world definition.
     *
     * @param worldDefinition The world definition.
     */
    public static void addWorld(WorldDefinition worldDefinition) {
        WORLD_LIST.add(worldDefinition);
    }

    /**
     * Sorts the {@link #WORLD_LIST} by a value.
     */
    public static void sort(final String variable, final String direction) {
        Collections.sort(WORLD_LIST, new Comparator<WorldDefinition>() {
            public int compare(WorldDefinition worldDefinition, WorldDefinition worldDefinition1) {
                int value = 0;
                int value1 = 0;
                switch (variable) {
                    case "WORLD":
                        value = worldDefinition.getId();
                        value1 = worldDefinition1.getId();
                        break;
                    case "PLAYERS":
                        value = worldDefinition.getPlayers();
                        value1 = worldDefinition1.getPlayers();
                        break;
                    case "LOCATION":
                        value = worldDefinition.getCountry();
                        value1 = worldDefinition1.getCountry();
                        break;
                    case "TYPE":
                        value = worldDefinition.getFlag();
                        value1 = worldDefinition1.getFlag();
                        break;
                }
                if (direction.equalsIgnoreCase("ASC") && !SORT_DIRECTION.equalsIgnoreCase("ASC") && !isAscending(variable)) {
                    return value > value1 ? -1 : 1;
                }
                if (direction.equalsIgnoreCase("DESC") && !SORT_DIRECTION.equalsIgnoreCase("DESC") && !isDescending(variable)) {
                    return value > value1 ? 1 : -1;
                }
                return 0;
            }
        });
        SORT_VARIABLE = variable;
        SORT_DIRECTION = direction;
    }

    /**
     * Checks if the world list is being sorted ascending by a variable.
     *
     * @param variable The variable.
     * @return <code>True</code> if so.
     */
    public static boolean isAscending(String variable) {
        return SORT_VARIABLE.equalsIgnoreCase(variable) && SORT_DIRECTION.equalsIgnoreCase("ASC");
    }

    /**
     * Checks if the world list is being sorted descending by a variable.
     *
     * @param variable The variable.
     * @return <code>True</code> if so.
     */
    public static boolean isDescending(String variable) {
        return SORT_VARIABLE.equalsIgnoreCase(variable) && SORT_DIRECTION.equalsIgnoreCase("DESC");
    }

    /**
     * Gets the id of this world.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the activity of this world.
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Gets the country flag of this world.
     */
    public int getCountry() {
        return country;
    }

    /**
     * Gets the flag of this world.
     */
    public int getFlag() {
        return flag;
    }

    /**
     * Gets the address of this world.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the port of this world.
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the region this world is in.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets the player count of this world.
     *
     * @return The player count.
     */
    public int getPlayers() {
        return players;
    }

    /**
     * Sets the player count of this world.
     *
     * @param players The player count to set.
     */
    public void setPlayers(int players) {
        this.players = players;
    }
}

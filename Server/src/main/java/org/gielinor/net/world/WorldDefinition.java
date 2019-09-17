package org.gielinor.net.world;

import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.config.Constants;

/**
 * Represents a world's definition.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class WorldDefinition {

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
     * Gets the player count.
     *
     * @return The player count.
     */
    public int getPlayerCount() {
        if (id == Constants.WORLD_ID) {
            return Repository.getPlayers().size();
        }
        return players;
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

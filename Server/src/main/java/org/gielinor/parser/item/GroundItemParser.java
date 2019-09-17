package org.gielinor.parser.item;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.parser.Parser;
import org.gielinor.rs2.config.DatabaseDetails;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the class used to loadPlayer ground items.
 *
 * @author 'Vexia
 */
public final class GroundItemParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(GroundItemParser.class);

    /**
     * Represents the ground item parser singleton.
     */
    private static final GroundItemParser SINGLETON = new GroundItemParser();

    /**
     * Represents the list of ground item spawns.
     */
    private static final List<GroundSpawn> SPAWNS = new ArrayList<>();

    @Override
    public boolean parse() {
        log.info("Requesting ground item spawns...");
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM " + DatabaseDetails.getGameSingleton().GROUND_ITEM_TABLE)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int itemId = resultSet.getInt("item_id");
                    int itemCount = resultSet.getInt("item_count");
                    int respawnTicks = resultSet.getInt("respawn_ticks");
                    int x = resultSet.getInt("item_x");
                    int y = resultSet.getInt("item_y");
                    int z = resultSet.getShort("item_z");
                    GroundSpawn spawn = new GroundSpawn(respawnTicks, new Item(itemId, itemCount), new Location(x, y, z));
                    spawn.init();
                }
            }
        } catch (SQLException ex) {
            log.error("SQL exception whilst loading ground item spawns.", ex);
        } catch (IOException ex) {
            log.error("Failed to load ground item spawns.", ex);
        }
        log.info("Received and parsed {} ground item spawns.", SPAWNS.size());
        return true;
    }

    /**
     * Method used to add the spawn to the list.
     *
     * @param spawn
     *            the spawn.
     */
    public static void add(final GroundSpawn spawn) {
        spawn.init();
    }

    /**
     * Gets the ground item parser instance.
     *
     * @return the instance of this class.
     */
    public static GroundItemParser getInstance() {
        return SINGLETON;
    }

    /**
     * Gets the spawns.
     *
     * @return the spawns.
     */
    public List<GroundSpawn> getSpawns() {
        return SPAWNS;
    }

    /**
     * Represents a ground spawn item.
     *
     * @author 'Vexia
     */
    public static final class GroundSpawn extends GroundItem {

        /**
         * Represents the rate at which a ground item will respawn in ticks.
         */
        private int respawnRate;

        /**
         * Constructs a new {@code GroundItemParser} {@code Object}.
         *
         * @param respawnRate
         *            The respawn rate in ticks.
         * @param item
         *            The item.
         * @param location
         *            The location.
         */
        public GroundSpawn(final int respawnRate, Item item, Location location) {
            super(item, location);
            this.respawnRate = respawnRate;
        }

        /**
         * Method used to save this ground item to a byte buffer.
         *
         * @param buffer
         *            the buffer.
         */
        public final void save(final ByteBuffer buffer) {
            buffer.putInt(respawnRate);
            buffer.putShort((short) getId());
            buffer.putInt(getCount());
            buffer.putShort((short) (getLocation().getX() & 0xFFFF)).putShort((short) (getLocation().getY() & 0xFFFF))
                .put((byte) getLocation().getZ());
        }

        /**
         * Method used to initialize this spawn.
         */
        public final void init() {
            GroundItemManager.create(this);
            SPAWNS.add(this);
        }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public boolean isPrivate() {
            return false;
        }

        @Override
        public boolean isAutoSpawn() {
            return true;
        }

        /**
         * Method used to start the respawn pulse of the ground item.
         */
        public final void respawn() {
            World.submit(new Pulse(getRespawnRate()) {

                @Override
                public boolean pulse() {
                    GroundItemManager.create(GroundSpawn.this);
                    return true;
                }
            });
        }

        /**
         * Gets the respawn rate in ticks.
         *
         * @return The rate.
         */
        public int getRespawnRate() {
            return respawnRate;
        }
    }
}

package org.gielinor.parser.object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.gielinor.database.DataSource;
import org.gielinor.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an in-game world door definition.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class DoorDefinition implements Parser {

    private static final Logger log = LoggerFactory.getLogger(DoorDefinition.class);

    /**
     * The doors mapping.
     */
    public static Map<Integer, Door> DEFINITIONS = new HashMap<>();

    @Override
    public boolean parse() throws Throwable {
        log.info("Requesting door definitions...");
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM door_definition")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Door door = new Door(resultSet.getInt("object_id"));
                    door.replaceId = resultSet.getInt("replace_id");
                    door.fence = resultSet.getBoolean("fence");
                    DEFINITIONS.put(door.getId(), door);
                }
            }
        } catch (SQLException ex) {
            log.error("Error while requesting door definitions.", ex);
        }
        log.info("Received and parsed {} door definitions.", DEFINITIONS.size());
        return true;
    }

    /**
     * Gets the door for the given object id.
     *
     * @param id
     *            The object id.
     * @return The door.
     */
    public static Door forId(int id) {
        return DEFINITIONS.get(id);
    }

    /**
     * Gets the doors.
     *
     * @return The doors.
     */
    public static Map<Integer, Door> getDefinitions() {
        return DEFINITIONS;
    }

    /**
     * Represents a door.
     *
     * @author Emperor
     */
    public static class Door {

        /**
         * The door's object id.
         */
        private final int id;

        /**
         * The door's replace object id.
         */
        public int replaceId;

        /**
         * If the door is closed.
         */
        public boolean fence;

        /**
         * If the player should automaticly walk through it.
         */
        private boolean autoWalk;

        /**
         * Constructs a new {@code DoorManager} {@code Object}.
         */
        public Door(int id) {
            this.id = id;
        }

        /**
         * Gets the id.
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the replaceId.
         *
         * @return The replaceId.
         */
        public int getReplaceId() {
            return replaceId;
        }

        /**
         * Sets the replaceId.
         *
         * @param replaceId
         *            The replaceId to set.
         */
        public void setReplaceId(int replaceId) {
            this.replaceId = replaceId;
        }

        /**
         * Gets the autoWalk.
         *
         * @return The autoWalk.
         */
        public boolean isAutoWalk() {
            return autoWalk;
        }

        /**
         * Sets the autoWalk.
         *
         * @param autoWalk
         *            The autoWalk to set.
         */
        public void setAutoWalk(boolean autoWalk) {
            this.autoWalk = autoWalk;
        }

        /**
         * Gets the fence.
         *
         * @return The fence.
         */
        public boolean isFence() {
            return fence;
        }

        /**
         * Sets the fence.
         *
         * @param fence
         *            The fence to set.
         */
        public void setFence(boolean fence) {
            this.fence = fence;
        }

    }

}

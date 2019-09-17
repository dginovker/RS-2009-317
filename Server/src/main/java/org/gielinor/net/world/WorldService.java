package org.gielinor.net.world;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.config.DatabaseDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the world service.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class WorldService {

    private static final Logger log = LoggerFactory.getLogger(WorldService.class);

    /**
     * Constructs a new {@link org.gielinor.net.world.WorldService} for this
     * world.
     */
    public WorldService() {
    }

    /**
     * Initializes the worlds list.
     */
    public void initializeWorlds() {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM " + DatabaseDetails.getGameSingleton().dbname + ".world;")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int worldId = resultSet.getByte("id");
                    String activity = resultSet.getString("activity");
                    int country = resultSet.getByte("country");
                    int flag = resultSet.getByte("flag");
                    String address = resultSet.getString("address");
                    int port = resultSet.getInt("port");
                    String region = resultSet.getString("region");
                    WorldList.addWorld(new WorldDefinition(worldId, activity, country, flag, address, port, region));
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Failed to load world information.", ex);
        }
        WorldList.flagUpdate();
    }

    /**
     * Sets the player count for this world.
     */
    public void updatePlayers() {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE " + DatabaseDetails.getGameSingleton().dbname + ".world SET players=? WHERE id=?")) {
                preparedStatement.setInt(1, Repository.getPlayers().size());
                preparedStatement.setInt(2, Constants.WORLD_ID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception whilst attempting to update playercount.", ex);
        }
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT id, players FROM " + DatabaseDetails.getGameSingleton().dbname + ".world;")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    WorldDefinition worldDefinition = WorldList.forId(resultSet.getInt("id"));
                    if (worldDefinition != null) {
                        worldDefinition.setPlayers(resultSet.getInt("players"));
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception whilst attempting to fetch world player counts.", ex);
        }
        WorldList.flagUpdate();
    }

}

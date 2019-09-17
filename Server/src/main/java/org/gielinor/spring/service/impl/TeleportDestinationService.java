package org.gielinor.spring.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.gielinor.database.DataSource;
import org.gielinor.game.system.command.impl.TeleportCommand;
import org.gielinor.game.world.map.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Represents the teleport destination service.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
@Service("teleportDestinationService")
public class TeleportDestinationService {

    private static final Logger log = LoggerFactory.getLogger(TeleportDestinationService.class);

    /**
     * Initializes the teleport destination service.
     */
    public void init() {
        HashMap<String, Location> destinations = new HashMap<>();
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM teleport_destination;")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    destinations.put(resultSet.getString("name").toLowerCase().replaceAll(" ", "_"),
                        new Location(resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getInt("z")));
                }
            }
        } catch (IOException | SQLException ex) {
            log.error("Unable to load teleportation destinations.", ex);
        }
        log.info("Loaded {} teleportation destinations.", destinations.size());
        TeleportCommand.setDestinations(destinations);
    }

}

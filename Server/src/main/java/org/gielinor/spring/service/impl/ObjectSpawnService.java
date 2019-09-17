package org.gielinor.spring.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Region;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.RegionPlane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
@Service("objectSpawnService")
public class ObjectSpawnService {

    private static final Logger log = LoggerFactory.getLogger(ObjectSpawnService.class);

    /**
     * Initializes world objects, deleting from coordinates and spawning new
     * ones.
     */
    public void initializeWorldObjects() {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM object_delete")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int key = resultSet.getInt("id");
                    int objectId = resultSet.getInt("object_id");
                    int x = (int) resultSet.getShort("x");
                    int y = (int) resultSet.getShort("y");
                    int z = (int) resultSet.getShort("z");
                    if (objectId == 0) {
                        Location removeLocation = Location.create(x, y, z);
                        RegionPlane regionPlane = RegionManager.getRegionPlane(removeLocation);
                        Region.load(regionPlane.getRegion());
                        int localX = removeLocation.getLocation().getLocalX();
                        int localY = removeLocation.getLocation().getLocalY();
                        regionPlane.getFlags().unflagSolidObject(localX, localY, 1, 1, false);
                        regionPlane.getProjectileFlags().unflagSolidObject(localX, localY, 1, 1, false);
                        regionPlane.getFlags().unflagTileObject(localX, localY);
                        regionPlane.getProjectileFlags().unflagTileObject(localX, localY);
                        regionPlane.getFlags().unflagDoorObject(localX, localY, 0, 0, false);
                        regionPlane.getProjectileFlags().unflagDoorObject(localX, localY, 0, 0, false);
                        regionPlane.remove(localX, localY);
                        continue;
                    }
                    GameObject gameObject = RegionManager.getDeleteObject(x, y, z);
                    if (gameObject == null) {
                        continue;
                    }
                    RegionManager.REMOVED_OBJECTS.put(key, gameObject);
                    ObjectBuilder.remove(gameObject);
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM object_spawn")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int key = resultSet.getInt("id");
                    int objectId = resultSet.getInt("object_id");
                    int x = (int) resultSet.getShort("x");
                    int y = (int) resultSet.getShort("y");
                    int z = (int) resultSet.getShort("z");
                    int face = resultSet.getInt("face");
                    int type = resultSet.getInt("type");
                    ObjectBuilder.add(new GameObject(objectId, x, y, z, type, face));
                }
            }
        } catch (IOException | SQLException ex) {
            log.error("Failed to initialize world objects.", ex);
        }
    }

    /**
     * Inserts a spawned object into the database.
     *
     * @param gameObject
     *            The {@link org.gielinor.game.node.object.GameObject}.
     */
    public boolean insertSpawnedObject(GameObject gameObject) {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO object_spawn (object_id, x, y, z, face, type) VALUES(?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, gameObject.getId());
                preparedStatement.setInt(2, gameObject.getLocation().getX());
                preparedStatement.setInt(3, gameObject.getLocation().getY());
                preparedStatement.setInt(4, gameObject.getLocation().getZ());
                preparedStatement.setInt(5, gameObject.getRotation());
                preparedStatement.setInt(6, gameObject.getType());
                preparedStatement.execute();
            }
        } catch (IOException | SQLException ex) {
            log.error("Failed to insert spawned object - {}.", gameObject, ex);
            return false;
        }
        return true;
    }

    /**
     * Deletes a spawned object into the database.
     *
     * @param objectInfo
     *            The game object info.
     */
    public boolean deleteSpawnedObject(Player player, int[] objectInfo) {
        boolean deleted;
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM object_spawn WHERE object_id = ? AND x = ? AND y = ?")) {
                preparedStatement.setInt(1, objectInfo[0]);
                preparedStatement.setInt(2, objectInfo[1]);
                preparedStatement.setInt(3, objectInfo[2]);
                deleted = preparedStatement.executeUpdate() > 0;
            }
            if (!deleted) {
                try (PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO object_delete (object_id, x, y, z) VALUES (?, ?, ?, ?)")) {
                    preparedStatement.setInt(1, objectInfo[0]);
                    preparedStatement.setInt(2, objectInfo[1]);
                    preparedStatement.setInt(3, objectInfo[2]);
                    preparedStatement.setInt(4, player.getLocation().getZ());
                    preparedStatement.execute();
                }
            }
        } catch (IOException | SQLException ex) {
            log.error("Failed to delete spawned object - {}.", Arrays.toString(objectInfo), ex);
            return false;
        }
        return true;
    }

}

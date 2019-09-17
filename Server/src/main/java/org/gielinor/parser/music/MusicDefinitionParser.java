package org.gielinor.parser.music;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.link.music.MusicEntry;
import org.gielinor.game.node.entity.player.link.music.MusicZone;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the {@link org.gielinor.parser.Parser} for
 * {@link org.gielinor.game.node.entity.player.link.music.MusicEntry}
 * definitions.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class MusicDefinitionParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(MusicDefinitionParser.class);

    @Override
    public boolean parse() {
        // TODO use join
        log.info("Requesting music definitions...");

        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM music")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int musicId = resultSet.getInt("music_id");
                        String name = resultSet.getString("name");
                        int buttonId = resultSet.getInt("button_id");
                        int index = resultSet.getInt("music_index");

                        if (!MusicEntry.getSongs().containsKey(musicId)) {
                            MusicEntry.getSongs().put(musicId, new MusicEntry(musicId, name, buttonId, index));
                        }

                        int southWestX = resultSet.getInt("southwest_x");
                        int southWestY = resultSet.getInt("southwest_y");
                        int northEastX = resultSet.getInt("northeast_x");
                        int northEastY = resultSet.getInt("northeast_y");
                        ZoneBorders zoneBorders = new ZoneBorders(southWestX, southWestY, northEastX, northEastY);
                        MusicZone musicZone = new MusicZone(musicId, zoneBorders);

                        for (int id : zoneBorders.getRegionIds()) {
                            RegionManager.forId(id).getMusicZones().add(musicZone);
                        }
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Failed whilst requesting music definitions.", ex);
            return false;
        }

        log.info("Received and parsed {} music definitions.", MusicEntry.getSongs().size());
        return true;
    }

}

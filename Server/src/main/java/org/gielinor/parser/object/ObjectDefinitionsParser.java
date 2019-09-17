package org.gielinor.parser.object;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.database.DataSource;
import org.gielinor.parser.Parser;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The object definitions parser.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ObjectDefinitionsParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(ObjectDefinitionsParser.class);

    @Override
    public boolean parse() {
        log.info("Requesting object definitions...");
        try (Connection connection = DataSource.getGameConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM object_definition");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ObjectDefinition objectDefinition = new ObjectDefinition();
                objectDefinition.setId(resultSet.getInt("id"));
                objectDefinition.setModelIds(TextUtils.explode(resultSet.getString("model_ids"), ":"));
                objectDefinition.setModelConfiguration(TextUtils.explode(resultSet.getString("model_config"), ":"));
                objectDefinition.setName(resultSet.getString("name"));
                objectDefinition.setExamine(resultSet.getString("examine"));
                objectDefinition.setOptions(resultSet.getString("options").split(":"));
                objectDefinition.setSizeX(resultSet.getInt("size_x"));
                objectDefinition.setSizeY(resultSet.getInt("size_y"));
                objectDefinition.setProjectileClipped(resultSet.getBoolean("projectile_clipped"));
                objectDefinition.setClipType(resultSet.getInt("clip_type"));
                objectDefinition.setIsInteractive(resultSet.getInt("interactive"));
                objectDefinition.setAnimationId(resultSet.getInt("animation_id"));
                objectDefinition.setMapIcon(resultSet.getInt("mapicon"));
                objectDefinition.setScaleX(resultSet.getInt("scale_x"));
                objectDefinition.setScaleY(resultSet.getInt("scale_y"));
                objectDefinition.setScaleX(resultSet.getInt("scale_z"));
                objectDefinition.setWalkingFlag(resultSet.getInt("walking_flag"));
                objectDefinition.setSecondBool(resultSet.getBoolean("secondbool"));
                objectDefinition.setNotClipped(resultSet.getBoolean("not_clipped"));
                objectDefinition.setAnInt3855(resultSet.getInt("anint3855"));
                objectDefinition.setConfigFileId(resultSet.getInt("config_file_id"));
                objectDefinition.setConfigId(resultSet.getInt("config_id"));
                objectDefinition.setChildrenIds(TextUtils.explode(resultSet.getString("children_ids"), ":"));
                objectDefinition.setMembersOnly(resultSet.getBoolean("members_only"));
                if (objectDefinition.notClipped) {
                    objectDefinition.clipType = 0;
                    objectDefinition.projectileClipped = false;
                }
                ObjectDefinition.getDefinitions().put(resultSet.getInt("id"), objectDefinition);
            }
        } catch (SQLException | IOException ex) {
            log.error("Exception while loading object definitions.", ex);
            return false;
        }
        log.info("Received and parsed {} object definitions.", ObjectDefinition.getDefinitions().size());
        return true;
    }

}

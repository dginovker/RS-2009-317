package org.gielinor.parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.gielinor.cache.def.impl.CS2Mapping;
import org.gielinor.database.DataSource;

/**
 * The CS2 definition parser.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class CS2DefinitionsParser implements Parser {

    @Override
    public boolean parse() throws Throwable {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cs2_definition")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        CS2Mapping cs2Mapping = CS2Mapping.forId(resultSet.getInt("script_id"));
                        if (cs2Mapping == null) {
                            cs2Mapping = new CS2Mapping(resultSet.getInt("script_id"));
                        }
                        cs2Mapping.setUnknown(resultSet.getInt("unknown"));
                        cs2Mapping.setUnknown1(resultSet.getInt("unknown"));
                        cs2Mapping.setDefaultString(resultSet.getString("default_string"));
                        cs2Mapping.setDefaultInt(resultSet.getInt("default_int"));
                        cs2Mapping.setMap(new HashMap<>());
                        CS2Mapping.add(resultSet.getInt("script_id"), cs2Mapping);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM cs2_map_definition")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        CS2Mapping cs2Mapping = CS2Mapping.forId(resultSet.getInt("script_id"));
                        cs2Mapping.getMap().put(resultSet.getInt("map_key"),
                            (resultSet.getBoolean("string") ? resultSet.getString("data")
                                : Integer.parseInt(resultSet.getString("data"))));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}

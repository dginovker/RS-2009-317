package org.gielinor.parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.gielinor.cache.def.impl.ConfigFileDefinition;
import org.gielinor.database.DataSource;

/**
 * The config file definition parser.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ConfigFileDefinitionsParser implements Parser {

    @Override
    public boolean parse() throws Throwable {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM config_file_definition")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        ConfigFileDefinition configFileDefinition = new ConfigFileDefinition(resultSet.getInt("id"));
                        configFileDefinition.setConfigId(resultSet.getInt("config_id"));
                        configFileDefinition.setBitShift(resultSet.getInt("shift"));
                        configFileDefinition.setBitSize(resultSet.getInt("size"));
                        ConfigFileDefinition.getMapping().put(resultSet.getInt("id"), configFileDefinition);
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

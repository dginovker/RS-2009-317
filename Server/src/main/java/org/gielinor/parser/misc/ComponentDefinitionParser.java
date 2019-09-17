package org.gielinor.parser.misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentType;
import org.gielinor.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses component definitions.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ComponentDefinitionParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(ComponentDefinitionParser.class);

    @Override
    public boolean parse() throws Throwable {
        log.info("Requesting component definitions...");
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM component_definition")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    ComponentDefinition componentDefinition = new ComponentDefinition();
                    // component_id, parent_id, tab_index, walkable, `type`,
                    // actions, message, tooltip)
                    componentDefinition.setId(resultSet.getInt("component_id"));
                    componentDefinition.setParentId(resultSet.getInt("parent_id"));
                    componentDefinition.setTabIndex(resultSet.getByte("tab_index"));
                    componentDefinition.setWalkable(resultSet.getBoolean("walkable"));
                    componentDefinition.setComponentType(ComponentType.valueOf(resultSet.getString("type")));
                    if (!resultSet.getString("actions").equals("NO_VALUE")) {
                        componentDefinition.setActions(resultSet.getString("actions").split(":"));
                    }
                    componentDefinition.setMessage(resultSet.getString("message"));
                    componentDefinition.setTooltip(resultSet.getString("tooltip"));
                    ComponentDefinition.getDefinitions().put(componentDefinition.getId(), componentDefinition);
                }
            }
        } catch (SQLException ex) {
            log.error("SQL exception whilst loading component definitions.", ex);
        }
        log.info("Received and parsed {} component definitions.", ComponentDefinition.getDefinitions().size());
        return true;
    }

}

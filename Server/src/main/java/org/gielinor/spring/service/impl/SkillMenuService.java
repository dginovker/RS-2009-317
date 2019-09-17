package org.gielinor.spring.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.content.skill.SkillMenu;
import org.gielinor.game.content.skill.menu.SkillMenuDefinition;
import org.gielinor.game.world.callback.CallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the {@link org.gielinor.game.content.skill.SkillMenu} definition
 * service.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SkillMenuService implements CallBack {

    private static final Logger log = LoggerFactory.getLogger(SkillMenuService.class);

    @Override
    public boolean call() {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM skill_menu_subsection LEFT JOIN skill_menu ON skill_menu_subsection.skill_id = skill_menu.skill_id ORDER BY skill_menu.ordinal ASC;")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    SkillMenu skillMenu = SkillMenu.forId(resultSet.getInt("skill_id"));
                    if (skillMenu == null) {
                        skillMenu = new SkillMenu(resultSet.getInt("skill_id"),
                            resultSet.getString("subsections").split(":"));
                        SkillMenu.getSkillMenus().add(skillMenu);
                    }
                    skillMenu.getSkillMenuDefinitions()
                        .add(new SkillMenuDefinition(resultSet.getInt("skill_id"), resultSet.getInt("subsection"),
                            resultSet.getInt("level"), resultSet.getInt("item_id"),
                            resultSet.getString("message"), resultSet.getBoolean("full_text")));
                }
            }
        } catch (IOException | SQLException ex) {
            log.error("Failed to load skill menu.", ex);
            return false;
        }
        return true;
    }

}

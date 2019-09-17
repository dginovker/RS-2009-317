package org.gielinor.game.node.entity.player.requirement.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.requirement.Requirement;

/**
 * @author David Insley
 */
public class SkillRequirement extends Requirement {

    private final int skill;
    private final int level;
    private final String error;
    private final boolean current;

    public SkillRequirement(int skill, int level, boolean current, String concat) {
        this.skill = skill;
        this.level = level;
        this.current = current;
        error = "You need level " + level + " " + StringUtils.capitalize(Skills.SKILL_NAME[skill]) + " to " + concat + ".";
    }

    @Override
    public boolean hasRequirement(Player player) {
        if (current) {
            return player.getSkills().getLevel(skill) >= level;
        } else {
            return player.getSkills().getStaticLevel(skill) >= level;
        }
    }

    @Override
    public void displayErrorMessage(Player player) {
        player.getActionSender().sendMessage(error);

    }

    @Override
    public void fulfill(Player player) {
    }

}

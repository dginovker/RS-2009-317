package org.gielinor.game.system.command.impl;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Sets all of the player's skill levels to 99.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class MaxStatsCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public boolean isBeta() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "max", "master", "unmax", "max2" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("max", "Maxes out all statistics", getRights(), null));
        CommandDescription.add(new CommandDescription("unmax", "Sets all statistics to level 1", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("unmax")) {
            player.getSavedData().getGlobalData().setMilestone(0, false);
            player.getSavedData().getGlobalData().setMilestone(1, false);
            player.getSavedData().getGlobalData().setMilestone(2, false);
            player.getSavedData().getGlobalData().setMilestone(3, false);
            player.getSavedData().getGlobalData().setMilestone(4, false);
            player.getSavedData().getGlobalData().setMilestone(5, false);
            player.getSavedData().getGlobalData().setMilestone(6, false);
            player.getSavedData().getGlobalData().setMilestone(7, false);
            player.getSavedData().getGlobalData().setMilestone(8, false);
            player.getSavedData().getGlobalData().setMilestone(9, false);

            for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
                player.getSkills().setLevel(i, 1);
                player.getSkills().setStaticLevel(i, 1);
                player.getSavedData().getGlobalData().setExperienceMilestone(i, false);
            }
            return;
        }
        for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
            if (!player.specialDetails()) {
                if (!Skills.ENABLED_SKILLS[i]) {
                    continue;
                }
            }
            player.getSkills().setLevel(i, 99);
            player.getSkills().setStaticLevel(i, 99);
        }
    }
}

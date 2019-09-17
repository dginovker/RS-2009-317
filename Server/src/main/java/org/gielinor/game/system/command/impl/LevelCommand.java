package org.gielinor.game.system.command.impl;

import java.util.Arrays;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Sets a player's skill level
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class LevelCommand extends Command {

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
        return new String[]{ "setlvl", "setlevel", "level", "lvl", "levels", "givexp", "giveexp" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("level", "Sets a level by name", getRights(),
            "::level <lt>name> <lt>level><br>Example:<br>::level attack 50"));
        CommandDescription.add(new CommandDescription("givexp", "Gives experience in a skill by name", getRights(),
            "::givexp <lt>name> <lt>experience><br>Example:<br>::givexp magic 500000"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("levels")) {
            for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
                double experience = player.getSkills().getExperienceForLevel(player.getSkills().getLevel(i) + 10);
                player.getSkills().addExperienceNoMod(i, experience);
            }
            return;
        }
        try {
            if (args.length > 3) {
                boolean xp = args[0].equals("givexp");
                int skillId = Arrays.asList(Skills.SKILL_NAME)
                    .indexOf(args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase());
                int level = Integer.parseInt(args[2]);
                String username = toString(args, 3);
                Player otherPlayer = Repository.getPlayerByName(username);
                if (otherPlayer == null) {
                    player.getActionSender()
                        .sendMessage("No player cound be found by the name of \"" + username + "\".");
                    return;
                }
                if (level >= 101 && player.getRights() != Rights.GIELINOR_MODERATOR) {
                    level = 99;
                }
                if (xp) {
                    otherPlayer.getSkills().addExperienceNoMod(skillId, level);
                } else {
                    otherPlayer.getSkills().setSkill(skillId, level,
                        otherPlayer.getSkills().getExperienceForLevel(level));
                }
                otherPlayer.getSkills().updateCombatLevel();
                otherPlayer.getSkills().refresh();
                otherPlayer.getAppearance().sync();
                return;
            }
            if (args.length == 3) {
                int skillId = Arrays.asList(Skills.SKILL_NAME)
                    .indexOf(args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase());
                if (args[0].equalsIgnoreCase("givexp") || args[0].equalsIgnoreCase("giveexp")) {
                    double experience = Double.parseDouble(args[2]);
                    int level = player.getSkills().getLevelByExperience(experience);
                    if (level >= 101 && player.getRights() != Rights.GIELINOR_MODERATOR) {
                        experience = player.getSkills().getExperienceForLevel(99);
                    }
                    player.getSkills().addExperienceNoMod(skillId, experience);
                } else {
                    int level = Integer.parseInt(args[2]);
                    if (level >= 101 && player.getRights() != Rights.GIELINOR_MODERATOR) {
                        level = 99;
                    }
                    player.getSkills().setSkill(skillId, level, player.getSkills().getExperienceForLevel(level));
                }
                player.getSkills().updateCombatLevel();
                player.getSkills().refresh();
                player.getAppearance().sync();
            } else {
                player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>skill name> <lt>level>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>skill name> <lt>level>");
        }
    }
}

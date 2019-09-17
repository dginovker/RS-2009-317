package org.gielinor.game.system.command.impl;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Finishes the player's achievements.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class FinishAchievementsCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "achievements", "achs", "resetachievements", "resetachs" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("achievements", "Completes all achievement diaries", getRights(), null));
        CommandDescription
            .add(new CommandDescription("resetachievements", "Resets all achievement diaries", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args == null || args[0].equalsIgnoreCase("resetachievements") || args[0].equalsIgnoreCase("resetachs")) {
            player.getAchievementRepository().getAchievements().clear();
            player.getAchievementRepository().getFinishedAchievements().clear();
            player.getAchievementRepository().update(player);
            return;
        }
        for (AchievementTask achievementTask : AchievementTask.values()) {
            if (achievementTask.getAmount() > 0) {
                player.getAchievementRepository().getAchievements().put(achievementTask, achievementTask.getAmount());
            }
            AchievementDiary.finalize(player, achievementTask);
        }
    }

}

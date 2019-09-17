package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import java.util.concurrent.TimeUnit;

/**
 * @author Erik
 */
@SuppressWarnings("unused") // Loaded via reflection
public class PlaytimeCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "playtime" };
    }

    @Override
    public void execute(Player player, String[] params) {
        if (params.length >= 2 && (player.getRights().isAdministrator() || player.getRights().isDeveloper())) {
            if ("reset".equalsIgnoreCase(params[1])) {
                player.getSavedData().getGlobalData().setPlayTime(0);
                player.getActionSender().sendConsoleMessage("Your cached playtime has been reset. Your session playtime remains.");
            }
        }

        long seconds = player.getSavedData().getGlobalData().getTotalPlayTime();
        String formatted = format(seconds);
        if (formatted == null) {
            player.getActionSender().sendMessage("Checking playtime already? You just joined us! Welcome!");
        } else {
            player.getActionSender().sendMessage("You've played for " + formatted + ".");
        }
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("playtime", "See how long you've played for!", getRights(), null));
    }

    private String format(long duration) {
        StringBuilder builder = new StringBuilder(128);
        long days = TimeUnit.SECONDS.toDays(duration);
        long hours = TimeUnit.SECONDS.toHours(duration) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(duration) % 60;

        if ((days | hours | minutes) == 0L) {
            return null;
        }

        if (days > 0) {
            builder.append(days);
            if (days > 1) builder.append(" days, ");
            else builder.append(" day, ");
        }

        if (hours > 0 || days > 0) {
            builder.append(hours);
            if (hours > 1) builder.append(" hours ");
            else builder.append(" hour ");
        }

        if ((days | hours) != 0L) {
            builder.append("and ");
        }

        if (minutes > 0 || hours > 0 || days > 0) {
            builder.append(minutes);
            if (minutes > 1) builder.append(" minutes");
            else builder.append(" minute");
        }

        return builder.toString();
    }

}

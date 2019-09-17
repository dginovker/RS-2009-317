package bot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.gielinor.ServerLauncher;

/**
 * Created by Corey on 14/06/2017.
 */
public class UptimeCommand implements CommandExecutor {

    public UptimeCommand() {
    }

    @Command(aliases = { "::uptime" }, description = "How long the server has been online")
    public String onUptimeCommand() {
        long startTime = ServerLauncher.START_EPOCH_MILLIS;
        long currentTime = System.currentTimeMillis();

        long uptime = (currentTime - startTime) / 1000;

        return "The server has been online for: ```xml\n" + getUptime(uptime) + "\n```";
    }

    public String getUptime(long time) {
        long DAY = (time / 86400);
        long HR = (time / 3600) - (DAY * 24);
        long MIN = (time / 60) - (DAY * 1440) - (HR * 60);
        long SEC = time - (DAY * 86400) - (HR * 3600) - (MIN * 60);
        return ("Days: " + DAY + " Hours: " + HR + " Minutes: " + MIN + " Seconds: " + SEC);
    }

}

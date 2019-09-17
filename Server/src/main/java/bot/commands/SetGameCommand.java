package bot.commands;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;

/**
 * Created by Corey on 07/06/2017.
 */
public class SetGameCommand implements CommandExecutor {

    private final CommandHandler commandHandler;
    private DiscordAPI api;

    public SetGameCommand(CommandHandler commandHandler, DiscordAPI api) {
        this.commandHandler = commandHandler;
        this.api = api;
    }

    @Command(aliases = { "::setgame" }, description = "Sets the Bots game status", showInHelpPage = false, requiredPermissions = "Admin")
    public String onSetGameCommand(String[] args) {

        String title = "";
        for (String arg : args) {
            title += arg + " ";
        }
        title = title.trim();

        api.setGame(title);


        return "Setting game to " + title;
    }

}

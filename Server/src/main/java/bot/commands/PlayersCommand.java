package bot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.repository.Repository;

/**
 * Created by Corey on 07/06/2017.
 */
public class PlayersCommand implements CommandExecutor {

    private final CommandHandler commandHandler;

    public PlayersCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Command(aliases = { "::players" }, description = "Get the player count")
    public String onPlayersCommand(String[] args) {

        StringBuilder sb = new StringBuilder("```xml\n");

        if (args.length > 0 && args[0].equalsIgnoreCase("full")) {
            sb.append("Players online:\n\n");

            for (Player p : Repository.getPlayers()) {

                sb.append("Username: " + p.getName() + "\n");
                sb.append("Total Level: " + p.getSkills().getTotalLevel() + "\n");
                sb.append("Account created: " + new java.util.Date(p.getDetails().joinDate * 1000).toString() + "\n");

                sb.append("\n\n");

            }
        }

        sb.append("Total players online: " + Repository.getPlayers().size());
        sb.append("```");
        return sb.toString();
    }

}

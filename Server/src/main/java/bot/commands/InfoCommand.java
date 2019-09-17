package bot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.repository.Repository;

import java.text.DecimalFormat;

/**
 * Created by Corey on 14/06/2017.
 */
public class InfoCommand implements CommandExecutor {

    private final CommandHandler commandHandler;

    public InfoCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Command(aliases = { "::info" }, description = "Get Info about a player", usage = "::info [player name]")
    public String onInfoCommand(String[] args) {
        if (args.length < 1) {
            return "Please define a player using ::info [player name]";
        }

        String name = "";
        for (String arg : args) {
            name += arg + " ";
        }
        name = name.trim();

        Player player = Repository.getPlayerByName(name);

        if (player == null) {
            return "Player '" + name + "' not found.";
        }

        StringBuilder sb = new StringBuilder(name + "'s Info:\n```xml\n");

        sb.append("\n");
        sb.append("Username: " + player.getName());

        sb.append("\n");
        sb.append("Rank: " + player.getDetails().getRights().name());

        sb.append("\n");
        sb.append("Donator Status: " + player.getDonorManager().getDonorStatus().name());

        sb.append("\n");
        sb.append("Title: " + player.getTitleManager().getTitleName());

        sb.append("\n");
        sb.append("Join Date: " + new java.util.Date(player.getDetails().joinDate * 1000).toString());

        sb.append("\n");
        sb.append("Total Level: " + player.getSkills().getTotalLevel());

        sb.append("\n");
        sb.append("Total Exp: " + new DecimalFormat("###,###,###,###,###,###,##0").format(player.getSkills().getTotalExperience()));

        sb.append("\n");
        sb.append("Total Networth: " + new DecimalFormat("###,###,###,###,###,###,##0").format(player.getNetworth()) + "gp");

        if (player.getSlayer().hasTask()) {
            sb.append("\n");
            sb.append("Slayer Task: " + player.getSlayer().getAmount() + "x " + player.getSlayer().getTaskName());
        }

        sb.append("\n```");

        return sb.toString();
    }

}

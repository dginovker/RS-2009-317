package org.gielinor.game.system.command.impl;

import org.gielinor.game.content.bot.Bot;
import org.gielinor.game.content.bot.impl.AutoAgility;
import org.gielinor.game.content.bot.impl.AutoMasterThiever;
import org.gielinor.game.content.bot.impl.AutoMonkFisher;
import org.gielinor.game.content.bot.impl.AutoRunecraft;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;

/**
 * Runs a server side bot script.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class RunCommand extends Command {

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
        return new String[]{ "run", "pause", "unpause", "stop" };
    }

    @Override
    public void execute(Player player, String[] args) {
        switch (args[0].toLowerCase()) {
            case "pause":
                if (player.getRunningBot() == null) {
                    player.getActionSender().sendMessage("No bot currently running.");
                    return;
                }
                if (player.getRunningBot().isPaused()) {
                    player.getActionSender().sendMessage("Bot is already paused.");
                    return;
                }
                player.getActionSender().sendMessage(player.getRunningBot().getScriptName() + " paused.");
                player.getRunningBot().pause();
                return;
            case "unpause":
                if (player.getRunningBot() == null) {
                    player.getActionSender().sendMessage("No bot currently running.");
                    return;
                }
                if (!player.getRunningBot().isPaused()) {
                    player.getActionSender().sendMessage("Bot is not paused.");
                    return;
                }
                player.getActionSender().sendMessage(player.getRunningBot().getScriptName() + " unpaused.");
                player.getRunningBot().unpause();
                return;
            case "stop":
                if (player.getRunningBot() == null) {
                    player.getActionSender().sendMessage("No bot currently running.");
                    return;
                }
                player.getActionSender().sendMessage("Stopped bot " + player.getRunningBot().getScriptName() + ".");
                player.getRunningBot().end();
                return;
        }
        String script = args[1].toLowerCase();
        player.getActionSender().sendMessage("Preparing to run script " + script);
        Bot bot = null;
        switch (script.toLowerCase()) {
            case "autoagility":
                bot = new AutoAgility(player);
                break;
            case "automonk":
                bot = new AutoMonkFisher(player);
                break;
            case "automaster":
                bot = new AutoMasterThiever(player);
                break;
            case "autorc":
            case "autorunecraft":
                bot = new AutoRunecraft(player);
                break;
            default:
                player.getActionSender().sendMessage("No available bot for " + script + ".");
                break;
        }
        if (player.getRunningBot() != null) {
            player.getRunningBot().end();
        }
        if (bot == null) {
            return;
        }
        bot.onStart();
    }
}

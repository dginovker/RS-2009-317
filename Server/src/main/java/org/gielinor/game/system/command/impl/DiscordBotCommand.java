package org.gielinor.game.system.command.impl;

import bot.DiscordBot;
import bot.DiscordConstants;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Copies another player's items, appearance, stats, inventory or bank
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DiscordBotCommand extends Command {

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
        return new String[]{ "discord", "bot", "db" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("discord", "Manages the server Discord Bot", getRights(),
            "::discord <lt>args1> <lt>args2> ..."));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length >= 1) {
            switch (args[1].toLowerCase()) {
                case "message":
                    String channelId = args[2];

                    switch (args[2].toLowerCase()) {
                        case "bot_spam":
                            channelId = DiscordConstants.BOT_SPAM_CHANNEL_ID;
                            break;

                        case "spam":
                            channelId = DiscordConstants.STAFF_SPAM_CHANNEL_ID;
                            break;

                        case "staffgen":
                        case "staff_gen":
                        case "staff_general":
                            channelId = DiscordConstants.STAFF_GENERAL_CHANNEL_ID;
                            break;

                        case "general":
                            channelId = DiscordConstants.GENERAL_CHANNEL_ID;
                            break;
                    }

                    String message = getMessage(args, 3);

                    DiscordBot.getBot().sendMessage(channelId, message);
                    player.getActionSender().sendMessage("Sending \"" + message + "\" to channel_id " + channelId);
                    break;

                case "dc":
                case "disconnect": // TODO
                    DiscordBot.getBot().getApi().setAutoReconnect(false);
                    // DiscordBot.getBot().sendMessage("315140484429250581", "Bot going offline...");
                    DiscordBot.getBot().getApi().disconnect();

                    player.getActionSender().sendMessage("Disconnecting bot");
                    break;

                case "connect":
                    DiscordBot.getBot().start();
                    DiscordBot.getBot().getApi().setAutoReconnect(true);
                    player.getActionSender().sendMessage("Starting Discord Bot...");
                    break;

                case "game":
                case "setgame":
                    String game = getMessage(args, 2);

                    DiscordBot.getBot().getApi().setGame(game);
                    player.getActionSender().sendMessage("Setting game to " + game);
                    break;
            }

        } else {
            player.getActionSender().sendMessage("Incorrect number of args");
        }
    }

    private String getMessage(String[] args, int startPos) {
        StringBuilder sb = new StringBuilder();

        for (int i = startPos; i < args.length; i++) {
            sb.append(args[i] + " ");
        }

        return sb.toString().trim();
    }

}

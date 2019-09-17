package bot;

import bot.commands.*;
import bot.listeners.NewGielinorianListener;
import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.utils.LoggerUtil;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import org.gielinor.game.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Corey on 07/06/2017.
 */
public class DiscordBot {

    private static final Logger log = LoggerFactory.getLogger(DiscordBot.class);

    private static DiscordBot bot = null;

    public static DiscordBot getBot() {
        if (bot == null) {
            bot = new DiscordBot(World.getConfiguration().getDiscordBotToken());
        }
        return bot;
    }

    private DiscordAPI api;
    private String token;

    private boolean running;

    public boolean isRunning() {
        return running;
    }

    public DiscordAPI getApi() {
        return api;
    }

    public DiscordBot(String token) {
        this.token = token;
    }

    public void start() {
        if (token == null || token.trim().length() == 0) {
            log.warn("Missing discord BOT-token.");
            return;
        }

        log.info("Starting Discord bot.");
        api = Javacord.getApi(token, true);
        api.connect(new FutureCallback<DiscordAPI>() {

            @Override
            public void onSuccess(DiscordAPI api) {
                setGame("gielinor.org | ::help");

                /*api.registerListener((MessageCreateListener) (api1, message) -> {
                    if (message.getContent().equalsIgnoreCase("::channels")) {
                        message.reply("Nothing to see here");
                    }https://www.rune-server.ee/members/gielinor/
                });*/

                CommandHandler handler = new JavacordHandler(api);
                handler.registerCommand(new DevCommand(handler, api));
                handler.registerCommand(new PlayersInWildCommand());
                handler.registerCommand(new UptimeCommand());
                handler.registerCommand(new InfoCommand(handler));
                handler.registerCommand(new SetGameCommand(handler, api));
                handler.registerCommand(new PlayersCommand(handler));
                handler.registerCommand(new HelpCommand(handler));
                handler.registerCommand(new VerifyCommand());
                handler.registerCommand(new RewardCommand());
                handler.registerCommand(new DiceCommand());
                handler.registerCommand(new StackCommand());
                handler.registerCommand(new UpdateRankCommand());
                // TODO load all channels and servers by id and store in hashmaps
            }

            @Override
            public void onFailure(Throwable t) {
                log.error("Failure in discord bot handling.", t);
            }

        });
        api.registerListener(new NewGielinorianListener());
        LoggerUtil.setDebug(false);
        log.info("Discord bot ready.");
        running = true;
    }

    private void setGame(String game) {
        if (api == null) {
            log.error("Cannot set game when API is null.");
            return;
        }
        api.setGame(game);
    }

    public void sendMessage(String channelId, String message) {
        if (!running) {
            return;
        }
        try {
            sendMessage(api.getChannelById(channelId), message);
        } catch (Exception ex) {
            log.error("Failed to send message to [{}]: {}", channelId, message, ex);
        }
    }

    public void sendMessage(Channel channel, String message) {
        if (!running) {
            return;
        }
        channel.sendMessage(message);
    }

}

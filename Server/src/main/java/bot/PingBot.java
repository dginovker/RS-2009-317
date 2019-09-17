package bot;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple ping-pong bot.
 */
public class PingBot {

    private static final Logger log = LoggerFactory.getLogger(PingBot.class);

    public static void main(String[] args) {
        new PingBot("MzIxNzk5MDY5NzUwMjYzODA4.DBjSRw.7q_vi0ZzF4t59RkzVv1FUDQ_qdM");
    }

    public PingBot(String token) {
        // See "How to get the token" below
        DiscordAPI api = Javacord.getApi(token, true);
        // connect
        api.connect(new FutureCallback<DiscordAPI>() {

            @Override
            public void onSuccess(DiscordAPI api) {
                // register listener
                api.setGame("gielinor.org | ::help");
                api.registerListener((MessageCreateListener) (api1, message) -> {
                    // check the content of the message
                    if (message.getContent().equalsIgnoreCase("::help")) {
                        // reply to the message
                        message.reply("Nothing to see here");
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                log.error("Ping bot failure.", t);
            }

        });
    }

}
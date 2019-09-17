package org.gielinor.game.content.bot;

import org.gielinor.game.node.entity.player.Player;

/**
 * Handles game messages sent.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */

public class BotMessageHandler {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates the {@link BotMessageHandler}.
     *
     * @param player The player.
     */
    public BotMessageHandler(Player player) {
        this.player = player;
    }

    public void handleMessage(String message) {
        if (player.getRunningBot() == null) {
            return;
        }
        if (this.botMessageListener != null) {
            this.botMessageListener.messageReceived(message);
            return;
        }
        this.botMessageListener = (BotMessageListener) player.getRunningBot();
        this.botMessageListener.messageReceived(message);
    }

    public BotMessageListener botMessageListener = null;

}

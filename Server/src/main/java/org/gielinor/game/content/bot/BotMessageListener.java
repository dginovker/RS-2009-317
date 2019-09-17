package org.gielinor.game.content.bot;

/**
 * Listens for game messages sent.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public interface BotMessageListener {

    /**
     * Called when a message is received from the server.
     *
     * @param message The message.
     */
    public abstract void messageReceived(String message);

}

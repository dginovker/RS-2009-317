package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The URL context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class URLContext implements Context {

    /**
     * The player.
     */
    private Player player;

    /**
     * The URL to launch.
     */
    private final String url;

    /**
     * Constructs a new {@code URLContext} {@code Object}.
     *
     * @param player The player.
     * @param url    The URL to launch.
     */
    public URLContext(Player player, String url) {
        this.player = player;
        this.url = url;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player.
     *
     * @param player The player.
     * @return This context instance.
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Gets the URL.
     *
     * @return The url.
     */
    public String getUrl() {
        return url;
    }
}

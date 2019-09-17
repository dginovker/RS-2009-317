package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The packet context for clan-related outgoing packets.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class URLMessageContext implements Context {

    /**
     * The player
     */
    private final Player player;
    /**
     * The URL.
     */
    private final String url;

    /**
     * If the url should launch immediately.
     */
    private final boolean launch;
    /**
     * The message to show.
     */
    private String message;

    /**
     * Constructs a new {@code ClanContext} {@code Object}.
     *
     * @param player the player.
     * @param url    The URL.
     * @param open   If the url should launch immediately.
     */
    public URLMessageContext(Player player, String url, boolean open, String message) {
        this.player = player;
        this.url = url;
        this.launch = open;
        this.message = message;
    }

    /**
     * Constructs a new {@code ClanContext} {@code Object}.
     *
     * @param player the player.
     * @param url    The URL.
     * @param open   If the url should launch immediately.
     */
    public URLMessageContext(Player player, String url, boolean open) {
        this(player, url, open, null);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the URL.
     *
     * @return The {@link #url}.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets if the url should launch immediately.
     *
     * @return <code>True</code> if so.
     */
    public boolean isLaunch() {
        return launch;
    }

    /**
     * Gets the message to show.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }
}
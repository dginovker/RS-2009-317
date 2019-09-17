package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The packet context for the contact packets.
 * @author Emperor
 *
 */
public final class ContactContext implements Context {

    /**
     * The update communication server state type.
     */
    public static final int UPDATE_STATE_TYPE = 0;

    /**
     * The update friend type.
     */
    public static final int UPDATE_FRIEND_TYPE = 1;

    /**
     * The ignore list type.
     */
    public static final int IGNORE_LIST_TYPE = 2;

    /**
     * The player.
     */
    private final Player player;

    /**
     * The contact packet type.
     */
    private int type;

    /**
     * The player name.
     */
    private String name;

    /**
     * If the contact is online.
     */
    private boolean online;

    /**
     * Constructs a new {@code ContactContext} {@code Object}.
     * @param player The player.
     * @param type The contact packet type.
     */
    public ContactContext(Player player, int type) {
        this.player = player;
        this.type = type;
    }

    /**
     * Constructs a new {@code ContactContext} {@code Object}.
     * @param player The player.
     * @param name The player name.
     */
    public ContactContext(Player player, String name, boolean online) {
        this.player = player;
        this.name = name;
        this.online = online;
        this.type = UPDATE_FRIEND_TYPE;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the type.
     * @return The type.
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type.
     * @param type The type to set.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the name.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the online.
     * @return The online.
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Sets the online.
     * @param online The online to set.
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

}

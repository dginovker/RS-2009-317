package org.gielinor.game.world.update.flag.context;

import org.gielinor.game.node.entity.player.Player;

/**
 * Represents a single chat message.
 *
 * @author Graham Edgecombe
 */
public class ChatMessage {

    /**
     * The player who sent the chat message.
     */
    private final Player player;

    /**
     * The colour.
     */
    private final int colour;

    /**
     * The effects.
     */
    private final int effects;

    /**
     * The packed chat text.
     */
    private final byte[] text;

    /**
     * The name of the player who said this message.
     */
    private String playerName;

    /**
     * The time of the chat message.
     */
    private long timestamp;

    /**
     * Creates a new chat message.
     *
     * @param colour  The message colour.
     * @param effects The message effects.
     * @param text    The packed chat text.
     */
    public ChatMessage(Player player, int colour, int effects, byte[] text) {
        this.player = player;
        this.colour = colour;
        this.effects = effects;
        this.text = text;
    }

    /**
     * Creates a new chat message.
     *
     * @param colour     The message colour.
     * @param effects    The message effects.
     * @param text       The packed chat text.
     * @param playerName
     * @param timestamp
     */
    public ChatMessage(Player player, int colour, int effects, byte[] text, String playerName, long timestamp) {
        this.player = player;
        this.colour = colour;
        this.effects = effects;
        this.text = text;
        this.playerName = playerName;
        this.timestamp = timestamp;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the message colour.
     *
     * @return The message colour.
     */
    public int getColour() {
        return colour;
    }

    /**
     * Gets the message effects.
     *
     * @return The message effects.
     */
    public int getEffects() {
        return effects;
    }

    /**
     * Gets the packed message text.
     *
     * @return The packed message text.
     */
    public byte[] getText() {
        return text;
    }

    /**
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

}

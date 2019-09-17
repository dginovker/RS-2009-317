package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * Packet context for communication message packets.
 *
 * @author Emperor
 */
public final class MessageContext implements Context {

    /**
     * Represents the packet id to use when receiving a message.
     */
    public static final int RECEIVE_MESSAGE = 196;

    /**
     * Represents the packet id use to send a clan message.
     */
    public static final int CLAN_MESSAGE = 217;

    /**
     * The player.
     */
    private final Player player;

    /**
     * The other player.
     */
    private final Player other;

    /**
     * The opcode.
     */
    private final int opcode;

    /**
     * The message.
     */
    private byte[] message;

    /**
     * The length of the message.
     */
    private int length;

    /**
     * The message as a string.
     */
    private String messageString;

    /**
     * Constructs a new MessageContext Object.
     *
     * @param player  The player.
     * @param other   The communicated player.
     * @param opcode  The opcode.
     * @param message The message.
     * @param length  The length of the message.
     */
    public MessageContext(Player player, Player other, int opcode, byte[] message, int length) {
        this.player = player;
        this.other = other;
        this.opcode = opcode;
        this.message = message;
        this.length = length;
    }

    /**
     * Constructs a new MessageContext Object.
     *
     * @param player        The player.
     * @param other         The communicated player.
     * @param opcode        The opcode.
     * @param messageString The message string.
     */
    public MessageContext(Player player, Player other, int opcode, String messageString) {
        this.player = player;
        this.other = other;
        this.opcode = opcode;
        this.messageString = messageString;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the other.
     *
     * @return The other.
     */
    public Player getOther() {
        return other;
    }

    /**
     * Gets the opcode.
     *
     * @return The opcode.
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * Gets the message.
     *
     * @return The message.
     */
    public byte[] getMessage() {
        return message;
    }

    /**
     * Gets the length of the message.
     *
     * @return The length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the message string.
     *
     * @return The message.
     */
    public String getMessageString() {
        return messageString;
    }
}
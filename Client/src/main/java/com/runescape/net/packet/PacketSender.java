package com.runescape.net.packet;

import com.runescape.Game;
import com.runescape.net.RSStream;
import com.runescape.util.ChatMessageCodec;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sends packets to the server.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class PacketSender {
    /**
     * The client focus opcode.
     */
    public static final int CLIENT_FOCUS_OPCODE = 3;
    /**
     * The public chat opcode.
     */
    public static final int CHAT_OPCODE = 4;
    /**
     * The wield item option opcode.
     */
    public static final int WIELD_ITEM_OPCODE = 41;
    /**
     * The Summoning orb action opcode.
     */
    public static final int SUMMONING_ORB_ACTION = 59;
    /**
     * The chat setting opcode.
     */
    public static final int CHAT_SETTING_OPCODE = 95;
    /**
     * The command opcode.
     */
    public static final int COMMAND_OPCODE = 103;
    /**
     * The region change opcode.
     */
    public static final int REGION_CHANGED = 121;
    /**
     * The third click item opcode.
     */
    public static final int THIRD_CLICK_ITEM_OPCODE = 145;
    /**
     * The region loaded opcode.
     */
    public static final int REGION_LOADED = 210;
    /**
     * The switch item opcode.
     */
    public static final int SWITCH_ITEM = 214;
    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(PacketSender.class.getName());
    /**
     * The {@link com.runescape.Game} instance.
     */
    private final Game game;

    /**
     * Constructs the <code>PacketSender</code>.
     *
     * @param game The {@link com.runescape.Game} instance.
     */
    public PacketSender(Game game) {
        this.game = game;
    }

    /**
     * Sends a packet.
     *
     * @param opcode The packet opcode.
     * @param data   The packet data.
     */
    public void send(int opcode, Object... data) {
        RSStream rsStream = game.getOutgoing();
        rsStream.putOpcode(opcode);
        switch (opcode) {
            case CLIENT_FOCUS_OPCODE:
                boolean focused = (Boolean) data[0];
                rsStream.writeByte(focused ? 1 : 0);
                break;

            case CHAT_OPCODE:
                rsStream.writeByte(0);
                int currentPosition = rsStream.currentPosition;
                rsStream.writeByteS((int) data[0]);
                rsStream.writeByteS((int) data[1]);
                game.getChatBuffer().currentPosition = 0;
                ChatMessageCodec.encode((String) data[2], game.getChatBuffer());
                rsStream.writeReverseDataA(game.getChatBuffer().payload, 0, game.getChatBuffer().currentPosition);
                rsStream.writeBytes(rsStream.currentPosition - currentPosition);
                break;

            case WIELD_ITEM_OPCODE:
                rsStream.putLEShort((int) data[0]);
                rsStream.putShortA((int) data[1]);
                rsStream.putShortA((int) data[2]);
                break;

            case SUMMONING_ORB_ACTION:
                rsStream.writeByte((byte) data[0]);
                break;

            case CHAT_SETTING_OPCODE:// TODO Clan mode and assist mode
                rsStream.writeByte(game.getPublicChatMode());
                rsStream.writeByte(game.getPrivateChatMode());
                rsStream.writeByte(game.getTradeMode());
                break;

            case COMMAND_OPCODE:
                String command = (String) data[0];
                rsStream.writeByte(command.length() - 1);
                rsStream.putString(command.substring(2));
                break;

            case REGION_CHANGED:

                break;

            case THIRD_CLICK_ITEM_OPCODE:
                rsStream.putShortA((int) data[0]);
                rsStream.putShortA((int) data[1]);
                rsStream.putShortA((int) data[2]);
                break;

            case REGION_LOADED:
                rsStream.putInt(0x3f008edd);
                break;

            case SWITCH_ITEM:
                rsStream.writeLEShortA((int) data[0]);
                rsStream.writeNegatedByte((int) data[1]);
                rsStream.writeLEShortA((int) data[2]);
                rsStream.putLEShort((int) data[3]);
                break;

            default:
                logger.log(Level.WARNING, "Unknown outgoing packet, opcode={0}.", opcode);
                break;
        }
    }
}

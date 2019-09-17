package org.gielinor.game.world.update.flag.player;

import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.game.world.update.flag.context.ChatMessage;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles the chat flag.
 *
 * @author Emperor
 */
public class ChatFlag extends UpdateFlag<ChatMessage> {

    /**
     * Constructs a new {@code ChatFlag.java} {@code Object}.
     *
     * @param context The context.
     */
    public ChatFlag(ChatMessage context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packet) {
        ChatMessage chatMessage = context;
        byte[] bytes = chatMessage.getText();
        packet.putLEShort(((chatMessage.getColour() & 0xFF) << 8) | (chatMessage.getEffects() & 0xFF));
        packet.put((byte) context.getPlayer().getRights().getMemberGroupId());
        for (int icon : Rights.Companion.getChatIcons(context.getPlayer())) {
            packet.put((byte) icon); // each individual chat icon
        }
        packet.putC(bytes.length);
        packet.putReverse(bytes, bytes.length, 0);
    }

    @Override
    public int data() {
        return maskData();
    }

    @Override
    public int ordinal() {
        return 4;
    }

    /**
     * Gets the mask data of the chat update flag.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x80;
    }
}

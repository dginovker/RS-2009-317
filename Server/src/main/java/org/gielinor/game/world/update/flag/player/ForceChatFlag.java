package org.gielinor.game.world.update.flag.player;

import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The force chat update flag.
 *
 * @author Emperor
 */
public final class ForceChatFlag extends UpdateFlag<String> {

    /**
     * Constructs a new {@code ForceChatFlag} {@code Object}.
     *
     * @param context The chat message.
     */
    public ForceChatFlag(String context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder buffer) {
        buffer.putString(context);
    }

    @Override
    public int data() {
        return maskData();
    }

    @Override
    public int ordinal() {
        return 3;
    }

    /**
     * Gets the mask data.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x4;
    }

}

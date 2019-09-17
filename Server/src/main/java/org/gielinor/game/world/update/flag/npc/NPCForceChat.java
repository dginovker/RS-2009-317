package org.gielinor.game.world.update.flag.npc;

import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The NPC force chat update flag.
 *
 * @author Emperor
 */
public final class NPCForceChat extends UpdateFlag<String> {

    /**
     * Constructs a new {@code NPCForceChat} {@code Object}.
     *
     * @param context The chat message.
     */
    public NPCForceChat(String context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packet) {
        packet.putString(context);
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
     * Gets the mask data.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x1;
    }
}

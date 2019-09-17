package org.gielinor.game.world.update.flag.npc;

import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The switch NPC id update flag.
 *
 * @author Emperor
 */
public final class NPCSwitchId extends UpdateFlag<Integer> {

    /**
     * Constructs a new {@code NPCSwitchId} {@code Object}.
     *
     * @param context The new NPC id.
     */
    public NPCSwitchId(int context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder buffer) {
        buffer.putLEShortA(context);
    }

    @Override
    public int data() {
        return 0x2;
    }

    @Override
    public int ordinal() {
        return getOrdinal();
    }

    /**
     * Gets the mask ordinal.
     *
     * @return The ordinal.
     */
    public static int getOrdinal() {
        return 6;
    }

}

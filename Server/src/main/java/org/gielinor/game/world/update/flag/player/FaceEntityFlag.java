package org.gielinor.game.world.update.flag.player;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The face entity update flag.
 *
 * @author Emperor
 */
public final class FaceEntityFlag extends UpdateFlag<Entity> {

    /**
     * Constructs a new {@code FaceEntityFlag} {@code Object}.
     *
     * @param context The entity to face.
     */
    public FaceEntityFlag(Entity context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packetBuilder) {
        int index = -1;
        if (context != null) {
            index = context instanceof NPC ? context.getIndex() : (context.getIndex() | 0x8000);
        }
        packetBuilder.putLEShort(index);
    }

    @Override
    public int data() {
        return 0x1;
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
        return 5;
    }

}
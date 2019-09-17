package org.gielinor.game.world.update.flag.npc;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The face entity update flag for NPCs.
 *
 * @author Emperor
 */
public final class NPCFaceEntity extends UpdateFlag<Entity> {

    /**
     * Constructs a new {@code NPCFaceEntity} {@code Object}.
     *
     * @param context The context.
     */
    public NPCFaceEntity(Entity context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packetBuilder) {
        int index = -1;
        if (context != null) {
            index = context instanceof NPC ? context.getClientIndex() : (context.getClientIndex() | 0x8000);
        }
        packetBuilder.putShort(index);
    }

    @Override
    public int data() {
        return 0x20;
    }

    @Override
    public int ordinal() {
        return getOrdinal();
    }

    /**
     * Gets the mask data.
     *
     * @return The mask data.
     */
    public static int getOrdinal() {
        return 3;
    }

}

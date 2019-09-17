package org.gielinor.game.world.update.flag.npc;

import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles an NPC's graphic update flag.
 *
 * @author Emperor
 */
public final class NPCGraphic extends UpdateFlag<Graphics> {

    /**
     * Constructs a new {@code NPCGraphic} {@code Object}.
     *
     * @param context The graphics.
     */
    public NPCGraphic(Graphics context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packet) {
        packet.putShort(context.getId());
        packet.putInt(context.getDelay() | context.getHeight() << 16);
    }

    @Override
    public int data() {
        return maskData();
    }

    @Override
    public int ordinal() {
        return 2;
    }

    /**
     * Gets the mask data.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x80;
    }

}

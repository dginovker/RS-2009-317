package org.gielinor.game.world.update.flag.npc;

import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The NPC face coordinates update flag.
 *
 * @author Emperor
 */
public final class NPCFaceLocation extends UpdateFlag<Location> {

    /**
     * Constructs a new {@code NPCFaceLocation} {@code Object}.
     *
     * @param context The location to face.
     */
    public NPCFaceLocation(Location context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packet) {
        packet.putLEShort((context.getX() << 1) + 1);
        packet.putLEShort((context.getY() << 1) + 1);
    }

    @Override
    public int data() {
        return 0x4;
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
        return 7;
    }

}

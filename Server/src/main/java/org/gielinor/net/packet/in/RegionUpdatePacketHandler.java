package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The {@link org.gielinor.net.packet.IncomingPacket} for a player's region being changed / loaded.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class RegionUpdatePacketHandler implements IncomingPacket {

    /**
     * Represents the region changed opcode.
     */
    private static final int REGION_CHANGED = 121;

    /**
     * Represents the region loaded opcode.
     */
    private static final int REGION_LOADED = 210;

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        switch (opcode) {
            case REGION_CHANGED:

                break;
            case REGION_LOADED:
                packet.getInt();
                break;
        }
    }
}

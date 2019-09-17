package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Packet received when a player's region has changed.
 * @author Emperor
 * @author 'Vexia
 *
 */
public class RegionChangePacket implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder buffer) {
        buffer.getInt();
    }

}

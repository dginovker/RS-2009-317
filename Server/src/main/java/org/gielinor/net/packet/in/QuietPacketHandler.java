package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Represents a packet not in use.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class QuietPacketHandler implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder buffer) {

    }
}
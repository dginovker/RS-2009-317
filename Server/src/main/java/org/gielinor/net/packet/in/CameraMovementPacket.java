package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles an incoming camera movement changed packet.
 * @author Emperor
 *
 */
public final class CameraMovementPacket implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder buffer) {

    }

}

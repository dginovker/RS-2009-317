package org.gielinor.net.packet;

import org.gielinor.game.node.entity.player.Player;

/**
 * Represents an incoming packet.
 * @author Emperor
 *
 */
public interface IncomingPacket {

    /**
     * Decodes the incoming packet.
     * @param player The player.
     * @param opcode The opcode.
     * @param buffer The buffer.
     * @return The new buffer to send in response.
     */
    void decode(Player player, int opcode, PacketBuilder buffer);

}

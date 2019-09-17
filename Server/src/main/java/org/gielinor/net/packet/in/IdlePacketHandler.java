package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles the idle packet handler.
 *
 * @author Emperor
 */
public final class IdlePacketHandler implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder buffer) {
        if (player != null && player.getSession() != null) {
            player.getSession().setLastPing(System.currentTimeMillis());
            if (player.getAttribute("keep_logged_in", false) || player.inCombat()) {
                return;
            }
            player.getActionSender().sendLogout();
        }
    }

}
package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles an incoming chat settings update packet.
 *
 * @author Emperor
 */
public final class ChatSettingsPacket implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder buffer) {
        player.getSettings().setChatSettings(buffer.get(), buffer.get(), buffer.get());
    }
}
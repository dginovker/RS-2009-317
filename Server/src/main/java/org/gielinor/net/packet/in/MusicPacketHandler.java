package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles music-related incoming packets.
 * @author Emperor
 *
 */
public final class MusicPacketHandler implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder buffer) {
        int musicId = buffer.getInt();
        if (player.getMusicPlayer().isLooping()) {
            player.getMusicPlayer().replay();
            return;
        }
        if (player.getMusicPlayer().getCurrentMusicId() == musicId) {
            player.getMusicPlayer().setPlaying(false);
        }
    }

}

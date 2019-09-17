package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContactContext;
import org.gielinor.net.packet.out.ContactPackets;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the packet used to handle all incoming packets related to communication.
 *
 * @author 'Vexia
 */
public final class CommunicationPacket implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        switch (packet.opcode()) {
            case 188:
                player.getCommunication().add(TextUtils.longToString(packet.getLong()));
                break;
            case 215:
                player.getCommunication().remove(TextUtils.longToString(packet.getLong()));
                break;
            case 137:
                player.getCommunication().block(TextUtils.longToString(packet.getLong()));
                break;
            case 13:
                player.getCommunication().unblock(TextUtils.longToString(packet.getLong()));
                break;
            case 126:
                final long nameAsLong = packet.getLong();
                final byte message[] = new byte[256];
                final int messageSize = (byte) (packet.getLength() - 8);
                packet.get(message, 0, messageSize);
                String unpackedMessage = TextUtils.decode(message, messageSize);
                player.getCommunication().message(TextUtils.longToString(nameAsLong), unpackedMessage, message, messageSize);
                break;
        }
        if (packet.opcode() != 126) {
            PacketRepository.send(ContactPackets.class, new ContactContext(player, ContactContext.UPDATE_STATE_TYPE));
            if (player.getAttribute("UPDATE_PULSE") == null && player.getCommunication().getClan() != null) {
                player.getActionSender().sendMessage("Changes will take effect on your clan in the next 30 seconds.");
                Pulse updatePulse = new Pulse(50, player) {

                    @Override
                    public boolean pulse() {
                        player.getCommunication().getClan().update();
                        player.removeAttribute("UPDATE_PULSE");
                        return true;
                    }
                };
                player.setAttribute("UPDATE_PULSE", updatePulse);
                World.submit(updatePulse);
            }
        }
    }

}

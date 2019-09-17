package org.gielinor.net.event;

import java.nio.ByteBuffer;

import org.gielinor.net.IoReadEvent;
import org.gielinor.net.IoSession;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.rs2.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles game packet reading.
 *
 * @author Emperor
 */
public final class GameReadEvent extends IoReadEvent {

    private static final Logger log = LoggerFactory.getLogger(GameReadEvent.class);

    /**
     * The incoming packet sizes, sorted by opcode.
     */
    private static final int[] PACKET_SIZES = Constants.PACKET_SIZES;

    /**
     * Constructs a new {@code GameReadEvent}.
     *
     * @param session The session.
     * @param buffer  The buffer to read from.
     */
    public GameReadEvent(IoSession session, ByteBuffer buffer) {
        super(session, buffer);
    }

    @Override
    public void read(IoSession session, ByteBuffer buffer) {
        int last = -1;

        while (buffer.hasRemaining()) {
            int opcode = buffer.get() & 0xFF;

            if (Constants.ISAAC_ENABLED) {
                opcode = (opcode - session.getIsaacPair().getInCipher().getNextValue()) & 0xFF;
            }

            if (opcode >= PACKET_SIZES.length) {
                log.warn("Invalid packet with opcode: {}. No packet size ({}) available?",
                    opcode, PACKET_SIZES.length);
                break;
            }

            int header = PACKET_SIZES[opcode];
            int size = header;

            if (header < 0) {
                size = getPacketSize(buffer, opcode, header, last);
            }

            if (size == -1) {
                break;
            }

            if (buffer.remaining() < size) {
                switch (header) {
                    case -2:
                        queueBuffer(opcode, size >> 8, size);
                        break;
                    case -1:
                        queueBuffer(opcode, size);
                        break;
                    default:
                        queueBuffer(opcode);
                        break;
                }
                break;
            }

            byte[] data = new byte[size];
            buffer.get(data);
            PacketBuilder packetBuilder = new PacketBuilder(opcode, null, ByteBuffer.wrap(data));
            IncomingPacket packet = PacketRepository.getIncoming(opcode);

            if (packet == null) {
                log.warn("Unhandled packet from [{}] - opcode: {}, size: {}, header: {}.",
                    session.getPlayer().getName(), opcode, size, header);
                continue;
            }

            last = opcode;

            try {
                packet.decode(session.getPlayer(), opcode, packetBuilder);
            } catch (Throwable t) {
                log.error("Failed to handle packet [{}] for [{}].",
                    opcode, session.getPlayer().getName(), t);
            }
        }
    }

    /**
     * Gets the packet size for the given opcode.
     *
     * @param buffer The buffer.
     * @param opcode The opcode.
     * @param header The packet header.
     * @param last   The last opcode.
     * @return The packet size.
     */
    private int getPacketSize(ByteBuffer buffer, int opcode, int header, int last) {
        if (header == -1) {
            if (buffer.remaining() < 1) {
                queueBuffer(opcode);
                return -1;
            }
            return buffer.get() & 0xFF;
        }
        if (header == -2) {
            if (buffer.remaining() < 2) {
                queueBuffer(opcode);
                return -1;
            }
            return buffer.getShort() & 0xFFFF;
        }
        log.error("Invalid packet. Opcode={}, Last={}, Queued={}.",
            opcode, last, usedQueuedBuffer);
        return -1;
    }

}
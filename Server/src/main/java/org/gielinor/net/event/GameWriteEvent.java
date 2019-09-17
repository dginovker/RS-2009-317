package org.gielinor.net.event;

import java.nio.ByteBuffer;

import org.gielinor.net.IoSession;
import org.gielinor.net.IoWriteEvent;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.rs2.config.Constants;

/**
 * Handles game packet writing events.
 *
 * @author Emperor
 */
public final class GameWriteEvent extends IoWriteEvent {

    /**
     * Constructs a new {@code GameWriteEvent}.
     *
     * @param session The session.
     * @param context The context.
     */
    public GameWriteEvent(IoSession session, Object context) {
        super(session, context);
    }

    @Override
    public void write(IoSession session, Object context) {
        if (context instanceof ByteBuffer) {
            session.queue((ByteBuffer) context);
            return;
        }
        PacketBuilder buffer = (PacketBuilder) context;
        ByteBuffer buf = (ByteBuffer) buffer.toByteBuffer().flip();
        if (buffer.opcode() != -1) {
            int packetLength = buf.remaining() + 4;
            ByteBuffer response = ByteBuffer.allocate(packetLength);
            int opcode = buffer.opcode();
            if (Constants.ISAAC_ENABLED) {
                opcode += session.getIsaacPair().getOutCipher().getNextValue();
            }
            response.put((byte) opcode);
            switch (buffer.getHeader()) {
                case BYTE:
                    response.put((byte) buf.remaining());
                    break;
                case SHORT:
                    response.putShort((short) buf.remaining());
                    break;
                default:
                    break;
            }
            buf = (ByteBuffer) response.put(buf).flip();
        }
        session.queue(buf);
    }

}
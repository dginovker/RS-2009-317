package org.gielinor.net.event;

import java.nio.ByteBuffer;

import org.gielinor.net.IoReadEvent;
import org.gielinor.net.IoSession;
import org.gielinor.utilities.buffer.ByteBufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles JS-5 reading events.
 * @author Emperor
 * @author Tyler
 */
public final class JS5ReadEvent extends IoReadEvent {

    private static final Logger log = LoggerFactory.getLogger(JS5ReadEvent.class);

    /**
     * Constructs a new {@code JS5ReadEvent}.
     * @param session The session.
     * @param buffer The buffer.
     */
    public JS5ReadEvent(IoSession session, ByteBuffer buffer) {
        super(session, buffer);
    }

    @Override
    public void read(IoSession session, ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            int opcode = buffer.get() & 0xFF;
            if (buffer.remaining() < 3) {
                queueBuffer(opcode);
                return;
            }
            switch (opcode) {
                case 0:
                case 1:
                    int container = buffer.get() & 0xFF;
                    int archive = buffer.getShort() & 0xFFFF;
                    //TODO: Validate request + anti-js5 flooding
                    session.write(new int[]{ container, archive, opcode });
                    break;
                case 2: //music
                case 3: //Music
                    buffer.get();
                    buffer.getShort();
                    break;
                case 4:
                    session.setJs5Encryption(buffer.get());
                    if (buffer.getShort() != 0) {
                        session.disconnect();
                        return;
                    }
                    break;
                case 5:
                case 9:
                    if (buffer.remaining() < 4) {
                        queueBuffer(opcode);
                        return;
                    }
                    buffer.getInt();
                    break;
                case 6:
                    ByteBufferUtils.getTriByte(buffer); //Value should be 3
                    buffer.getShort(); //Value should be 0
                    break;
                case 7:
                    buffer.get();
                    buffer.getShort();
                    session.disconnect();
                    return;
                default:
                    log.warn("Unhandled JS5 opcode: [{}].", opcode);
                    buffer.get();
                    buffer.getShort();
                    break;
            }
        }
    }

}

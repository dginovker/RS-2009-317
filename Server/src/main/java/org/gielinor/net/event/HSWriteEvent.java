package org.gielinor.net.event;

import java.nio.ByteBuffer;

import org.gielinor.net.IoSession;
import org.gielinor.net.IoWriteEvent;
import org.gielinor.net.producer.JS5EventProducer;
import org.gielinor.net.producer.LoginEventProducer;

/**
 * Handles Handshake write events.
 *
 * @author Emperor
 */
public final class HSWriteEvent extends IoWriteEvent {

    /**
     * The JS-5 event producer.
     */
    private static final JS5EventProducer JS5_PRODUCER = new JS5EventProducer();

    /**
     * The login event producer.
     */
    private static final LoginEventProducer LOGIN_PRODUCER = new LoginEventProducer();

    /**
     * Initial login response.
     */
    private static final byte[] INITIAL_RESPONSE = new byte[]{
        0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0
    };

    /**
     * Constructs a new {@code HSWriteEvent} {@code Object}.
     *
     * @param session The session.
     * @param context The context.
     */
    public HSWriteEvent(IoSession session, Object context) {
        super(session, context);
    }

    @Override
    public void write(IoSession session, Object context) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(INITIAL_RESPONSE);
        byteBuffer.put((byte) 0);
        if ((Boolean) context) {
            byteBuffer.putLong(session.getServerKey());
            session.setProducer(LOGIN_PRODUCER);
        } else {
            session.setProducer(JS5_PRODUCER);
        }
        byteBuffer.flip();
        session.queue(byteBuffer);
    }

}

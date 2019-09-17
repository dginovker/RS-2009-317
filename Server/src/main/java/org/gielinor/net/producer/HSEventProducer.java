package org.gielinor.net.producer;

import java.nio.ByteBuffer;

import org.gielinor.net.EventProducer;
import org.gielinor.net.IoReadEvent;
import org.gielinor.net.IoSession;
import org.gielinor.net.IoWriteEvent;
import org.gielinor.net.event.HSReadEvent;
import org.gielinor.net.event.HSWriteEvent;

/**
 * Produces I/O events for the handshake protocol.
 *
 * @author Emperor
 */
public final class HSEventProducer implements EventProducer {

    @Override
    public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
        return new HSReadEvent(session, buffer);
    }

    @Override
    public IoWriteEvent produceWriter(IoSession session, Object context) {
        return new HSWriteEvent(session, context);
    }

}

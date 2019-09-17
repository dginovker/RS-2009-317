package org.gielinor.net.producer;

import java.nio.ByteBuffer;

import org.gielinor.net.EventProducer;
import org.gielinor.net.IoReadEvent;
import org.gielinor.net.IoSession;
import org.gielinor.net.IoWriteEvent;
import org.gielinor.net.event.GameReadEvent;
import org.gielinor.net.event.GameWriteEvent;

/**
 * Produces game packet I/O events.
 * @author Emperor
 */
public final class GameEventProducer implements EventProducer {

    @Override
    public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
        return new GameReadEvent(session, buffer);
    }

    @Override
    public IoWriteEvent produceWriter(IoSession session, Object context) {
        return new GameWriteEvent(session, context);
    }

}

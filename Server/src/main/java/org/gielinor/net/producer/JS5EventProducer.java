package org.gielinor.net.producer;

import java.nio.ByteBuffer;

import org.gielinor.net.EventProducer;
import org.gielinor.net.IoReadEvent;
import org.gielinor.net.IoSession;
import org.gielinor.net.IoWriteEvent;
import org.gielinor.net.event.JS5ReadEvent;
import org.gielinor.net.event.JS5WriteEvent;

/**
 * Produces JS-5 I/O events.
 * @author Tyler
 * @author Emperor
 */
public class JS5EventProducer implements EventProducer {

    @Override
    public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
        return new JS5ReadEvent(session, buffer);
    }

    @Override
    public IoWriteEvent produceWriter(IoSession session, Object context) {
        return new JS5WriteEvent(session, context);
    }

}

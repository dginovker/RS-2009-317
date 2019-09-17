package org.gielinor.net.producer;

import java.nio.ByteBuffer;

import org.gielinor.net.EventProducer;
import org.gielinor.net.IoReadEvent;
import org.gielinor.net.IoSession;
import org.gielinor.net.IoWriteEvent;
import org.gielinor.net.event.LoginReadEvent;
import org.gielinor.net.event.LoginWriteEvent;

/**
 * Produces login I/O events.
 *
 * @author Emperor
 */
public final class LoginEventProducer implements EventProducer {

    @Override
    public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
        return new LoginReadEvent(session, buffer);
    }

    @Override
    public IoWriteEvent produceWriter(IoSession session, Object context) {
        return new LoginWriteEvent(session, context);
    }

}

package org.gielinor.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.channels.CancelledKeyException;

/**
 * Handles a writing event.
 * @author Emperor
 */
public abstract class IoWriteEvent implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(IoWriteEvent.class);

    /**
     * The I/O session.
     */
    private final IoSession session;

    /**
     * The buffer.
     */
    private final Object context;

    /**
     * Constructs a new {@code IoWriteEvent}.
     * @param session The session.
     * @param context The write event context.
     */
    public IoWriteEvent(IoSession session, Object context) {
        this.session = session;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            write(session, context);
        } catch (CancelledKeyException ex) {
            session.disconnect();
        } catch (Throwable t) {
            log.error("Error writing [{}] to session.", context, t);
            session.disconnect();
        }
    }

    /**
     * Writes the data.
     * @param session The session.
     * @param context The write event context.
     */
    public abstract void write(IoSession session, Object context);

}

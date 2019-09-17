package org.gielinor.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;

/**
 * Handles a reading event.
 * @author Emperor
 */
public abstract class IoReadEvent implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(IoReadEvent.class);

    /**
     * The I/O session.
     */
    private final IoSession session;

    /**
     * The buffer.
     */
    private ByteBuffer buffer;

    /**
     * If the queued reading buffer was used (debugging purposes).
     */
    protected boolean usedQueuedBuffer;

    /**
     * Constructs a new {@code IoReadEvent}.
     * @param session The session.
     * @param buffer The buffer to read from.
     */
    public IoReadEvent(IoSession session, ByteBuffer buffer) {
        this.session = session;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            if (session.getReadingQueue() != null) {
                buffer = session.getReadingQueue().put(buffer);
                buffer.flip();
                session.setReadingQueue(null);
                usedQueuedBuffer = true;
            }
            read(session, buffer);
        } catch (Throwable t) {
            log.error("Exception occured.", t);
            session.disconnect();
        }
    }

    /**
     * Queues the buffer until more data has been received.
     * @param data The data that has been read already.
     */
    public void queueBuffer(int... data) {
        ByteBuffer queue = ByteBuffer.allocate(data.length + buffer.remaining() + 100_000);
        for (int value : data) {
            queue.put((byte) value);
        }
        queue.put(buffer);
        session.setReadingQueue(queue);
    }

    /**
     * Reads the data from the buffer.
     * @param session The session.
     * @param buffer The buffer to read from.
     */
    public abstract void read(IoSession session, ByteBuffer buffer);

}

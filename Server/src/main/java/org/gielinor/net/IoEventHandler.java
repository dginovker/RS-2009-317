package org.gielinor.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

/**
 * I/O event handling.
 *
 * @author Emperor
 *
 */
public final class IoEventHandler {

    private static final Logger log = LoggerFactory.getLogger(IoEventHandler.class);

    /**
     * The executor service.
     */
    private final ExecutorService service;

    /**
     * Constructs a new {@code IoEventHandler}.
     *
     * @param service
     *            The executor service used for handling events.
     */
    public IoEventHandler(ExecutorService service) {
        this.service = service;
    }

    /**
     * Used for accepting a new connection.
     *
     * @param key
     *            The selection key.
     * @param selector
     *            The selector.
     * @throws IOException
     *             When an I/O exception occurs.
     */
    public void accept(SelectionKey key, Selector selector) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        sc.configureBlocking(false);
        sc.socket().setTcpNoDelay(true);
        sc.register(selector, SelectionKey.OP_READ);
    }

    /**
     * Reads the incoming packet data.
     *
     * @param selectionKey
     *            The selection key.
     * @throws IOException
     *             When an I/O exception occurs.
     */
    public void read(SelectionKey selectionKey) throws IOException {
        ReadableByteChannel channel = (ReadableByteChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(100_000);
        IoSession session = (IoSession) selectionKey.attachment();

        if (!channel.isOpen() || channel.read(buffer) == -1) {
            return;
        }

        buffer.flip();

        if (session == null) {
            selectionKey.attach(session = new IoSession(selectionKey, service));
        }

        service.execute(session.getProducer().produceReader(session, buffer));
    }

    /**
     * Writes the outgoing packet data.
     *
     * @param key
     *            The selection key.
     */
    public void write(SelectionKey key) {
        IoSession session = (IoSession) key.attachment();
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        session.write();
    }

    /**
     * Disconnects a connection.
     *
     * @param key
     *            The selection key.
     * @param t
     *            The occurred exception (if any).
     */
    public void disconnect(SelectionKey key, Throwable t) {
        try {
            IoSession session = (IoSession) key.attachment();
            String cause = String.valueOf(t);

            if (t != null && !(t instanceof ClosedChannelException || cause.contains("De externe host")
                || cause.contains("De software op uw") || cause.contains("An established connection was aborted")
                || cause.contains("An existing connection") || cause.contains("AsynchronousClose")
                || cause.contains("Connection reset by peer"))) {
                log.error("Error in disconnect.", t);
            }

            if (session != null) {
                session.disconnect();
            }
        } catch (Throwable e) {
            log.error("Error in disconnect({}, {}).", key, t, e);
        }
    }

}

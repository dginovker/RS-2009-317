package org.gielinor.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Handles (NIO-based) networking events using the reactor pattern.
 *
 * @author Emperor
 *
 */
public final class NioReactor implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(NioReactor.class);

    /**
     * The executor service.
     */
    private final ExecutorService service;

    /**
     * The selector
     */
    private Selector selector;

    /**
     * The socket channel.
     */
    private ServerSocketChannel channel;

    /**
     * The I/O event handling instance.
     */
    private final IoEventHandler eventHandler;

    /**
     * If the reactor is running.
     */
    private boolean running;

    /**
     * Constructs a new {@code NioReactor}.
     *
     * @param poolSize
     *            The pool size.
     */
    private NioReactor(int poolSize) {
        this.service = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Service-Reactor"));
        this.eventHandler = new IoEventHandler(Executors.newFixedThreadPool(poolSize, new NamedThreadFactory("Event-Reactor")));
    }

    /**
     * Creates and configures a new {@code NioReactor} with a pool size of 1.
     *
     * @param port
     *            The port.
     * @return The {@code NioReactor} {@code Object}.
     * @throws IOException
     *             When an I/O exception occurs.
     */
    public static NioReactor configure(int port) throws IOException {
        return configure(port, 1);
    }

    /**
     * Creates and configures a new {@code NioReactor}.
     *
     * @param port
     *            The port.
     * @param poolSize
     *            The amount of threads in the thread pool.
     * @return The {@code NioReactor} {@code Object}.
     * @throws IOException
     *             When an I/O exception occurs.
     */
    public static NioReactor configure(int port, int poolSize) throws IOException {
        NioReactor reactor = new NioReactor(poolSize);
        reactor.channel = ServerSocketChannel.open();
        reactor.selector = Selector.open();
        reactor.channel.bind(new InetSocketAddress(port));
        reactor.channel.configureBlocking(false);
        reactor.channel.register(reactor.selector, SelectionKey.OP_ACCEPT);
        return reactor;
    }

    /**
     * Starts the reactor.
     */
    public void start() {
        running = true;
        service.execute(this);
    }

    @Override
    public void run() {
        while (running) {
            try {
                selector.select();
            } catch (IOException ex) {
                log.error("Error in select() in NioReactor.", ex);
            }

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                try {
                    if (!key.isValid() || !key.channel().isOpen()) {
                        key.cancel();
                        continue;
                    }

                    if (key.isAcceptable()) {
                        eventHandler.accept(key, selector);
                    }

                    if (key.isReadable()) {
                        eventHandler.read(key);
                    } else if (key.isWritable()) {
                        eventHandler.write(key);
                    }
                } catch (Throwable t) {
                    eventHandler.disconnect(key, t);
                }
            }
        }
    }

    /**
     * Terminates the reactor (once it's done processing current I/O events).
     */
    public void terminate() {
        running = false;
    }

    private static class NamedThreadFactory implements ThreadFactory {
        private final String name;
        private final AtomicLong threadCount = new AtomicLong();

        NamedThreadFactory(String name) {
            this.name = Objects.requireNonNull(name, "name");
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r,name + '-' + threadCount.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        }
    }

}

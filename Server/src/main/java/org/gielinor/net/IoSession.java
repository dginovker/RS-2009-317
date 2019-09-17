package org.gielinor.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.login.Response;
import org.gielinor.game.world.World;
import org.gielinor.net.producer.HSEventProducer;
import org.gielinor.net.producer.LoginEventProducer;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.crypto.ISAACPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a connected I/O session.
 *
 * @author Emperor
 *
 */
public class IoSession {

    private static final Logger log = LoggerFactory.getLogger(IoSession.class);

    /**
     * The handshake event producer.
     */
    private static final EventProducer HANDSHAKE_PRODUCER = new HSEventProducer();

    /**
     * The selection key.
     */
    private final SelectionKey key;

    /**
     * The executor service.
     */
    private final ExecutorService service;

    /**
     * The event producer.
     */
    private EventProducer producer = HANDSHAKE_PRODUCER;

    /**
     * The currently queued writing data.
     */
    private Queue<ByteBuffer> writingQueue = new ArrayDeque<>();

    /**
     * The currently queued reading data.
     */
    private ByteBuffer readingQueue;

    /**
     * The writing lock.
     */
    private Lock writingLock = new ReentrantLock();

    /**
     * The ISAAC cipher pair.
     */
    private ISAACPair isaacPair;

    /**
     * The name hash.
     */
    private int nameHash;

    /**
     * The server key.
     */
    private long serverKey;

    /**
     * The JS-5 encryption value.
     */
    private int js5Encryption;

    /**
     * The login size.
     */
    private int loginSize;

    /**
     * The login encrypt size.
     */
    private int loginEncryptSize;

    /**
     * The player.
     */
    private Player player;

    /**
     * If the session is active.
     */
    private boolean active = true;

    /**
     * The last ping time stamp.
     */
    private long lastPing;

    /**
     * Constructs a new {@code IoSession}.
     *
     * @param key
     *            The selection key.
     * @param service
     *            The executor service.
     */
    public IoSession(SelectionKey key, ExecutorService service) {
        this.key = key;
        this.service = service;
    }

    /**
     * Fires a write event created using the current event producer.
     *
     * @param context
     *            The event context.
     */
    public void write(Object context) {
        write(context, false);
    }

    /**
     * Fires a write event created using the current event producer.
     *
     * @param context
     *            The event context.
     * @param instant
     *            If the event should be instantly executed on this thread.
     */
    public void write(Object context, boolean instant) {
        if (context == null) {
            throw new IllegalStateException("Invalid writing context!");
        }

        if (!(context instanceof Response) && producer instanceof LoginEventProducer) {
            throw new IllegalStateException("Bad context: " + context.getClass().getName());
        }

        if (instant) {
            producer.produceWriter(this, context).run();
            return;
        }

        service.execute(producer.produceWriter(this, context));
    }

    /**
     * Sends the packet data (without write event encoding).
     *
     * @param buffer
     *            The buffer.
     */
    public void queue(ByteBuffer buffer) {
        writingLock.lock();
        writingQueue.offer(buffer);
        writingLock.unlock();
        write();
    }

    /**
     * Handles the writing of all buffers in the queue.
     */
    public void write() {
        if (!key.isValid()) {
            disconnect();
            return;
        }

        writingLock.lock();
        SocketChannel channel = (SocketChannel) key.channel();

        try {
            while (!writingQueue.isEmpty()) {
                ByteBuffer buffer = writingQueue.peek();
                channel.write(buffer);

                if (buffer.hasRemaining()) {
                    key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                    break;
                }

                writingQueue.poll();
            }
        } catch (IOException ex) {
            disconnect();
        }

        writingLock.unlock();
    }

    /**
     * Disconnects the session.
     */
    public void disconnect() {
        try {
            if (!active) {
                return;
            }

            active = false;
            key.cancel();
            SocketChannel channel = (SocketChannel) key.channel();
            channel.socket().close();

            if (player != null) {
                final Player p = player;

                World.submit(new Pulse(0) {

                    @Override
                    public boolean pulse() {
                        if (p.isActive() && !p.getSession().active) {
                            p.clear();
                        }
                        return true;
                    }
                });

                player = null;
            }
        } catch (IOException ex) {
            log.error("Error during disconnect: [{}].", player.getName(), ex);
        }
    }

    /**
     * Gets the remote address of this session.
     *
     * @return The remote address, as a String.
     */
    public String getRemoteAddress() {
        try {
            return ((SocketChannel) key.channel()).getRemoteAddress().toString();
        } catch (IOException ex) {
            log.error("Cannot get remote address: [{}].", player.getName(), ex);
            throw new IllegalStateException("Cannot get remote address for: " + player.getName(), ex);
        }
    }

    /**
     * Gets the current event producer.
     *
     * @return The producer.
     */
    public EventProducer getProducer() {
        return producer;
    }

    /**
     * Sets the event producer.
     *
     * @param producer
     *            The producer to set.
     */
    public void setProducer(EventProducer producer) {
        this.producer = producer;
    }

    /**
     * Gets the queued reading data.
     *
     * @return The readingQueue.
     */
    public ByteBuffer getReadingQueue() {
        synchronized (this) {
            return readingQueue;
        }
    }

    /**
     * Queues reading data.
     *
     * @param readingQueue
     *            The readingQueue to set.
     */
    public void setReadingQueue(ByteBuffer readingQueue) {
        synchronized (this) {
            this.readingQueue = readingQueue;
        }
    }

    /**
     * Gets the writing lock.
     *
     * @return The writing lock.
     */
    public Lock getWritingLock() {
        return writingLock;
    }

    /**
     * Gets the selection key.
     *
     * @return The selection key.
     */
    public SelectionKey getKey() {
        return key;
    }

    /**
     * @return The active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return The js5Encryption.
     */
    public int getJs5Encryption() {
        return js5Encryption;
    }

    /**
     * @param js5Encryption
     *            The js5Encryption to set.
     */
    public void setJs5Encryption(int js5Encryption) {
        this.js5Encryption = js5Encryption;
    }

    /**
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @param player
     *            The player to set.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the lastPing.
     *
     * @return The lastPing.
     */
    public long getLastPing() {
        return lastPing;
    }

    /**
     * Sets the lastPing.
     *
     * @param lastPing
     *            The lastPing to set.
     */
    public void setLastPing(long lastPing) {
        this.lastPing = lastPing;
    }

    /**
     * Gets the nameHash.
     *
     * @return The nameHash.
     */
    public int getNameHash() {
        return nameHash;
    }

    /**
     * Sets the nameHash.
     *
     * @param nameHash
     *            The nameHash to set.
     */
    public void setNameHash(int nameHash) {
        this.nameHash = nameHash;
    }

    /**
     * Gets the serverKey.
     *
     * @return The serverKey.
     */
    public long getServerKey() {
        return serverKey;
    }

    /**
     * Sets the serverKey.
     *
     * @param serverKey
     *            The serverKey to set.
     */
    public void setServerKey(long serverKey) {
        this.serverKey = serverKey;
    }

    /**
     * Gets the isaacPair.
     *
     * @return The isaacPair.
     */
    public ISAACPair getIsaacPair() {
        return isaacPair;
    }

    /**
     * Sets the isaacPair.
     *
     * @param isaacPair
     *            The isaacPair to set.
     */
    public void setIsaacPair(ISAACPair isaacPair) {
        this.isaacPair = isaacPair;
    }

    /**
     * The login size.
     */
    public int getLoginSize() {
        return loginSize;
    }

    public void setLoginSize(int loginSize) {
        this.loginSize = loginSize;
    }

    /**
     * The login encrypt size.
     */
    public int getLoginEncryptSize() {
        return loginEncryptSize;
    }

    public void setLoginEncryptSize(int loginEncryptSize) {
        this.loginEncryptSize = loginEncryptSize;
    }

}

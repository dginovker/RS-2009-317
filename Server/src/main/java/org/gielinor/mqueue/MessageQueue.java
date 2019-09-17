package org.gielinor.mqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.gielinor.mqueue.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Queues a message.
 *
 * @author <a href="http://www.rune-server.org/members/mike/">Mike</a>
 */
public class MessageQueue implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(MessageQueue.class);

    private boolean running = false;

    private Thread thread;

    private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>(100000);

    private final ExecutorService service = Executors.newFixedThreadPool(4);

    @Override
    public void run() {
        while (running) {
            try {
                final Message msg = messages.take();
                service.execute(msg::execute);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pushTask(Message task) {
        messages.offer(task);
    }

    public void start() {
        if (running) {
            throw new IllegalStateException("The Message Queue is already running.");
        }
        this.running = true;
        thread = new Thread(this);
        thread.start();
        log.info("Message queue initialized successfully.");
    }

    public void stop() {
        if (!running) {
            throw new IllegalStateException("The Message Queue is already stopped.");
        }
        running = false;
        thread.interrupt();
    }

    public boolean isRunning() {
        return running;
    }

}

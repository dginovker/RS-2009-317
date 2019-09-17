package org.gielinor.worker;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.gielinor.game.system.SystemManager;
import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.UpdateSequence;

/**
 * The major update worker, this handles the game updating etc.
 * @author Emperor
 *
 */
public final class MajorUpdateWorker implements Runnable {

    /**
     * The updating sequence to use.
     */
    private final UpdateSequence sequence = new UpdateSequence();

    /**
     * The executor.
     */
    private final Executor EXECUTOR = Executors.newSingleThreadExecutor(new UpdaterThreadFactory());

    /**
     * The start time of a cycle.
     */
    private long start;

    /**
     * If the major update worker has started.
     */
    private boolean started;

    /**
     * Constructs a new {@code MajorUpdateWorker} {@code Object}.
     */
    public MajorUpdateWorker() {
        /*
         * Empty.
         */
    }

    /**
     * Starts the update worker.
     */
    public void start() {
        if (started) {
            return;
        }
        started = true;
        EXECUTOR.execute(MajorUpdateWorker.this);
    }

    @Override
    public void run() {
        while (SystemManager.isActive()) {
            try {
                start = System.currentTimeMillis();

                World.pulse();

                if (sequence.start()) {
                    sequence.run();
                    sequence.end();
                } else {
                    System.out.println("Failed to run sequence!");
                }

                Repository.getDisconnectionQueue().update();

                sleep();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        started = false;
    }

    /**
     * Lets the current thread sleep.
     * @throws InterruptedException When the thread is interrupted.
     */
    private void sleep() throws InterruptedException {
        // StatisticsTab.reportPerformance((int) ((System.currentTimeMillis() - start) - 600));
        long duration = 600 - ((System.currentTimeMillis() - start) % 600);
        if (duration > 0) {
            Thread.sleep(duration);
        } else {
            System.err.println("Updating cycle duration took " + -duration + "ms too long!");
        }
    }

    /**
     * Gets the started.
     * @return The started.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Sets the started.
     * @param started The started to set.
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

    private static class UpdaterThreadFactory implements ThreadFactory {
        private final AtomicLong threadCount = new AtomicLong();
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "Game-Thread-" + threadCount.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        }
    }

}

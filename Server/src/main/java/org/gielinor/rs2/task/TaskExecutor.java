package org.gielinor.rs2.task;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class holding methods to execute tasks.
 *
 * @author Emperor
 */
public final class TaskExecutor {

    /**
     * The executor to use.
     */
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * The timer task executor.
     */
    private static final Timer TIMER = new Timer();

    /**
     * Executes a new timer task.
     *
     * @param task  The timer task.
     * @param delay The delay until the task should execute.
     */
    public static void run(TimerTask task, long delay) {
        TIMER.schedule(task, delay);
    }

    /**
     * Executes the task.
     *
     * @param task The task to execute.
     */
    public static void execute(Runnable task) {
        EXECUTOR.execute(task);
    }

    /**
     * Gets the executor.
     *
     * @return The executor.
     */
    public static ExecutorService getExecutor() {
        return EXECUTOR;
    }
}

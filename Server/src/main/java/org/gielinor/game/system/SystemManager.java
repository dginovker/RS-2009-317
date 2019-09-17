package org.gielinor.game.system;

import org.gielinor.game.world.World;

/**
 * Manages the "game system" states, such as updating or terminating.
 *
 * @author Emperor
 */
public final class SystemManager {

    /**
     * The system state.
     */
    private static SystemState state = SystemState.TERMINATED;

    /**
     * The system update handler.
     */
    private static final SystemUpdate UPDATER = new SystemUpdate();

    /**
     * The system termination handler.
     */
    private static final SystemTermination TERMINATOR = new SystemTermination();

    /**
     * Constructs a new {@code SystemManager} {@code Object}.
     */
    private SystemManager() {
        /*
         * empty.
         */
    }

    /**
     * Sets the current state and handles it accordingly.
     *
     * @param state The system state.
     */
    public static void flag(SystemState state) {
        if (SystemManager.state == state) {
            return;
        }
        SystemManager.state = state;
        switch (state) {
            case ACTIVE:
            case PRIVATE:
                World.getMajorUpdateWorker().start();
                break;
            case UPDATING:
                UPDATER.schedule();
                break;
            case TERMINATED:
                TERMINATOR.terminate();
                break;
        }
    }

    /**
     * Checks if the system is still active (updating keeps the system active until termination).
     *
     * @return <code>True</code> if the state does not equal {@link SystemState#TERMINATED}.
     */
    public static boolean isActive() {
        return state != SystemState.TERMINATED;
    }

    /**
     * Checks if the system is being updated.
     *
     * @return <code>True</code> if so.
     */
    public static boolean isUpdating() {
        return state == SystemState.UPDATING;
    }

    /**
     * Checks if the system is private, so only developers can connect.
     *
     * @return <code>True</code> if so.
     */
    public static boolean isPrivate() {
        return state == SystemState.PRIVATE;
    }

    /**
     * Checks if the system has been terminated.
     *
     * @return <code>True</code> if so.
     */
    public static boolean isTerminated() {
        return state == SystemState.TERMINATED;
    }

    /**
     * Gets the current system state.
     *
     * @return The state.
     */
    public static SystemState state() {
        return state;
    }

    /**
     * Gets the updater.
     *
     * @return The updater.
     */
    public static SystemUpdate getUpdater() {
        return UPDATER;
    }

    /**
     * Gets the terminator.
     *
     * @return The terminator.
     */
    public static SystemTermination getTerminator() {
        return TERMINATOR;
    }
}

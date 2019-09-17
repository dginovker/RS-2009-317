package org.gielinor.game.system;

import java.util.concurrent.Executors;

import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles a system update.
 *
 * @author Emperor
 */
public final class SystemUpdate extends Pulse {

    /**
     * The default countdown for an update, in ticks.
     */
    private static final int DEFAULT_COUNTDOWN = 100;

    /**
     * The amount of ticks left of when to create a backup.
     */
    public static final int BACKUP_TICK = 5;

    /**
     * If a backup should be created.
     */
    private boolean createBackup = SystemManager.isActive();

    /**
     * Constructs a new {@code SystemUpdate} {@code Object}.
     */
    protected SystemUpdate() {
        super(DEFAULT_COUNTDOWN);
    }

    @Override
    public boolean pulse() {
        if (getDelay() >= BACKUP_TICK && createBackup) {
            try {
                //	SystemManager.getTerminator().generateBackup();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            setDelay(BACKUP_TICK - 1);
            return false;
        }
        SystemManager.flag(SystemState.TERMINATED);
        return true;
    }

    /**
     * Notifies the players.
     */
    public void notifyPlayers() {
        try {
            int time = getDelay() + (getDelay() == 0 ? 0 : (createBackup ? BACKUP_TICK : 0));
            Repository.getPlayers().stream().filter(p -> p != null).forEach(p -> {
                p.getActionSender().sendSystemUpdate(time);
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Schedules an update.
     */
    public void schedule() {
        super.setTicksPassed(0);
        super.start();
        if (World.getMajorUpdateWorker().isStarted()) {
            notifyPlayers();
            World.submit(this);
            return;
        }
        Executors.newSingleThreadExecutor().submit(() -> {
            while (isRunning()) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (update()) {
                    break;
                }
            }
        });
    }

    /**
     * Sets the system update countdown.
     *
     * @param ticks The amount of ticks.
     */
    public void setCountdown(int ticks) {
        if (createBackup) {
            if (ticks < BACKUP_TICK) {
                ticks = BACKUP_TICK;
            }
            ticks -= BACKUP_TICK;
        }
        super.setDelay(ticks);
    }

    /**
     * Cancels the system update task.
     */
    public void cancel() {
        super.stop();
        SystemManager.flag(SystemState.ACTIVE);
    }

    /**
     * Gets the createBackup.
     *
     * @return The createBackup.
     */
    public boolean isCreateBackup() {
        return createBackup;
    }

    /**
     * Sets the createBackup.
     *
     * @param createBackup The createBackup to set.
     */
    public void setCreateBackup(boolean createBackup) {
        this.createBackup = createBackup;
    }
}

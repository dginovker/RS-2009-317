package org.gielinor.rs2.task.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;

/**
 * A task called upon when a player disconnects from the game.
 *
 * @author Emperor
 */
public abstract class LogoutTask {

    /**
     * The amount of ticks this logout task is valid for.
     */
    private int validity;

    /**
     * Constructs a new {@code LogoutTask} {@code Object}.
     */
    public LogoutTask() {
        this(Integer.MAX_VALUE - World.getTicks());
    }

    /**
     * Constructs a new {@code LogoutTask} {@code Object}.
     *
     * @param ticks The amount of ticks this logout task is valid.
     */
    public LogoutTask(int ticks) {
        this.validity = World.getTicks() + ticks;
    }

    /**
     * Runs the task.
     *
     * @param player The player.
     */
    public abstract void run(Player player);

    /**
     * Fires the logout task, if it is still valid.
     *
     * @param player The player logging out.
     */
    public void fire(Player player) {
        if (isValid()) {
            run(player);
        }
    }

    /**
     * Checks if the logout task is valid.
     *
     * @return <code>True</code> if so.
     */
    public boolean isValid() {
        return validity > World.getTicks();
    }

    /**
     * Sets the new validity delay.
     *
     * @param ticks The amount of ticks this task should remain valid for.
     */
    public void setDelay(int ticks) {
        this.validity = World.getTicks() + ticks;
    }
}
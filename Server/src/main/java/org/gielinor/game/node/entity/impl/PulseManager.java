package org.gielinor.game.node.entity.impl;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.CombatPulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Represents an entity's pulse manager.
 *
 * @author Emperor
 */
public final class PulseManager {

    /**
     * The movement pulse.
     */
    private Pulse current;

    /**
     * Runs a pulse.
     *
     * @param pulse     The pulse.
     * @param pulseType The pulse type we're trying to run.
     */
    public void run(Pulse pulse, String... pulseType) {
        if (!clear(pulseType)) {
            return;
        }
        pulse.start();
        if (pulse.isRunning()) {
            World.submit(current = pulse);
        }
    }

    /**
     * Clears the pulses.
     *
     * @param pulseType The pulse types.
     */
    public boolean clear(String... pulseType) {
        if (current != null && current.isRunning()) {
            if (pulseType.length > 0) {
                for (String type : pulseType) {
                    if (!current.removeFor(type)) {
                        return false;
                    }
                }
            } else if (!current.removeFor("unspecified")) {
                return false;
            }
            current.stop();
        }
        return true;
    }

    /**
     * Runs the unhandled action pulse ("Nothing interesting happens.")
     *
     * @param player    The player.
     * @param pulseType The pulse type.
     * @return The pulse.
     */
    public Pulse runUnhandledAction(final Player player, String... pulseType) {
        Pulse pulse = new Pulse(1, player) {

            @Override
            public boolean pulse() {
                player.getActionSender().sendMessage("Nothing interesting happens.");
                return true;
            }
        };
        run(pulse, pulseType);
        return pulse;
    }

    /**
     * Checks if the current pulse moves the entity.
     *
     * @return <code>True</code> if so.
     */
    public boolean isMovingPulse() {
        return !(current != null && !current.isRunning()) && (current instanceof CombatPulse || current instanceof MovementPulse);
    }

    /**
     * Checks if a pulse is running.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasPulseRunning() {
        return current != null && current.isRunning();
    }

    /**
     * Cancels the death task, if any.
     *
     * @param e The entity.
     */
    public static void cancelDeathTask(Entity e) {
        if (!DeathTask.isDead(e) || e.getPulseManager().current == null) {
            return;
        }
        e.getPulseManager().current.stop();
    }

    /**
     * Sets the current pulse.
     *
     * @param pulse The pulse to set.
     * @deprecated This should only be used by death pulse, use {@link #run(Pulse, String...)} instead.
     */
    @Deprecated
    public void set(Pulse schedule) {
        this.current = schedule;
    }

    /**
     * Gets the current.
     *
     * @return The current.
     */
    public Pulse getCurrent() {
        return current;
    }
}

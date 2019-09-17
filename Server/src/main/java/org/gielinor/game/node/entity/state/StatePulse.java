package org.gielinor.game.node.entity.state;

import java.nio.ByteBuffer;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.world.World;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents a state pulse.
 *
 * @author Emperor
 */
public abstract class StatePulse extends Pulse {

    /**
     * The entity.
     */
    protected final Entity entity;

    /**
     * Constructs a new {@code StatePulse} {@code Object}.
     *
     * @param entity The entity.
     * @param ticks  The amount of ticks.
     */
    public StatePulse(Entity entity, int ticks) {
        super(ticks, entity);
        super.stop();
        this.entity = entity;
    }

    /**
     * Checks if data has to be saved.
     *
     * @return <code>True</code> if so.
     */
    public abstract boolean isSaveRequired();

    /**
     * Saves the state data.
     *
     * @param buffer The buffer.
     */
    public abstract void save(ByteBuffer buffer);

    /**
     * Parses the state data.
     *
     * @param entity The entity.
     * @param buffer The buffer.
     * @return The state pulse created.
     */
    public abstract StatePulse parse(Entity entity, ByteBuffer buffer);

    /**
     * Creates a new instance of this state pulse.
     *
     * @param entity The entity.
     * @param args   The arguments.
     * @return The state pulse.
     */
    public abstract StatePulse create(Entity entity, Object... args);

    /**
     * Checks if this pulse can be ran for the given entity.
     *
     * @param entity The entity.
     * @return <code>True</code> if so.
     */
    public boolean canRun(Entity entity) {
        return true;
    }

    /**
     * Gets the value as a long.
     *
     * @return The value.
     */
    public abstract long getSaveValue();

    /**
     * Parses the value.
     *
     * @param value The value.
     * @return The {@link org.gielinor.game.node.entity.state.StatePulse}.
     */
    public abstract StatePulse parseValue(Entity entity, long value);

    /**
     * Called when the pulse gets manually removed.
     */
    public void remove() {
        /*
         * empty.
         */
    }

    /**
     * Runs the pulse.
     */
    public void run() {
        if (isRunning()) {
            return;
        }
        restart();
        start();
        World.submit(this);
    }

}

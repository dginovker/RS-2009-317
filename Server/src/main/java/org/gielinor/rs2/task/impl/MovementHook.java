package org.gielinor.rs2.task.impl;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * An interface used for hooking to the {@link MovementPulse} class.
 *
 * @author Emperor
 */
public interface MovementHook {

    /**
     * Called when an entity has moved.
     *
     * @param e The moving entity.
     * @param l The location.
     * @return <code>True</code> if no further hooks should be called.
     */
    boolean handle(Entity e, Location l);

}

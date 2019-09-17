package org.gielinor.game.node.entity.state.impl;

import java.nio.ByteBuffer;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.StatePulse;
import org.gielinor.game.world.World;

/**
 * Handles the Gielinor state pulse.
 *
 * @author Emperor
 */
public final class GielinorStatePulse extends StatePulse {

    /**
     * The amount of ticks immunity lasts.
     */
    private static final int IMMUNITY_TICK = 7;

    /**
     * Constructs a new {@code GielinorStatePulse} {@code Object}.
     *
     * @param entity The entity.
     * @param ticks  The ticks to freeze for.
     */
    public GielinorStatePulse(Entity entity, int ticks) {
        super(entity, ticks);
    }

    @Override
    public boolean canRun(Entity entity) {
        return entity.getAttribute("freeze_immunity", -1) < World.getTicks();
    }

    @Override
    public long getSaveValue() {
        return 0;
    }


    @Override
    public StatePulse parseValue(Entity entity, long value) {
        return null;
    }

    @Override
    public void start() {
        super.start();
        entity.getWalkingQueue().reset();
        entity.getLocks().lockMovement(getDelay());
        entity.setAttribute("freeze_immunity", World.getTicks() + getDelay() + IMMUNITY_TICK);
        if (entity instanceof Player) {
            ((Player) entity).getActionSender().sendMessage("You have been frozen!", 1);
        }
    }

    @Override
    public StatePulse create(Entity entity, Object... args) {
        return new GielinorStatePulse(entity, (Integer) args[0]);
    }

    @Override
    public boolean pulse() {
        return true;
    }

    @Override
    public boolean isSaveRequired() {
        return false;
    }

    @Override
    public void save(ByteBuffer buffer) {
        /*
         * empty.
         */
    }

    @Override
    public StatePulse parse(Entity entity, ByteBuffer buffer) {
        return null;
    }

}

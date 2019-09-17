package org.gielinor.game.node.entity.state.impl;

import java.nio.ByteBuffer;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.StatePulse;
import org.gielinor.game.world.World;


/**
 * The pulse used for fire resistant.
 *
 * @author Vexia
 */
public class FireResistantPulse extends StatePulse {

    /**
     * The time to finish.
     */
    private static final int END_TIME = World.getConfiguration().isDevelopmentEnabled() ? 30 : 600;

    /**
     * The current tick.
     */
    private int currentTick;

    /**
     * Constructs a new {@code FireResistantPulse} {@code Object}
     *
     * @param entity the entity.
     * @param ticks  the ticks.
     */
    public FireResistantPulse(Entity entity, int ticks, int currentTick) {
        super(entity, ticks);
        this.currentTick = currentTick;
    }

    @Override
    public boolean isSaveRequired() {
        return true;
    }

    @Override
    public void save(ByteBuffer buffer) {
        buffer.putInt(currentTick);
    }

    @Override
    public StatePulse parse(Entity entity, ByteBuffer buffer) {
        return new FireResistantPulse(entity, 1, buffer.getInt());
    }

    @Override
    public StatePulse create(Entity entity, Object... args) {
        int ticks = args.length > 0 ? (Integer) args[0] : END_TIME;
        return new ChargedStatePulse(entity, ticks);
    }

    @Override
    public long getSaveValue() {
        return getDelay() - getTicksPassed();
    }

    @Override
    public StatePulse parseValue(Entity entity, long value) {
        return create(entity, (int) value);
    }

    @Override
    public boolean pulse() {
        if (entity instanceof Player) {
            if (currentTick == (END_TIME - 25)) {
                entity.asPlayer().getActionSender().sendMessage("<col=4C0E00>Your resistance to dragonfire is about to run out.");
            } else if (currentTick == (END_TIME - 1)) {
                entity.asPlayer().getActionSender().sendMessage("<col=4C0E00>Your resistance to dragonfire has run out.");
            }
        }
        return ++currentTick >= END_TIME;
    }

}

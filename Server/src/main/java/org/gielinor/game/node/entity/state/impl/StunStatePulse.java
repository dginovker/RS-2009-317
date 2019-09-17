package org.gielinor.game.node.entity.state.impl;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.StatePulse;
import org.gielinor.game.world.update.flag.context.Graphics;

import java.nio.ByteBuffer;

/**
 * Handles the stunned state.
 *
 * @author Emperor
 */
public final class StunStatePulse extends StatePulse {

    /**
     * The stun graphic.
     */
    private static final Graphics STUN_GRAPHIC = new Graphics(80, 96);
    private final String stunMessage;

    /**
     * Constructs a new {@code GielinorStatePulse} {@code Object}.
     *
     * @param entity The entity.
     * @param ticks  The ticks to freeze for.
     * @param stunMessage the message to send before saying the player is stunned.
     */
    public StunStatePulse(Entity entity, int ticks, String stunMessage) {
        super(entity, ticks);
        this.stunMessage = stunMessage;
    }

    @Override
    public void start() {
        super.start();
        entity.getWalkingQueue().reset();
        entity.getLocks().lock(getDelay());
        entity.graphics(STUN_GRAPHIC);
        if (entity instanceof Player) {
            ((Player) entity).getAudioManager().send(458);
            if (stunMessage != null) {
                ((Player) entity).getActionSender().sendMessage(stunMessage);
            }
            ((Player) entity).getActionSender().sendMessage("You have been stunned!", 1);
        }
    }

    @Override
    public StatePulse create(Entity entity, Object... args) {
        if (args.length > 1 && args[1] != null) {
            return new StunStatePulse(entity, (Integer) args[0], (String) args[1]);
        }
        return new StunStatePulse(entity, (Integer) args[0], null);
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
    public boolean pulse() {
        if (entity.getAnimator().getGraphics() == STUN_GRAPHIC) {
            entity.graphics(Graphics.create(-1));
        }
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

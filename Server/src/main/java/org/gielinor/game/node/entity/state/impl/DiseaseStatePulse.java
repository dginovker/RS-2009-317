package org.gielinor.game.node.entity.state.impl;

import java.nio.ByteBuffer;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.combat.ImpactHandler.Impact;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.StatePulse;
import org.gielinor.game.world.World;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the diseased state.
 *
 * @author Emperor
 */
public final class DiseaseStatePulse extends StatePulse {

    /**
     * The current value.
     */
    private int value;

    /**
     * Constructs a new {@code DiseaseStatePulse} {@code Object}.
     *
     * @param entity The entity.
     */
    public DiseaseStatePulse(Entity entity) {
        super(entity, 30);
    }

    @Override
    public boolean canRun(Entity entity) {
        return entity.getAttribute("disease:immunity", -1) < World.getTicks();
    }

    @Override
    public long getSaveValue() {
        return value;
    }

    @Override
    public void start() {
        super.start();
        if (entity instanceof Player) {
            ((Player) entity).getActionSender().sendMessage("You have been diseased!", 1);
        }
    }

    @Override
    public boolean pulse() {
        int damage = RandomUtil.random(1, 5);
        entity.getImpactHandler().getImpactQueue().add(new Impact(entity, damage, null, HitsplatType.DISEASE));
        int skill = RandomUtil.random(24);
        if (skill == 3) {
            skill--;
        }
        entity.getSkills().updateLevel(skill, -damage, 0);
        value -= 2;
        if (value < 10) {
            if (entity instanceof Player) {
                ((Player) entity).getActionSender().sendMessage("The disease has wore off.", 1);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isSaveRequired() {
        return value > 9;
    }

    @Override
    public void save(ByteBuffer buffer) {
        buffer.put((byte) value);
    }

    @Override
    public StatePulse parse(Entity entity, ByteBuffer buffer) {
        return create(entity, buffer.get() & 0xFF, entity);
    }

    @Override
    public StatePulse create(Entity entity, Object... args) {
        DiseaseStatePulse pulse = new DiseaseStatePulse(entity);
        pulse.value = (Integer) args[0];
        return pulse;
    }

    /**
     * Gets the value.
     *
     * @return The value.
     */
    public int getValue() {
        return value;
    }

    @Override
    public StatePulse parseValue(Entity entity, long value) {
        return create(entity, (int) value, entity);
    }

    /**
     * Sets the value.
     *
     * @param value The value to set.
     */
    public void setValue(int value) {
        this.value = value;
    }
}
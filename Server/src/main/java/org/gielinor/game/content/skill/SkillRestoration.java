package org.gielinor.game.content.skill;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.world.World;

/**
 * Handles the skill restoration data.
 *
 * @author Emperor
 */
public final class SkillRestoration {

    /**
     * The skill index.
     */
    private final int skillId;

    /**
     * The current tick.
     */
    private int tick;

    /**
     * Constructs a new {@code SkillRestoration} {@code Object}.
     *
     * @param skillId The skill id.
     */
    public SkillRestoration(int skillId) {
        this.skillId = skillId;
        restart(100);
    }

    /**
     * Restores the skill.
     *
     * @param entity The entity.
     */
    public void restore(Entity entity) {
        Skills skills = entity.getSkills();
        if (tick < World.getTicks()) {
            if (skillId == Skills.HITPOINTS) {
                int max = skills.getMaximumLifepoints();
                if (skills.getLifepoints() != max) {
                    skills.heal(skills.getLifepoints() < max ? 1 : -1);
                }
            } else {
                int dynamic = skills.getLevel(skillId);
                int stat = skills.getStaticLevel(skillId);
                if (dynamic != stat) {
                    skills.updateLevel(skillId, dynamic < stat ? 1 : -1, stat);
                }
            }
            int ticks = 100;
            if (entity instanceof Player) {
                if (((Player) entity).getPrayer().get(PrayerType.BERSERKER)) {
                    ticks = 75;
                }
            }
            restart(ticks);
        }
    }

    /**
     * Gets the tick.
     *
     * @return The tick.
     */
    public int getTick() {
        return tick;
    }

    /**
     * Sets the tick.
     *
     * @param tick The tick to set.
     */
    public void setTick(int tick) {
        this.tick = tick;
    }

    /**
     * Gets the skillId.
     *
     * @return The skillId.
     */
    public int getSkillId() {
        return skillId;
    }

    /**
     * Restarts the restoration.
     */
    public void restart(int ticks) {
        this.tick = World.getTicks() + ticks;
    }
}
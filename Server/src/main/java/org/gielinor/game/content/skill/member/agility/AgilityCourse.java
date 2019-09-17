package org.gielinor.game.content.skill.member.agility;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an agility course.
 *
 * @author Emperor
 */
public abstract class AgilityCourse extends OptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AgilityCourse.class);

    /**
     * The player doing the agility course.
     */
    private final Player player;

    /**
     * The obstacles the player has already passed.
     */
    private final boolean[] obstaclesPassed;

    /**
     * The completion experience.
     */
    private double completionExperience;

    /**
     * Constructs a new {@code AgilityCourse} {@code Object}.
     *
     * @param player               The player.
     * @param size                 The amount of obstacles in the course.
     * @param completionExperience The amount of experience to gain upon completing the course.
     */
    public AgilityCourse(Player player, int size, double completionExperience) {
        this.player = player;
        this.obstaclesPassed = new boolean[size];
        this.completionExperience = completionExperience;
    }

    @Override
    public final Plugin<Object> newInstance(Object arg) {
        if (arg == null) {
            configure();
            return this;
        }
        return createInstance((Player) arg);
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Configures the course.
     */
    public abstract void configure();

    /**
     * Creates a new instance for the player.
     *
     * @param player The player.
     * @return The agility course instance.
     */
    public abstract AgilityCourse createInstance(Player player);

    /**
     * Gets the agility course for the given player.
     * If the player didn't have the course as an extension, an extension will be added.
     *
     * @param player The player.
     * @return The agility course instance.
     */
    public AgilityCourse getCourse(Player player) {
        AgilityCourse course = player.getExtension(AgilityCourse.class);
        if (course == null || course.getClass() != getClass()) {
            try {
                player.addExtension(AgilityCourse.class, course = createInstance(player));
            } catch (Throwable ex) {
                log.error("Error adding extension: {}.", course, ex);
            }
        }
        return course;
    }

    /**
     * Finishes the lap by checking for completion and resetting.
     */
    public void finish() {
        if (isCompleted()) {
            player.getSkills().addExperience(Skills.AGILITY, completionExperience);
        }
        reset();
    }

    /**
     * Checks if the course has been completed.
     *
     * @return <code>True</code> if so.
     */
    public boolean isCompleted() {
        for (boolean anObstaclesPassed : obstaclesPassed) {
            if (!anObstaclesPassed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Resets the currently passed obstacles in the course.
     */
    public void reset() {
        for (int i = 0; i < obstaclesPassed.length; i++) {
            obstaclesPassed[i] = false;
        }
    }

    /**
     * Flags an obstacles as passed.
     *
     * @param index The obstacle index.
     */
    public void flag(int index) {
        obstaclesPassed[index] = true;
        if (index == obstaclesPassed.length - 1) {
            finish();
        }
    }

    /**
     * Gets the amount of damage to deal.
     *
     * @param player The player.
     * @return The amount of damage.
     */
    protected static int getHitAmount(Player player) {
        int hit = player.getSkills().getLifepoints() / 12;
        if (hit < 2) {
            hit = 2;
        }
        return hit;
    }


    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the passed obstacles flags.
     *
     * @return The passed obstacles flags.
     */
    public boolean[] getPassedObstacles() {
        return obstaclesPassed;
    }

}

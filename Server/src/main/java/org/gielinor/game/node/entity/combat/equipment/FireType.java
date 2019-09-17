package org.gielinor.game.node.entity.combat.equipment;

import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.task.NodeTask;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * The fire types.
 *
 * @author Emperor
 *
 */
public enum FireType {

    /**
     * The normal dragonfire fire type.
     */
    FIERY_BREATH(new Animation(81, Priority.HIGH), 393, new NodeTask(0) {

        @Override
        public boolean run(Node node, Node... n) {
            return true;
        }
    }),

    /**
     * The shocking breath fire type.
     */
    SHOCKING_BREATH(new Animation(84, Priority.HIGH), 396, new NodeTask(0) {

        @Override
        public boolean run(Node node, Node... n) {
            if (RandomUtil.random(10) < 3) {
                ((Entity) node).getSkills().updateLevel(RandomUtil.random(3), -5, 0);
                if (node instanceof Player) {
                    ((Player) node).getActionSender().sendMessage("You have been shocked.");
                }
            }
            return true;
        }
    }),

    /**
     * The toxic breath fire type.
     */
    TOXIC_BREATH(new Animation(82, Priority.HIGH), 394, new NodeTask(0) {

        @Override
        public boolean run(Node node, Node... n) {
            ((Entity) node).getStateManager().register(EntityState.POISONED, false, 80, (Entity) n[0]);
            return true;
        }
    }),

    /**
     * The freezing breath fire type.
     */
    ICY_BREATH(new Animation(83, Priority.HIGH), 395, new NodeTask(0) {

        @Override
        public boolean run(Node node, Node... n) {
            ((Entity) node).getStateManager().set(EntityState.FROZEN, 7);
            return true;
        }
    });

    /**
     * The attack animation.
     */
    private final Animation animation;

    /**
     * The projectile id.
     */
    private final int projectileId;

    /**
     * The breath effect.
     */
    private final NodeTask task;

    /**
     * Constructs a new {@code FireType} {@code Object}.
     *
     * @param animation
     *            The animation.
     */
    private FireType(Animation animation, int projectileId, NodeTask breathEffect) {
        this.animation = animation;
        this.projectileId = projectileId;
        task = breathEffect;
    }

    /**
     * Gets the animation.
     *
     * @return The animation.
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Gets the projectileId.
     *
     * @return The projectileId.
     */
    public int getProjectileId() {
        return projectileId;
    }

    /**
     * Gets the task.
     *
     * @return The task.
     */
    public NodeTask getTask() {
        return task;
    }

}

package plugin.combat.special;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Bone dagger special attack "Backstab".
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Bone_dagger
 *
 * @author Emperor
 *
 */
public final class BackstabSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 75;
    private static final Animation ANIMATION = new Animation(4198, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(704);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(8872, this);
        CombatStyle.MELEE.getSwingHandler().register(8874, this);
        CombatStyle.MELEE.getSwingHandler().register(8876, this);
        CombatStyle.MELEE.getSwingHandler().register(8878, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        int hit = 0;
        double accuracy = 1.0;
        if (!victim.getProperties().getCombatPulse().isAttacking()) {
            accuracy = 1.75;
        }
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, accuracy, 0.98)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.0));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.0));
            victim.getSkills().updateLevel(Skills.DEFENCE, -hit / 10, 0);
        }
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }
}

package plugin.combat.special;

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
 * Handles the Dragon dagger special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_dagger
 *
 * @author Emperor
 * @version 1.0
 */
public final class PunctureSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 25;
    private static final Animation ANIMATION = new Animation(1062, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(252, 96);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public void impact(Entity entity, Entity victim, BattleState battleState) {
        handleExtraEffects(victim, battleState);
        victim.getImpactHandler().handleImpact(entity, battleState.getEstimatedHit(), CombatStyle.MELEE, battleState);
        victim.getImpactHandler().handleImpact(entity, battleState.getSecondaryHit(), CombatStyle.MELEE, battleState, null, true);
    }

    @Override public Plugin<Object> newInstance(Object arg) throws Throwable {
        if (CombatStyle.MELEE.getSwingHandler().register(1215, this)
            && CombatStyle.MELEE.getSwingHandler().register(1231, this)
            && CombatStyle.MELEE.getSwingHandler().register(5680, this)
            && CombatStyle.MELEE.getSwingHandler().register(5698, this)
            && CombatStyle.MELEE.getSwingHandler().register(13465, this)
            && CombatStyle.MELEE.getSwingHandler().register(13466, this)
            && CombatStyle.MELEE.getSwingHandler().register(13467, this)) {
            CombatStyle.MELEE.getSwingHandler().register(13468, this);
        }
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {

        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY))
            return -1;

        int hit = 0;

        // First hit
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.2, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.1306));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.1306));
        }
        state.setEstimatedHit(hit);

        // Second hit
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.2, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.1306));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.1306));
        } else
            hit = 0;

        entity.asPlayer().getAudioManager().send(2537);
        state.setSecondaryHit(hit);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }
}

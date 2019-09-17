package plugin.combat.special;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;


/**
 * Handles the Barrelchest anchor's special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Barrelchest_anchor
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class SunderSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 50;
    private static final Animation ANIMATION = new Animation(5870, Animator.Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(1027);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(10887, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {

        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY))
            return -1;

        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.5, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.0));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.0));
        }
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }
}

package plugin.combat.special;

import org.gielinor.constants.ToxicBlowpipeConstants;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.ToxicBlowpipeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Toxic blowpipe special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Toxic_blowpipe
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 * @version 1.0
 */
public final class ToxicSiphonSpecial extends ToxicBlowpipeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 50;
    private static final Animation ANIMATION = new Animation(21208, Priority.HIGH);
    private static final Projectile PROJECTILE = Projectile.create((Entity) null, null, 2049, 40, 36, 32, 32, 15, 11);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        CombatStyle.RANGE.getSwingHandler().register(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState battleState) {
        configureRangeData(entity, battleState);
        if (battleState.getWeapon() == null || !hasAmmo(entity, battleState)) {
            entity.getProperties().getCombatPulse().stop();
            return -1;
        }
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.RANGE)) {
            int max = calculateHit(entity, victim, 1.0);
            battleState.setMaximumHit(max);
            hit = RandomUtil.random(max);
            if (hit >= 2) {
                hit = (hit + (hit / 2));
                entity.getSkills().heal(hit / 2);
            }
        }
        battleState.setEstimatedHit(hit);
        useAmmo(entity, battleState);
        return 1 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState battleState) {
        entity.animate(ANIMATION);
        PROJECTILE.transform(entity, victim, entity instanceof NPC, 58, 10).send();
    }
}

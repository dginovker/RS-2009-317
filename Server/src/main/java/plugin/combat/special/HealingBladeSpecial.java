package plugin.combat.special;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles Saradomin godsword's special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Saradomin_godsword
 *
 * @author Emperor
 * @version 1.0
 */
public final class HealingBladeSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 50;
    private static final Animation ANIMATION = new Animation(7071, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(1220);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(11698, this);
        CombatStyle.MELEE.getSwingHandler().register(13452, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.12, 0.98)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.005));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.005));
        }
        state.setEstimatedHit(hit);
        int healthRestore = hit / 2;
        double prayerRestore = hit * 0.25;
        if (healthRestore < 10) {
            healthRestore = 10;
        }
        if (prayerRestore < 5) {
            prayerRestore = 5;
        }
        entity.getSkills().heal(healthRestore);
        entity.getSkills().incrementPrayerPoints(prayerRestore);
        entity.asPlayer().getAudioManager().send(new Audio(3857), true);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }

}

package plugin.combat.special;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Dragon scimitar special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_scimitar
 *
 * @author Emperor
 * @version 1.0
 */
public final class SeverSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 55;
    private static final Animation ANIMATION = new Animation(1872, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(347, 96);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        if (CombatStyle.MELEE.getSwingHandler().register(4587, this)) {
            CombatStyle.MELEE.getSwingHandler().register(13477, this);
        }
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY))
            return -1;
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.124, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.0));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.0));
            if (victim instanceof Player) {
                Player p = (Player) victim;
                if (p.getPrayer().get(PrayerType.PROTECT_FROM_MAGIC))
                    p.getPrayer().toggle(PrayerType.PROTECT_FROM_MAGIC);

                if (p.getPrayer().get(PrayerType.PROTECT_FROM_MELEE))
                    p.getPrayer().toggle(PrayerType.PROTECT_FROM_MELEE);

                if (p.getPrayer().get(PrayerType.PROTECT_FROM_MISSILES))
                    p.getPrayer().toggle(PrayerType.PROTECT_FROM_MISSILES);

                if (p.getPrayer().get(PrayerType.DEFLECT_MAGIC))
                    p.getPrayer().toggle(PrayerType.DEFLECT_MAGIC);

                if (p.getPrayer().get(PrayerType.DEFLECT_MELEE))
                    p.getPrayer().toggle(PrayerType.DEFLECT_MELEE);

                if (p.getPrayer().get(PrayerType.DEFLECT_MISSILES))
                    p.getPrayer().toggle(PrayerType.DEFLECT_MISSILES);

                if (p.getPrayer().get(PrayerType.DEFLECT_SUMMONING))
                    p.getPrayer().toggle(PrayerType.DEFLECT_SUMMONING);
            }
        }
        entity.asPlayer().getAudioManager().send(2540);
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }
}

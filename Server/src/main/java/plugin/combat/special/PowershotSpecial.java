package plugin.combat.special;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.RangeSwingHandler;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Magic (comp) longbow special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Magic_longbow
 *  - http://oldschoolrunescape.wikia.com/wiki/Magic_comp_bow
 *
 * @author Emperor
 * @version 1.0
 */
public final class PowershotSpecial extends RangeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 35;
    private static final Graphics GRAPHIC = new Graphics(250, 96);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.RANGE.getSwingHandler().register(859, this);
        CombatStyle.RANGE.getSwingHandler().register(10284, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        Player p = (Player) entity;
        configureRangeData(p, state);
        if (state.getWeapon() == null || !hasAmmo(entity, state)) {
            entity.getProperties().getCombatPulse().stop();
            p.getSettings().toggleSpecialBar();
            return -1;
        }
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.RANGE, 1.98, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.0));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.0));
        }
        entity.asPlayer().getAudioManager().send(2536);
        state.setEstimatedHit(hit);
        useAmmo(entity, state, victim.getLocation());
        return 1 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(state.getRangeWeapon().getAnimation(), GRAPHIC);
        int speed = (int) (46 + (entity.getLocation().getDistance(victim.getLocation()) * 5));
        Projectile.create(entity, victim, 256, 40, 36, 45, speed, 5, 11).send();
    }
}

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
 * Handles the Abyssal whip's Energy drain special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Abyssal_whip
 *
 * @author Emperor
 * @version 1.0
 */
public final class EnergyDrainSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 50;
    private static final Animation ANIMATION = new Animation(1658, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(341, 96);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.MELEE.getSwingHandler().register(4151, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.2, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1));
            hit = RandomUtil.random(calculateHit(entity, victim, 1));
        }
        if (victim instanceof Player) {
            ((Player) victim).getSettings().updateRunEnergy(10);
            ((Player) entity).getSettings().updateRunEnergy(-10);
        }
        state.setEstimatedHit(hit);
        entity.asPlayer().getAudioManager().send(new Audio(1081), true);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.animate(ANIMATION);
        victim.graphics(GRAPHIC);
    }
}

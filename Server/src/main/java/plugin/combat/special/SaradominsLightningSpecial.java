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
 * Handles the Saradomin sword special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Saradomin_sword
 *
 * @author Emperor
 * @version 1.0
 */
public final class SaradominsLightningSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 100;
    private static final Animation ANIMATION = new Animation(7072, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(1224);

    @Override public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(11730, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        int hit = 0;
        int secondary = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.10, 0.98)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.1));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.1));
            secondary = 5 + RandomUtil.RANDOM.nextInt(14);
        }
        state.setEstimatedHit(hit);
        state.setSecondaryHit(secondary);
        entity.asPlayer().getAudioManager().send(new Audio(3853), true);
        return 1;
    }

    @Override public void impact(Entity entity, Entity victim, BattleState battleState) {
        handleExtraEffects(victim, battleState);
        int hit = battleState.getEstimatedHit();
        if (hit > -1) {
            victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MELEE, battleState);
        }
        hit = battleState.getSecondaryHit();
        if (hit > -1) {
            victim.graphics(Graphics.create(1194));
            victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC, battleState);
        }
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }
    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }
}

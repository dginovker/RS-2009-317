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
 * Handles the Abyssal dagger's special attack.
 *
 * TODO: find corresponding graphic id.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Abyssal_dagger
 *
 * @author Stan van der Bend
 * @version 1.0
 */
public final class AbyssalPunctureSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 25;
    private static final Animation ANIMATION = new Animation(1062, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(252, 96);
    private static final Audio SOUND = new Audio(2537);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }
    @Override public void impact(Entity entity, Entity victim, BattleState battleState) {
        handleExtraEffects(victim, battleState);
        victim.getImpactHandler().handleImpact(entity, battleState.getEstimatedHit(), CombatStyle.MELEE, battleState);
        victim.getImpactHandler().handleImpact(entity, battleState.getSecondaryHit(), CombatStyle.MELEE, battleState, null, true);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) {
       CombatStyle.MELEE.getSwingHandler().register(13265, this);
       return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        boolean firstAccuracte = false;

        int firstHit = 0, secondHit = 0;

        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.25, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.0D));
            firstHit = RandomUtil.random(calculateHit(entity, victim, 0.85D));
            firstAccuracte = true;
        }

        // Second hit
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.25, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.0D));
            secondHit = RandomUtil.random(calculateHit(entity, victim, 0.85D));
        } else
            secondHit = 0;

        if(firstAccuracte) {
            state.setEstimatedHit(firstHit);
            state.setSecondaryHit(secondHit);
        }

        entity.asPlayer().getAudioManager().send(SOUND);
        return 1;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }
}

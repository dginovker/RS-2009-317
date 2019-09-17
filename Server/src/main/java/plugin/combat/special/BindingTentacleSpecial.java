package plugin.combat.special;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.concurrent.TimeUnit;

/**
 * Handles the Abyssal tentacle special attack.
 *
 * TODO: look into increasing the victim's susceptibility for poison.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Abyssal_tentacle
 *
 * @author Stan van der Bend
 * @version 1.0
 */
public final class BindingTentacleSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 50;
    private static final Animation ANIMATION = new Animation(1658, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(341, 96);
    private static final Audio SOUND = new Audio(1081);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(12006, this);
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE)) {
            state.setMaximumHit(calculateHit(entity, victim, 1));
            hit = RandomUtil.random(calculateHit(entity, victim, 1));
        }

        victim.getWalkingQueue().reset();
        victim.getStateManager().register(EntityState.FROZEN, false, 5);

        state.setEstimatedHit(hit);
        entity.asPlayer().getAudioManager().send(SOUND, true);
        return 1;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.animate(ANIMATION);
        victim.graphics(GRAPHIC);
    }
}

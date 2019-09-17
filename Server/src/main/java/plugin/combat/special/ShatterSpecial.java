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
 * Handles the Dragon mace special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_mace
 *
 * @author Emperor
 * @version 1.0
 */
public final class ShatterSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 25;
    private static final Animation ANIMATION = new Animation(1060, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(251, 96);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(1434, this);
        CombatStyle.MELEE.getSwingHandler().register(13479, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 0.87, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.3546));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.3546));
        }
        entity.asPlayer().getAudioManager().send(new Audio(2541), true);
        state.setEstimatedHit(hit);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }
}

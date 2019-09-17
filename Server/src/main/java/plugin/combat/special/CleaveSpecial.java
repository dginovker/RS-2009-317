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
 * Represents the Dragon longsword's special handler.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_longsword
 *
 * @author Emperor
 * @version 1.0
 */
public final class CleaveSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 25;
    private static final Animation ANIMATION = new Animation(1058, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(248, 96);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        if (CombatStyle.MELEE.getSwingHandler().register(1305, this)) {
            CombatStyle.MELEE.getSwingHandler().register(13475, this);
        }
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.MELEE, 1.18, 1.0)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.2203));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.2203));
        }
        state.setEstimatedHit(hit);
        entity.asPlayer().getAudioManager().send(new Audio(2529), true);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }
}

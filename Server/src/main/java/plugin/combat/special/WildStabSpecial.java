package plugin.combat.special;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Dragon sword special attack.
 *
 * TODO: find the correct sound
 * TODO: find the correct anim
 * TODO: find the correct GFX
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_sword
 *
 * @author Stan van der Bend
 */
public final class WildStabSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 40;
    private static final Animation ANIMATION = new Animation(1, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(1);
    private static final Audio SOUND = new Audio(1);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(21009, this);
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        final Player player = (Player) entity;

        if (!player.getSettings().drainSpecial(SPECIAL_ENERGY))
            return -1;

        player.visualize(ANIMATION, GRAPHIC);

        int hit = 0;
        if (isAccurateImpact(entity, victim, null, 1.25D, 1.0D)) {
            hit = RandomUtil.random(calculateHit(entity, victim, 1.25));
        }

        victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MELEE);
        player.getAudioManager().send(SOUND, true);
        return 1;
    }

}

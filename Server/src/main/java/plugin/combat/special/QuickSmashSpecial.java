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
 * Handles the Granite maul special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Granite_maul
 *
 * @author Emperor
 * @version 1.0
 */
public final class QuickSmashSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 50;
    private static final Animation ANIMATION = new Animation(1667, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(340, 96);

    @Override public Object fireEvent(String identifier, Object... args) {
        switch (identifier) {
            case "instant_spec":
                return true;
        }
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(4153, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        Player p = (Player) entity;
        if (victim == null) {
            victim = p.getProperties().getCombatPulse().getLastVictim();
            if (victim == null || World.getTicks() - p.getAttribute("combat-stop", -1) > 2 || !MeleeSwingHandler.canMelee(p, victim, 1)) {
                p.getActionSender().sendMessage("Warning: Since the maul's special is an instant attack, it will be wasted when used ");
                p.getActionSender().sendMessage("on a first strike.");
                return -1;
            }
        }
        if (!p.getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        visualize(entity, victim, null);
        int hit = 0;
        if (isAccurateImpact(entity, victim)) {
            state.setMaximumHit(calculateHit(entity, victim, 1.0));
            hit = RandomUtil.random(calculateHit(entity, victim, 1.0));
        }
        victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MELEE);
        entity.asPlayer().getAudioManager().send(new Audio(2715), true);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
        victim.animate(victim.getProperties().getDefenceAnimation());
    }

}

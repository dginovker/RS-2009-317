package plugin.combat.special;

import java.util.List;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.InteractionType;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Dragon 2h sword special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_2h_sword
 *
 * @author Emperor
 * @version 1.0
 */
public final class PowerstabSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 60;
    private static final Animation ANIMATION = new Animation(3157, Priority.HIGH);
    private static final Graphics GRAPHIC = new Graphics(1225);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public void impact(Entity entity, Entity victim, BattleState battleState) {
        if (battleState.getTargets() != null) {
            for (BattleState bs : battleState.getTargets()) {
                if (bs != null) {
                    handleExtraEffects(victim, bs);
                    bs.getVictim().getImpactHandler().handleImpact(entity, bs.getEstimatedHit(), CombatStyle.MELEE, bs);
                }
            }
            return;
        }
        handleExtraEffects(victim, battleState);
        victim.getImpactHandler().handleImpact(entity, battleState.getEstimatedHit(), CombatStyle.MELEE, battleState);
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(7158, this);
        return this;
    }

    @Override public int swing(Entity entity, Entity victim, BattleState state) {
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        boolean multi = entity.getProperties().isMultiZone();
        if (!multi) {
            return super.swing(entity, victim, state);
        }
        @SuppressWarnings("rawtypes")
        List list = victim instanceof NPC ? RegionManager.getSurroundingNPCs(entity, 9, entity) : RegionManager.getSurroundingPlayers(entity, 9, entity);
        BattleState[] targets = new BattleState[list.size()];
        int count = 0;
        for (Object o : list) {
            Entity e = (Entity) o;
            if (CombatStyle.RANGE.getSwingHandler().canSwing(entity, e) != InteractionType.NO_INTERACT) {
                BattleState s = targets[count++] = new BattleState(entity, e);
                int hit = 0;
                if (isAccurateImpact(entity, e)) {
                    state.setMaximumHit(calculateHit(entity, victim, 1.0));
                    hit = RandomUtil.random(calculateHit(entity, e, 1.0));
                }
                s.setEstimatedHit(hit);
            }
        }
        state.setTargets(targets);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }

    @Override public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
        if (state.getTargets() != null) {
            for (BattleState s : state.getTargets()) {
                if (s != null) {
                    s.getVictim().animate(victim.getProperties().getDefenceAnimation());
                }
            }
            return;
        }
        victim.animate(victim.getProperties().getDefenceAnimation());
    }
}

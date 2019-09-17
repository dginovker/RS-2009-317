package org.gielinor.game.node.entity.combat.handlers;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.InteractionType;
import org.gielinor.game.node.entity.combat.equipment.SwitchAttack;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles combat swings with switching combat styles.
 *
 * @author Emperor
 */
public class MultiSwingHandler extends CombatSwingHandler {

    /**
     * The attacks available.
     */
    private final SwitchAttack[] attacks;

    /**
     * If the entity has to be in melee distance to switch to melee.
     */
    private final boolean meleeDistance;

    /**
     * The current attack.
     */
    protected SwitchAttack current;

    /**
     * The current attack.
     */
    protected SwitchAttack next;

    /**
     * Constructs a new {@code MultiSwingHandler} {@code Object}.
     *
     * @param attacks The available attacks.
     */
    public MultiSwingHandler(SwitchAttack... attacks) {
        this(true, attacks);
    }

    /**
     * Constructs a new {@code MultiSwingHandler} {@code Object}.
     *
     * @param meleeDistance If the entity has to be in melee distance to switch to melee.
     * @param attacks       The available attacks.
     */
    public MultiSwingHandler(boolean meleeDistance, SwitchAttack... attacks) {
        super(CombatStyle.RANGE);
        this.next = current = attacks[0];
        this.meleeDistance = meleeDistance && !(attacks.length == 1 && attacks[0].getStyle() == CombatStyle.MELEE);
        this.attacks = attacks;
    }

    public SwitchAttack getNext(Entity entity, Entity victim, BattleState state, int index) {
        SwitchAttack pick = attacks[index];
        while (!pick.canSelect(entity, victim, state)) {
            index = RandomUtil.randomize(attacks.length);
            pick = attacks[index];
        }
        return pick;
    }

    @Override
    public InteractionType canSwing(Entity entity, Entity victim) {
        if (meleeDistance) {
            return CombatStyle.RANGE.getSwingHandler().canSwing(entity, victim);
        }
        return next.getHandler().canSwing(entity, victim);
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        current = next;
        if (meleeDistance && current.getStyle() == CombatStyle.MELEE && CombatStyle.MELEE.getSwingHandler().canSwing(entity, victim) != InteractionType.STILL_INTERACT) {
            for (SwitchAttack attack : attacks) {
                if (attack.getStyle() != CombatStyle.MELEE) {
                    current = attack;
                    break;
                }
            }
        }
        CombatStyle style = current.getStyle();
        setType(style);
        int index = RandomUtil.randomize(attacks.length);
        SwitchAttack pick = getNext(entity, victim, state, index);
        next = pick;
        if (current.isUseHandler()) {
            return current.getHandler().swing(entity, victim, state);
        }
        int ticks = 1;
        if (style != CombatStyle.MELEE) {
            ticks += (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * (getType() == CombatStyle.MAGIC ? 0.5 : 0.3));
        }
        int hit = 0;
        if (isAccurateImpact(entity, victim, style)) {
            int max = calculateHit(entity, victim, 1.0);
            state.setMaximumHit(max);
            hit = RandomUtil.random(max);
        }
        state.setEstimatedHit(hit);
        state.setStyle(style);
        return ticks;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        if (current.isUseHandler()) {
            current.getHandler().visualize(entity, victim, state);
            return;
        }
        entity.visualize(current.getAnimation(), current.getStartGraphic());
        if (current.getProjectile() != null) {
            current.getProjectile().transform(entity, victim, entity instanceof NPC, 46, current.getStyle() == CombatStyle.MAGIC ? 10 : 5).send();
        }
    }

    @Override
    public void impact(Entity entity, Entity victim, BattleState battleState) {
        if (current.isUseHandler()) {
            handleExtraEffects(victim, battleState);
            current.getHandler().impact(entity, victim, battleState);
            return;
        }
        BattleState[] targets = battleState.getTargets();
        if (targets == null) {
            targets = new BattleState[]{ battleState };
        }
        for (BattleState bs : targets) {
            handleExtraEffects(victim, bs);
            int hit = bs.getEstimatedHit();
            if (hit > -1) {
                victim.getImpactHandler().handleImpact(entity, hit, current.getStyle(), bs);
            }
            hit = bs.getSecondaryHit();
            if (hit > -1) {
                victim.getImpactHandler().handleImpact(entity, hit, current.getStyle(), bs);
            }
            hit = bs.getThirdHit();
            if (hit > -1) {
                victim.getImpactHandler().handleImpact(entity, hit, current.getStyle(), bs);
            }
            hit = bs.getFourthHit();
            if (hit > -1) {
                victim.getImpactHandler().handleImpact(entity, hit, current.getStyle(), bs);
            }
        }
    }

    @Override
    public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
        if (current.isUseHandler()) {
            current.getHandler().adjustBattleState(entity, victim, state);
            return;
        }
        super.adjustBattleState(entity, victim, state);
    }

    @Override
    public void addExperience(Entity entity, Entity victim, BattleState state) {

    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
        if (current.isUseHandler()) {
            current.getHandler().visualizeImpact(entity, victim, state);
            return;
        }
        victim.visualize(victim.getProperties().getDefenceAnimation(), current.getEndGraphic());
    }

    @Override
    public int calculateAccuracy(Entity entity) {
        return current.getHandler().calculateAccuracy(entity);
    }

    @Override
    public int calculateHit(Entity entity, Entity victim, double modifier) {
        return current.getHandler().calculateHit(entity, victim, modifier);
    }

    @Override
    public int calculateDefence(Entity entity, Entity attacker) {
        return current.getHandler().calculateDefence(entity, attacker);
    }

    @Override
    public double getSetMultiplier(Entity e, int skillId) {
        return current.getHandler().getSetMultiplier(e, skillId);
    }

    /**
     * Gets the meleeDistance.
     *
     * @return The meleeDistance.
     */
    public boolean isMeleeDistance() {
        return meleeDistance;
    }

    /**
     * Gets the attacks available.
     *
     * @return The attacks.
     */
    public SwitchAttack[] getAttacks() {
        return attacks;
    }

}

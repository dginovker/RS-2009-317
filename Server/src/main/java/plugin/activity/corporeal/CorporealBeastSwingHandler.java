package plugin.activity.corporeal;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.InteractionType;
import org.gielinor.game.node.entity.combat.equipment.ArmourSet;
import org.gielinor.game.node.entity.impl.Animator;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the Corporeal Beast NPC.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CorporealBeastSwingHandler extends CombatSwingHandler {

    /**
     * The melee attack animation.
     */
    private static final Animation MELEE_ATTACK = new Animation(10057, Animator.Priority.HIGH);

    /**
     * The range attack animation.
     */
    private static final Animation MAGIC_ATTACK = new Animation(10053, Animator.Priority.HIGH);

    /**
     * The offsets for the mage GFX.
     */
    private static final int[][] PROJECTILE_OFFSETS = new int[][]{
        { -3, 0 },
        { 3, 2 },
        { 0, -3 },
        { -3, 2 },
        { 3, -1 }
    };

    public int lastSwing;
    /**
     * The {@link java.security.SecureRandom}.
     */
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * The list of entities to hit with the magic projectile.
     */
    private final List<Entity> hitList = new ArrayList<>();

    /**
     * Constructs a new {@code CorporealBeastSwingHandler}.
     */
    public CorporealBeastSwingHandler() {
        super(CombatStyle.MELEE);
    }

    @Override
    public InteractionType canSwing(Entity entity, Entity victim) {
        return CombatStyle.MELEE.getSwingHandler().canSwing(entity, victim);
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        int ticks = 1;
        if (secureRandom.nextDouble() < 0.5) {
            int hit = 0;
            if (CombatStyle.MELEE.getSwingHandler().isAccurateImpact(entity, victim, CombatStyle.MELEE)) {
                int max = 51;//CombatStyle.MELEE.getSwingHandler().calculateHit(entity, victim, 1.0);
                hit = RandomUtil.random(max);
                state.setMaximumHit(max);
            }
            state.setEstimatedHit(hit);
            state.setStyle(CombatStyle.MELEE);
        } else {
            ticks += (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
            NPC npc = (NPC) entity;
            List<BattleState> list = new ArrayList<>();
            for (Entity t : RegionManager.getLocalPlayers(npc, 14)) {
                if (!CorporealBeastNPC.LAIR.insideBorder(t.getLocation())) {
                    continue;
                }
                if (t.isAttackable(npc, CombatStyle.MAGIC)) {
                    list.add(new BattleState(entity, t));
                }
            }
            BattleState[] targets;
            state.setStyle(CombatStyle.MAGIC);
            state.setTargets(targets = list.toArray(new BattleState[list.size()]));
            for (BattleState s : targets) {
                if (!hitList.contains(s.getVictim())) {
                    continue;
                }
                s.setStyle(CombatStyle.MAGIC);
                int hit = 0;
                if (isAccurateImpact(entity, s.getVictim(), CombatStyle.MAGIC)) {
                    int max = 65;//calculateHit(entity, s.getVictim(), 1.0);
                    s.setMaximumHit(max);
                    hit = secureRandom.nextInt(max);
                }
                s.setEstimatedHit(hit);
            }
            hitList.clear();
        }
        return ticks;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState battleState) {
        switch (battleState.getStyle()) {
            case MELEE:
                entity.animate(MELEE_ATTACK);
                break;
            default:
                entity.animate(MAGIC_ATTACK);
                Projectile.send(Projectile.create(entity, battleState.getVictim(), 1824, 46, 32, 56, 75, 3, 11));
                for (int[] offset : PROJECTILE_OFFSETS) {
                    Location otherLocation = battleState.getVictim().getLocation().transform(offset[0], offset[1], 0);
                    for (BattleState battleState1 : battleState.getTargets()) {
                        if (battleState1.getVictim() == null) {
                            continue;
                        }
                        if (battleState1.getVictim().getLocation().getDistance(battleState.getVictim().getLocation()) < 4) {
                            hitList.add(battleState1.getVictim());
                        }
                    }
                    hitList.add(battleState.getVictim());
                    Projectile.send(Projectile.create(battleState.getVictim().getLocation(), otherLocation, 1825, 46, 32, 56, 100, 3, 11));
                }
                break;
        }
    }

    @Override
    public ArmourSet getArmourSet(Entity e) {
        return getType().getSwingHandler().getArmourSet(e);
    }

    @Override
    public double getSetMultiplier(Entity e, int skillId) {
        return getType().getSwingHandler().getSetMultiplier(e, skillId);
    }

    @Override
    public void impact(Entity entity, Entity victim, BattleState battleState) {
        if (battleState.getStyle() == CombatStyle.MELEE) {
            handleExtraEffects(victim, battleState);
            battleState.getStyle().getSwingHandler().impact(entity, victim, battleState);
            return;
        }
        for (BattleState s : battleState.getTargets()) {
            if (s == null || s.getEstimatedHit() < 0) {
                continue;
            }
            if (!hitList.contains(s.getVictim())) {
                continue;
            }
            handleExtraEffects(s.getVictim(), s);
            double hitFormat = secureRandom.nextDouble();
            int hit = secureRandom.nextInt(s.getVictim() == battleState.getVictim() ? 65 : 30);
            if (s.getVictim().hasProtectionPrayer(battleState.getStyle())) {
                if (hitFormat >= 0.3 && hit > 4) {
                    hit = hit / 3;
                } else {
                    hit = 0;
                }
            }
            s.getVictim().getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC, s);
        }
    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
        if (state.getStyle() == CombatStyle.MELEE) {
            victim.animate(victim.getProperties().getDefenceAnimation());
            return;
        }
        for (BattleState s : state.getTargets()) {
            s.getVictim().animate(s.getVictim().getProperties().getDefenceAnimation());
        }
    }

    @Override
    public void adjustBattleState(Entity entity, Entity victim, BattleState battleState) {
        if (battleState.getStyle() == CombatStyle.MELEE) {
            battleState.getStyle().getSwingHandler().adjustBattleState(entity, victim, battleState);
            return;
        }
        for (BattleState bs : battleState.getTargets()) {
            CombatStyle.MAGIC.getSwingHandler().adjustBattleState(entity, bs.getVictim(), bs);
        }
    }

    @Override
    public int calculateAccuracy(Entity entity) {
        return getType().getSwingHandler().calculateAccuracy(entity);
    }

    @Override
    public int calculateDefence(Entity entity, Entity attacker) {
        return getType().getSwingHandler().calculateDefence(entity, attacker);
    }

    @Override
    public int calculateHit(Entity entity, Entity victim, double modifier) {
        return getType().getSwingHandler().calculateHit(entity, victim, modifier);
    }
}

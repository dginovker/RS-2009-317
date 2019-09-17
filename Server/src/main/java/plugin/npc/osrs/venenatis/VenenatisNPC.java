package plugin.npc.osrs.venenatis;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.combat.InteractionType;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior;
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Venenatis NPC.
 *
 * @author Vexia
 */
public class VenenatisNPC extends AbstractNPC {

    /**
     * The venenatis combat swing handler.
     */
    private final VenenatisCombatSwingHandler handler = new VenenatisCombatSwingHandler();

    /**
     * Constructs a new @{Code VenenatisNPC} object.
     */
    public VenenatisNPC() {
        super(-1, null);
        super.setAggressive(true);
    }

    /**
     * Constructs a new @{Code VenenatisNPC} object.
     *
     * @param id       The id.
     * @param location The location.
     */
    public VenenatisNPC(int id, Location location) {
        super(id, location);
        super.setAggressive(true);
    }

    @Override
    public void init() {
        super.init();
        super.setDefaultBehavior();
        configureBossData();
    }

    @Override
    public void setDefaultBehavior() {
        setAggressive(true);
        aggressiveHandler = new AggressiveHandler(this, AggressiveBehavior.DEFAULT);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new VenenatisNPC(id, location);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            // add kill count for bosses in the future maybe?
            //BossKillCounter.addtoKillcount((Player) killer, this.getId());
        }
    }

    @Override
    public void sendImpact(BattleState state) {
        if (state.getEstimatedHit() > 50) {
            state.setEstimatedHit(RandomUtil.random(40, 50));
        } else if (state.getSecondaryHit() > 50) {
            state.setEstimatedHit(RandomUtil.random(40, 50));
        }
        super.sendImpact(state);
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return handler;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 26504 };
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        init();
        return super.newInstance(arg);
    }

    /**
     * Handles the combat for the npc Venenatis.
     *
     * @author Vexia
     */
    public class VenenatisCombatSwingHandler extends CombatSwingHandler {

        /**
         * The current combat style.
         */
        private CombatStyle style = CombatStyle.MAGIC;

        /**
         * If the special attack is launched.
         */
        private boolean special;

        /**
         * If the player drain attack is launched.
         */
        private boolean prayerDrain;

        /**
         * Constructs a new @{Code VenenatisCombatSwingHandler} object.
         */
        public VenenatisCombatSwingHandler() {
            super(CombatStyle.MAGIC);
        }

        @Override
        public InteractionType canSwing(Entity entity, Entity victim) {
            return style.getSwingHandler().canSwing(entity, victim);
        }

        @Override
        public int swing(Entity entity, Entity victim, BattleState state) {
            if (style == CombatStyle.MAGIC) {
                return 2 + (int) Math.floor(entity.getLocation().getDistance(victim.getLocation()) * 0.5);
            }
            return style.getSwingHandler().swing(entity, victim, state);
        }

        @Override
        public void impact(Entity entity, Entity victim, BattleState state) {
            if (victim instanceof Player) {
                Player p = (Player) victim;
                if (special) {
                    int hit = style.getSwingHandler().calculateHit(entity, victim, 1.0);
                    if (hit < 10) {
                        hit = RandomUtil.random(10, 30);
                    }
                    if (hit > 50) {
                        hit = 50;
                    }
                    p.getStateManager().register(EntityState.STUNNED, true, 5, "Venenatis hurls her web at you, sticking you to the ground.");
                    p.getImpactHandler().manualHit(entity, hit, HitsplatType.NORMAL);
                    special = false;
                } else if (prayerDrain) {
                    p.graphics(new Graphics(170, 96));
                    p.getSkills().decrementPrayerPoints(p.getSkills().getPrayerPoints() * 0.30);
                    p.getActionSender().sendMessage("You prayer has been drained!");
                    prayerDrain = false;
                }
            }
            style.getSwingHandler().impact(entity, victim, state);
            if (RandomUtil.random(2) == 1) {
                style = CombatStyle.MAGIC;
            } else {
                style = CombatStyle.MELEE;
            }
            this.setType(style);
        }

        @Override
        public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
            if ((style == CombatStyle.MAGIC || style == CombatStyle.MELEE) && RandomUtil.random(20) == 1) {
                special = true;
            }
            switch (style) {
                case MAGIC:
                    Projectile.create(entity, victim, 165).send();
                    break;
                case MELEE:
                    if (!special && victim.getSkills().getPrayerPoints() > 0 && RandomUtil.random(20) == 1) {
                        prayerDrain = true;
                    }
                    return;
                default:
                    break;
            }
            style.getSwingHandler().visualizeImpact(entity, victim, state);
        }

        @Override
        public int calculateAccuracy(Entity entity) {
            return style.getSwingHandler().calculateAccuracy(entity);
        }

        @Override
        public int calculateHit(Entity entity, Entity victim, double modifier) {
            return style.getSwingHandler().calculateHit(entity, victim, modifier);
        }

        @Override
        public int calculateDefence(Entity entity, Entity attacker) {
            return style.getSwingHandler().calculateDefence(entity, attacker);
        }

        @Override
        public double getSetMultiplier(Entity e, int skillId) {
            return style.getSwingHandler().getSetMultiplier(e, skillId);
        }

    }
}

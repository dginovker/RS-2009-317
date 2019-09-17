package plugin.npc.osrs.callisto;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.*;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior;
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Callisto NPC.
 *
 * @author Vexia
 */
public final class CallistoNPC extends AbstractNPC {

    /**
     * The callisto combat swing handler.
     */
    private final CallistoCombatSwingHandler handler = new CallistoCombatSwingHandler();

    /**
     * Constructs a new @{Code CallistoNPC} object.
     */
    public CallistoNPC() {
        super(-1, null);
        super.setAggressive(true);
    }

    /**
     * Constructs a new @{Code CallistoNPC} object.
     *
     * @param id       The id.
     * @param location The location.
     */
    public CallistoNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void init() {
        super.init();
        configureBossData();
    }

    @Override
    public void setDefaultBehavior() {
        setAggressive(true);
        aggressiveHandler = new AggressiveHandler(this, AggressiveBehavior.DEFAULT);
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
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CallistoNPC(id, location);
    }

    @Override
    public void sendImpact(BattleState state) {
        if (state.getEstimatedHit() > 60) {
            state.setEstimatedHit(RandomUtil.random(40, 60));
        } else if (state.getSecondaryHit() > 60) {
            state.setEstimatedHit(RandomUtil.random(40, 60));
        }
        super.sendImpact(state);
    }

    @Override
    public void checkImpact(BattleState state) {
        if (state.getEstimatedHit() > 0 && handler.isHeal()) {
            handler.setHeal(false);
            int dif = (int) (state.getEstimatedHit() * 0.20);
            if (state.getEstimatedHit() - dif > 0) {
                state.setEstimatedHit(state.getEstimatedHit() - dif);
            }
            getSkills().heal(dif);
        }
        super.checkImpact(state);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return handler;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 26503, 26609 };
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        init();
        return super.newInstance(arg);
    }

    /**
     * Handles the combat for the npc Callisto.
     *
     * @author Vexia
     */
    public class CallistoCombatSwingHandler extends CombatSwingHandler {

        /**
         * The current combat style.
         */
        private CombatStyle style = CombatStyle.MAGIC;

        /**
         * If the special attack is launched.
         */
        private boolean special;

        /**
         * If the heal attack is launched.
         */
        private boolean heal;

        /**
         * Constructs a new @{Code CallistoCombatSwingHandler} object.
         */
        public CallistoCombatSwingHandler() {
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
                    for (Player o : RegionManager.getLocalPlayers(entity, 6)) {
                        Direction dir = Direction.getLogicalDirection(entity.getLocation(), o.getLocation());
                        Location loc = o.getLocation().transform(dir.getStepX() * 7, dir.getStepY() * 7, 0);
                        if (!RegionManager.isTeleportPermitted(loc) || RegionManager.getObject(loc) != null) {
                            continue;
                        }
                        o.teleport(loc);
                        o.getStateManager().register(EntityState.STUNNED, true, 3, "Callisto's roar throws you backwards.");
                        o.getImpactHandler().manualHit(entity, 3, ImpactHandler.HitsplatType.NORMAL);
                    }
                }
                if (!special && style == CombatStyle.MAGIC) {
                    p.getStateManager().register(EntityState.STUNNED, true, 1, "Callisto's fury sends an almighty shockwave through you.");
                }
            }
            if (style == CombatStyle.MAGIC) {
                state.setMaximumHit(60);
            }
            if (!special) {
                style.getSwingHandler().impact(entity, victim, state);
            }
            if (Location.getDistance(entity.getLocation(), victim.getLocation()) > entity.size()) {
                if (RandomUtil.random(10) <= 2) {
                    style = CombatStyle.MELEE;
                } else {
                    style = CombatStyle.MAGIC;
                }
            } else {
                if (RandomUtil.random(10) <= 2) {
                    style = CombatStyle.MAGIC;
                } else {
                    style = CombatStyle.MELEE;
                }
            }
            this.setType(style);
            if (special) {
                special = false;
            }
        }

        @Override
        public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
            int randomInt = RandomUtil.random(20);
            if (randomInt == 16) {
                special = true;
            }
            if (special) {
                return;
            }
            if (entity.getSkills().getLifepoints() < entity.getSkills().getStaticLevel(Skills.HITPOINTS) && RandomUtil.random(20) == 1) {
                setHeal(true);
                entity.graphics(Graphics.create(481));
            }
            switch (style) {
                case MAGIC:
                    if (victim instanceof Player && !victim.asPlayer().getPrayer().get(PrayerType.PROTECT_FROM_MAGIC) || victim instanceof NPC) {
                        state.setEstimatedHit(RandomUtil.random(1, 60));
                    }
                    Projectile.create(entity, victim, 395).send();
                    break;
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

        /**
         * @return the heal
         */
        public boolean isHeal() {
            return heal;
        }

        /**
         * @param heal the heal to set
         */
        public void setHeal(boolean heal) {
            this.heal = heal;
        }
    }
}

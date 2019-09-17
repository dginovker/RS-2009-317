package plugin.activity.corporeal;

import java.security.SecureRandom;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.equipment.ArmourSet;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior;
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.CombatPulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the Corporeal Beast {@link org.gielinor.game.node.entity.npc.AbstractNPC}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class CorporealBeastNPC extends AbstractNPC {

    /**
     * The Corporeal Beast lair.
     */
    public static final ZoneBorders LAIR = new ZoneBorders(2882, 4371, 2917, 4408);
    /**
     * The {@link java.security.SecureRandom}.
     */
    final SecureRandom secureRandom = new SecureRandom();

    /**
     * The {@link plugin.activity.corporeal.DarkEnergyCoreNPC}.
     */
    private DarkEnergyCoreNPC darkEnergyCoreNPC;
    /**
     * The {@link plugin.activity.corporeal.CorporealBeastSwingHandler}.
     */
    private CorporealBeastSwingHandler combatSwingHandler;

    /**
     * Constructs a new {@code CommanderZilyanaNPC} {@code Object}.
     */
    public CorporealBeastNPC() {
        super(20319, null);
    }

    /**
     * Constructs a new {@code CommanderZilyanaNPC} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public CorporealBeastNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CorporealBeastNPC(id, location);
    }

    @Override
    public void init() {
        setAggressive(false);
        super.init();
        super.getProperties().setArmourSet(ArmourSet.VERAC);
        combatSwingHandler = new CorporealBeastSwingHandler();
        AggressiveBehavior behavior = new AggressiveBehavior() {

            @Override
            public boolean canSelectTarget(Entity entity, Entity target) {
                if (!target.isActive() || DeathTask.isDead(target)) {
                    return false;
                }
                if (!target.getProperties().isMultiZone() && target.inCombat()) {
                    return false;
                }
                if (target instanceof Player) {
                    if (((Player) target).getSavedData().getGlobalData().getVisibility() != 0) {
                        return false;
                    }
                }
                if (target.getLocation().getDistance(entity.getLocation()) > 1.75) { // Fix safespotting
                    return false;
                }
                return LAIR.insideBorder(target.getLocation());
            }
        };
        super.setAggressiveHandler(new AggressiveHandler(this, behavior));
        getProperties().setNPCWalkable(true);
        getProperties().setCombatTimeOut(2);
        getAggressiveHandler().setChanceRatio(6);
        getAggressiveHandler().setRadius(90);
        getAggressiveHandler().setAllowTolerance(false);
        walkRadius = 90;
        super.setWalkRadius(90);
    }

    @Override
    public void tick() {
        CombatPulse pulse = getProperties().getCombatPulse();
        if (pulse.isAttacking()) {
            Entity e = pulse.getVictim();
            Entity target = getImpactHandler().getMostDamageEntity();
            if (target != null && target != e && target instanceof Player) {
                pulse.setVictim(target);
            }
        }
        combatSwingHandler.lastSwing++;
        if (combatSwingHandler.lastSwing == RandomUtil.random(15, 26)) {
            getSkills().heal(125);
            combatSwingHandler.lastSwing = 0;
        }
        super.tick();
    }

    @Override
    public void onImpact(final Entity entity, BattleState state) {
        super.onImpact(entity, state);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player player = (Player) killer;
            // TODO Pet dark energy core
            player.getSavedData().getBossKillLog().increaseCorporealBeastKills(1);
        }
        if (darkEnergyCoreNPC == null) {
            return;
        }
        darkEnergyCoreNPC.clear();
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        return super.isAttackable(entity, style);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        if (combatSwingHandler != null) {
            return combatSwingHandler;
        }
        return super.getSwingHandler(swing);
    }

    /**
     * Checks if we should reduce the player's hit by 50%.
     *
     * @param entity The entity.
     */
    public boolean[] reduceHit(Entity entity) {
        if (!(entity instanceof Player)) {
            return new boolean[]{ true, false };
        }
        Player player = (Player) entity;
        Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
        if (weapon == null) {
            return new boolean[]{ true, false };
        }
        // TODO BGS SPECIAL
//        if (weapon.getId() == 11696) {
//            return new boolean[]{true, true};
//        }
        return new boolean[]{ !weapon.getName().toLowerCase().contains("spear"), false };
    }

    @Override
    public void checkImpact(BattleState battleState) {
        super.checkImpact(battleState);
        int hit = battleState.getEstimatedHit();
        int secondaryHit = battleState.getSecondaryHit();
        boolean[] reduceHit = reduceHit(battleState.getAttacker());
        getAggressiveHandler().setTargetSwitching(true);
        getAggressiveHandler().selectTarget();
        if (hit > 0 && reduceHit[0]) {
            battleState.setEstimatedHit(hit / 2);
            if (reduceHit[1]) {
                getSkills().hit(hit / 2);
            }
        }
        if (secondaryHit > 0 && reduceHit[0]) {
            battleState.setSecondaryHit(secondaryHit / 2);
        }
        handleDarkEnergyCoreSpawn(battleState);
    }

    /**
     * Handles the {@link plugin.activity.corporeal.DarkEnergyCoreNPC} spawning.
     *
     * @param battleState The {@link org.gielinor.game.node.entity.combat.BattleState}.
     */
    public void handleDarkEnergyCoreSpawn(BattleState battleState) {
        int health = 1000;
        for (Player player : RegionManager.getLocalPlayers(this, 15)) {
            health += 35;
        }
        if (getSkills().getLifepoints() <= health && darkEnergyCoreNPC == null) {
            Location coreLocation = this.getLocation();
            coreLocation = coreLocation.transform(Direction.values()[secureRandom.nextInt(Direction.values().length - 1)],
                4);
            Projectile.send(Projectile.create(getLocation(), coreLocation, 1290, 9, 9, 5,
                Projectile.getSpeed(this, coreLocation), 0, 3));
            final Location finalCoreLocation = coreLocation;
            darkEnergyCoreNPC = new DarkEnergyCoreNPC(8126, finalCoreLocation);
            darkEnergyCoreNPC.setCorporealBeastNPC(this);
            World.submit(new Pulse(3) {

                @Override
                public boolean pulse() {
                    darkEnergyCoreNPC.init();
                    darkEnergyCoreNPC.animate(new Animation(10393));
                    return true;
                }
            });
        }
    }

    /**
     * Gets a random attacker to follow.
     *
     * @return The attacker to follow.
     */
    public Player getRandomAttacker() {
        for (Player player : RegionManager.getLocalPlayers(this, 64)) {
            if (player.getLocation().inArea(LAIR)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 20319 };
    }

}

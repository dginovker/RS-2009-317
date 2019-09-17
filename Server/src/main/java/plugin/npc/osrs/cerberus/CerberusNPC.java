package plugin.npc.osrs.cerberus;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.combat.equipment.SwitchAttack;
import org.gielinor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior;
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Cerberus NPC.
 *
 * @author Emperor
 */
public final class CerberusNPC extends AbstractNPC {

    /**
     * The combat handler.
     */
    private final CombatHandler combatHandler = new CombatHandler();

    /**
     * The current lock entity.
     */
    private Entity lock;

    /**
     * The game object.
     */
    private GameObject[] fire = new GameObject[3];

    /**
     * Constructs a new {@code CerberusNPC Object}.
     */
    public CerberusNPC() {
        super(25862, Location.create(1238, 1251, 0));
    }

    /**
     * Constructs a new {@code CerberusNPC Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public CerberusNPC(int id, Location location) {
        super(id, location);
        super.setAggressive(true);
        super.setTask(Tasks.HELLHOUNDS.getTask());
        super.setSlayerExperience(690.0);
    }

    @Override
    public void setDefaultBehavior() {
        setAggressive(true);
        aggressiveHandler = new AggressiveHandler(this, AggressiveBehavior.DEFAULT);
    }

    @Override
    public void init() {
        super.init();
        super.configureBossData();
        super.setWalks(false);
        super.setNeverWalks(true);
        super.getAggressiveHandler().setTargetSwitching(false);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return combatHandler;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CerberusNPC(id, location);
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (getProperties().getCombatPulse().getVictim() != lock) {
            if (lock != null) {
                super.fullRestore();
            }
            lock = getProperties().getCombatPulse().getVictim();
        }
        checkPlayers();
    }

    /**
     * Checks the amount of players in the region.
     */
    public void checkPlayers() {
        boolean fire = false;
        for (Player p : getViewport().getCurrentPlane().getPlayers()) {
            if (p.getLocation().getLocalY() > 26) {
                fire = true;
                break;
            }
        }
        if (fire == (this.fire[0] == null)) {
            for (int i = 0; i < 3; i++) {
                if (fire) {
                    Location l = getViewport().getRegion().getBaseLocation().transform(23 + i, 26, 0);
                    this.fire[i] = new GameObject(40443, l, 10, 0);
                    ObjectBuilder.add(this.fire[i]);
                } else {
                    ObjectBuilder.remove(this.fire[i]);
                    this.fire[i] = null;
                }
            }
        }
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            // add kill count for bosses in the future maybe?
            //BossKillCounter.addtoKillcount((Player) killer, this.getId());
        }
        lock = null;
        for (int i = 0; i < combatHandler.souls.length; i++) {
            if (combatHandler.souls[i] != null) {
                combatHandler.souls[i].setDeath();
            }
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{ 25862, 25863, 25866, 27883 };
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.getSkills().getStaticLevel(Skills.SLAYER) < 91) {
                player.getActionSender().sendMessage("You need a Slayer level of 91 to attack Cerberus.");
                return true;
            }
        }
        return super.isAttackable(entity, style);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        init();
        new CerberusNPC(25862, Location.create(1300, 1300, 0)).init();
        new CerberusNPC(25862, Location.create(1366, 1251, 0)).init();
        return super.newInstance(arg);
    }

    /**
     * Handles Cerberus' combat.
     *
     * @author Emperor
     */
    static class CombatHandler extends MultiSwingHandler {

        /**
         * The attack index.
         */
        private int index = 0;

        /**
         * The count of attacks done for the current style.
         */
        private int count;

        /**
         * The summoned souls.
         */
        private SummonedSoulNPC[] souls = new SummonedSoulNPC[3];

        /**
         * The last time souls were summoned.
         */
        private int lastSoulSummon;

        /**
         * The last time explosive markers were placed.
         */
        private int lastExplosiveMarker;

        /**
         * Constructs a new {@code CombatHandler} {@code Object}.
         */
        public CombatHandler() {
            super(true,
                //Melee
                new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(24486, Priority.HIGH)),
                //Magic
                new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), new Animation(24485, Priority.HIGH), null, Graphics.create(21243, 0), Projectile.create(null, null, 21242, 60, 36, 41, 46)),
                //Range
                new SwitchAttack(CombatStyle.RANGE.getSwingHandler(), new Animation(24487, Priority.HIGH), null, Graphics.create(21244, 0), Projectile.create(null, null, 21245, 60, 36, 41, 46))
            );
        }

        @Override
        public int swing(Entity entity, Entity victim, BattleState state) {
            if (lastSoulSummon < World.getTicks() && entity.getSkills().getLifepoints() < 400 && RandomUtil.randomize(10) == 0) {
                summonSouls(entity, victim);
                return -1;
            }
            if (lastExplosiveMarker < World.getTicks() && entity.getSkills().getLifepoints() < 200 && RandomUtil.randomize(10) == 0) {
                setExplosiveMarkers(entity, victim);
                return -1;
            }
            if (++count >= 3) {
                index = (index + 1) % super.getAttacks().length;
                count = 0;
            }
            super.next = super.getAttacks()[index];
            return super.swing(entity, victim, state);
        }

        /**
         * Sets the explosive markers.
         *
         * @param entity The entity.
         * @param victim The victim.
         */
        public void setExplosiveMarkers(final Entity entity, Entity victim) {
            entity.animate(new Animation(24488, Priority.HIGH));
            entity.sendChat("Grrrrrrrrrrrrrr");
            lastExplosiveMarker = World.getTicks() + 50;
            Location l = victim.getLocation();
            final Location[] markers = new Location[]{ l, RegionManager.getTeleportLocation(l, 8), RegionManager.getTeleportLocation(l, 8) };
            for (Location loc : markers) {
                Graphics.send(Graphics.create(21246), loc);
            }
            World.submit(new Pulse(2, entity) {

                int cycle = 0;

                @Override
                public boolean pulse() {
                    for (Location loc : markers) {
                        for (Player p : entity.getViewport().getCurrentPlane().getPlayers()) {
                            if (p.getLocation().equals(loc)) {
                                p.getImpactHandler().manualHit(entity, 10, HitsplatType.NORMAL);
                            } else if (p.getLocation().withinDistance(loc, 1)) {
                                p.getImpactHandler().manualHit(entity, 7, HitsplatType.NORMAL);
                            }
                        }
                    }
                    if (cycle++ == 2) {
                        for (Location loc : markers) { //19427
                            Graphics.send(Graphics.create(21247), loc);
                        }
                    }
                    return cycle == 3;
                }
            });
        }

        /**
         * Summons the souls.
         *
         * @param entity The entity.
         * @param victim The victim.
         */
        public void summonSouls(Entity entity, Entity victim) {
            entity.animate(new Animation(24489, Priority.HIGH));
            entity.sendChat("Aaarrrooooooo");
            if (souls[0] == null) {
                Location l = entity.getViewport().getRegion().getBaseLocation();
                for (int i = 0; i < 3; i++) {
                    souls[i] = new SummonedSoulNPC(27886 + i, l.transform(23 + i, 50, 0));
                    souls[i].init();
                }
            }
            for (int i = 0; i < 3; i++) {
                SummonedSoulNPC soul = souls[i];
                if (!soul.isRenderable()) {
                    soul.init();
                }
                soul.aidCerberus((index + i) % 3, victim);
            }
            lastSoulSummon = World.getTicks() + 50;
        }
    }

}

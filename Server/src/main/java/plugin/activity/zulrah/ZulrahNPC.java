package plugin.activity.zulrah;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.impl.Animator;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * The Zulrah boss plugin.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         First before attacking spawn Range, but do not attack
 *         new Location(2268, 3068) // player spawn location
 */
public class ZulrahNPC extends AbstractNPC {

    /**
     * The magic npc id.
     */
    public static final int MAGIC_ID = 8604;

    /**
     * The range npc id.
     */
    public static final int RANGE_ID = 8605;

    /**
     * The melee npc id.
     */
    public static final int MELEE_ID = 8606;

    /**
     * The animation for when Zulrah spawns an entity.
     */
    public static final Animation SPAWN_ENTITY_ANIMATION = new Animation(5068, true);

    /**
     * The animation for when Zulrah retreats.
     */
    public static final Animation RETREAT_ANIMATION = new Animation(5072, 0, Animator.Priority.HIGH, true);

    /**
     * The animation for when Zulrah spawns.
     */
    public static final Animation SPAWN_ANIMATION = new Animation(5073, 0, Animator.Priority.HIGH, true);

    /**
     * The projectile for spawning toxic smoke.
     */
    private static final Projectile SMOKE_PROJECTILE = Projectile.create((Location) null, null, 2046, 70, 0, 35, 85, 64, 16);

    /**
     * The toxic smoke object id.
     */
    public static final int TOXIC_SMOKE_ID = 43984;

    /**
     * The toxic spawn locations.
     */
    private static Queue<ToxicLocation> toxicLocations = new LinkedList<>();

    /**
     * The current {@link plugin.activity.zulrah.ZulrahCombatSwingHandler}.
     */
    private ZulrahCombatSwingHandler swingHandler;

    /**
     * The {@link plugin.activity.zulrah.ZulrahCombatSwingHandler} for the range stage.
     */
    private final ZulrahCombatSwingHandler RANGE_SWING_HANDLER = new ZulrahRangeSwingHandler();

    /**
     * The {@link org.gielinor.game.node.entity.combat.CombatSwingHandler} for the melee stage.
     */
    private final ZulrahCombatSwingHandler MELEE_SWING_HANDLER = new ZulrahMeleeSwingHandler();

    /**
     * The {@link org.gielinor.game.node.entity.player.Player} fighting.
     */
    private Player player;

    /**
     * The {@link plugin.activity.zulrah.ToxicCloudPulse}.
     */
    private ToxicCloudPulse toxicCloudPulse;

    /**
     * The current stage.
     */
    private int stage = 0;

    /**
     * The amount of hits this stage has taken.
     */
    public int hits = 0;

    /**
     * If we're currently busy switching stages or spawning entities.
     */
    private boolean busy;

    /**
     * Constructs a new <code>ZulrahNPC</code>.
     */
    public ZulrahNPC() {
        super(-1, null);
    }

    /**
     * Constructs a new <code>ZulrahNPC</code>.
     *
     * @param id       The id.
     * @param location The location.
     */
    public ZulrahNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void init() {
        super.init();
        RANGE_SWING_HANDLER.setZulrahNPC(this);
        MELEE_SWING_HANDLER.setZulrahNPC(this);
        swingHandler = RANGE_SWING_HANDLER;
        configureBossData();
        playAnimation(SPAWN_ANIMATION);
        toxicLocations.addAll(Arrays.asList(ToxicLocation.values()));
        spawnToxicSmoke(toxicLocations);
        getPulseManager().run(new Pulse(4, this, player) {

            @Override
            public boolean pulse() {
                spawnToxicSmoke(toxicLocations);
                return true;
            }

            @Override
            public boolean removeFor(String pulse) {
                return !pulse.equalsIgnoreCase("combat");
            }
        });
        lock();
    }

    /**
     * Spawns the toxic smoke objects.
     *
     * @param toxicLocations The smoke cloud object spawn locations.
     */
    public void spawnToxicSmoke(Queue<ToxicLocation> toxicLocations) {
        final ZulrahNPC zulrahNPC = this;
        if (toxicCloudPulse == null) {
            toxicCloudPulse = new ToxicCloudPulse(this);
            World.submit(toxicCloudPulse);
        }
        getPulseManager().run(new Pulse(1, player, this) {

            int count = 0;

            @Override
            public boolean pulse() {
                ToxicLocation toxicLocation = toxicLocations.peek();
                if (toxicLocation == null) {
                    getPlayer().getProperties().getCombatPulse().stop();
                    setStage(1, SpawnLocation.NORTH);
                    return true;
                }
                if (count == 2 || count == 6 || count == 10 || count == 14) {
                    faceLocation(new Location(toxicLocation.getPoints()[0].x, toxicLocation.getPoints()[0].y, 0));
                    for (Point point : toxicLocation.getPoints()) {
                        SMOKE_PROJECTILE.setSourceLocation(zulrahNPC.getLocation());
                        SMOKE_PROJECTILE.setEndLocation(new Location(point.x, point.y, 0));
                        SMOKE_PROJECTILE.send();
                    }
                    zulrahNPC.animate(SPAWN_ENTITY_ANIMATION);
                }
                if (count == 4 || count == 8 || count == 12 || count == 16) {
                    for (Point point : toxicLocation.getPoints()) {
                        ObjectBuilder.add(new GameObject(TOXIC_SMOKE_ID, new Location(point.x, point.y, 0)), 40);
                    }
                    toxicLocations.remove();
                }
                count++;
                return count >= 19;
            }

            @Override
            public boolean removeFor(String pulse) {
                return !pulse.equalsIgnoreCase("combat");
            }
        });
    }

    /**
     * Sets the next stage.
     *
     * @param stage The stage.
     */
    public void setStage(int stage, SpawnLocation spawnLocation) {
        setBusy(true);
        this.stage = stage;
        this.hits = 0;
        lock();
        playAnimation(RETREAT_ANIMATION);
        final ZulrahNPC zulrahNPC = this;
        getPulseManager().run(new Pulse(4, zulrahNPC, player) {

            @Override
            public boolean pulse() {
                switchStage(stage, spawnLocation);
                return true;
            }

            @Override
            public boolean removeFor(String pulse) {
                return !pulse.equalsIgnoreCase("combat");
            }
        });
    }

    private void switchStage(int stage, SpawnLocation spawnLocation) {
        switch (stage) {
            case 1:
                transform(MELEE_ID);
                face(getPlayer());
                setLocation(spawnLocation.getLocation());
                setTeleportTarget(spawnLocation.getLocation());
                swingHandler = MELEE_SWING_HANDLER;
                configureBossData();
                playAnimation(SPAWN_ANIMATION);
                break;
        }
        getPulseManager().run(new Pulse(3, this, player) {

            @Override
            public boolean pulse() {
                setBusy(false);
                unlock();
                getProperties().getCombatPulse().attack(player);
                return true;
            }

            @Override
            public boolean removeFor(String pulse) {
                return !pulse.equalsIgnoreCase("combat");
            }
        });
    }

    @Override
    public void configureBossData() {
        getProperties().setNPCWalkable(false);
        getProperties().setCombatTimeOut(Integer.MAX_VALUE);
        getAggressiveHandler().setRadius(30);
        setRespawn(false);
        setAggressive(true);
    }

    @Override
    public void onAttack(Entity target) {
        super.onAttack(target);
    }

    @Override
    public void checkImpact(BattleState battleState) {
        if (isBusy()) {
            getPlayer().getProperties().getCombatPulse().stop();
            return;
        }
        super.checkImpact(battleState);
    }

    @Override
    public void onImpact(final Entity entity, BattleState battleState) {
        if (!isActive()) {
            return;
        }
        super.onImpact(entity, battleState);
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle combatStyle) {
        return super.isAttackable(entity, combatStyle) && !isBusy();
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return swingHandler;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new ZulrahNPC(id, location);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        return super.newInstance(arg);
    }

    @Override
    public int[] getIds() {
        return new int[]{ RANGE_ID, MAGIC_ID, MELEE_ID };
    }

    /**
     * Gets the {@link org.gielinor.game.node.entity.player.Player} fighting.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the {@link org.gielinor.game.node.entity.player.Player} fighting.
     *
     * @param player The player.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets if we're currently busy switching stages or spawning entities.
     *
     * @return <code>True</code> if so.
     */
    public boolean isBusy() {
        return busy;
    }

    /**
     * Sets if we're currently busy switching stages or spawning entities.
     *
     * @param busy If we're busy.
     */
    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}

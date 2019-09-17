package org.gielinor.game.node.entity.npc;

import java.nio.ByteBuffer;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.slayer.Master;
import org.gielinor.game.content.skill.member.slayer.SlayerManager;
import org.gielinor.game.content.skill.member.slayer.Task;
import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.equipment.DefaultCombatSpell;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface.AttackStyle;
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior;
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.UpdateSequence;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.npc.NPCFaceEntity;
import org.gielinor.game.world.update.flag.npc.NPCFaceLocation;
import org.gielinor.game.world.update.flag.npc.NPCForceChat;
import org.gielinor.game.world.update.flag.npc.NPCSwitchId;
import org.gielinor.parser.npc.NPCConfiguration;
import org.gielinor.rs2.pulse.impl.MovementPulse;
import org.gielinor.utilities.misc.RandomUtil;

import plugin.skill.hunter.HunterNPC;

/**
 * Represents a non-player character.
 *
 * @author Emperor
 */
public class NPC extends Entity {

    /**
     * The non-player character's id.
     */
    private int id;

    /**
     * The original NPC id.
     */
    private final int originalId;

    /**
     * The NPC definitions.
     */
    private NPCDefinition definition;

    /**
     * If the NPC walks;
     */
    private boolean walks;

    /**
     * If the NPC is aggressive.
     */
    private boolean aggressive;

    /**
     * Represents if the NPC will respawn.
     */
    private boolean respawn = true;

    /**
     * Represents if the NPC is hidden.
     */
    private boolean hidden = false;

    /**
     * The movement path of the NPC.
     */
    protected Location[] movementPath;

    /**
     * The walking radius.
     */
    protected int walkRadius = 11;

    /**
     * The movement index.
     */
    protected int movementIndex;

    /**
     * The respawn tick.
     */
    private int respawnTick;

    /**
     * If the NPC walks bound to a path.
     */
    private boolean pathBoundMovement;

    /**
     * The current dialogue.
     */
    public Player dialoguePlayer;

    /**
     * The aggressive handler of the NPC.
     */
    protected AggressiveHandler aggressiveHandler;

    /**
     * The walk back radius.
     */
    private int nextWalk;

    /**
     * The children NPCs.
     */
    private NPC[] children;

    /**
     * The amount of slayer experience received when killing this NPC.
     */
    private double slayerExperience;

    /**
     * The slayer task.
     */
    private Task task;

    /**
     * Whether or not this NPC never walks.
     */
    private boolean neverWalks;

    /**
     * Constructs a new {@code NPC} {@code Object}.
     *
     * @param id The NPC id.
     */
    protected NPC(int id) {
        this(id, null);
    }

    /**
     * Constructs a new {@code NPC} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public NPC(int id, Location location, Direction direction) {
        super(NPCDefinition.forId(id).getName(), location);
        this.id = id;
        this.originalId = id;
        this.definition = NPCDefinition.forId(id);
        super.size = definition.size;
        super.direction = direction;
        super.interaction = new Interaction(this);
    }

    /**
     * Constructs a new {@code NPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    protected NPC(int id, Location location) {
        this(id, location, Direction.SOUTH);
    }

    /**
     * Creates a new NPC object.
     *
     * @param id        The NPC id.
     * @param location  The location.
     * @param direction the dir.
     * @param objects
     * @return The NPC object.
     */
    public static NPC create(int id, Location location, Direction direction, Object... objects) {
        NPC n = AbstractNPC.forId(id);
        if (n != null) {
            n = ((AbstractNPC) n).construct(id, location, objects);
        }
        if (n == null) {
            n = new NPC(id, location, direction);
        }
        return n;
    }

    /**
     * Creates a new NPC object.
     *
     * @param id       the id.
     * @param location the location.
     * @param objects
     * @return the npc object.
     */
    public static NPC create(int id, Location location, Object... objects) {
        return create(id, location, Direction.SOUTH, objects);
    }

    @Override
    public void init() {
        super.init();
        getProperties().setSpawnLocation(getLocation());
        initConfig();
        Repository.getNpcs().add(this);
        if (getLocation() != null && getLocation().getRegionId() != -1) {
            RegionManager.move(this);
        }
        if (getViewport().getRegion() != null) {
            if (getViewport().getRegion().isActive()) {
                UpdateSequence.getRenderableNpcs().add(this);
            }
        }
        interaction.setDefault();
        configure();
        setDefaultBehavior();
        if (definition.childNPCIds != null) {
            children = new NPC[definition.childNPCIds.length];
            for (int i = 0; i < children.length; i++) {
                NPC npc = children[i] = new NPC(definition.childNPCIds[i]);
                npc.interaction.setDefault();
                npc.index = index;
                npc.size = size;
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        UpdateSequence.getRenderableNpcs().remove(this);
        Repository.getNpcs().remove(this);
        getViewport().setCurrentPlane(null);
    }

    /**
     * Initializes the configurations.
     */
    private void initConfig() {
        int defaultLevel = definition.getCombatLevel() / 2;
        getProperties().setCombatLevel(definition.getCombatLevel());
        getSkills().setStaticLevel(Skills.ATTACK, definition.getConfiguration(NPCConfiguration.ATTACK_LEVEL, defaultLevel));
        getSkills().setStaticLevel(Skills.STRENGTH, definition.getConfiguration(NPCConfiguration.STRENGTH_LEVEL, defaultLevel));
        getSkills().setStaticLevel(Skills.DEFENCE, definition.getConfiguration(NPCConfiguration.DEFENCE_LEVEL, defaultLevel));
        getSkills().setStaticLevel(Skills.RANGE, definition.getConfiguration(NPCConfiguration.RANGE_LEVEL, defaultLevel));
        getSkills().setStaticLevel(Skills.MAGIC, definition.getConfiguration(NPCConfiguration.MAGIC_LEVEL, defaultLevel));
        if (getId() == 2028) {
            getSkills().setStaticLevel(Skills.HITPOINTS, 100);
        } else {
            getSkills().setStaticLevel(Skills.HITPOINTS, definition.getConfiguration(NPCConfiguration.LIFEPOINTS, defaultLevel));
        }
        getSkills().setLevel(Skills.HITPOINTS, getSkills().getMaximumLifepoints());
        aggressive = definition.getConfiguration(NPCConfiguration.AGGRESSIVE, aggressive);
        Animation anim = null;
        for (int i = 0;
             i < 3;
             i++) {
            if (definition.getCombatAnimations()[i] != null) {
                getProperties().setAttackAnimation(anim = definition.getCombatAnimations()[i]);
                break;
            }
        }
        for (int i = 0;
             i < 3;
             i++) {
            if (definition.getCombatAnimations()[i] == null) {
                definition.getCombatAnimations()[i] = anim;
            }
        }
        if (definition.getCombatAnimations()[3] != null) {
            getProperties().setDefenceAnimation(definition.getCombatAnimations()[3]);
        }
        if (definition.getCombatAnimations()[4] != null) {
            getProperties().setDeathAnimation(definition.getCombatAnimations()[4]);
        }
        definition.initCombatGraphics(definition.getConfigurations());
        getProperties().setBonuses(definition.getConfiguration(NPCConfiguration.BONUSES, new int[15]));
        getProperties().setAttackSpeed(definition.getConfiguration(NPCConfiguration.ATTACK_SPEED, 5));
    }

    /**
     * Checks if the NPC is currently invisible.
     *
     * @return <code>True</code> if so.
     */
    public boolean isInvisible() {
        if (isHidden()) {
            return true;
        }
        if (!isActive()) {
            return true;
        }
        return getRespawnTick() > World.getTicks();
    }

    /**
     * Checks if the NPC is hidden for the player.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public boolean isHidden(Player player) {
        if (getId() == 654) {
            if (getAttribute("PLAYER_HIDDEN") != null) {
                if (player != getAttribute("PLAYER_HIDDEN")) {
                    return true;
                }
            }
        }
        return isInvisible();
    }

    /**
     * Sets the default aggressive behavior of the NPC.
     */
    public void setDefaultBehavior() {
        if (aggressive && definition.getCombatLevel() > 0) {
            aggressiveHandler = new AggressiveHandler(this, AggressiveBehavior.DEFAULT);
            aggressiveHandler.setRadius(definition.getConfiguration(NPCConfiguration.AGGRESSIVE_RADIUS, 4));
        }
    }

    /**
     * Configures the movement path.
     *
     * @param movementPath The movement path.
     */
    public void configureMovementPath(Location... movementPath) {
        this.movementPath = movementPath;
        this.movementIndex = 0;
        this.pathBoundMovement = true;
    }

    @Override
    public void checkImpact(BattleState state) {
        super.checkImpact(state);
        Entity entity = state.getAttacker();
        if (task != null && entity instanceof Player && task.getLevel() > entity.getSkills().getStaticLevel(Skills.SLAYER)) {
            state.neutralizeHits();
        }
    }

    @Override
    public int getProjectileLockonIndex() {
        return getIndex() + 1;
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (!definition.hasAction("attack") || isInvisible()) {
            return false;
        }
        if (task != null && entity instanceof Player && task.getLevel() > entity.getSkills().getStaticLevel(Skills.SLAYER)) {
            ((Player) entity).getActionSender().sendMessage("You need a Slayer level of " + task.getLevel() + " to attack this creature.");
            return false;
        }
        return super.isAttackable(entity, style);
    }

    @Override
    public int getDragonfireProtection(boolean fire) {
        return 0;
    }

    @Override
    public void tick() {
        if (!getViewport().getRegion().isActive()) {
            onRegionInactivity();
            return;
        }
        if (respawnTick > World.getTicks()) {
            return;
        }
        handleTickActions();
        super.tick();
    }

    public void handleTickActions() {
        if (!walks && (getId() >= 5073 && getId() <= 5080)) {
            walks = true;
        }
        if (!walks && (this instanceof HunterNPC || getName().toLowerCase().contains("impling"))) {
            walks = true;
        }
        if (!getLocks().isInteractionLocked()) {
            if (getId() != 20319 && !pathBoundMovement && walkRadius > 0 &&
                !getLocation().withinDistance(getProperties().getSpawnLocation(), walkRadius)) {
                getPulseManager().run(new MovementPulse(this, getProperties().getSpawnLocation(), Pathfinder.SMART) {

                    @Override
                    public boolean pulse() {
                        return true;
                    }
                }, "movement");
                if (aggressiveHandler != null) {
                    aggressiveHandler.setPauseTicks(walkRadius + 1);
                }
                nextWalk = World.getTicks() + walkRadius + 1;
                return;
            }
            if (aggressive && aggressiveHandler != null && aggressiveHandler.selectTarget()) {
                return;
            }
            if (getProperties().getCombatPulse().isAttacking()) {
                return;
            }
        }
        if (!getLocks().isMovementLocked()) {
            if (dialoguePlayer == null || !dialoguePlayer.isActive() || !dialoguePlayer.getInterfaceState().hasChatbox()) {
                dialoguePlayer = null;
                if (walks && !getPulseManager().hasPulseRunning() && !getProperties().getCombatPulse().isAttacking() &&
                    !getProperties().getCombatPulse().isInCombat() && nextWalk < World.getTicks()) {
                    setNextWalk();
                    Location l = getMovementDestination();
                    if (canMove(l)) {
                        Pathfinder.find(this, l, true, Pathfinder.DUMB).walk(this);
                    }
                }
            }
        }
    }

    /**
     * Handles the automatic actions of the NPC.
     */
    public void handleTickActions2() {
        if (!walks && (getId() >= 5073 && getId() <= 5080)) {
            walks = true;
        }
        if (!walks && (this instanceof HunterNPC || getName().toLowerCase().contains("impling"))) {
            walks = true;
        }
        if (!getLocks().isInteractionLocked()) {
            if (!pathBoundMovement && walkRadius > 0 && !getLocation().withinDistance(getProperties().getSpawnLocation(), walkRadius)) {
                getPulseManager().run(new MovementPulse(this, getProperties().getSpawnLocation(), Pathfinder.SMART) {

                    @Override
                    public boolean pulse() {
                        return true;
                    }
                }, "movement");
                if (aggressiveHandler != null) {
                    aggressiveHandler.setPauseTicks(walkRadius + 2);
                }
                nextWalk = World.getTicks() + walkRadius + 2;
                return;
            }
            if ((aggressive && definition.hasAttackOption()) &&
                aggressiveHandler != null && aggressiveHandler.selectTarget()) {
                return;
            }
            if (getProperties().getCombatPulse().isAttacking()) {
                if (getProperties().getCombatPulse().getVictim().getLocation() != null && Location.getDistance(getProperties().getSpawnLocation(), getProperties().getCombatPulse().getVictim().getLocation()) > 10) {
                    getProperties().getCombatPulse().stop();
                    Pathfinder.find(this, getProperties().getSpawnLocation(), true, Pathfinder.DUMB).walk(this);
                }
                if (Location.getDistance(getProperties().getSpawnLocation(), getLocation()) > 12) {
                    getProperties().getCombatPulse().stop();
                    Pathfinder.find(this, getProperties().getSpawnLocation(), true, Pathfinder.DUMB).walk(this);
                }
                return;
            }
        }
        if (!getLocks().isMovementLocked() && World.getTicks() > nextWalk) {
            if (dialoguePlayer == null || !dialoguePlayer.isActive() || !dialoguePlayer.getInterfaceState().hasChatbox()) {
                dialoguePlayer = null;
                if (walks && !getPulseManager().hasPulseRunning() &&
                    !getProperties().getCombatPulse().isAttacking() && nextWalk < World.getTicks()) {
                    nextWalk = World.getTicks() + 5 + RandomUtil.randomize(15);
                    Location l = getMovementDestination();
                    if (canMove(l)) {
                        Pathfinder.find(this, l, true, Pathfinder.DUMB).walk(this);
                    }
                }
            }
        }
    }

    /**
     * Called when the region goes inactive.
     */
    public void onRegionInactivity() {
        getWalkingQueue().reset();
        getPulseManager().clear();
        getUpdateMasks().reset();
        UpdateSequence.getRenderableNpcs().remove(this);
    }

    @Override
    public boolean isPoisonImmune() {
        return definition.getConfiguration(NPCConfiguration.POISON_IMMUNE, false);
    }

    /**
     * TODO Make sure we're not giving kill count for Slayer NPCs twice!
     *
     * @param killer The killer of this entity.
     */
    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (getZoneMonitor().handleDeath(killer)) {
            return;
        }
        // TEMP
        if (killer instanceof Player) {
            Player player = (Player) killer;
            if (Tasks.forValue(getTask()) != null) {
                player.getSavedData().getSlayerKillLog().increaseKillCount(Tasks.forValue(getTask()));
            }
            player.getSavedData().getGlobalData().incrementNpcKillCount();
        }
        //
        if (task != null && killer instanceof Player && ((Player) killer).getSlayer().getTask() == task) {
            Player player = (Player) killer;
            double exp = (double) getDefinition().getConfiguration(NPCConfiguration.SLAYER_EXP, (double) 0);
            if (exp == 0 || exp == -1) {
                exp = (double) getSkills().getStaticLevel(Skills.HITPOINTS);
            }
            killer.getSkills().addExperience(Skills.SLAYER, exp);
            player.getSlayer().decrementAmount(1);
            int casketRate = SlayerManager.determineCasketDropRate(player);
            if (RandomUtil.getRandom(casketRate) == 1) {
                Item casket = new Item(2734);
                GroundItemManager.create(casket, location, player);
                player.getActionSender().sendPositionedGraphic(671, 0, 0, location);
                player.getActionSender().sendMessage("<shad=1><col=663300>A dirty casket fell from the monster's corpse...");
            }
            if (!player.getSlayer().hasTask() && player.getSlayer().getAmount() <= 1) {
                player.getSavedData().getSlayerKillLog().increaseKillCount(player.getSlayer().getTasks());
                player.getSlayer().setAmount(0);
                player.getSlayer().setTask(null);
                if (player.getSlayer().getMaster() != null && !player.getSlayer().hasTask()) {
                    if (player.getSlayer().getMaster().equals(Master.TURAEL) &&
                        player.getSavedData().getActivityData().getAccumulativeSlayerTasks() < 4) {
                        player.getSavedData().getActivityData().increaseAccumulativeSlayerTasks();
                    } else {
                        player.getSavedData().getActivityData().increaseAccumulativeSlayerTasks();
                    }
                    int points = SlayerManager.determinePointAmount(player);
                    player.getSlayer().clear();
                    if (!player.getSlayer().getMaster().equals(Master.TURAEL) &&
                        player.getSavedData().getActivityData().getAccumulativeSlayerTasks() > 3) {
                        player.getActionSender().sendMessage("You've completed " + player.getSavedData().getActivityData().getAccumulativeSlayerTasks() + " tasks in a row and received " + points + " points; return to a");
                        player.getActionSender().sendMessage("master.");
                        player.getSavedData().getActivityData().increaseSlayerPoints((int) (points * player.getDonorManager().getDonorStatus().getBonusSlayerPointsMultiplier()));
                        player.getSavedData().getSlayerKillLog().increaseKillCount(player.getSlayer().getTasks());
                        player.debug("SLAYER_POINT_INCREASE: " + points);
                    } else {
                        player.getSavedData().getSlayerKillLog().increaseKillCount(player.getSlayer().getTasks());
                        player.getActionSender().sendMessage("You've completed your task; return to a Slayer master.");
                    }
                }
            }
        }
        // TODO move to plugin
        // Tree spirit for Lost City
        if (getId() == 655) {
            if (killer instanceof Player) {
                ((Player) killer).getSavedData().getQuestData().setKilledTreeSpirit(true);
            }
        }
        setRespawnTick(World.getTicks() + definition.getConfiguration(NPCConfiguration.RESPAWN_DELAY, 17));
//		if (definition.getDropTables() != null && !getAttribute("disable:drop", false)) {
//			definition.getDropTables().drop(this, killer);
//			if (definition.getName().toLowerCase().contains("tzhaar") && killer instanceof Player) {
//				if (new SecureRandom().nextBoolean()) {
//					GroundItemManager.create(new Item(6529, new SecureRandom().nextInt(3000) + 1000), getLocation(), (Player) killer);
//				}
//			}
//		}
        Player p = killer == null || !(killer instanceof Player) ? null : (Player) killer;
        handleDrops(p, killer);
        if (!isRespawn()) {
            clear();
        }
    }

    /**
     * Handles the drops of the npc.
     *
     * @param p      the p.
     * @param killer the killer.
     */
    public void handleDrops(Player p, Entity killer) {
        if (getAttribute("disable:drop", false)) {
            return;
        }
        if (definition.getDropTables() != null) {
            definition.getDropTables().drop(this, killer);
        }
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public boolean face(Entity entity) {
        if (entity == null) {
            if (getUpdateMasks().unregisterSynced(NPCFaceEntity.getOrdinal())) {
                return getUpdateMasks().register(new NPCFaceEntity(entity));
            }
            return true;
        }
        return getUpdateMasks().register(new NPCFaceEntity(entity), true);
    }

    @Override
    public boolean faceLocation(Location location) {
        if (location == null) {
            getUpdateMasks().unregisterSynced(NPCFaceLocation.getOrdinal());
            return true;
        }
        return getUpdateMasks().register(new NPCFaceLocation(location), true);
    }

    @Override
    public boolean sendChat(String string) {
        return getUpdateMasks().register(new NPCForceChat(string));
    }

    /**
     * Gets an audio piece.
     *
     * @param index the index.
     * @return the audio.
     */
    public Audio getAudio(int index) {
        Audio[] audios = getDefinition().getConfiguration(NPCConfiguration.COMBAT_AUDIO, null);
        if (audios != null) {
            Audio audio = audios[index];
            if (audio != null && audio.getId() != 0) {
                return audio;
            }
        }
        return null;
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return getProperties().getCombatPulse().getStyle().getSwingHandler();
    }

    /**
     * Configures the NPC.
     * <br><b>Override this instead of {@link #init()} when creating a sub class.</b>
     */
    public void configure() {
        int[] bonus = definition.getConfiguration(NPCConfiguration.BONUSES, new int[3]);
        int highest = 0;
        int index = 0;
        for (int i = 0;
             i < 3;
             i++) {
            if (bonus[i] > highest) {
                highest = bonus[i];
                index = i;
            }
        }
        getProperties().setAttackStyle(new AttackStyle(WeaponInterface.STYLE_CONTROLLED, index));
        CombatStyle style = getDefinition().getConfiguration(NPCConfiguration.COMBAT_STYLE);
        if (style == CombatStyle.RANGE) {
            getProperties().getCombatPulse().setStyle(style);
        } else if (style == CombatStyle.MAGIC) {
            getProperties().getCombatPulse().setStyle(style);
            getProperties().setAutocastSpell(new DefaultCombatSpell(this));
        }
        task = Tasks.forId(getId());
    }

    /**
     * Method used to check if the NPC can attack the victim.
     * This method is used for aggressive behavior.
     *
     * @param victim The victim.
     * @return <code>True</code> if so.
     */
    public boolean canAttack(final Entity victim) {
        return true;
    }

    @Override
    public boolean hasProtectionPrayer(CombatStyle style) {
        return false;
    }

    @Override
    public String toString() {
        return "NPC " + id + ", " + getLocation() + ", " + getIndex();
    }

    /**
     * Method used to dump an NPC's data to the spawn file.
     *
     * @param buffer the current buffer.
     */
    public void dump(final ByteBuffer buffer) {
        if (!getAttribute("spawned:npc", false)) {
            return;
        }
        buffer.put((byte) 1).putShort((short) (getId() & 0xFFFF));
        buffer.put((byte) 2).putShort((short) (getLocation().getX() & 0xFFFF)).putShort((short) (getLocation().getY() & 0xFFFF)).put((byte) getLocation().getZ());
        buffer.put((byte) 5).put((byte) direction.toInteger());
        if (walks) {
            buffer.put((byte) 3);
        } else if (aggressive) {
            buffer.put((byte) 4);
        }
        buffer.put((byte) 0);
    }

    /**
     * Transforms this NPC.
     *
     * @param id The new NPC id.
     */
    public void transform(int id) {
        this.id = id;
        this.definition = NPCDefinition.forId(id);
        super.name = definition.getName();
        super.size = definition.size;
        super.interaction = new Interaction(this);
        initConfig();
        interaction.setDefault();
        if (id == originalId) {
            getUpdateMasks().unregisterSynced(NPCSwitchId.getOrdinal());
        }
        getUpdateMasks().register(new NPCSwitchId(id), id != originalId);
    }

    /**
     * Transforms this NPC back to original.
     */
    public void reTransform() {
        if (originalId == this.id) {
            return;
        }
        transform(originalId);
    }

    /**
     * Gets the movement destination of the NPC.
     *
     * @return The movement destination.
     */
    protected Location getMovementDestination() {
        if (!pathBoundMovement || movementPath == null || movementPath.length < 1) {
            int modifier = RandomUtil.random(-5, 5);
            return getProperties().getSpawnLocation().transform(modifier + RandomUtil.random(getWalkRadius()), modifier +
                RandomUtil.random(getWalkRadius()), 0);
        }
        Location l = movementPath[movementIndex++];
        if (movementIndex == movementPath.length) {
            movementIndex = 0;
        }
        return l;
    }

    /**
     * Checks if the npc can start combat.
     *
     * @param victim the victim.
     * @return <code>True</code> if so.
     */
    public boolean canStartCombat(Entity victim) {
        return true;
    }

    /**
     * Gets the definition.
     *
     * @return The definition.
     */
    public NPCDefinition getDefinition() {
        return definition;
    }

    /**
     * Sets the definition.
     *
     * @param definition The definition to set.
     */
    public void setDefinition(NPCDefinition definition) {
        this.definition = definition;
    }

    /**
     * Get the id.
     *
     * @return The non-player character's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id.
     *
     * @param id The non-player character's id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the walks.
     *
     * @return The walks.
     */
    public boolean isWalks() {
        return walks;
    }

    /**
     * Sets the walks.
     *
     * @param walks The walks to set.
     */
    public void setWalks(boolean walks) {
        this.walks = walks;
    }

    /**
     * Gets the aggressive.
     *
     * @return The aggressive.
     */
    public boolean isAggressive() {
        return aggressive;
    }

    /**
     * Sets the aggressive.
     *
     * @param aggressive The aggressive to set.
     */
    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    /**
     * Sets the name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the movementPath.
     *
     * @return The movementPath.
     */
    public Location[] getMovementPath() {
        return movementPath;
    }

    /**
     * @return the respawn.
     */
    public boolean isRespawn() {
        return respawn;
    }

    /**
     * @param respawn the respawn to set.
     */
    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    /**
     * Gets the pathBoundMovement.
     *
     * @return The pathBoundMovement.
     */
    public boolean isPathBoundMovement() {
        return pathBoundMovement;
    }

    /**
     * Sets the pathBoundMovement.
     *
     * @param pathBoundMovement The pathBoundMovement.
     */
    public void setPathBoundMovement(boolean pathBoundMovement) {
        this.pathBoundMovement = pathBoundMovement;
    }

    /**
     * Gets the dialoguePlayer.
     *
     * @return The dialoguePlayer.
     */
    public Player getDialoguePlayer() {
        return dialoguePlayer;
    }

    /**
     * Sets the dialoguePlayer.
     *
     * @param dialoguePlayer The dialoguePlayer.
     */
    public void setDialoguePlayer(Player dialoguePlayer) {
        this.dialoguePlayer = dialoguePlayer;
    }

    /**
     * @return the hidden.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set.
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Gets the aggressive handler.
     *
     * @return The aggressive handler.
     */
    public AggressiveHandler getAggressiveHandler() {
        return aggressiveHandler;
    }

    /**
     * Sets the aggressive handler.
     *
     * @param handler The handler.
     */
    public void setAggressiveHandler(AggressiveHandler handler) {
        this.aggressiveHandler = handler;
    }

    /**
     * Gets the next walk.
     *
     * @return The next walk.
     */
    public int getNextWalk() {
        return nextWalk;
    }

    /**
     * Sets the next walk.
     */
    public void setNextWalk() {
        nextWalk = World.getTicks() + 5 + RandomUtil.randomize(10);
    }

    /**
     * Gets the respawnTick.
     *
     * @return The respawnTick.
     */
    public int getRespawnTick() {
        return respawnTick;
    }

    /**
     * Sets the walking radius.
     *
     * @param radius The walking radius.
     */
    public void setWalkRadius(int radius) {
        this.walkRadius = radius;
    }

    /**
     * Gets the walking radius.
     *
     * @return the walking radius.
     */
    public int getWalkRadius() {
        return walkRadius;
    }

    /**
     * Sets the respawnTick.
     *
     * @param respawnTick The respawnTick to set.
     */
    public void setRespawnTick(int respawnTick) {
        this.respawnTick = respawnTick;
    }

    /**
     * Gets the originalId.
     *
     * @return The originalId.
     */
    public int getOriginalId() {
        return originalId;
    }

    /**
     * Gets the currently shown NPC.
     *
     * @param player The player to check for.
     * @return The shown NPC.
     */
    public NPC getShownNPC(Player player) {
        if (children == null) {
            return this;
        }
        int npcId = definition.getChildNPC(player).getId();
        for (NPC npc : children) {
            if (npc.getId() == npcId) {
                return npc;
            }
        }
        return this;
    }

    /**
     * Gets the slayerExperience.
     *
     * @return The slayerExperience.
     */
    public double getSlayerExperience() {
        return slayerExperience;
    }

    /**
     * Sets the slayerExperience.
     *
     * @param slayerExperience The slayerExperience to set.
     */
    public void setSlayerExperience(double slayerExperience) {
        this.slayerExperience = slayerExperience;
    }

    /**
     * Gets the task.
     *
     * @return The task.
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets the task.
     *
     * @param task The task to set.
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Gets the neverWalks.
     *
     * @return the neverWalks
     */
    public boolean isNeverWalks() {
        return neverWalks;
    }

    /**
     * Sets the baneverWalks.
     *
     * @param neverWalks the neverWalks to set.
     */
    public void setNeverWalks(boolean neverWalks) {
        this.neverWalks = neverWalks;
    }
}

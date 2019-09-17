package org.gielinor.game.content.skill.member.summoning.familiar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.SkillBonus;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.summoning.SummoningScroll;
import org.gielinor.game.content.skill.member.summoning.pet.Pet;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.map.zone.ZoneRestriction;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.SummoningInformationContext;
import org.gielinor.net.packet.out.SummoningInformationPacket;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.CombatPulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;
import org.gielinor.utilities.misc.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a familiar.
 *
 * @author Emperor
 */
public abstract class Familiar extends NPC implements Plugin<Object> {

    private static final Logger log = LoggerFactory.getLogger(Familiar.class);

    /**
     * The summon graphics for a small familiar.
     */
    protected static final Graphics SMALL_SUMMON_GRAPHIC = Graphics.create(1314);

    /**
     * The spawn graphics for a large familiar.
     */
    protected static final Graphics LARGE_SUMMON_GRAPHIC = Graphics.create(1315);

    /**
     * The owner.
     */
    protected Player owner;

    /**
     * The amount of ticks left.
     */
    protected int ticks;

    /**
     * The initial amount of ticks.
     */
    protected int maximumTicks;

    /**
     * The amount of special points left.
     */
    protected int specialPoints = 60;

    /**
     * The pouch id.
     */
    private final int pouchId;

    /**
     * The special move cost.
     */
    private final int specialCost;

    /**
     * The combat action.
     */
    private CombatSwingHandler combatHandler;

    /**
     * If the familiar is a combat familiar.
     */
    protected boolean combatFamiliar;

    /**
     * If the familiars special is charged.
     */
    protected boolean charged;

    /**
     * The invisible familiar boosts.
     */
    protected List<SkillBonus> boosts = new ArrayList<>();

    /**
     * The orb options.
     */
    protected final List<String> orbOptions;

    /**
     * Constructs a new {@code Familiar} {@code Object}.
     *
     * @param owner       The owner.
     * @param id          The NPC id.
     * @param ticks       The ticks left.
     * @param pouchId     The pouch.
     * @param specialCost The special move cost.
     */
    public Familiar(Player owner, int id, int ticks, int pouchId, int specialCost) {
        super(id, null);
        this.owner = owner;
        this.maximumTicks = ticks;
        this.ticks = ticks;
        this.pouchId = pouchId;
        this.specialCost = specialCost;
        this.combatFamiliar = NPCDefinition.forId(getOriginalId() + 1).getName().equals(getName());
        this.orbOptions = new ArrayList<>();
    }

    @Override
    public void init() {
        location = getSpawnLocation();
        if (location == null) {
            location = owner.getLocation();
            setHidden(true);
        }
        super.init();
        startFollowing();
        sendConfiguration();
        call();
    }

    @Override
    public void handleTickActions() {
        if (ticks-- % 50 == 0) {
            updateSpecialPoints(-15);
            owner.getSkills().updateLevel(Skills.SUMMONING, -1, 0);
            if (!getText().isEmpty()) {
                super.sendChat(getText());
            }
        }
        sendTimeRemaining();
        switch (ticks) {
            case 100:
                owner.getActionSender().sendMessage("<col=ff0000>You have 1 minute before your familiar vanishes.");
                break;
            case 50:
                owner.getActionSender().sendMessage("<col=ff0000>You have 30 seconds before your familiar vanishes.");
                break;
            case 0:
                if (isBurdenBeast() && !((BurdenBeast) this).getContainer().isEmpty()) {
                    owner.getActionSender().sendMessage("<col=ff0000>Your familiar has dropped all the items it was holding.");
                } else {
                    owner.getActionSender().sendMessage("<col=ff0000>Your familiar has vanished.");
                }
                dismiss();
                return;
        }
        CombatPulse combat = owner.getProperties().getCombatPulse();
        if (!isHidden() && !getProperties().getCombatPulse().isAttacking() && (combat.isAttacking() || owner.inCombat())) {
            Entity victim = combat.getVictim();
            if (victim == null) {
                victim = owner.getAttribute("combat-attacker");
            }
            if (combat.getVictim() != this && victim != null && getProperties().isMultiZone() && owner.getProperties().isMultiZone() && isCombatFamiliar()) {
                getProperties().getCombatPulse().attack(victim);
            }
        }
        if ((!isHidden() && owner.getLocation().getDistance(getLocation()) > 12) || (isHidden() && ticks % 25 == 0)) {
            if (!call()) {
                setHidden(true);
            }
        } else if (!getPulseManager().hasPulseRunning()) {
            startFollowing();
        }
        handleFamiliarTick();
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (entity == owner) {
            owner.getActionSender().sendMessage("You can't attack your own familiar!");
            return false;
        }
        if (!getProperties().isMultiZone()) {
            if (entity instanceof Player && !entity.getProperties().isMultiZone()) {
                ((Player) entity).getActionSender().sendMessage("You have to be in multicombat to attack a player's familiar.");
                return false;
            }
            if (!owner.getSkullManager().isWilderness()) {
                if (entity instanceof Player) {
                    ((Player) entity).getActionSender().sendMessage("This familiar's owner is not in a multicombat zone.");
                }
                return false;
            }
        }
        if (entity instanceof Player) {
            if (!((Player) entity).getSkullManager().isWilderness()) {
                ((Player) entity).getActionSender().sendMessage("You have to be in the wilderness to attack a player's familiar.");
                return false;
            }
            if (!owner.getSkullManager().isWilderness()) {
                ((Player) entity).getActionSender().sendMessage("This familiar's owner is not in the wilderness.");
                return false;
            }
        }
        return super.isAttackable(entity, style);
    }

    @Override
    public void onRegionInactivity() {
        call();
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        if (combatHandler != null) {
            return combatHandler;
        }
        return super.getSwingHandler(swing);
    }

    /**
     * Constructs a new {@code Familiar} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The NPC id.
     * @return The familiar.
     */
    public abstract Familiar construct(Player owner, int id);

    /**
     * Executes the special move.
     *
     * @param special The familiar special object.
     * @return <code>True</code> if the move was executed.
     */
    protected abstract boolean specialMove(FamiliarSpecial special);

    /**
     * Handles the familiar special tick.
     */
    protected void handleFamiliarTick() {
    }

    /**
     * Configures use with events, and other plugin related content..
     */
    protected void configureFamiliar() {

    }

    /**
     * Gets the forced chat text for this familiar.
     *
     * @return The forced chat text.
     */
    protected String getText() {
        return "";
    }

    /**
     * Sends the time remaining.
     */
    public void sendTimeRemaining() {
        int minutes = (int) Math.ceil(ticks * 0.01);
//        int hash = minutes << 7 | ((ticks - (minutes * 100)) > 49 ? 1 : 0) << 6;
        // TODO CONVERT HASH
        owner.getActionSender().sendString(25924, "<col=C4B074>%1/%2");
        owner.getActionSender().sendString(25925, (minutes + ".00"));
    }

    /**
     * Checks if the familiar can execute its special move and does so if able.
     *
     * @param familiarSpecial The familiar special object.
     */
    public void executeSpecialMove(FamiliarSpecial familiarSpecial) {
        if (familiarSpecial.getNode() == this) {
            return;
        }
        if (specialCost > specialPoints) {
            owner.getActionSender().sendMessage("Your familiar does not have enough special move points left.");
            return;
        }
        SummoningScroll scroll = SummoningScroll.forPouch(pouchId);
        if (scroll == null) {
            owner.getActionSender().sendMessage("Invalid scroll for pouch " + pouchId + ".");
            return;
        }
        if (owner.getAttribute("special-delay", 0) > World.getTicks()) {
            return;
        }
        if (!owner.getInventory().contains(scroll.getItemId(), 1)) {
            owner.getActionSender().sendMessage("You do not have enough scrolls left to do this special move.");
            return;
        }
        if (owner.getLocation().getDistance(getLocation()) > 15) {
            owner.getActionSender().sendMessage("Your familiar is too far away to use that scroll, or it cannot see you.");
            return;
        }
        if (specialMove(familiarSpecial)) {
            owner.setAttribute("special-delay", World.getTicks() + 3);
            owner.getInventory().remove(new Item(scroll.getItemId()));
            visualizeSpecialMove();
            if (owner.getAttribute("summ_spec", 1) == 0) {
                updateSpecialPoints(specialCost);
            }
            owner.getSkills().addExperience(Skills.SUMMONING, scroll.getExperience());
        }
    }

    /**
     * Sends the special move visualization for the owner.
     */
    public void visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1316));
    }

    /**
     * Sends a familiar hit.
     *
     * @param target   the target.
     * @param maxHit   the max hit.
     * @param graphics the graphics.
     */
    public void sendFamiliarHit(final Entity target, final int maxHit, final Graphics graphics) {
        final int ticks = 2 + (int) Math.floor(getLocation().getDistance(target.getLocation()) * 0.5);
        getProperties().getCombatPulse().setNextAttack(4);
        World.submit(new Pulse(ticks, this, target) {

            @Override
            public boolean pulse() {
                BattleState state = new BattleState(Familiar.this, target);
                int hit = 0;
                if (getCombatStyle().getSwingHandler().isAccurateImpact(Familiar.this, target)) {
                    hit = RandomUtil.randomize(maxHit);
                }
                state.setEstimatedHit(hit);
                target.getImpactHandler().handleImpact(owner, hit, CombatStyle.MAGIC, state);
                if (graphics != null) {
                    target.graphics(graphics);
                }
                return true;
            }
        });
    }

    /**
     * Sends a projectile to the target.
     *
     * @param target       the target.
     * @param projectileId the projectile id.
     */
    public void projectile(final Entity target, final int projectileId) {
        Projectile.magic(this, target, projectileId, 40, 36, 51, 10).send();
    }

    /**
     * Sends a familiar hit.
     *
     * @param maxHit the max hit.
     */
    public void sendFamiliarHit(final Entity target, final int maxHit) {
        sendFamiliarHit(target, maxHit, null);
    }

    /**
     * Checks if this familiar can attack the target (used mainly for special moves).
     */
    public boolean canAttack(Entity target, boolean message) {
        if (target.getLocation().getDistance(getLocation()) > 8) {
            if (message) {
                owner.getActionSender().sendMessage("That target is too far.");

            }
            return false;
        }
        if (target.getLocks().isInteractionLocked() || !target.isAttackable(this, CombatStyle.MAGIC)) {
            return false;
        }
        return isCombatFamiliar();
    }

    /**
     * Checks if this familiar can attack the target.
     */
    public boolean canAttack(Entity target) {
        return canAttack(target, true);
    }

    /**
     * Checks if a familiar can perform a combat special attack.
     *
     * @param target  the target.
     * @param message show  message.
     * @return <code>True</code> if so.
     */
    public boolean canCombatSpecial(Entity target, boolean message) {
        if (!canAttack(target, message)) {
            return false;
        }
        if (!isOwnerAttackable()) {
            return false;
        }
        if (getAttribute("special-delay", 0) > World.getTicks()) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a faimiliar can perform a combat special attack.
     *
     * @param target the target.
     * @return <code>True</code> if so.
     */
    public boolean canCombatSpecial(Entity target) {
        return canCombatSpecial(target, true);
    }

    /**
     * Checks if the owner is attackable.
     *
     * @return <code>True</code> if so.
     */
    public boolean isOwnerAttackable() {
        if (!owner.getProperties().getCombatPulse().isAttacking() && !owner.inCombat()) {
            owner.getActionSender().sendMessage("Your familiar cannot fight whilst you are not in combat.");
            return false;
        }
        return true;
    }

    /**
     * Gets the combat style.
     *
     * @return the style.
     */
    public CombatStyle getCombatStyle() {
        return CombatStyle.MAGIC;
    }

    /**
     * Adjusts a players battle state.
     *
     * @param state the state.
     */
    public void adjustPlayerBattle(final BattleState state) {

    }

    /**
     * Starts following the owner.
     */
    public void startFollowing() {
        getPulseManager().run(new MovementPulse(this, owner, Pathfinder.DUMB) {

            @Override
            public boolean pulse() {
                return false;
            }
        }, "movement");
        face(owner);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        dismiss();
    }

    /**
     * Checks if the familiar is a combat familiar.
     *
     * @return <code>True</code> if so.
     */
    public boolean isCombatFamiliar() {
        return combatFamiliar;
    }

    /**
     * Sends the familiar packets.
     */
    public void sendConfiguration() {
        owner.getInterfaceState().set(InterfaceConfiguration.SUMMONING_SPECIAL_MOVE, (isCombatFamiliar() || isBurdenBeast()));
        owner.getActionSender().sendNpcOnInterface(getOriginalId(), 25906, 0);
        owner.getActionSender().sendAnimationInterface((isCombatFamiliar() || isBurdenBeast()) ? 6550 : 8373, 25906, 0);
        owner.getActionSender().sendString(25918, getName());
        SummoningScroll summoningScroll = SummoningScroll.forPouch(getPouchId());
        if (summoningScroll != null) {
            String specialName = new Item(summoningScroll.getItemId()).getDefinition().getName().replace(" scroll", "");
            PacketRepository.send(SummoningInformationPacket.class, new SummoningInformationContext(owner, specialName, summoningScroll.getFullName(), summoningScroll.getLevel(), summoningScroll.getItemId(), 1, summoningScroll.getDescription(), summoningScroll.getInterfaceType()));
            sendTimeRemaining();
            owner.getInterfaceState().set(InterfaceConfiguration.HAS_FAMILIAR, true);
            owner.getInterfaceState().force(351, 1, false);
            owner.getInterfaceState().force(352, 2, false);
            owner.getInterfaceState().force(isBurdenBeast() ? 51 : 50, 25931, false);
            owner.getInterfaceState().force(isBurdenBeast() ? 51 : 50, 25938, false);
            owner.getInterfaceState().force(51, 25915, false);
            owner.getInterfaceState().force(51, 25942, false);
            owner.getActionSender().sendString(25937, "Call familiar");
            owner.getActionSender().sendString(25939, "Dismiss familiar");
            owner.getActionSender().sendString(25940, "Summoning points\\nremaining");
            owner.getActionSender().sendString(25941, "Familiar time\\nremaining");
            sendLeftClickOptions();
            updateSpecialPoints(0);
        }
    }

    /**
     * Sets and sends the left-click options.
     */
    public void sendLeftClickOptions() {
        String leftClick = owner.getSavedData().getGlobalData().getLeftClickOption();
        String[] options = new String[]{
            "Follower details", "Special attack", isCombatFamiliar() ? "Attack" : "", "Call follower",
            "Dismiss follower", isBurdenBeast() ? "Take BoB" : "", "Renew Familiar", "Select left-click option"
        };
        int leftClickIndex = 0;
        for (String option : options) {
            if (option == null || option.isEmpty()) {
                continue;
            }
            if (option.equals(leftClick)) {
                break;
            }
            leftClickIndex++;
        }
        if (leftClickIndex > 0) {
            leftClickIndex = leftClickIndex - 1;
        }
        if (leftClickIndex > options.length) {
            return;
        }
        options[leftClickIndex] = options[0];
        options[0] = leftClick;
        // options[options.length - 1] = "Select left-click option";
        owner.getActionSender().sendSummoningOptions(options);
        orbOptions.clear();
        Collections.addAll(orbOptions, options);
        Collections.reverse(orbOptions);
    }

    /**
     * Calls the familiar.
     */
    public boolean call() {
        Location destination = getSpawnLocation();
        if (destination == null) {
            owner.getActionSender().sendMessage("Your familiar is too big to fit here. Try calling it again when you are standing");
            owner.getActionSender().sendMessage("somewhere with more space.");
            return false;
        }
        setHidden(getZoneMonitor().isRestricted(ZoneRestriction.FOLLOWERS));
        getProperties().setTeleportLocation(destination);
        if (!(this instanceof Pet)) {
            if (size() > 1) {
                graphics(LARGE_SUMMON_GRAPHIC);
            } else {
                graphics(SMALL_SUMMON_GRAPHIC);
            }
        }
        if (getProperties().getCombatPulse().isAttacking()) {
            startFollowing();
        } else {
            face(owner);
        }
        if (!isRenderable() && owner.isActive()) {
            getWalkingQueue().update();
            getUpdateMasks().prepare(this);
        }
        return true;
    }

    /**
     * Gets the spawning location of the familiar.
     *
     * @return The spawn location.
     */
    public Location getSpawnLocation() {
        Location destination = null;
        for (int i = 0; i < 4; i++) {
            Direction dir = Direction.get(i);
            Location l = owner.getLocation().transform(dir, dir.toInteger() < 2 ? 1 : size());
            boolean success = true;
            loop:
            {
                for (int x = 0; x < size(); x++) {
                    for (int y = 0; y < size(); y++) {
                        if (RegionManager.isClipped(l.transform(x, y, 0))) {
                            success = false;
                            break loop;
                        }
                    }
                }
            }
            if (success) {
                destination = l;
                break;
            }
        }
        return destination;
    }

    /**
     * Dismisses the familiar.
     */
    public void dismiss() {
        clear();
        getPulseManager().clear();
        if (owner.getInterfaceState().getCurrentTabIndex() == Sidebar.SUMMONING_TAB.ordinal()) {
            owner.getActionSender().sendSidebarTab(Sidebar.INVENTORY_TAB.ordinal());
        }
        owner.getFamiliarManager().setFamiliar(null);
        if (owner.getSkullManager().isWilderness()) {
            owner.getAppearance().sync();
        }
        owner.getActionSender().sendSummoningOptions("");
        orbOptions.clear();
        orbOptions.add("");
        owner.getInterfaceState().force(InterfaceConfiguration.HAS_FAMILIAR, 0, false);
    }

    /**
     * Updates the special move points.
     *
     * @param diff The difference to decrease with.
     */
    public void updateSpecialPoints(int diff) {
        specialPoints -= diff;
        if (specialPoints > 60) {
            specialPoints = 60;
        }
        owner.getInterfaceState().set(InterfaceConfiguration.SUMMONING_SPECIAL_MOVE_POINTS, specialPoints);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) {
        for (int id : getIds()) {
            if (FamiliarManager.getFamiliars().containsKey(id)) {
                log.warn("Familiar [{}] was already registered.");
                return null;
            }
            FamiliarManager.getFamiliars().put(id, this);
            configureFamiliar();
        }
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Gets the charged.
     *
     * @return The charged.
     */
    public boolean isCharged() {
        if (charged) {
            owner.getActionSender().sendMessage("Your familiar is already charging its attack!");
            return true;
        }
        return false;
    }

    /**
     * Gets a familiar boost.
     *
     * @param skill the skill.
     * @return the boost.
     */
    public int getBoost(int skill) {
        SkillBonus bonus = null;
        for (SkillBonus b : boosts) {
            if (b.getSkillId() == skill) {
                bonus = b;
                break;
            }
        }
        if (bonus == null) {
            return 0;
        }
        return (int) bonus.getBonus();
    }

    /**
     * Charges a familiar.
     */
    public void charge() {
        setCharged(true);
    }

    /**
     * Sets the charged.
     *
     * @param charged The charged to set.
     */
    public void setCharged(boolean charged) {
        this.charged = charged;
    }

    /**
     * Checks if the familiar is a beast of burden.
     *
     * @return <code>True</code> if so.
     */
    public boolean isBurdenBeast() {
        return false;
    }

    /**
     * Gets the NPC ids.
     *
     * @return The npc ids.
     */
    public abstract int[] getIds();

    /**
     * Gets the pouch id.
     *
     * @return The pouch id.
     */
    public int getPouchId() {
        return pouchId;
    }

    /**
     * Gets the owner.
     *
     * @return The owner.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     *
     * @param owner The owner to set.
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Sets the familiar's ticks.
     *
     * @param ticks The ticks to set.
     */
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Sets the familiar's special points.
     *
     * @param specialPoints the special points to set.
     */
    public void setSpecialPoints(int specialPoints) {
        this.specialPoints = specialPoints;
    }

    /**
     * Gets the familiar's ticks.
     *
     * @return The ticks.
     */
    public int getTicks() {
        return ticks;
    }

    /**
     * Gets the familiar's special points.
     *
     * @return The special points.
     */
    public int getSpecialPoints() {
        return specialPoints;
    }

    /**
     * Gets the combatHandler.
     *
     * @return The combatHandler.
     */
    public CombatSwingHandler getCombatHandler() {
        return combatHandler;
    }

    /**
     * Sets the combatHandler.
     *
     * @param combatHandler The combatHandler to set.
     */
    public void setCombatHandler(CombatSwingHandler combatHandler) {
        this.combatHandler = combatHandler;
    }

    /**
     * Gets the view animation for remote viewing.
     *
     * @return the animation.
     */
    public Animation getViewAnimation() {
        return null;
    }

    /**
     * Gets the orb options.
     *
     * @return The options.
     */
    public List<String> getOrbOptions() {
        return orbOptions;
    }

}

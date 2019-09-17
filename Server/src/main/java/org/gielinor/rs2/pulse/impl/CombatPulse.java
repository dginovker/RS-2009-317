package org.gielinor.rs2.pulse.impl;

import java.util.Map;

import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.InteractionType;
import org.gielinor.game.node.entity.combat.equipment.ArmourSet;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.pulse.Pulse;

/**
 * The combat-handling pulse implementation.
 *
 * @author Emperor
 */
public final class CombatPulse extends Pulse {

    /**
     * The entity.
     */
    private Entity entity;

    /**
     * The victim.
     */
    private Entity victim;

    /**
     * The current combat style used.
     */
    private CombatStyle style = CombatStyle.MELEE;

    /**
     * The temporary combat swing handler.
     */
    private CombatSwingHandler temporaryHandler;

    /**
     * The current combat swing handler.
     */
    private CombatSwingHandler handler = style.getSwingHandler();

    /**
     * The last victim.
     */
    private Entity lastVictim;

    /**
     * The tick value of when we can start another hit-cycle.
     */
    private int nextAttack = -1;

    /**
     * The combat time out counter.
     */
    private int combatTimeOut;

    /**
     * The movement handling pulse.
     */
    private MovementPulse movement;

    /**
     * Constructs a new {@code CombatPulse} {@code Object}.
     *
     * @param source The attacking entity.
     */
    public CombatPulse(Entity source) {
        super(1, source, null);
        this.entity = source;
//        Pathfinder pathfinder = Pathfinder.DUMB;
//        if (source instanceof Player || (source instanceof NPC && source.getId() == 8133)) {
//            pathfinder = Pathfinder.SMART;
//        }
        this.movement = new MovementPulse(source, null) {

            @Override
            public boolean pulse() {
                return false;
            }
        };
    }

    @Override
    public boolean pulse() {
        if (victim == null || DeathTask.isDead(entity) || DeathTask.isDead(victim)) {
            return true;
        }
        if (!entity.getViewport().getRegion().isActive() || !victim.getViewport().getRegion().isActive()) {
            return true;
        }
        if (!interactable()) {
            return !entity.getWalkingQueue().isMoving() && combatTimeOut++ > entity.getProperties().getCombatTimeOut();
        }
        combatTimeOut = 0;
        entity.face(victim);
        if (nextAttack <= World.getTicks()) {
            final Entity v = victim;
            CombatSwingHandler handler = temporaryHandler;
            if (handler == null) {
                handler = entity.getSwingHandler(true);
            }
            if (!swing(entity, victim, handler)) {
                temporaryHandler = null;
                updateStyle();
                return false;
            }
            int speed = entity.getProperties().getAttackSpeed();
            if (entity instanceof Player && handler.getType() == CombatStyle.MAGIC) {
                speed = 5;
            } else if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_RAPID) {
                speed--;
            }
            setNextAttack(speed);
            temporaryHandler = null;
            setCombatFlags(v);
        }
        return (victim != null && victim.getSkills().getLifepoints() < 1) || entity.getSkills().getLifepoints() < 1;
    }

    /**
     * Executes a combat swing.
     *
     * @param entity  The entity.
     * @param victim  The victim.
     * @param handler The combat swing handler.
     * @return <code>True</code> if successfully started the swing.
     */
    public static boolean swing(final Entity entity, final Entity victim, final CombatSwingHandler handler) {
        final BattleState state = new BattleState(entity, victim);
        ArmourSet set = handler.getArmourSet(entity);
        entity.getProperties().setArmourSet(set);
        int delay = handler.swing(entity, victim, state);
        if (delay < 0) {
            return false;
        }
        if (victim == null) {
            entity.faceTemporary(victim, 1);//face back to entity.
        }
        handler.adjustBattleState(entity, victim, state);
        handler.visualize(entity, victim, state);
        if (delay - 1 < 1) {
            handler.visualizeImpact(entity, victim, state);
        }
        handler.visualizeAudio(entity, victim, state);
        if (set != null && set.effect(entity, victim, state)) {
            set.visualize(entity, victim);
        }
        World.submit(new Pulse(delay - 1, entity, victim) {

            boolean impact;

            @Override
            public boolean pulse() {
                if (DeathTask.isDead(victim)) {
                    return true;
                }
                if (impact || getDelay() == 0) {
                    handler.impact(entity, victim, state);
                    handleSoulsplit(entity, victim, state.getEstimatedHit());
                    handler.onImpact(entity, victim, state);
                    return true;
                }
                setDelay(1);
                impact = true;
                handler.visualizeImpact(entity, victim, state);
                return false;
            }
        });
        return true;
    }

    public static void handleSoulsplit(Entity entity, Entity victim, int totalHit) {
        if (!(entity instanceof Player)) {
            return;
        }
        if (totalHit > 0 && ((Player) entity).getPrayer().get(PrayerType.SOUL_SPLIT) &&
            victim.getSkills().getLifepoints() > 0) {
            Projectile.send(Projectile.create(entity, victim, 1994, 9, 9, 5, 20, 0));
            if (victim instanceof Player && victim.getSkills().getPrayerPoints() > 0) {
                victim.getSkills().decrementPrayerPoints(totalHit * 0.2);
            }
            victim.getSkills().decrementLifePoints(totalHit * 0.25);
            entity.getSkills().heal((int) Math.floor(totalHit * 0.25));
            World.submit(new Pulse(1) {

                int ticks = 0;

                @Override
                public boolean pulse() {
                    if (++ticks == 1) {
                        Projectile.send(Projectile.create(victim, entity, 1994, 9, 9, 5, 20, 0));
                    }
                    if (++ticks == 2) {
                        victim.graphics(new Graphics(1995));
                        this.stop();
                    }
                    return false;
                }

            });
        }
    }

    /**
     * Sets the "in combat" flag for the victim and handles closing.
     *
     * @param victim The victim.
     */
    public void setCombatFlags(Entity victim) {
        if (entity instanceof Player) {
            Player p = ((Player) entity);
            p.getInterfaceState().close();
            p.getInterfaceState().closeChatbox();
        }
        if (victim instanceof Player) {
            if (entity instanceof Player && ((Player) entity).getSkullManager().isWilderness()) {
                ((Player) entity).getSkullManager().checkSkull(victim);
            }
            ((Player) victim).getInterfaceState().closeChatbox();
            ((Player) victim).getInterfaceState().close();
        }
        if (!victim.getPulseManager().isMovingPulse()) {
            victim.getPulseManager().clear("combat");
        }
        victim.setAttribute("combat-time", System.currentTimeMillis() + 10000);
        victim.setAttribute("combat-attacker", entity);
    }

    /**
     * Checks if the mover can interact with the victim.
     *
     * @return <code>True</code> if so.
     */
    private boolean interactable() {
        if (victim == null) {
            return false;
        }
        if (entity instanceof NPC && victim instanceof Player && ((NPC) entity).isHidden((Player) victim)) {
            stop();
            return false;
        }
        if (victim instanceof NPC && entity instanceof Player && ((NPC) victim).isHidden((Player) entity)) {
            stop();
            return false;
        }
        if (entity instanceof NPC && !entity.asNpc().canStartCombat(victim)) {
            stop();
            return false;
        }
        InteractionType interactionType = canInteract();
        if (interactionType == InteractionType.STILL_INTERACT) {
            return true;
        }
        if (entity == null || victim == null || entity.getLocks().isMovementLocked()) {
            return false;
        }
        movement.findPath();
        for (Map.Entry<ZoneBorders, Location> zoneBorders : MovementPulse.ZONE_BORDERS.entrySet()) {
            if (zoneBorders.getKey().insideBorder(entity.getLocation())) {
                return true;
            }
        }
        return interactionType == InteractionType.MOVE_INTERACT;
    }

    /**
     * Sets the combat style.
     */
    public void updateStyle() {
        if (entity instanceof Player) {
            Player p = (Player) entity;
            if (p.getProperties().getSpell() != null) {
                style = CombatStyle.MAGIC;
                return;
            }
            if (p.getProperties().getAutocastSpell() != null) {
                style = CombatStyle.MAGIC;
                return;
            }
            switch (p.getProperties().getAttackStyle().getBonusType()) {
                case WeaponInterface.BONUS_MAGIC:
                    style = CombatStyle.MAGIC;
                    break;
                case WeaponInterface.BONUS_RANGE:
                    style = CombatStyle.RANGE;
                    break;
                default:
                    style = CombatStyle.MELEE;
                    break;
            }
        }
    }

    /**
     * Attacks the node.
     *
     * @param victim The victim node.
     */
    public void attack(Node victim) {
        if (victim == null) {
            return;
        }
        if (entity.getLocks().isInteractionLocked()) {
            return;
        }
        if (victim == this.victim && isAttacking()) {
            return;
        }
        if (victim instanceof NPC) {
            ((NPC) victim).getWalkingQueue().reset();
        }
        setVictim(victim);
        entity.onAttack((Entity) victim);
        entity.getPulseManager().run(this);
    }

    /**
     * Sets the victim.
     *
     * @param victim The victim.
     */
    public void setVictim(Node victim) {
        super.addNodeCheck(1, victim);
        movement.setLast(null);
        movement.setDestination(victim);
        this.victim = (Entity) victim;
        this.combatTimeOut = 0;
    }

    /**
     * Sets the next attack.
     *
     * @param ticks The amount of ticks.
     */
    public void setNextAttack(int ticks) {
        nextAttack = World.getTicks() + ticks;
    }

    /**
     * Delays the next attack.
     *
     * @param ticks The amount of ticks to delay the next attack with.
     */
    public void delayNextAttack(int ticks) {
        nextAttack += ticks;
    }

    /**
     * Gets the next attack tick.
     *
     * @return The next attack tick.
     */
    public int getNextAttack() {
        return nextAttack;
    }

    /**
     * Checks if we can fight with the victim.
     *
     * @return <code>True</code> if so.
     */
    public InteractionType canInteract() {
        if (victim == null) {
            return InteractionType.NO_INTERACT;
        }
        if (temporaryHandler != null) {
            return temporaryHandler.canSwing(entity, victim);
        }
        return entity.getSwingHandler(false).canSwing(entity, victim);
    }

    @Override
    public void start() {
        super.start();
        entity.face(victim);
    }

    @Override
    public void stop() {
        super.stop();
        entity.setAttribute("combat-stop", World.getTicks());
        this.lastVictim = victim;
        super.addNodeCheck(1, victim = null);
        entity.face(victim);
        entity.getProperties().setSpell(null);
    }

    @Override
    public boolean removeFor(String pulse) {
        if (isAttacking()) {
            pulse = pulse.toLowerCase();
            if (pulse.startsWith("interaction:attack")) {
                if (victim.hashCode() == Integer.parseInt(pulse.replace("interaction:attack:", ""))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if this entity is attacking.
     *
     * @return <code>True</code> if so.
     */
    public boolean isAttacking() {
        return victim != null && isRunning();
    }

    /**
     * If the entity has an attacker.
     *
     * @return {@code True} if so.
     */
    public boolean isInCombat() {
        Entity entity = this.entity.getAttribute("combat-attacker");
        return entity != null && entity.getProperties().getCombatPulse().isAttacking();
    }

    /**
     * Gets the current victim.
     *
     * @return The victim.
     */
    public Entity getVictim() {
        return victim;
    }

    /**
     * Gets the style.
     *
     * @return The style.
     */
    public CombatStyle getStyle() {
        return style;
    }

    /**
     * Sets the style.
     *
     * @param style The style to set.
     */
    public void setStyle(CombatStyle style) {
        this.style = style;
    }

    /**
     * @return the temporaryHandler.
     */
    public CombatSwingHandler getTemporaryHandler() {
        return temporaryHandler;
    }

    /**
     * @param temporaryHandler the temporaryHandler to set.
     */
    public void setTemporaryHandler(CombatSwingHandler temporaryHandler) {
        this.temporaryHandler = temporaryHandler;
    }

    /**
     * @return the lastVictim.
     */
    public Entity getLastVictim() {
        return lastVictim;
    }

    /**
     * @param lastVictim the lastVictim to set.
     */
    public void setLastVictim(Entity lastVictim) {
        this.lastVictim = lastVictim;
    }

    /**
     * Gets the handler.
     *
     * @return The handler.
     */
    public CombatSwingHandler getHandler() {
        return handler;
    }

    /**
     * Sets the handler.
     *
     * @param handler The handler to set.
     */
    public void setHandler(CombatSwingHandler handler) {
        this.handler = handler;
    }

}

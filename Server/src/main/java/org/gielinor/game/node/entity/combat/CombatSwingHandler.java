package org.gielinor.game.node.entity.combat;

import org.gielinor.constants.StaffOfTheDeadGraphicsConstants;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.ArmourSet;
import org.gielinor.game.node.entity.combat.equipment.DegradableEquipment;
import org.gielinor.game.node.entity.combat.handlers.MagicSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.RangeSwingHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Point;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.impl.Equipment;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles a combat swing.
 *
 * @author Emperor
 */
public abstract class CombatSwingHandler {

    /**
     * The amount of experience to get per hit.
     */
    public static final double EXPERIENCE_MOD = 7.5;
    /**
     * The mapping of the special attack handlers.
     */
    private Map<Integer, CombatSwingHandler> specialHandlers;
    /**
     * The combat style.
     */
    private CombatStyle type;
    /**
     * The {@link java.security.SecureRandom}.
     */
    private final SecureRandom secureRandom = new SecureRandom();
    /**
     * If this was a cannon swing.
     */
    private boolean cannon;

    /**
     * Gets the player's maximum hit.
     *
     * @return The maximum hit.
     */
    public static int getMaximumHit(Entity entity, Entity victim) {
        double modifier = 1.0;
        CombatSwingHandler combatSwingHandler = entity.getSwingHandler(false);
        CombatSpell spell = entity.getProperties().getSpell();
        if (spell == null && combatSwingHandler instanceof MagicSwingHandler) {
            spell = entity.getProperties().getAutocastSpell();
        } else {
            modifier = entity.getSwingHandler(true).getSetMultiplier(entity, combatSwingHandler
                instanceof RangeSwingHandler ? Skills.RANGE :
                combatSwingHandler instanceof MagicSwingHandler ? Skills.MAGIC :
                    Skills.STRENGTH);
        }
        if (spell != null && combatSwingHandler instanceof MagicSwingHandler) {
            modifier = spell.getMaximumImpact(entity, victim, null);
        }
        entity.getProperties().setArmourSet(combatSwingHandler.getArmourSet(entity));
        return entity.getSwingHandler(true).calculateHit(entity, victim, modifier);
    }

    /**
     * Constructs a new {@code CombatSwingHandler} {@code Object}
     *
     * @param type The combat style.
     */
    public CombatSwingHandler(CombatStyle type) {
        this.type = type;
    }

    /**
     * Starts the combat swing.
     *
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state  The battle state instance.
     * @return The amount of ticks before impact of the attack.
     */
    public abstract int swing(Entity entity, Entity victim, BattleState state);

    /**
     * Handles the impact of the combat swing (victim getting hit).
     *
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state  The battle state instance.
     */
    public abstract void impact(Entity entity, Entity victim, BattleState state);

    /**
     * Visualizes the impact itself (end animation, end GFX, ...)
     *
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state  The battle state instance.
     */
    public abstract void visualizeImpact(Entity entity, Entity victim, BattleState state);

    /**
     * Calculates the maximum accuracy of the entity.
     *
     * @param entity The entity.
     * @return The maximum accuracy value.
     */
    public abstract int calculateAccuracy(Entity entity);

    /**
     * Calculates the maximum strength of the entity.
     *
     * @param entity   The entity.
     * @param victim   The victim.
     * @param modifier The modifier.
     * @return The maximum strength value.
     */
    public abstract int calculateHit(Entity entity, Entity victim, double modifier);

    /**
     * Calculates the maximum defence of the entity.
     *
     * @param entity   The entity.
     * @param attacker The entity to defend against.
     * @return The maximum defence value.
     */
    public abstract int calculateDefence(Entity entity, Entity attacker);

    /**
     * Gets the void set multiplier.
     *
     * @param e       The entity.
     * @param skillId The skill id.
     * @return The multiplier.
     */
    public abstract double getSetMultiplier(Entity e, int skillId);

    /**
     * Visualizes the combat swing (start animation, GFX, projectile, ...)
     *
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state  The battle state instance.
     */
    public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.animate(getAttackAnimation(entity, type));
    }

    /**
     * Method called when the impact method got called.
     *
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state  The battle state.
     */
    public void onImpact(final Entity entity, final Entity victim, final BattleState state) {
        if (entity instanceof Player && victim != null) {
            DegradableEquipment.degrade((Player) entity, victim, true);
        }
        if (state == null) {
            return;
        }
        if (state.getTargets() != null && state.getTargets().length > 0) {
            if (!(state.getTargets().length == 1 && state.getTargets()[0] == state)) {
                for (BattleState s : state.getTargets()) {
                    if (s != null && s != state) {
                        onImpact(entity, s.getVictim(), s);
                    }
                }
                return;
            }
        }
        victim.onImpact(entity, state);
    }

    /**
     * Gets the currently worn armour set, if any.
     *
     * @param e The entity.
     * @return The armour set worn.
     */
    public ArmourSet getArmourSet(Entity e) {
        return null;
    }

    /**
     * Checks if the container contains a void knight set.
     *
     * @param c The container to check.
     * @return <code>True</code> if so.
     */
    public boolean containsVoidSet(Container c) {
        Item top = c.getNew(Equipment.SLOT_CHEST);
        return !(top.getId() != 8839 && top.getId() != 10611) && c.getNew(Equipment.SLOT_LEGS).getId() == 8840 && c.getNew(Equipment.SLOT_HANDS).getId() == 8842;
    }

    /**
     * Checks if the hit will be accurate.
     *
     * @param entity The entity.
     * @param victim The victim.
     * @return <code>True</code> if the hit is accurate.
     */
    public boolean isAccurateImpact(Entity entity, Entity victim) {
        return isAccurateImpact(entity, victim, type, 1.0, 1.0);
    }

    /**
     * Checks if the hit will be accurate.
     *
     * @param entity The entity.
     * @param victim The victim.
     * @param style  The combat style used.
     * @return <code>True</code> if the hit is accurate.
     */
    public boolean isAccurateImpact(Entity entity, Entity victim, CombatStyle style) {
        return isAccurateImpact(entity, victim, style, 1.0, 1.0);
    }

    /**
     * Checks if the hit will be accurate.
     *
     * @param entity      The entity.
     * @param victim      The victim.
     * @param style       The combat style (null to ignore prayers).
     * @param accuracyMod The accuracy modifier.
     * @param defenceMod  The defence modifier.
     * @return <code>True</code> if the hit is accurate.
     */
    public boolean isAccurateImpact(Entity entity, Entity victim, CombatStyle style, double accuracyMod, double defenceMod) {
        double mod = 1.0;
        if (style != null) {
            if (victim.hasProtectionPrayer(style)) {
                mod = entity instanceof NPC ? 0.0 : 0.6;
            }
        }
        double attack = calculateAccuracy(entity) * accuracyMod * mod * getSetMultiplier(entity, Skills.ATTACK);
        double defence = calculateDefence(victim, entity) * defenceMod * getSetMultiplier(victim, Skills.DEFENCE);
        double chance = 0.0;
        if (attack < defence) {
            chance = (attack - 1) / (defence * 2);
        } else {
            chance = 1 - ((defence + 1) / (attack * 2));
        }
        double ratio = chance * 100;
        double accuracy = Math.floor(ratio);
        double block = Math.floor(101 - ratio);
        double acc = Math.random() * accuracy;
        double def = Math.random() * block;
        return acc > def;
    }

    /**
     * Checks if the entity can execute a combat swing at the victim.
     *
     * @param entity The entity.
     * @param victim The victim.
     * @return <code>True</code> if so.
     */
    public InteractionType canSwing(Entity entity, Entity victim) {
        return isAttackable(entity, victim);
    }

    /**
     * Checks if the victim can be attacked by the entity.
     *
     * @param entity The attacking entity.
     * @param victim The entity being attacked.
     * @return <code>True</code> if so.
     */
    public InteractionType isAttackable(Entity entity, Entity victim) {
        if (entity.getLocation().equals(victim.getLocation())) {
            if (entity.getIndex() < victim.getIndex() && victim.getProperties().getCombatPulse().getVictim() == entity) {
                return InteractionType.STILL_INTERACT;
            }
            return InteractionType.NO_INTERACT;
        }
        Location el = entity.getLocation();
        Location vl = victim.getLocation();
        Location evl = vl.transform(victim.size(), victim.size(), 0);
        if (el.getX() >= vl.getX() && el.getX() < evl.getX() && el.getY() >= vl.getY() && el.getY() < evl.getY()) {
            return InteractionType.NO_INTERACT;
        }
        if (!victim.isAttackable(entity, type)) {
            entity.getProperties().getCombatPulse().stop();
            return InteractionType.NO_INTERACT;
        }
        return InteractionType.STILL_INTERACT;
    }

    /**
     * Handles extra effects for hitting victims in a {@link org.gielinor.game.node.entity.combat.BattleState}.
     *
     * @param entity      The entity being hit (Victim).
     * @param battleState The battle state.
     */
    public void handleExtraEffects(Entity entity, BattleState battleState) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            Container container = ((Player) entity).getEquipment();
            Item shield = container.get(Equipment.SLOT_SHIELD);
            if (shield != null && shield.getId() != -1) {
                switch (shield.getId()) {
                    /**
                     * Elysian spirit shield.
                     */
                    case Item.ELYSIAN_SPIRIT_SHIELD:
                        if (secureRandom.nextDouble() < 0.70D) {
                            player.graphics(new Graphics(2044));
                            if (battleState.getEstimatedHit() >= 4) {
                                battleState.setEstimatedHit((battleState.getEstimatedHit() * 25) / 100);
                            }
                            if (battleState.getSecondaryHit() >= 4) {
                                battleState.setSecondaryHit((battleState.getSecondaryHit() * 25) / 100);
                            }
                            if (battleState.getThirdHit() >= 4) {
                                battleState.setThirdHit((battleState.getThirdHit() * 25) / 100);
                            }
                            if (battleState.getFourthHit() >= 4) {
                                battleState.setFourthHit((battleState.getFourthHit() * 25) / 100);
                            }
                        }
                        break;
                }
            }

        }
    }

    /**
     * Gets the dragonfire message.
     *
     * @param protection The protection value.
     * @param fireName   The fire breath name.
     * @return The message to send.
     */
    public String getDragonfireMessage(int protection, String fireName) {
        if ((protection & 0x4) != 0) {
            if ((protection & 0x2) != 0) {
                return "Your potion and shield fully protects you from the dragon's " + fireName + ".";
            }
            if ((protection & 0x8) != 0) {
                return "Your prayer and shield absorbs most of the dragon's " + fireName + ".";
            }
            return "Your shield absorbs most of the dragon's " + fireName + ".";
        }
        if ((protection & 0x2) != 0) {
            if ((protection & 0x8) != 0) {
                return "Your prayer and potion absorbs most of the dragon's " + fireName + ".";
            }
            return "Your antifire potion helps you defend the against the dragon's " + fireName + ".";
        }
        if ((protection & 0x8) != 0) {
            return "Your magic prayer absorbs some of the dragon's " + fireName + ".";
        }
        if ((protection & 0x10) != 0) {
            return "Your special membership allows you to withstand the dragon's " + fireName + ".";
        }
        return "You are horribly burnt by the dragon's " + fireName + ".";
    }

    /**
     * Visualizes the audio.
     *
     * @param entity the entity.
     * @param victim the victim.
     * @param state  the state.
     */
    public void visualizeAudio(Entity entity, Entity victim, BattleState state) {
        if (entity instanceof Player) {
            int styleIndex = ((Player) entity).getSettings().getAttackStyleIndex();
            if (state.getWeapon() != null && state.getWeapon().getItem() != null) {
                Item weapon = state.getWeapon().getItem();
                Audio[] audios = weapon.getDefinition().getConfiguration(ItemConfiguration.ATTACK_AUDIO, null);
                if (audios != null) {
                    Audio audio = null;
                    if (styleIndex < audios.length) {
                        audio = audios[styleIndex];
                    }
                    if (audio == null || audio.getId() == 0) {
                        audio = audios[0];
                    }
                    if (audio != null) {
                        entity.asPlayer().getAudioManager().send(audio, true);
                    }
                }
            } else {
                entity.asPlayer().getAudioManager().send(417);
            }
        } else if (entity instanceof NPC && victim instanceof Player) {
            NPC npc = entity.asNpc();
            Audio audio = npc.getAudio(0);
            if (audio != null) {
                audio.send(victim.asPlayer(), true);
            }
        }
    }

    /**
     * Gets the combat distance.
     *
     * @param e        The entity.
     * @param v        The victim.
     * @param distance The distance.
     * @return The actual distance used for combat.
     */
    public int getCombatDistance(Entity e, Entity v, int distance) {
        if (e instanceof NPC) {
            NPC n = (NPC) e;
            if (n.getDefinition().getCombatDistance() > 0) {
                distance = n.getDefinition().getCombatDistance();
            }
        }
        return (e.size() >> 1) + (v.size() >> 1) + distance;
    }

    /**
     * Formats the hit for the victim. (called as victim.getSwingHandler(false).formatHit(victim, hit))
     *
     * @param e      The entity dealing the hit.
     * @param victim The entity receiving the hit.
     * @param hit    The hit to format.
     * @return The formatted hit.
     */
    public int formatHit(Entity e, Entity victim, int hit) {
        if (hit < 1) {
            return hit;
        }
        if (hit > victim.getSkills().getLifepoints()) {
            hit = victim.getSkills().getLifepoints();
        }
        return hit;
    }

    /**
     * Adjusts the battle state object for this combat swing.
     *
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state  The battle state.
     */
    public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
        int totalHit = 0;
        if (entity instanceof Player) {
            ((Player) entity).getFamiliarManager().adjustBattleState(state);
        }
        entity.sendImpact(state);
        victim.checkImpact(state);
        if (state.getEstimatedHit() > 0) {
            state.setEstimatedHit(getFormattedHit(entity, victim, state, state.getEstimatedHit()));
            totalHit += state.getEstimatedHit();
        }
        if (state.getSecondaryHit() > 0) {
            state.setSecondaryHit(getFormattedHit(entity, victim, state, state.getSecondaryHit()));
            totalHit += state.getSecondaryHit();
        }
        if (state.getThirdHit() > 0) {
            state.setThirdHit(getFormattedHit(entity, victim, state, state.getThirdHit()));
            totalHit += state.getThirdHit();
        }
        if (state.getFourthHit() > 0) {
            state.setFourthHit(getFormattedHit(entity, victim, state, state.getFourthHit()));
            totalHit += state.getFourthHit();
        }
        if (entity instanceof Player) {
            Player p = (Player) entity;
            if (totalHit > 0 && p.getPrayer().get(PrayerType.SMITE) && victim.getSkills().getPrayerPoints() > 0) {
                victim.getSkills().decrementPrayerPoints(totalHit * 0.1);
            }
        }
    }

    /**
     * Adds the experience for the current combat swing.
     *
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state  The battle state.
     */
    public void addExperience(Entity entity, Entity victim, BattleState state) {
    }

    /**
     * Gets the formatted hit.
     *
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state  The battle state.
     * @param hit    The hit to format.
     * @return The formatted hit.
     */
    protected int getFormattedHit(Entity entity, Entity victim, BattleState state, int hit) {
        if (state.getArmourEffect() != ArmourSet.VERAC &&
            victim.hasProtectionPrayer(state.getStyle() == null ? type : state.getStyle())) {
            hit *= entity instanceof Player ? 0.6 : 0;
        }
        if (victim instanceof NPC && victim.hasProtectionPrayer(state.getStyle() == null ? type : state.getStyle())) {
            if (victim.asNpc().getId() == 1158 || victim.asNpc().getId() == 1160) {
                hit = 0;
            }
        }
        if (victim instanceof Player) {
            if (victim.getStateManager().hasState(EntityState.STAFF_OF_THE_DEAD)) {
                if (entity.getProperties().getCombatPulse().getStyle().equals(CombatStyle.MELEE)) {
                    victim.graphics(StaffOfTheDeadGraphicsConstants.getIMPACT_GFX());
                    hit /= 2;
                }
            }
        }
//        else if (entity instanceof Familiar && victim instanceof Player) {
//            if (((Player) victim).getPrayer().get(PrayerType.PROTECT_FROM_SUMMONING)) {
//                hit = 0;
//            }
//        }
        return formatHit(entity, victim, hit);
    }

    /**
     * Checks if a projectile can be fired from the node location to the victim location.
     *
     * @param entity     The node.
     * @param victim     The victim.
     * @param checkClose If we are checking for a melee attack rather than a projectile.
     * @return <code>True</code> if so.
     */
    public static boolean isProjectileClipped(Node entity, Node victim, boolean checkClose) {
        if (victim instanceof NPC) {
            NPC n = (NPC) victim;
            if (entity instanceof Player) {
                return isProjectileClipped(n, entity, checkClose);
            }
            victim = n.getLocation().transform(n.size() >> 1, n.size() >> 1, 0);
        } else if (victim instanceof Player && entity instanceof Player) {
            return isProjectileClipped(entity, victim, checkClose, 1) || isProjectileClipped(victim, entity, checkClose, 1);
        }
        return isProjectileClipped(entity, victim, checkClose, 1);
    }

    /**
     * Checks if a projectile can be fired from the node location to the victim location.
     *
     * @param entity     The node.
     * @param victim     The victim.
     * @param checkClose If we are checking for a melee attack rather than a projectile.
     * @param size       The size of the victim.
     * @return <code>True</code> if so.
     */
    public static boolean isProjectileClipped(Node entity, Node victim, boolean checkClose, int size) {
        //TODO: Make a better way of doing this.
        int myX = entity.getLocation().getX();
        int myY = entity.getLocation().getY();
        if (entity instanceof NPC && size == 1) {
            NPC n = (NPC) entity;
            Node thist = n.getLocation().transform(n.size() >> 1, n.size() >> 1, 0);
            myX = thist.getLocation().getX();
            myY = thist.getLocation().getY();
        }
        int destX = victim.getLocation().getX();
        int destY = victim.getLocation().getY();
        if (myX == destX && myY == destY) {
            return true;
        }
        int lastTileX = myX;
        int lastTileY = myY;
        boolean s = false;
        while (true) {
            if (s) {
                if (myY < destY) {
                    myY++;
                } else if (myY > destY) {
                    myY--;
                }
            }
            if (myX < destX) {
                myX++;
            } else if (myX > destX) {
                myX--;
            }
            if (!s) {
                if (myY < destY) {
                    myY++;
                } else if (myY > destY) {
                    myY--;
                }
            }
            s = !s;
            Direction dir = Direction.getDirection(myX - lastTileX, myY - lastTileY);
            if (dir == null) {
                return false;
            }
            Location current = Location.create(lastTileX, lastTileY, entity.getLocation().getZ());
            Point p = Direction.getWalkPoint(dir);
            if (checkClose) {
                if (!Pathfinder.find(current, current.transform(p.getX(), p.getY(), current.getZ()), false, Pathfinder.DUMB).isSuccessful()) {
                    return false;
                }
            } else if (!Pathfinder.find(current, current.transform(p.getX(), p.getY(), current.getZ()), false, Pathfinder.PROJECTILE).isSuccessful()) {
                return false;
            }
            lastTileX = myX;
            lastTileY = myY;
            if (lastTileX == destX && lastTileY == destY) {
                return true;
            }
        }
    }

    /**
     * Handles curses.
     *
     * @param entity      The entity.
     * @param victim      The victim.
     * @param battleState The {@link org.gielinor.game.node.entity.combat.BattleState}.
     */
    public static void handleCurses(Entity entity, Entity victim, BattleState battleState) {
        if (victim instanceof NPC) {
            victim.animate(victim.getProperties().getDefenceAnimation());
            return;
        }
        int deflectGfx = -1;
        if (battleState == null || battleState.getStyle() == null) {
            victim.animate(victim.getProperties().getDefenceAnimation());
            return;
        }
        switch (battleState.getStyle()) {
            case MELEE:
                if (victim.asPlayer().getPrayer().get(PrayerType.DEFLECT_MELEE)) {
                    deflectGfx = 1999;
                }
                break;
            case RANGE:
                if (victim.asPlayer().getPrayer().get(PrayerType.DEFLECT_MISSILES)) {
                    deflectGfx = 1998;
                }
                break;
            case MAGIC:
                if (victim.asPlayer().getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
                    deflectGfx = 1997;
                }
                break;
        }
        if (victim.asPlayer().getPrayer().get(PrayerType.DEFLECT_SUMMONING) && entity instanceof Familiar) {
            deflectGfx = 1996;
        }
        if (deflectGfx != -1) {
            victim.visualize(Animation.create(12573), Graphics.create(deflectGfx));
        } else {
            victim.animate(victim.getProperties().getDefenceAnimation());
        }
    }

    /**
     * Gets the default animation of the entity.
     *
     * @param e     The entity.
     * @param style The combat style.
     * @return The attack animation.
     */
    public Animation getAttackAnimation(Entity e, CombatStyle style) {
        Animation anim = null;
        if (type != null && e instanceof NPC) {
            anim = ((NPC) e).getDefinition().getCombatAnimations()[style.ordinal() % 3];
        }
        if (anim == null) {
            return e.getProperties().getAttackAnimation();
        }
        return anim;
    }

    /**
     * Registers a special attack handler.
     *
     * @param itemId  The item id.
     * @param handler The combat swing handler.
     * @return <code>True</code> if succesful.
     */
    public boolean register(int itemId, CombatSwingHandler handler) {
        if (specialHandlers == null) {
            specialHandlers = new HashMap<>();
        }
        if (specialHandlers.containsKey(itemId)) {
            System.err.println("Already contained special attack handler for item " + itemId + " - [old=" + specialHandlers.get(itemId).getClass().getSimpleName() + ", new=" + handler.getClass().getSimpleName() + "].");
            return false;
        }
        return specialHandlers.put(itemId, handler) == null;
    }

    /**
     * Gets the special attack handler for the given item id.
     *
     * @param itemId The item id.
     * @return The special attack handler, or {@code null} if this item has no special attack handler.
     */
    public CombatSwingHandler getSpecial(int itemId) {
        if (specialHandlers == null) {
            specialHandlers = new HashMap<>();
        }
        return specialHandlers.get(itemId);
    }

    /**
     * Sets the combat style.
     *
     * @param type The type.
     */
    public void setType(CombatStyle type) {
        this.type = type;
    }

    /**
     * @return the type.
     */
    public CombatStyle getType() {
        return type;
    }


    /**
     * Gets if this is a cannon swing.
     *
     * @return <code>True</code> if so.
     */
    public boolean isCannon() {
        return cannon;
    }

    /**
     * Sets if this was a cannon swing.
     *
     * @param cannon If this is a cannon swing.
     */
    public void setCannon(boolean cannon) {
        this.cannon = cannon;
    }
}

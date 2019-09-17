package org.gielinor.game.node.entity.combat.handlers;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.InteractionType;
import org.gielinor.game.node.entity.combat.equipment.Ammunition;
import org.gielinor.game.node.entity.combat.equipment.ArmourSet;
import org.gielinor.game.node.entity.combat.equipment.RangeWeapon;
import org.gielinor.game.node.entity.combat.equipment.Weapon;
import org.gielinor.game.node.entity.combat.equipment.Weapon.WeaponType;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface.AttackStyle;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the range combat swings.
 *
 * @author Emperor
 */
public class RangeSwingHandler extends CombatSwingHandler {

    /**
     * Constructs a new {@code RangeSwingHandler} {@Code Object}.
     */
    public RangeSwingHandler() {
        super(CombatStyle.RANGE);
    }

    @Override
    public InteractionType canSwing(Entity entity, Entity victim) {
        if (!isProjectileClipped(entity, victim, false)) {
            return InteractionType.NO_INTERACT;
        }
        int distance = 8;
        if (entity instanceof Player && ((WeaponInterface) entity.getExtension(WeaponInterface.class)).getWeaponInterface().getInterfaceId() == 91) {
            distance -= 2;
        }
        if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_LONG_RANGE) {
            distance += 2;
        }
        boolean goodRange = victim.getCenterLocation().withinDistance(entity.getCenterLocation(), getCombatDistance(entity, victim, distance));
        InteractionType type = InteractionType.STILL_INTERACT;
        if (victim.getWalkingQueue().isMoving() && !goodRange) {
            goodRange = victim.getCenterLocation().withinDistance(entity.getCenterLocation(), getCombatDistance(entity, victim, ++distance));
            type = InteractionType.MOVE_INTERACT;
        }
        if (goodRange && super.canSwing(entity, victim) != InteractionType.NO_INTERACT) {
            if (type == InteractionType.STILL_INTERACT) {
                entity.getWalkingQueue().reset();
            }
            return type;
        }
        return InteractionType.NO_INTERACT;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        configureRangeData(entity, state);
        if (state.getWeapon() == null || !hasAmmo(entity, state)) {
            entity.getProperties().getCombatPulse().stop();
            return -1;
        }
        int hit = 0;
        if (isAccurateImpact(entity, victim, CombatStyle.RANGE)) {
            int max = calculateHit(entity, victim, 1.0);
            state.setMaximumHit(max);
            hit = RandomUtil.random(max);
        }
        state.setEstimatedHit(hit);
        if (state.getWeapon().getType() == WeaponType.DOUBLE_SHOT) {
            if (isAccurateImpact(entity, victim, CombatStyle.RANGE)) {
                hit = RandomUtil.random(calculateHit(entity, victim, 1.0));
            }
            state.setSecondaryHit(hit);
        }
        useAmmo(entity, state, victim.getLocation());
        return 1 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
    }

    /**
     * Configures the range data.
     *
     * @param entity The entity.
     * @param state  The battle state.
     */
    public void configureRangeData(Entity entity, BattleState state) {
        state.setStyle(CombatStyle.RANGE);
        Weapon weapon = null;
        if (entity instanceof Player) {
            Player p = (Player) entity;
            RangeWeapon rangeWeapon = RangeWeapon.get(p.getEquipment().getNew(3).getId());
            if (rangeWeapon == null) {
                System.err.println("Unhandled range weapon used - [item id=" + p.getEquipment().getNew(3).getId() + "].");
                return;
            }
            weapon = new Weapon(p.getEquipment().get(3), rangeWeapon.getAmmunitionSlot(), p.getEquipment().getNew(rangeWeapon.getAmmunitionSlot()));
            weapon.setType(rangeWeapon.getWeaponType());
            state.setRangeWeapon(rangeWeapon);
            int ammunationId = weapon.getAmmunition().getId();
            state.setAmmunition(Ammunition.get(ammunationId));
        } else {
            weapon = new Weapon(null);
        }
        state.setWeapon(weapon);
    }

    @Override
    public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
        if (state.getAmmunition() != null && entity instanceof Player) {
            int damage = state.getAmmunition().getPoisonDamage();
            if (state.getEstimatedHit() > 0 && damage > 8 && RandomUtil.random(10) < 4) {
                victim.getStateManager().register(EntityState.POISONED, false, damage, entity);
            }
        }
        super.adjustBattleState(entity, victim, state);
    }

    @Override
    public void addExperience(Entity entity, Entity victim, BattleState state) {
        if (entity instanceof Player) {
            int hit = state.getEstimatedHit();
            if (hit < 0) {
                hit = 0;
            }
            if (state.getSecondaryHit() > 0) {
                hit += state.getSecondaryHit();
            }
            entity.getSkills().addExperience(Skills.HITPOINTS, hit * 1.33);
            if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_LONG_RANGE) {
                entity.getSkills().addExperience(Skills.RANGE, hit * (EXPERIENCE_MOD / 2));
                entity.getSkills().addExperience(Skills.DEFENCE, hit * (EXPERIENCE_MOD / 2));
            } else {
                entity.getSkills().addExperience(Skills.RANGE, hit * EXPERIENCE_MOD);
            }
        }
    }

    /**
     * Checks if the entity has the ammunition needed to proceed.
     *
     * @param e     The entity.
     * @param state The battle state.
     * @return <code>True</code> if so.
     */
    public static boolean hasAmmo(Entity e, BattleState state) {
        if (!(e instanceof Player)) {
            return true;
        }
        Player p = (Player) e;
        WeaponType type = state.getWeapon().getType();
        int amount = type == WeaponType.DOUBLE_SHOT ? 2 : 1;
        if (type == WeaponType.DEGRADING) {
            return true;
        }
        Item item = p.getEquipment().get(state.getWeapon().getAmmunitionSlot());
        if (item != null && item.getCount() >= amount) {
            if (item.getId() == 10033 && !state.getRangeWeapon().getAmmunition().contains(10033)) {
                state.getRangeWeapon().getAmmunition().add(10033);
            }
            if (!state.getRangeWeapon().getAmmunition().contains(item.getId())) {
                p.getActionSender().sendMessage("You can't use this type of ammunition with this bow.");
                return false;
            }
            return true;
        }
        if (type == WeaponType.DOUBLE_SHOT) {
            state.getWeapon().setType(WeaponType.DEFAULT);
            return hasAmmo(e, state);
        }
        p.getActionSender().sendMessage("You do not have enough ammo left.");
        return false;
    }

    /**
     * Uses the ammunition for the range weapon.
     *
     * @param e            The entity.
     * @param state        The battle state.
     * @param dropLocation The drop location.
     */
    public static void useAmmo(Entity e, BattleState state, Location dropLocation) {
        if (!(e instanceof Player)) {
            return;
        }
        Player p = (Player) e;
        WeaponType type = state.getWeapon().getType();
        int amount = type == WeaponType.DOUBLE_SHOT ? 2 : 1;
        if (type == WeaponType.DEGRADING) {
            degrade(p, state, amount);
            return;
        }
        Item ammo = state.getWeapon().getAmmunition();
        if (state.getWeapon().getAmmunitionSlot() == -1 || ammo == null) {
            return;
        }
        boolean deleteAmmo = true;
        if (p.getEquipment().contains(10498) || p.getEquipment().contains(10499) || p.getEquipment().contains(11880) || p.getDonorManager().hasMembership()) {
            double chance = Math.random();
            deleteAmmo = (chance < 0.5D);
        }
        if (deleteAmmo) {
            p.getEquipment().replace(new Item(ammo.getId(), ammo.getCount() - amount, ammo.getCharge()), state.getWeapon().getAmmunitionSlot());
            if (dropLocation != null && state.getRangeWeapon().isDropAmmo()) {
                double rate = 5 + (1.0 + p.getSkills().getLevel(Skills.RANGE) * 0.01);
                if (RandomUtil.randomize((int) rate) != 0) {
                    GroundItemManager.increase(new Item(ammo.getId(), amount), dropLocation, p);
                }
            }
        }

        if (p.getEquipment().get(state.getRangeWeapon().getAmmunitionSlot()) == null) {
            p.getActionSender().sendMessage("You have no ammo left in your quiver!");
        }
    }

    /**
     * Degrades the player's range weapon used.
     *
     * @param p      The player.
     * @param state  The battle state.
     * @param amount The amount of shots to degrade.
     */
    private static void degrade(Player p, BattleState state, int amount) {
        if (state.getWeapon().getItem().getId() == 4212) { //New crystal bow.
            p.getActionSender().sendMessage("Your crystal bow has degraded!");
            p.getEquipment().replace(new Item(4214, 1, 996), 3);
            return;
        }
        int charge = state.getWeapon().getItem().getCharge() - (amount * 4);
        state.getWeapon().getItem().setCharge(charge);
        if (charge < 1) {
            int id = state.getWeapon().getId() + 1;
            if (id < 4224) {
                p.getActionSender().sendMessage("Your crystal bow has degraded!");
                p.getEquipment().replace(new Item(id, 1, 999), 3);
            } else {
                Item replace = null;
                if (!p.getInventory().add(new Item(4207))) {
                    replace = new Item(4207);
                }
                p.getEquipment().replace(replace, 3);
                p.getActionSender().sendMessage("Your crystal bow has degraded to a small crystal seed.");
            }
        }
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        Graphics start = null;
        if (state.getAmmunition() != null) {
            start = state.getAmmunition().getStartGraphics();
            state.getAmmunition().getProjectile().copy(entity, victim, 5).send();
            if (state.getWeapon().getType() == WeaponType.DOUBLE_SHOT && state.getAmmunition().getDarkBowGraphics() != null) {
                start = state.getAmmunition().getDarkBowGraphics();
                int speed = (int) (55 + (entity.getLocation().getDistance(victim.getLocation()) * 10));
                Projectile.create(entity, victim, state.getAmmunition().getProjectile().getProjectileId(), 40, 36, 41, speed, 25).send();
            }
        } else if (entity instanceof NPC) {
            NPC n = (NPC) entity;
            if (n.getDefinition().getCombatGraphics()[0] != null) {
                start = n.getDefinition().getCombatGraphics()[0];
            }
            Graphics g = n.getDefinition().getCombatGraphics()[1];
            if (g != null) {
                Projectile.ranged(entity, victim, g.getId(), g.getHeight(), 36, 41, 5).send();
            }
        }
        RangeWeapon weapon;
        int anim = entity.getProperties().getAttackAnimation().getId();
        if ((anim == 422 || anim == 423) && state.getWeapon().getId() > 0 && (weapon = RangeWeapon.get(state.getWeapon().getId())) != null) {
            entity.visualize(weapon.getAnimation(), start);
            return;
        }
        entity.visualize(entity.getProperties().getAttackAnimation(), start);
    }

    @Override
    public void impact(final Entity entity, final Entity victim, final BattleState state) {
        int hit = state.getEstimatedHit();
        victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.RANGE, state);
        if (state.getSecondaryHit() > -1) {
            final int hitt = state.getSecondaryHit();
            World.submit(new Pulse(1, victim) {

                @Override
                public boolean pulse() {
                    victim.getImpactHandler().handleImpact(entity, hitt, CombatStyle.RANGE, state);
                    return true;
                }
            });
        }
    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
        CombatSwingHandler.handleCurses(entity, victim, state);
    }

    @Override
    public int calculateAccuracy(Entity entity) {
        int baseLevel = entity.getSkills().getStaticLevel(Skills.RANGE);
        int weaponRequirement = baseLevel;
        if (entity instanceof Player) {
            Item weapon = ((Player) entity).getEquipment().get(3);
            if (weapon != null) {
                weaponRequirement = weapon.getDefinition().getRequirement(Skills.RANGE);
            } else {
                weaponRequirement = 1;
            }
        }
        double weaponBonus = 0.0;
        if (baseLevel > weaponRequirement) {
            weaponBonus = (baseLevel - weaponRequirement) * .3;
        }
        int level = entity.getSkills().getLevel(Skills.RANGE);
        double prayer = 1.0;
        if (entity instanceof Player) {
            prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.RANGE);
        }
        double additional = 1.0; //Slayer helmet/salve/...
        int styleBonus = 0;
        if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_RANGE_ACCURATE) {
            styleBonus = 3;
        }
        double effective = Math.floor(((level * prayer) * additional) + styleBonus + weaponBonus);
        int bonus = entity.getProperties().getBonuses()[WeaponInterface.BONUS_RANGE];
        return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
    }

    @Override
    public int calculateHit(Entity entity, Entity victim, double modifier) {
        int level = entity.getSkills().getLevel(Skills.RANGE);
        int bonus = entity.getProperties().getBonuses()[14]; // TODO ranged bonus NPC
        double prayer = 1.0;
        if (entity instanceof Player) {
            prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.RANGE);
        }
        double cumulativeStr = Math.floor(level * prayer);
        if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_RANGE_ACCURATE) {
            cumulativeStr += 3;
        } else if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_LONG_RANGE) {
            cumulativeStr += 1;
        }
        cumulativeStr *= getSetMultiplier(entity, Skills.RANGE);
        return (int) ((14 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865)) * modifier) / 10 + 1;
    }

    @Override
    public int calculateDefence(Entity entity, Entity attacker) {
        AttackStyle style = entity.getProperties().getAttackStyle();
        int styleBonus = 0;
        if (style.getStyle() == WeaponInterface.STYLE_DEFENSIVE || style.getStyle() == WeaponInterface.STYLE_LONG_RANGE) {
            styleBonus = 3;
        } else if (style.getStyle() == WeaponInterface.STYLE_CONTROLLED) {
            styleBonus = 1;
        }
        int level = entity.getSkills().getLevel(Skills.DEFENCE);
        double prayer = 1.0;
        if (entity instanceof Player) {
            prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.DEFENCE);
        }
        double effective = Math.floor((level * prayer) + styleBonus);
        int equipment = entity.getProperties().getBonuses()[WeaponInterface.BONUS_RANGE + 5];
        return (int) Math.floor(((effective + 8) * (equipment + 64)) / 10);
    }

    @Override
    public double getSetMultiplier(Entity e, int skillId) {
        if (e instanceof Player) {
            Container c = ((Player) e).getEquipment();
            if (containsVoidSet(c) && c.getNew(Equipment.SLOT_HAT).getId() == 11664) {
                return 1.1;
            }
        }
        return 1.0;
    }

    @Override
    public ArmourSet getArmourSet(Entity e) {
        if (ArmourSet.KARIL.isUsing(e)) {
            return ArmourSet.KARIL;
        }
        return super.getArmourSet(e);
    }

}

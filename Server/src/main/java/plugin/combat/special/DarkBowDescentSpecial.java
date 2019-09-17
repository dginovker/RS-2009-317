package plugin.combat.special;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.equipment.Ammunition;
import org.gielinor.game.node.entity.combat.equipment.RangeWeapon;
import org.gielinor.game.node.entity.combat.equipment.Weapon;
import org.gielinor.game.node.entity.combat.equipment.Weapon.WeaponType;
import org.gielinor.game.node.entity.combat.handlers.RangeSwingHandler;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the dark bow special handler.
 * Consists out of two parts.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dark_bow
 *
 * @author Emperor
 * @version 1.0
 */
public final class DarkBowDescentSpecial extends RangeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 65;

    private static final Projectile
        DRAGON_PROJECTILE = Projectile.create((Entity) null, null, 1099, 40, 36, 41, 46, 5, 11),
        DRAGON_PROJECTILE_1 = Projectile.create((Entity) null, null, 1099, 40, 36, 41, 55, 25, 11),
        DARKNESS_PROJECTILE = Projectile.create((Entity) null, null, 1101, 40, 36, 41, 46, 5, 11),
        DARKNESS_PROJECTILE_1 = Projectile.create((Entity) null, null, 1101, 40, 36, 41, 55, 25, 11);

    private static final Graphics DRAGON_IMPACT = new Graphics(1100, 96);
    private static final Graphics DARKNESS_IMPACT = new Graphics(1103, 96);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        if (CombatStyle.RANGE.getSwingHandler().register(11235, this)
            && CombatStyle.RANGE.getSwingHandler().register(13405, this)) ;
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        Player p = (Player) entity;
        RangeWeapon rw = RangeWeapon.get(p.getEquipment().getNew(3).getId());
        if (rw == null) {
            return -1;
        }
        Weapon w = new Weapon(p.getEquipment().get(3), rw.getAmmunitionSlot(), p.getEquipment().getNew(rw.getAmmunitionSlot()));
        w.setType(rw.getWeaponType());
        state.setRangeWeapon(rw);
        state.setAmmunition(Ammunition.get(w.getAmmunition().getId()));
        state.setWeapon(w);
        if (!hasAmmo(entity, state)) {
            entity.getProperties().getCombatPulse().stop();
            p.getSettings().toggleSpecialBar();
            return -1;
        }
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        state.setFrozen(ItemDefinition.forId(state.getAmmunition().getItemId()).getName().contains("ragon arrow"));
        double mod = state.isFrozen() ? 1.5 : 1.3;
        int minDamage = state.isFrozen() ? 8 : 5;
        int max = calculateHit(entity, victim, mod);
        state.setMaximumHit(max);
        int hit = minDamage;
        if (isAccurateImpact(entity, victim, CombatStyle.RANGE, 1.15, 1.0)) {
            hit += RandomUtil.random(max - minDamage);
        }
        state.setEstimatedHit(hit);
        if (w.getType() == WeaponType.DOUBLE_SHOT) {
            hit = minDamage;
            if (isAccurateImpact(entity, victim, CombatStyle.RANGE, 1.15, 1.0)) {
                hit += RandomUtil.random(max - minDamage);
            }
            state.setSecondaryHit(hit);
        }
        useAmmo(entity, state, victim.getLocation());
        return 1 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        Graphics start = null;
        if (state.getAmmunition() != null) {
            start = state.getAmmunition().getStartGraphics();
            if (state.isFrozen()) {
                if (entity instanceof Player) {
                    entity.asPlayer().getAudioManager().send(new Audio(3736), true);
                    entity.asPlayer().getAudioManager().send(new Audio(3737), true);
                }
                DRAGON_PROJECTILE.copy(entity, victim, 5).send();
            } else {
                state.getAmmunition().getProjectile().copy(entity, victim, 5).send();
                DARKNESS_PROJECTILE.copy(entity, victim, 5).send();
            }
            if (state.getWeapon().getType() == WeaponType.DOUBLE_SHOT && state.getAmmunition().getDarkBowGraphics() != null) {
                start = state.getAmmunition().getDarkBowGraphics();
                if (state.isFrozen()) {
                    DRAGON_PROJECTILE_1.copy(entity, victim, 10).send();
                } else {
                    int speed = (int) (55 + (entity.getLocation().getDistance(victim.getLocation()) * 10));
                    Projectile.create(entity, victim, state.getAmmunition().getProjectile().getProjectileId(), 40, 36, 41, speed, 25).send();
                    DARKNESS_PROJECTILE_1.copy(entity, victim, 10).send();
                }
            }
        }
        entity.visualize(entity.getProperties().getAttackAnimation(), start);
    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
        victim.visualize(victim.getProperties().getDefenceAnimation(), state.isFrozen() ? DRAGON_IMPACT : DARKNESS_IMPACT);
    }
}

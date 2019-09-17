package plugin.npc.osrs.cerberus;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents a summoned soul.
 *
 * @author Emperor
 */
public final class SummonedSoulNPC extends AbstractNPC {

    /**
     * If the summoned soul should be death.
     */
    private boolean death;

    /**
     * If the soul is attacking.
     */
    private boolean attacking;

    /**
     * Constructs a new {@code SummonedSoulNPC Object}.
     */
    public SummonedSoulNPC() {
        super(25868, null);
    }

    /**
     * Constructs a new {@code SummonedSoulNPC Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public SummonedSoulNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void init() {
        super.init();
        super.aggressiveHandler = null;
        super.setWalks(false);
        super.lock();
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new SummonedSoulNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 25867, 25868, 25869 };
    }

    /**
     * Handles the summoned soul attack in aid of cerberus.
     *
     * @param index  The current attack index.
     * @param victim The victim.
     */
    public void aidCerberus(final int index, final Entity victim) {
        attacking = true;
        World.submit(new Pulse(1, this, victim) {

            int cycle = -1;

            @Override
            public boolean pulse() {
                Location base = getViewport().getRegion().getBaseLocation();
                switch (cycle++) {
                    case -1:
                        getProperties().setTeleportLocation(Location.create(base.transform(23 + index, 50, 0)));
                        return false;
                    case 0:
                        super.setDelay(12 + (index * 2));
                        getWalkingQueue().reset();
                        getWalkingQueue().addPath(base.getX() + 23 + index, base.getY() + 40);
                        return false;
                    case 1:
                        torment(victim);
                        super.setDelay(3);
                        return false;
                    case 2:
                        face(null);
                        getWalkingQueue().reset();
                        getWalkingQueue().addPath(base.getX() + 23 + index, base.getY() + 50);
                        attacking = false;
                        if (death) {
                            setDeath();
                            death = false;
                        }
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Torments the victim.
     *
     * @param victim The victim.
     */
    private void torment(final Entity victim) {
        final int i = getId() == 7886 ? 1 : getId() == 7887 ? 2 : 0;
        int animId = i == 0 ? -1 : (10938 + i);
        animate(Animation.create(animId));
        face(victim);
        int projectile = i == 0 ? 1821 : i == 1 ? 27 : 127;
        Projectile.magic(this, victim, projectile, 38, 32, 56, 35).send();
        World.submit(new Pulse((int) (2 + (victim.getLocation().getDistance(getLocation()) / 2)), this, victim) {

            @Override
            public boolean pulse() {
                CombatStyle style = CombatStyle.values()[i];
                if (victim.hasProtectionPrayer(style) && victim instanceof Player) {
                    int drain = 30;
                    if (((Player) victim).getEquipment().getNew(Equipment.SLOT_SHIELD).getId() == 13744) {
                        drain /= 2;
                    }
                    victim.getSkills().decrementPrayerPoints(drain);
                } else {
                    victim.getImpactHandler().manualHit(SummonedSoulNPC.this, 30, HitsplatType.NORMAL);
                }
                return true;
            }
        });
    }

    /**
     * Sets the souls as dead.
     */
    public void setDeath() {
        if (!attacking) {
            clear();
        } else {
            death = true;
        }
    }

}

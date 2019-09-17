package plugin.activity.corporeal;

import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the Dark energy core {@link org.gielinor.game.node.entity.npc.AbstractNPC}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DarkEnergyCoreNPC extends AbstractNPC {

    /**
     * The {@link plugin.activity.corporeal.CorporealBeastNPC}.
     */
    private CorporealBeastNPC corporealBeastNPC;
    /**
     * The primary {@link org.gielinor.game.node.entity.player.Player} we're following.
     */
    private Player following;
    /**
     * The following {@link org.gielinor.rs2.pulse.impl.MovementPulse}.
     */
    private MovementPulse movementPulse;
    /**
     * The last hit on the {@link #following} {@link org.gielinor.game.node.entity.Entity}.
     */
    private int lastHit = 5;

    /**
     * Constructs a new {@code CommanderZilyanaNPC} {@code Object}.
     */
    public DarkEnergyCoreNPC() {
        super(8126, null);
    }

    /**
     * Constructs a new {@code CommanderZilyanaNPC} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public DarkEnergyCoreNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void tick() {
        super.tick();
        if (corporealBeastNPC == null) {
            clear();
            return;
        }
        if (!isActive()) {
            return;
        }
        if (getId() == 8126) {
            return;
        }
        if (following == null || !following.getLocation().inArea(CorporealBeastNPC.LAIR)) {
            setFollowing();
        }
        if (!movementPulse.isRunning()) {
            setFollowing();
        }
        handleFollow();
    }

    @Override
    public void init() {
        super.init();
        getPulseManager().run(new Pulse(6) {

            @Override
            public boolean pulse() {
                transform(8127);
                updateFollow();
                return true;
            }
        });
    }

    public void updateFollow() {
        if (corporealBeastNPC == null || DeathTask.isDead(corporealBeastNPC)) {
            clear();
            return;
        }
        following = corporealBeastNPC.getRandomAttacker();
        if (following == null) {
            return;
        }
        movementPulse = new MovementPulse(this, following, Pathfinder.DUMB) {

            @Override
            public boolean pulse() {
                return false;
            }
        };
        getPulseManager().run(movementPulse, "movement");
    }

    /**
     * Sets a follower and restarts the {@link #movementPulse}.
     */
    public void setFollowing() {
        following = corporealBeastNPC.getRandomAttacker();
        if (following == null) {
            clear();
            return;
        }
        movementPulse.setDestination(following);
        movementPulse.restart();
        face(following);
    }

    /**
     * Handles following an entity.
     */
    public void handleFollow() {
        if (following == null) {
            setFollowing();
        }
        if (following == null) {
            return;
        }
        if (--lastHit < 1) {
            int hit = RandomUtil.random(1, 14);
            following.getImpactHandler().manualHit(this, hit, ImpactHandler.HitsplatType.NORMAL);
            following.getActionSender().sendMessage("The dark core creature steals some life from you for its master.", 1);
            corporealBeastNPC.getSkills().heal(hit);
            lastHit = RandomUtil.random(10, 25);
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new DarkEnergyCoreNPC(id, location);
    }

    /**
     * Sets the {@link plugin.activity.corporeal.CorporealBeastNPC}.
     *
     * @param corporealBeastNPC The Corporeal Beast npc.
     */
    public void setCorporealBeastNPC(CorporealBeastNPC corporealBeastNPC) {
        this.corporealBeastNPC = corporealBeastNPC;
    }

    /**
     * Sets the following {@link org.gielinor.rs2.pulse.impl.MovementPulse}.
     *
     * @param movementPulse The movement pulse.
     */
    public void setMovementPulse(MovementPulse movementPulse) {
        this.movementPulse = movementPulse;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 8126, 8127 };
    }
}

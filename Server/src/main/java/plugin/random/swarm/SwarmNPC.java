package plugin.random.swarm;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.rs2.pulse.impl.MovementPulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Swarm NPC.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class SwarmNPC extends AbstractNPC {

    /**
     * The player.
     */
    protected Player player;

    /**
     * The random event instance.
     */
    public SwarmRandomEvent event;

    /**
     * The last time in ticks this Swarm hit the player.
     */
    private int lastHit = 0;

    /**
     * Constructs a new {@code SwarmNPC} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public SwarmNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new SwarmNPC(id, location);
    }

    /**
     * Constructs a new {@code SwarmNPC} {@code Object}.
     */
    public SwarmNPC() {
        this(411, null);
    }

    @Override
    public void init() {
        super.init();
        if (player == null) {
            return;
        }
        setRespawn(false);
        player.getPulseManager().clear();
        getPulseManager().run(new MovementPulse(this, player, Pathfinder.DUMB) {

            @Override
            public boolean pulse() {
                return false;
            }
        }, "movement");
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (player == null) {
            clear();
            return;
        }
        if (!player.isActive() || !getLocation().withinDistance(player.getLocation(), 10)) {
            clear();
            return;
        }
        if (getLocation().getDistance(player.getLocation()) > 1) {
            return;
        }
        if (++lastHit == 10) {
            player.getImpactHandler().manualHit(player, RandomUtil.random(1, 2), ImpactHandler.HitsplatType.NORMAL);
            lastHit = 0;
        }
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
    }

    @Override
    public boolean isIgnoreMultiBoundaries(Entity victim) {
        return player != null && victim == player;
    }

    @Override
    public void onRegionInactivity() {
        super.onRegionInactivity();
        clear();
    }

    @Override
    public void clear() {
        super.clear();
        if (event != null) {
            event.terminate();
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{ 411 };
    }

}

package plugin.interaction.npc;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the imp npcs.
 *
 * @author 'Vexia
 */
public final class ImpNPC extends AbstractNPC {

    /**
     * The NPC ids of NPCs using this plugin.
     */
    private static final int[] ID = { 3134, 5007, 5008 };

    /**
     * Represents the graphics of an imp appearing and disappearing.
     */
    private static final Graphics GRAPHICS = new Graphics(86);

    /**
     * Represents the random teleport locations.
     */
    private static final Location[] LOCATIONS = new Location[]{ Location.create(3018, 3361, 0), Location.create(3053, 3364, 0), Location.create(3070, 3363, 0), Location.create(3134, 3294, 0), Location.create(3149, 3241, 0), Location.create(3185, 3240, 0), new Location(3301, 3203, 0), new Location(3222, 3218, 0), new Location(3220, 3163, 0), new Location(3080, 3250, 0), new Location(3022, 3242, 0), new Location(2956, 3224, 0), new Location(3038, 3356, 0), new Location(2966, 3380, 0), new Location(3086, 3502, 0), new Location(3164, 3472, 0), new Location(2919, 3437, 0), new Location(2864, 3448, 0), new Location(2757, 3477, 0), new Location(2742, 3443, 0), new Location(3083, 3418, 0), new Location(3046, 3374, 0), new Location(2694, 3486, 0), new Location(2662, 3307, 0), new Location(2608, 3295, 0), new Location(2458, 3418, 0), new Location(3114, 3209, 0), new Location(3301, 3203, 0), new Location(3222, 3218, 0), new Location(3220, 3163, 0), new Location(3080, 3250, 0), new Location(3022, 3242, 0), new Location(2956, 3224, 0), new Location(3038, 3356, 0), new Location(2966, 3380, 0), new Location(3086, 3502, 0), new Location(3164, 3472, 0), new Location(2919, 3437, 0), new Location(2864, 3448, 0), new Location(2757, 3477, 0), new Location(2742, 3443, 0), new Location(3083, 3418, 0), new Location(3046, 3374, 0), new Location(2694, 3486, 0), new Location(2662, 3307, 0), new Location(2608, 3295, 0), new Location(2458, 3418, 0), new Location(3114, 3209, 0) };

    /**
     * Represents the delay of teleporting.
     */
    private long teleportDelay;

    /**
     * Constructs a new {@code ImpNPC.} {@code Object}.
     */
    public ImpNPC() {
        super(0, null);
    }

    /**
     * Constructs a new {@code ImpNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    private ImpNPC(int id, Location location) {
        super(id, location, true);
        teleportDelay = World.getTicks() + RandomUtil.random(100, 500);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new ImpNPC(id, location);
    }

    @Override
    public void tick() {
        super.tick();
        if (teleportDelay < World.getTicks()) {
            teleport();
        }
    }

    @Override
    public void onImpact(Entity entity, BattleState state) {
        super.onImpact(entity, state);
        if (RandomUtil.random(15) < 4) {
            teleport();
        }
    }

    @Override
    public int[] getIds() {
        return ID;
    }

    /**
     * Method used to teleport an imp.
     */
    public final void teleport() {
        teleport(LOCATIONS[RandomUtil.random(LOCATIONS.length)]);
    }

    /**
     * Method used to teleport the imp to a teleport location.
     */
    public final void teleport(final Location location) {
        if (DeathTask.isDead(this)) {
            teleportDelay = World.getTicks() + RandomUtil.random(200, 500);
            return;
        }
        graphics(GRAPHICS);
        World.submit(new Pulse(1, this) {

            int counter = 0;
            Location loc = null;

            @Override
            public boolean pulse() {
                switch (counter++) {
                    case 2:
                        loc = location;
                        getProperties().setTeleportLocation(loc);
                        teleportDelay = World.getTicks() + RandomUtil.random(200, 500);
                        return false;
                    case 3:
                        graphics(GRAPHICS);
                        return true;
                }
                return false;
            }
        });
    }

}

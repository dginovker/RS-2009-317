package plugin.npc;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.world.map.Location;

/**
 * Represents the abstract knight npc.
 * @author 'Vexia
 */
public class KnightNPC extends AbstractNPC {

    /**
     * The NPC ids of NPCs using this plugin.
     */
    private static final int[] ID = { 19, 23, 26, 178, 179, 221, 237, 1092, 2698, 2777, 3348, 3349, 3350, 6189, 6611, 6619, 6627, 6630, 6634, 6650, 6654, 6665, 6673, 6676, 6692, 6714, 6726, 6730 };

    /**
     * Constructs a new {@code KnightNPC} {@code Object}.
     */
    public KnightNPC() {
        super(0, null, true);
    }

    /**
     * Constructs a new {@code KnightNPC} {@code Object}.
     * @param id The NPC id.
     * @param location The location.
     */
    private KnightNPC(int id, Location location) {
        super(id, location, true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new KnightNPC(id, location);
    }

    @Override
    public void init() {
        setWalks(true);
        super.init();
        setWalks(true);
        if (getId() == 179) {
            setAggressive(true);
            setDefaultBehavior();
        }
    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public boolean canAttack(final Entity killer) {
        return super.canAttack(killer);
    }

    @Override
    public int[] getIds() {
        return ID;
    }

}

package plugin.npc;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;

/**
 * Represents a rat npc.
 *
 * @author 'Vexia
 */
public class RatNPC extends AbstractNPC {

    /**
     * The NPC ids of NPCs using this plugin.
     */
    private static final int[] ID = { 47, 87, 446, 950, 4395, 4922, 4923, 4924, 4925, 4926, 4927, 4942, 4943, 4944, 4945, 16154 };

    /**
     * Represents the rat tail item.
     */
    private static final Item RAT_TAIL = new Item(300);

    /**
     * Constructs a new {@code RatNPC} {@code Object}.
     */
    public RatNPC() {
        super(0, null);
    }

    /**
     * Constructs a new {@code RatNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    private RatNPC(int id, Location location) {
        super(id, location, true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new RatNPC(id, location);
    }

    @Override
    public void finalizeDeath(final Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            final Player p = ((Player) killer);
            if (!getName().equals("Giant rat")) {
                GroundItemManager.create(new Item(526), getLocation(), (Player) killer);
            }
        }
    }

    @Override
    public int[] getIds() {
        return ID;
    }

}

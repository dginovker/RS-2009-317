package plugin.npc;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Represents a guard for King Gjuki.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GjukiChestGuardNPC extends AbstractNPC {

    /**
     * Constructs a new {@code GjukiChestGuardNPC} {@code Object}.
     */
    public GjukiChestGuardNPC() {
        this(5517, null);
    }

    /**
     * Constructs a new {@code GjukiChestGuardNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    public GjukiChestGuardNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new GjukiChestGuardNPC(id, location);
    }

    @Override
    public boolean isHidden(Player player) {
        return getAttribute("player") != null && player != getAttribute("player") || super.isHidden(player);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player player = (Player) killer;
            player.setAttribute("gjuki-guard-dead", true);
        }
        this.clear();
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5517 };
    }

}

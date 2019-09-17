package plugin.npc;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.world.map.Location;

/**
 * Represents an NPC that has been spawned by an Administrator.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class AutoSpawnNPC extends NPC {

    /**
     * Constructs a new {@code AutoSpawnNPC}.
     *
     * @param id         The id of the {@link NPC}.
     * @param location   The {@link Location} to spawn.
     * @param walkRadius The walk radius.
     */
    public AutoSpawnNPC(int id, Location location, int walkRadius) {
        super(id, location);
        super.setWalkRadius(walkRadius);
        if (walkRadius > 0) {
            super.setWalks(true);
        }
    }

    public AutoSpawnNPC() {
        this(-1, null, 0);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        this.clear();
    }
}
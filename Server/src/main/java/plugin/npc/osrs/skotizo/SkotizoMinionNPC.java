package plugin.npc.osrs.skotizo;

import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.world.map.Location;

/**
 * Created by Stan van der Bend on 09/01/2018.
 * project: GielinorGS
 * package: plugin.npc.osrs.skotizo
 */
public class SkotizoMinionNPC extends AbstractNPC {

    private final static int NPC_ID = 7287;

    private SkotizoMinionNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new SkotizoMinionNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPC_ID};
    }
}

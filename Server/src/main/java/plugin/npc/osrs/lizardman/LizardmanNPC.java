package plugin.npc.osrs.lizardman;

import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.world.map.Location;

import java.util.stream.IntStream;

/**
 * Created by Stan van der Bend on 11/01/2018.
 * project: GielinorGS
 * package: plugin.npc.osrs.lizardman
 */
public class LizardmanNPC extends AbstractNPC {

    protected LizardmanNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new LizardmanNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return IntStream.rangeClosed(6914, 6917).toArray();
    }
}

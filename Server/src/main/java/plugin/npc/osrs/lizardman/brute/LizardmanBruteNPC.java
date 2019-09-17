package plugin.npc.osrs.lizardman.brute;

import org.gielinor.game.world.map.Location;
import plugin.npc.osrs.lizardman.LizardmanNPC;

/**
 * Created by Stan van der Bend on 11/01/2018.
 * project: GielinorGS
 * package: plugin.npc.osrs.lizardman.brute
 */
public class LizardmanBruteNPC extends LizardmanNPC {

    private LizardmanBruteNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{6918, 6919};
    }

}

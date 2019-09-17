package plugin.npc.osrs.skotizo;

import org.gielinor.game.world.map.Location;

/**
 * Created by Stan van der Bend on 09/01/2018.
 *
 * todo: verify the specified NPC IDs.
 *
 * project: GielinorGS
 * package: plugin.npc.osrs.skotizo
 */
public enum AwakendAltarType {

    NORTH(27288, 27289, new Location(1695, 9904)),
    EAST(27290, 27291, new Location(1714, 9887)),
    SOUTH(27292, 27293, new Location(1695, 9871)),
    WEST(27294, 27295, new Location(1678, 9889));

    private  int
        un_awakend_npc_id,
        awakend_npc_id;

    private Location location;

    AwakendAltarType(int un_awakend_npc_id, int awakend_npc_id, Location location) {
        this.un_awakend_npc_id = un_awakend_npc_id;
        this.awakend_npc_id = awakend_npc_id;
        this.location = location;
    }

    public int getUnAwakendNPCID() {
        return un_awakend_npc_id;
    }

    public int getAwakendNPCID() {
        return awakend_npc_id;
    }

    public Location getLocation() {
        return location;
    }
}

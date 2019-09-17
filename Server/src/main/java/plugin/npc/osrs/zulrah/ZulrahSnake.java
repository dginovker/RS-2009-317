package plugin.npc.osrs.zulrah;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.pulse.Pulse;

public class ZulrahSnake extends AbstractNPC {

    private final ZulrahNPC zulrahNPC;
    private boolean dead;

    ZulrahSnake(int id, Location location, ZulrahNPC zulrahNPC) {
        super(id, location);
        this.zulrahNPC = zulrahNPC;
        if (!zulrahNPC.getSnakes().contains(this)) {
            zulrahNPC.getSnakes().add(this);
        }
        pulse();
    }

    private void pulse() {
        World.submit(new Pulse() {

            @Override
            public boolean pulse() {
                attack(zulrahNPC.getPlayer());
                return dead;
            }
        });
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new ZulrahSnake(id, location, (ZulrahNPC) objects[0]);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        zulrahNPC.getSnakes().remove(this);
        dead = true;
        super.finalizeDeath(killer);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 22045, 22046, 22047 };
    }
}

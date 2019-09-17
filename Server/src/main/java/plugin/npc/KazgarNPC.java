package plugin.npc;

import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Represents the Kazgar {@link org.gielinor.game.node.entity.npc.AbstractNPC}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KazgarNPC extends AbstractNPC {

    /**
     * Constructs a new <code>KazgarNPC</code>.
     */
    public KazgarNPC() {
        super(0, null);
    }

    /**
     * Constructs a new <code>KazgarNPC</code>.
     *
     * @param id       The id.
     * @param location The location.
     */
    public KazgarNPC(int id, Location location) {
        super(id, location, true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new KazgarNPC(id, location);
    }

    @Override
    public boolean isHidden(Player player) {
        if (getLocation().getX() <= 3240 && !player.getSavedData().getGlobalData().hasWalkedToDorgeshKaan()) {
            return true;
        }
        return super.isHidden(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2086 };
    }

}

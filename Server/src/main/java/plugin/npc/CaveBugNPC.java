package plugin.npc;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Represents a Cave bug NPC.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CaveBugNPC extends AbstractNPC {

    /**
     * Constructs a new {@code CaveBugNPC}.
     */
    public CaveBugNPC() {
        super(1832, null);
    }

    /**
     * Constructs a new {@code CaveBugNPC).
     *
     * @param id       The id.
     * @param location The location.
     */
    private CaveBugNPC(int id, Location location) {
        super(id, location, true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new CaveBugNPC(id, location);
    }

    @Override
    public void finalizeDeath(final Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            final Player player = ((Player) killer);
            //if (getLocation().getRegionId() == 12693 || getLocation().getRegionId() == 12949) {
            AchievementDiary.finalize(player, AchievementTask.SLAY_CAVE_BUG_SWAMP_CAVES);
            // }
        }
    }


    @Override
    public int[] getIds() {
        return new int[]{ 1832, 5750 };
    }

}

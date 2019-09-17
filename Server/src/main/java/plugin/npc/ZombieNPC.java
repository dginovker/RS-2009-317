package plugin.npc;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;

/**
 * Represents a zombie NPC.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ZombieNPC extends AbstractNPC {

    /**
     * Constructs a new {@code ZombieNPC} {@code Object}.
     */
    public ZombieNPC() {
        super(0, null);
    }

    /**
     * Constructs a new {@code ZombieNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    private ZombieNPC(int id, Location location) {
        super(id, location, true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new ZombieNPC(id, location);
    }

    @Override
    public void finalizeDeath(final Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            if (killer.getLocation().getRegionId() == 12438 || killer.getLocation().getRegionId() == 12439) {
                AchievementDiary.finalize(killer.asPlayer(), AchievementTask.KILL_ZOMBIE_SEWERS);
            }
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{
            73, 74, 75, 76, 77, 419, 420, 421, 422, 423, 424, 1465, 1466, 1467, 1485, 1486, 2058, 2714, 2831,
            2832, 2833, 2834, 2835, 2836, 2837, 2838, 2839, 2840, 2841, 2842, 2843, 2844, 2845, 2846, 2847,
            2848, 2863, 2864, 2865, 2866, 2867, 2868, 2869, 2870, 2871, 2878, 3066, 3622, 4392, 4393, 4394,
            5293, 5294, 5295, 5296, 5297, 5298, 5299, 5300, 5301, 5302, 5303, 5304, 5305, 5306, 5307, 5308,
            5309, 5310, 5311, 5312, 5313, 5314, 5315, 5316, 5317, 5318, 5319, 5320, 5321, 5322, 5323, 5324,
            5325, 5326, 5327, 5328, 5329, 5330, 5331, 5375, 5376, 5377, 5378, 5379, 5380, 5393, 5394, 5395,
            5396, 5397, 5398, 5399, 5400, 5401, 5402, 5403, 5404, 5405, 5406, 5407, 5408, 5409, 5410, 5623,
            5624, 5625, 5626, 5629, 5630, 5631, 5632, 5633, 5634, 5635, 5636, 5637, 5638, 5639, 5640, 5641,
            5642, 5643, 5644, 5645, 5646, 5647, 5648, 5649, 5650, 5651, 5652, 5653, 5654, 5655, 5656, 5657,
            5658, 5659, 5660, 5661, 5662, 5663, 5664, 5665, 6088, 6089, 6090, 6099, 6100, 6131, 6761, 6762,
            6763, 7641
        };
    }

}

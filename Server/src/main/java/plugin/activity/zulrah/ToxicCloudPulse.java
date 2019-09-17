package plugin.activity.zulrah;

import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * The {@link org.gielinor.rs2.pulse.Pulse} for damaging players standing in a toxic cloud.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ToxicCloudPulse extends Pulse {

    /**
     * The {@link plugin.activity.zulrah.ZulrahNPC}.
     */
    private final ZulrahNPC zulrahNPC;

    /**
     * Constructs a <code>ToxicCloudPulse</code>.
     *
     * @param zulrahNPC The Zulrah NPC.
     */
    public ToxicCloudPulse(ZulrahNPC zulrahNPC) {
        super(1, zulrahNPC, zulrahNPC.getPlayer());
        this.zulrahNPC = zulrahNPC;
    }

    @Override
    public boolean pulse() {
        if (zulrahNPC == null || zulrahNPC.getPlayer() == null || DeathTask.isDead(zulrahNPC) || DeathTask.isDead(zulrahNPC.getPlayer())) {
            return true;
        }
        Player player = zulrahNPC.getPlayer();
        for (int x = player.getLocation().getX() - 1; x < player.getLocation().getX() + 1; x++) {
            for (int y = player.getLocation().getY() - 1; y < player.getLocation().getY() + 1; y++) {
                if (RegionManager.getDeleteObject(x, y, 0) != null) {
                    player.getImpactHandler().manualHit(zulrahNPC, 1 + RandomUtil.getRandom(3), HitsplatType.VENOM);
                    return false;
                }
            }
        }
        return false;
    }
}

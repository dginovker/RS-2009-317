package plugin.npc.osrs.lizardman.shaman;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import plugin.npc.osrs.lizardman.LizardmanNPC;

/**
 * Created by Stan van der Bend on 11/01/2018.
 *
 * project: GielinorGS
 * package: plugin.npc.osrs.lizardman.shaman
 */
public class LizardmanShamanNPC extends LizardmanNPC {

    protected LizardmanShamanNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{7573, 7574};
    }

}

class LizardmanShamanCombat extends MultiSwingHandler {

    private final static String KNOCK_BACK_MESSAGE = "";

    private void knockBack(Entity entity){

        for (Player localPlayer : RegionManager.getLocalPlayers(entity, 6)) {

            final Direction throwDirection = Direction.getLogicalDirection(entity.getLocation(), localPlayer.getLocation());
            final Location destination = localPlayer.getLocation().transform(throwDirection.getStepX() * 7, throwDirection.getStepY() * 7, 0);

            if (!RegionManager.isTeleportPermitted(destination) || RegionManager.getObject(destination) != null)
                continue;

            localPlayer.teleport(destination);
            localPlayer.getStateManager().register(EntityState.STUNNED, true, 3, KNOCK_BACK_MESSAGE);
            localPlayer.getImpactHandler().manualHit(entity, 3, ImpactHandler.HitsplatType.NORMAL);
        }
    }

}

package org.gielinor.game.content.skill.member.farming;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.callback.CallBack;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the pulsed used to update all players farming states.
 *
 * @author 'Vexia
 */
public final class FarmingPulse extends Pulse implements CallBack {

    @Override
    public boolean pulse() {
        for (Player p : Repository.getPlayers()) {
            if (p == null) {
                continue;
            }
            p.getFarmingManager().cycle();
        }
        return false;
    }

    @Override
    public boolean call() {
        World.submit(this);
        return true;
    }

}

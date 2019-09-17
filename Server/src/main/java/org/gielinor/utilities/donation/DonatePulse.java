package org.gielinor.utilities.donation;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.callback.CallBack;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the callback for the {@link org.gielinor.rs2.pulse.Pulse} for checking donations.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DonatePulse implements CallBack {

    private static final Logger log = LoggerFactory.getLogger(DonatePulse.class);

    /**
     * Creates the {@link org.gielinor.utilities.donation.DonatePulse} instance.
     */
    public DonatePulse() {
    }

    @Override
    public boolean call() {
        log.info("Starting donation-check pulse...");
        World.submit(new Pulse(100) {

            @Override
            public boolean pulse() {
                if (Repository.getPlayers().size() == 0) {
                    return false;
                }
                for (Player player : Repository.getPlayers()) {
                    if (player == null || player.isArtificial() || !player.isActive()) {
                        continue;
                    }
                    if (Repository.getLobbyPlayers().contains(player)) {
                        continue;
                    }
                    player.getDonorManager().claim();
                }
                return false;
            }
        });
        return true;
    }

}

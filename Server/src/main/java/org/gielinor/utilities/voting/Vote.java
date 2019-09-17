package org.gielinor.utilities.voting;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.callback.CallBack;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles player voting utilities.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class Vote implements CallBack {

    private static final Logger log = LoggerFactory.getLogger(Vote.class);

    /**
     * Creates the {@link org.gielinor.utilities.voting.Vote} instance.
     */
    public Vote() {
    }

    /**
     * Gets the points for the given username.
     *
     * @param player
     *            The player who voted.
     * @return -1 if they have no unclaimed rewards.
     */
    public int getPoints(Player player) {
        return 0;
    }

    @Override
    public boolean call() {
        log.info("Registering vote-check pulse...");
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
                    int points = getPoints(player);
                    if (points < 1) {
                        continue;
                    }
                    log.info("[{}] received {} vote points.", player.getName(), points);
					/*points += (int) player.getDonorManager().getAdditional(points,
							player.getDonorManager().isDonor() ? 25
									: player.getDonorManager().isSuperDonor() ? 35
											: player.getDonorManager().isExtremeDonor() ? 50 : 0);*/
                    // TODO vote point bonuses for members
                    if (points > 15) {
                        points = 15;
                    }
                    player.getActionSender().sendMessage("<col=8A0808>You have been rewarded " + points
                        + " voting point" + (points > 1 ? "s" : "") + ".");
                    player.getActionSender().sendMessage(
                        "<col=8A0808>Spend them by talking to <col=08088A>Honest Jimmy</col><col=8A0808> at <col=08088A>Home</col><col=8A0808>.");
                    player.getSavedData().getGlobalData().increaseVotingPoints(points);
                }
                return false;
            }
        });
        return true;
    }

}

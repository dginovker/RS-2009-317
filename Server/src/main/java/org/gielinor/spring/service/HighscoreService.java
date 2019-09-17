package org.gielinor.spring.service;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.world.World;
import org.gielinor.rs2.config.Constants;
import org.springframework.stereotype.Service;

/**
 * Represents the Highscore saving service.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
@Service("highscoreService")
public class HighscoreService {

    /**
     * Saves a player's scores.
     *
     * @param player The player.
     */
    public void saveScores(Player player) {
    }

    /**
     * Checks a player's scores can be saved.
     *
     * @return <code>True</code> if so.
     */
    public boolean canSave(Player player) {
        if (World.getConfiguration().isDevelopmentEnabled()) {
            return false;
        }
        if (!Constants.HIGHSCORES_ENABLED) {
            return false;
        }
        if (player.getPidn() < 1) {
            return false;
        }
        for (Rights rights : Constants.HIGHSCORE_RIGHTS_DISALLOW) {
            if (player.getRights() == rights) {
                return false;
            }
        }
        return true;
    }

}

package org.gielinor.utilities;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.PlayerDetails;
import org.gielinor.game.world.World;
import org.gielinor.rs2.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts player binary data files to MySQL by loading the player and saving.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BinaryConverter {

    private static final Logger log = LoggerFactory.getLogger(BinaryConverter.class);

    /**
     * The {@link java.util.List} of failed conversions.
     */
    private static final List<String> FAILED = new ArrayList<>();

    /**
     * Converts single character files.
     */
    public static void convert(String... usernames) {
        for (String username : usernames) {
            try {
                log.info("Converting player [{}] to MySQL.", username);
                final PlayerDetails playerDetails = new PlayerDetails(username, "", null);
                playerDetails.parse();
                final Player player = new Player(playerDetails);
                World.getWorld().getAccountService().loadPlayer(player);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    log.warn("Thread was interrupted!", ex);
                    Thread.currentThread().interrupt();
                }
                World.getWorld().getAccountService().savePlayer(player);
                log.info("Successfully converted [{}] to MySQL.", username);
            } catch (Exception ex) {
                log.error("Failed to convert [{}]. They are still believers. Maybe next time?", username, ex);
                FAILED.add(username);
            }
        }
        log.info("Finished converting players to MySQL.");
        if (!FAILED.isEmpty()) {
            log.info("Failed conversions: {}.", FAILED.size());
        }
    }

    /**
     * Starts the conversion.
     */
    public static void main(String[] args) {
        Path path = Paths.get(Constants.BINARY_PLAYER_SAVE_DIRECTORY);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.{dat}")) {
            for (Path entry : stream) {
                String username = entry.getFileName().toString().replaceFirst("[.][^.]+$", "");
                try {
                    log.info("Converting [{}] to MySQL.", username);
                    final PlayerDetails playerDetails = new PlayerDetails(username, "", null);
                    playerDetails.parse();
                    final Player player = new Player(playerDetails);
                    World.getWorld().getAccountService().loadPlayer(player);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        log.warn("Thread was interrupted!", ex);
                        Thread.currentThread().interrupt();
                    }

                    if (!player.isActive()) {
                        FAILED.add(username);
                    }

                    // World.getWorld().getAccountService().savePlayer(player);
                    log.info("Converted [{}] to MySQL.", username);
                } catch (Exception ex) {
                    log.error("Failed to convert [{}]. They are still believers. Maybe next time?", username, ex);
                    FAILED.add(username);
                }
            }
        } catch (IOException ex) {
            log.error("Failed to convert players.", ex);
        }
        log.info("Finished converting players to MySQL.");
        if (!FAILED.isEmpty()) {
            log.info("Failed conversions: {}.", FAILED.size());
        }
    }

}

package org.gielinor.rs2.pulse.impl;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.config.ServerVar;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link org.gielinor.rs2.pulse.Pulse} for server-wide announcements.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ServerAnnouncementPulse extends Pulse {

    private static final Logger log = LoggerFactory.getLogger(ServerAnnouncementPulse.class);

    /**
     * The {@link java.security.SecureRandom} for selecting a random
     * announcement.
     */
    private static final SecureRandom secureRandom = new SecureRandom();

    private static List<String> serverAnnouncements = new ArrayList<>();

    /**
     * Constructs the
     * {@link org.gielinor.rs2.pulse.impl.ServerAnnouncementPulse}
     * {@link org.gielinor.rs2.pulse.Pulse}.
     */
    public ServerAnnouncementPulse() {
        super(ServerVar.fetch("server_announcement_timer", 100));
    }

    public static void populateAnnouncements() {
        serverAnnouncements.clear();
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT text FROM server_announcement WHERE enabled=1;")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        serverAnnouncements.add(resultSet.getString("text").replaceAll("\\{@name}", Constants.SERVER_NAME));
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("Failed to load server announcements.", ex);
        }
        log.info("Fetched {} server announcements.", serverAnnouncements.size());
    }

    /**
     * @return
     */
    public static String fetchAnnouncement() {
		/*String announcement = "Want to donate to {@name}? You can do so by <col=50A6C2><a href=\"http://Gielinor.org/donate.php\">clicking here</a>";
		try (Connection connection = DataSource.getGameConnection()) {
			try (PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT text FROM server_announcement WHERE enabled=1 ORDER BY RAND() LIMIT 1;")) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						announcement = resultSet.getString("text");
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}*/

        //return announcement.replaceAll("\\{@name}", Constants.SERVER_NAME);
        return serverAnnouncements.get(secureRandom.nextInt(serverAnnouncements.size()));
    }

    @Override
    public boolean pulse() {
        if (ServerVar.fetch("server_announcements", 0) == 0) {
            return false;
        }
        for (Player player : Repository.getPlayers()) {
            if (player == null || !player.isActive()) {
                continue;
            }
            for (String message : fetchAnnouncement().split("<br>")) {
                player.getActionSender().sendMessage("<img=4><col=FFFF00><shad=1>" + message, 1);
            }
        }
        if (ServerVar.fetch("server_announcement_timer", 1000) != this.getDelay()) {
            this.setDelay(ServerVar.fetch("server_announcement_timer", 1000));
        }
        return false;
    }

}

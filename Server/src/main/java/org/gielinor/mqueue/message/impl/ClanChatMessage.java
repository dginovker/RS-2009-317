package org.gielinor.mqueue.message.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.mqueue.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs a clan chat message typed.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a> TODO
 */
public class ClanChatMessage implements Message {

    private static final Logger log = LoggerFactory.getLogger(ClanChatMessage.class);

    /**
     * The player sending the message.
     */
    private final Player player;

    /**
     * The name of the owner of the clan chat.
     */
    private final String owner;

    /**
     * The message sent.
     */
    private final String message;

    /**
     * Creates the {@link ClanChatMessage} {@link Message}.
     *
     * @param player
     *            The player sending the message.
     * @param owner
     *            The name of the owner of the clan chat.
     * @param message
     *            The message sent.
     */
    public ClanChatMessage(Player player, String owner, String message) {
        this.player = player;
        this.owner = owner;
        this.message = message;
    }

    @Override
    public void execute() {
        int otherPidn = -1;
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT pidn FROM player_detail WHERE username=?")) {
                preparedStatement.setString(1, owner);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        otherPidn = resultSet.getInt("pidn");
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_clan_chat_hist (pidn, owner_pidn, chat_message, timestamp) VALUES (?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getDetails().getPidn());
                preparedStatement.setInt(2, otherPidn);
                preparedStatement.setString(3, message);
                preparedStatement.setLong(4, System.currentTimeMillis());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            log.error("{} - failed to log clan chat history: [{}].", player.getName(), message, ex);
        }
    }

}

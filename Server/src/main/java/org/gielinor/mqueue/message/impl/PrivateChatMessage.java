package org.gielinor.mqueue.message.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.mqueue.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs a private message.
 *
 * @author <a href="http://www.rune-server.org/members/mike/">Mike</a> TODO
 */
public class PrivateChatMessage implements Message {

    private static final Logger log = LoggerFactory.getLogger(PrivateChatMessage.class);

    /**
     * The player sending the private message.
     */
    private final Player player;

    /**
     * The recipient of the private message.
     */
    private final Player recipientPlayer;

    /**
     * The message sent.
     */
    private final String message;

    /**
     * Creates the {@link PrivateChatMessage} {@link Message}.
     *
     * @param player
     *            The player sending the private message.
     * @param recipientPlayer
     *            The recipient of the private message.
     * @param message
     *            The message sent.
     */
    public PrivateChatMessage(Player player, Player recipientPlayer, String message) {
        this.player = player;
        this.recipientPlayer = recipientPlayer;
        this.message = message;
    }

    @Override
    public void execute() {
        int otherPidn = recipientPlayer.getDetails().getPidn();
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_private_chat_hist (pidn, recipient_pidn, chat_message, timestamp) VALUES (?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getDetails().getPidn());
                preparedStatement.setInt(2, otherPidn);
                preparedStatement.setString(3, message);
                preparedStatement.setLong(4, System.currentTimeMillis());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            log.error("{} - failed to log PM to {}: {}.", player.getName(), recipientPlayer.getName(), message, ex);
        }
    }

}

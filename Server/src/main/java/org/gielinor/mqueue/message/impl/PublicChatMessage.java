package org.gielinor.mqueue.message.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.ChatMessage;
import org.gielinor.mqueue.message.Message;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs a public {@link ChatMessage}.
 *
 * @author <a href="http://www.rune-server.org/members/mike/">Mike</a>
 */
public class PublicChatMessage implements Message {

    private static final Logger log = LoggerFactory.getLogger(PublicChatMessage.class);

    /**
     * The player.
     */
    final Player player;

    /**
     * The chat message sent.
     */
    final ChatMessage message;

    /**
     * Creates the {@link PublicChatMessage} {@link Message}.
     *
     * @param player
     *            The player who said the message.
     * @param message
     *            The chat message sent.
     */
    public PublicChatMessage(Player player, ChatMessage message) {
        this.player = player;
        this.message = message;
    }

    @Override
    public void execute() {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_public_chat_hist (pidn, chat_color, chat_effect, chat_message, timestamp) VALUES (?, ?, ?, ?, ?)")) {
                final byte[] unpack = message.getText();
                preparedStatement.setInt(1, player.getDetails().getPidn());
                preparedStatement.setInt(2, -1);
                preparedStatement.setInt(3, message.getEffects());
                String unpacked = TextUtils.decode(unpack, unpack.length);
                preparedStatement.setString(4, unpacked);
                preparedStatement.setLong(5, System.currentTimeMillis());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            log.error("{} - Failed to save public chat log..", player.getName(), ex);
        }
    }

}

package org.gielinor.mqueue.message.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.command.Command;
import org.gielinor.mqueue.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs a player {@link Command} typed.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CommandMessage implements Message {

    private static final Logger log = LoggerFactory.getLogger(CommandMessage.class);

    /**
     * The player.
     */
    final Player player;

    /**
     * The command arguments.
     */
    final String[] arguments;

    /**
     * Creates the {@link CommandMessage} {@link Message}.
     *
     * @param player
     *            The player.
     * @param arguments
     *            The arguments.
     */
    public CommandMessage(Player player, String[] arguments) {
        this.player = player;
        this.arguments = arguments;
    }

    @Override
    public void execute() {
        if (player == null || player.getDetails() == null) {
            return;
        }
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_command_hist (pidn, command, arguments, timestamp) VALUES (?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getDetails().getPidn());
                preparedStatement.setString(2, arguments[0]);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < arguments.length; i++) {
                    stringBuilder.append(arguments[i]);
                    if (i != arguments.length - 1) {
                        stringBuilder.append(" ");
                    }
                }
                preparedStatement.setString(3, stringBuilder.toString());
                preparedStatement.setLong(4, System.currentTimeMillis());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            log.error("{} - Failed to save command history: {}.",
                player.getName(), Arrays.toString(arguments), ex);
        }
    }

}

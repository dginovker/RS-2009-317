package org.gielinor.mqueue.message.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.mqueue.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The message for staking with players.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class StakePlayerMessage implements Message {

    private static final Logger log = LoggerFactory.getLogger(StakePlayerMessage.class);

    private final Player player;
    private final Player recipient;
    private final Item[] playerItems;
    private final Item[] recipientItems;

    /**
     * Creates the trade player message.
     *
     * @param player
     *            The player.
     * @param recipient
     *            The recipient player.
     * @param playerItems
     *            The items the player received from the recipient.
     * @param recipientItems
     *            The items the recipient received from the player.
     */
    public StakePlayerMessage(Player player, Player recipient, Item[] playerItems, Item[] recipientItems) {
        this.player = player;
        this.recipient = recipient;
        this.playerItems = playerItems;
        this.recipientItems = recipientItems;
    }

    @Override
    public void execute() {
        int tradeId = -1;
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_trade_hist (pidn, recipient_pidn, timestamp) " + "VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, player.getDetails().getPidn());
                preparedStatement.setInt(2, recipient.getDetails().getPidn());
                preparedStatement.setLong(3, System.currentTimeMillis());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                tradeId = resultSet.next() ? resultSet.getInt(1) : -1;
            }
            if (tradeId > -1) {
                if (playerItems != null && playerItems.length > 0) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO player_trade_inventory (trade_id, item_id, item_count, trade_slot, recipient_pidn) "
                            + "VALUES (?, ?, ?, ?, ?)")) {
                        preparedStatement.setInt(1, tradeId);
                        int slot = 0;
                        for (Item playerReceived : playerItems) {
                            // so rec = player.pidn
                            if (playerReceived == null) {
                                continue;
                            }
                            preparedStatement.setInt(2, playerReceived.getId());
                            preparedStatement.setInt(3, playerReceived.getCount());
                            preparedStatement.setInt(4, slot);
                            slot += 1;
                            preparedStatement.setInt(5, player.getDetails().getPidn());
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                }
                if (recipientItems != null && recipientItems.length > 0) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO player_trade_inventory (trade_id, item_id, item_count, trade_slot, recipient_pidn) "
                            + "VALUES (?, ?, ?, ?, ?)")) {
                        preparedStatement.setInt(1, tradeId);
                        int slot = 0;
                        for (Item recipientReceived : recipientItems) {
                            if (recipientReceived == null) {
                                continue;
                            }
                            preparedStatement.setInt(2, recipientReceived.getId());
                            preparedStatement.setInt(3, recipientReceived.getCount());
                            preparedStatement.setInt(4, slot);
                            slot += 1;
                            preparedStatement.setInt(5, recipient.getDetails().getPidn());
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            log.error("{} - Failed to log trade history with: {}.",
                player.getName(), recipient.getName(), ex);
        }
    }

}

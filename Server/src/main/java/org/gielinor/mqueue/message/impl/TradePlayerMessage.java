package org.gielinor.mqueue.message.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.request.trade.TradeModule;
import org.gielinor.game.node.item.Item;
import org.gielinor.mqueue.message.Message;

/**
 * Logs the trade message from the {@link TradeModule}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TradePlayerMessage implements Message {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The recipient of the trade.
     */
    private final Player recipient;

    /**
     * The items the player received.
     */
    private final Item[] playerItems;

    /**
     * The items the player received.
     */
    private final Item[] recipientItems;

    /**
     * The unique trade id.
     */
    private final long tradeId;

    /**
     * Creates the trade player message.
     *
     * @param player
     *            The player.
     * @param recipient
     *            The recipient of the trade.
     * @param playerItems
     *            The items the player received.
     * @param recipientItems
     *            The items the player received.
     */
    public TradePlayerMessage(Player player, Player recipient, Item[] playerItems, Item[] recipientItems) {
        this.player = player;
        this.recipient = recipient;
        this.playerItems = playerItems;
        this.recipientItems = recipientItems;
        this.tradeId = 1 << 16 | (player.getName().hashCode() + System.currentTimeMillis());
    }

    @Override
    public void execute() {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO player_trade_hist (trade_id, pidn, recipient_pidn, timestamp) "
                    + "VALUES (?, ?, ?, ?)")) {
                preparedStatement.setLong(1, tradeId);
                preparedStatement.setInt(2, player.getPidn());
                preparedStatement.setInt(3, recipient.getPidn());
                preparedStatement.setLong(4, System.currentTimeMillis());
                preparedStatement.executeUpdate();
            }
            if (playerItems != null && playerItems.length > 0) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO player_trade_inventory (trade_id, item_id, item_count, trade_slot, recipient_pidn) "
                        + "VALUES (?, ?, ?, ?, ?)")) {
                    preparedStatement.setLong(1, tradeId);
                    int slot = 0;
                    for (Item item : playerItems) {
                        if (item == null) {
                            continue;
                        }
                        preparedStatement.setInt(2, item.getId());
                        preparedStatement.setInt(3, item.getCount());
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
                    preparedStatement.setLong(1, tradeId);
                    int slot = 0;
                    for (Item item : recipientItems) {
                        if (item == null) {
                            continue;
                        }
                        preparedStatement.setInt(2, item.getId());
                        preparedStatement.setInt(3, item.getCount());
                        preparedStatement.setInt(4, slot);
                        slot += 1;
                        preparedStatement.setInt(5, recipient.getDetails().getPidn());
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

}

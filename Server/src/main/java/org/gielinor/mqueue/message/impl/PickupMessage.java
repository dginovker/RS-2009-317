package org.gielinor.mqueue.message.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.mqueue.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs the action of picking an item up.
 *
 * @author <a href="http://www.rune-server.org/members/mike/">Mike</a>
 */
public class PickupMessage implements Message {

    private static final Logger log = LoggerFactory.getLogger(PickupMessage.class);

    /**
     * The player.
     */
    final Player player;

    /**
     * The grounditem.
     */
    final GroundItem item;

    /**
     * Creates the {@link PickupMessage} {@link Message}.
     *
     * @param player
     *            The player.
     * @param item
     *            The grounditem.
     */
    public PickupMessage(Player player, GroundItem item) {
        this.player = player;
        this.item = item;
    }

    @Override
    public void execute() {
        try (Connection connection = DataSource.getGameConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO player_pickup_hist (pidn, controller_pidn, item_id, item_quantity, item_x, item_y, item_z, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, player.getDetails().getPidn());
                preparedStatement.setInt(2,
                    item.getDropper() == null ? -100 : item.getDropper().getDetails().getPidn());
                preparedStatement.setInt(3, item.getId());
                preparedStatement.setInt(4, item.getCount());
                preparedStatement.setInt(5, item.getLocation().getX());
                preparedStatement.setInt(6, item.getLocation().getY());
                preparedStatement.setInt(7, item.getLocation().getZ());
                preparedStatement.setLong(8, System.currentTimeMillis());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            log.error("{} - failed to save picked up item: {}.", player.getName(), item.toString(), ex);
        }
    }

}

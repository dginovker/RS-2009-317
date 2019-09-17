package org.gielinor.game.node.entity.player.info.portal.punishment.type;

import org.gielinor.database.DataSource;
import org.gielinor.database.DatabaseKt;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.portal.punishment.Punishment;
import org.gielinor.game.world.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * A Ban may be given for repeated violation of the Rules of RuneScape. An account starts off with 0
 * out of 10 black marks, but this may increase if the player breaks the rules. The account is in the
 * green zone if it has no black marks, though changes through yellow, orange and red as offences
 * accumulate. The black marks and the zone can change suddenly if the player commits serious offences.
 *
 * @author 'Vexia
 * @date 24/11/2013
 *
 * @author Corey
 * @date 10/09/2017
 *
 * @see <href>http://runescape.wikia.com/wiki/Ban</href>
 */
public class Ban extends Punishment {

    private static final Logger log = LoggerFactory.getLogger(Ban.class);

    public Ban(int pidn) {
        super(pidn);
    }

    @Override
    public void save(final Player player, final Player moderator) {
        try {
            DatabaseKt.saveDataToDatabase(DataSource.getGameConnection(), "INSERT INTO player_punishment(pidn, mod_pidn, type, ip_address, mod_ip_address, mac_address, time_of_punishment, expiry, reason) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", preparedStatement -> {
                try {
                    preparedStatement.setInt(1, player.getPidn());
                    preparedStatement.setInt(2, moderator.getPidn());
                    preparedStatement.setString(3, "BAN");
                    preparedStatement.setString(4, player.getSession().getRemoteAddress());
                    preparedStatement.setString(5, moderator.getSession().getRemoteAddress());
                    preparedStatement.setString(6, player.getDetails().getMacAddress());
                    preparedStatement.setLong(7, System.currentTimeMillis());
                    preparedStatement.setLong(8, expiry);
                    preparedStatement.setString(9, reason);
                    preparedStatement.closeOnCompletion();
                } catch (SQLException ex) {
                    log.error("{} - Failed to save ban on: {}.", moderator.getName(), player.getName(), ex);
                }
                return null;
            });
        } catch (SQLException | IOException ex) {
            log.error("{} - Failed to save ban on: {}.", moderator.getName(), player.getName(), ex);
        }
        Repository.getStaffOnline().forEach(
            user -> user.getActionSender().sendMessage("<img=0>[<col=800000>Punishments</col>] <col=255>" + player.getUsername() + "</col> was banned for: " + reason));
    }

    @Override
    public void parse(int pidn) {
        String query = String.format("SELECT * FROM `player_punishment` WHERE `pidn` = %d " +
            "AND `expiry` > %d AND `type` = 'BAN' AND `reversed` = 0 ORDER BY `expiry` DESC LIMIT 1;", pidn, System.currentTimeMillis());
        try {
            DatabaseKt.loadDataFromDatabase(DataSource.getGameConnection(), query, resultSet -> {
                try {
                    if (!resultSet.first()) {
                        return null;
                    }
                    this.id = resultSet.getInt("id");
                    this.expiry = resultSet.getLong("expiry");
                    // this.permanent = this.duration <= 0; // TODO perm punishments
                    this.reason = resultSet.getString("reason");
                } catch (SQLException ex) {
                    log.error("{} - Failed to retrieve punishments.", pidn);
                }
                return null;
            });
        } catch (SQLException | IOException ex) {
            log.error("{} - Failed to retrieve punishments.", pidn);
        }
    }

    @Override
    public void inflict(final Player player) {
        if (player.isActive()) {
            player.getActionSender().sendLogout();//disconnects.
        }
    }

    @Override
    public String toString() {
        return "You have been " + (permanent ? "permanently" : "temporarily") + " banned due to breaking a rule.";
    }

}

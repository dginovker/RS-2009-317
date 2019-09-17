package org.gielinor.utilities.foxvote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.gielinor.game.node.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoxVote implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FoxVote.class);

    public static final String HOST = "";
    public static final String USER = "";
    public static final String PASS = "";
    public static final String DATABASE = "";

    private Player player;
    private Connection conn;
    private Statement stmt;

    public FoxVote(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getUsername().replace(" ", "_");
            ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='" + name + "' AND claimed=0 AND callback_date IS NOT NULL");

            while (rs.next()) {
                String timestamp = rs.getTimestamp("callback_date").toString();
                String ipAddress = rs.getString("ip_address");
                int siteId = rs.getInt("site_id");

                // -- ADD CODE HERE TO GIVE TOKENS OR WHATEVER

                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();

                log.info("[{}] claimed vote reward (issued from [{}] to [{}] at [{}]).",
                    player.getName(), siteId, ipAddress, timestamp);
            }

            destroy();
        } catch (SQLException ex) {
            log.error("Failed while attempting to check vote rewards for [{}].", player.getName(), ex);
        }
    }

    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
        } catch (SQLException ex) {
            log.error("Failed to establish database connection.", ex);
        }
        return conn != null;
    }

    public void destroy() {
        try {
            conn.close();
            conn = null;
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch (SQLException ex) {
            log.error("Failed to destroy SQL connection.", ex);
        }
    }

    public int executeUpdate(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            log.error("Failed to execute update query [{}].", query, ex);
        }
        return -1;
    }

    public ResultSet executeQuery(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            log.error("Failed to execute query [{}].", query, ex);
        }
        return null;
    }

}

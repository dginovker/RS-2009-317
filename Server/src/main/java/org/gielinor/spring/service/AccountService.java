package org.gielinor.spring.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.PlayerDetails;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.player.info.login.Response;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.system.data.DataShelf;
import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.parser.ParserSequence;
import org.gielinor.rs2.config.ServerVar;
import org.gielinor.utilities.SHA;
import org.gielinor.utilities.game.IPBanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an interface for player account
 *
 * @author <a href="http://www.rune-server.org/members/mike/">Mike</a>
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public abstract class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    /**
     * Checks the player's login, examines necessary server variables, account
     * status, and credentials.
     *
     * @param playerDetails
     *            The
     *            {@link org.gielinor.game.node.entity.player.info.PlayerDetails}.
     * @return The
     *         {@link org.gielinor.game.node.entity.player.info.login.Response}.
     */
    public Response checkLogin(PlayerDetails playerDetails) {
        Response response = Response.UNEXPECTED_RESPONSE;
        if (!playerDetails.getSession().isActive()) {
            return null;
        }
        if (ParserSequence.getSequence().size() > 0) {
            return Response.LOGIN_SERVER_OFFLINE;
        }
        if (World.getConfiguration().isDevelopmentEnabled() && !isAuthorized(playerDetails) && ServerVar.fetch("closed_beta", 0) == 1) {
            return Response.CLOSED_BETA;
        }
        if (SystemManager.isUpdating()) {
            return Response.SERVER_UPDATING;
        }
        if (Repository.getPlayerByName(playerDetails.getUsername()) != null && playerDetails.getSession().isActive()) {
            return Response.ALREADY_LOGGED_IN;
        }
        if (IPBanRepository.getRepository().flagged(playerDetails.getIp())) {
            return Response.BANNED;
        }
        try (Connection forumConnection = DataSource.getForumConnection();
             PreparedStatement forumSelectStatement = forumConnection
                 .prepareStatement("SELECT * FROM NEWcore_members WHERE Name = ?")) {
            try {
                forumSelectStatement.setString(1, playerDetails.getUsername());
            } catch (SQLException ex) {
                log.error("Failed to load profile [{}].", playerDetails.getUsername(), ex);
                response = Response.ERROR_LOADING_PROFILE;
            }
            if (response != Response.ERROR_LOADING_PROFILE) {
                try (ResultSet forumResultSet = forumSelectStatement.executeQuery()) {
                    if (!forumResultSet.first()) {
                        try {
                            if (World.getConfiguration().isDevelopmentEnabled()) {
                                return Response.LOGIN_OK;
                            }
                            return register(playerDetails);
                        } catch (IOException ex) {
                            log.error("Unable to log in [{}].", playerDetails.getUsername(), ex);
                            return Response.COULD_NOT_LOGIN;
                        }
                    }
                    response = SHA.checkPassword(playerDetails.getPassword(), forumResultSet.getString("members_pass_hash"),
                        forumResultSet.getString("members_pass_salt")) ? Response.LOGIN_OK : Response.INVALID_CREDENTIALS;
                    if (response == Response.LOGIN_OK) {
                        playerDetails.parseFromResult(forumResultSet);
                    }
                } catch (SQLException ex) {
                    log.error("Could not check details on login for [{}].", playerDetails.getUsername(), ex);
                    response = Response.ERROR_LOADING_PROFILE;
                }
            }
        } catch (Exception ex) {
            log.error("Exception during login for [{}].", playerDetails.getUsername(), ex);
            return Response.COULD_NOT_LOGIN;
        }
        return response;
    }

    /**
     * Saves a player's work.
     *
     * @param player
     *            The player.
     */
    public abstract void savePlayer(Player player);

    /**
     * Loads a player's work.
     *
     * @param player
     *            The player.
     * @return The
     *         {@link org.gielinor.game.node.entity.player.info.login.Response}.
     */
    public abstract Response loadPlayer(Player player);

    /**
     * Registers a new account.
     *
     * @param playerDetails
     *            The
     *            {@link org.gielinor.game.node.entity.player.info.PlayerDetails}.
     * @throws java.sql.SQLException
     *             The SQL exception thrown upon an invalid query.
     */
    public Response register(PlayerDetails playerDetails) throws IOException {
        playerDetails.setSalt(SHA.generateSalt());
        String hashedPassword = SHA.hashPassword(playerDetails.getPassword(), playerDetails.getSalt());
        try (Connection connection = DataSource.getForumConnection();
             PreparedStatement registerStatement = connection.prepareStatement(
                 "INSERT INTO NEWcore_members (name, members_pass_hash, members_pass_salt, member_group_id, joined, ip_address, last_visit) VALUES (?, ?, ?, ?, ?, ?, ?)",
                 Statement.RETURN_GENERATED_KEYS)) {
            playerDetails.setPassword(hashedPassword);
            registerStatement.setString(1, playerDetails.getUsername());
            registerStatement.setString(2, hashedPassword);
            registerStatement.setString(3, playerDetails.getSalt());
            registerStatement.setInt(4, Rights.REGULAR_PLAYER.getMemberGroupId());
            registerStatement.setLong(5, System.currentTimeMillis() / 1000); // ipb uses seconds not milliseconds
            registerStatement.setString(6, playerDetails.getIp());
            registerStatement.setLong(7, System.currentTimeMillis() / 1000); // same as above ^
            registerStatement.execute();
            ResultSet result = registerStatement.getGeneratedKeys();
            result.next();
            int id = result.getInt(1);
            playerDetails.setPidn(id);
            log.info("Inserted new row into NEWcore_members with id: {}.", id);
        } catch (SQLException ex) {
            log.error("SQL exception while registering [{}].", playerDetails.getUsername(), ex);
            return Response.COULD_NOT_LOGIN;
        }

        try (Connection connection = DataSource.getGameConnection();
             PreparedStatement registerStatement = connection.prepareStatement(
                 "INSERT INTO player_detail (pidn, username, password, salt, rights, member, joindate, last_ip) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                 Statement.RETURN_GENERATED_KEYS)) {
            playerDetails.setPassword(hashedPassword);
            registerStatement.setInt(1, playerDetails.getPidn());
            registerStatement.setString(2, playerDetails.getUsername());
            registerStatement.setString(3, hashedPassword);
            registerStatement.setString(4, playerDetails.getSalt());
            registerStatement.setByte(5, (byte) Rights.REGULAR_PLAYER.getMemberGroupId());
            registerStatement.setBoolean(6, false);
            registerStatement.setLong(7, System.currentTimeMillis());
            registerStatement.setString(8, playerDetails.getIp());
            registerStatement.execute();
        } catch (SQLException ex) {
            log.error("Failed to update [player_detail] table for [{}].", playerDetails.getUsername(), ex);
            return Response.COULD_NOT_LOGIN;
        }
        return Response.LOGIN_OK;
    }

    /**
     * Checks a player is authorized to logon the server during closed beta.
     *
     * @return <code>True</code> if so.
     */
    public boolean isAuthorized(PlayerDetails playerDetails) {
        if (Arrays.asList(DataShelf.fetchStringArray("authorized_addresses")).contains(playerDetails.getIp())) {
            log.info("Authorized [{}] from [{}] (because of address).", playerDetails.getUsername(), playerDetails.getIp());
            return true;
        }
        if (Arrays.asList(DataShelf.fetchStringArray("authorized_users"))
            .contains(playerDetails.getUsername().toLowerCase())) {
            log.info("Authorized [{}] from [{}] (because of username).", playerDetails.getUsername(), playerDetails.getIp());
            return true;
        }
        if (playerDetails.getRights().isAdministrator()) {
            log.info("Administrator authorized [{}] from [{}].", playerDetails.getUsername(), playerDetails.getIp());
            return true;
        }
        return false;
    }

}

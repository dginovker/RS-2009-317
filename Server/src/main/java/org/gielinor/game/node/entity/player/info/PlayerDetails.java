package org.gielinor.game.node.entity.player.info;

import org.gielinor.database.DataSource;
import org.gielinor.game.node.entity.player.info.portal.PlayerPortal;
import org.gielinor.net.IoSession;
import org.gielinor.utilities.SHA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Represents the details of a players account.
 *
 * @author 'Vexia
 */
public final class PlayerDetails {

    private static final Logger log = LoggerFactory.getLogger(PlayerDetails.class);

    /**
     * Represents the player portal of these details.
     */
    private final PlayerPortal portal = new PlayerPortal();
    /**
     * Represents the username.
     */
    private String username;
    /**
     * Represents the password.
     */
    private String password;
    /**
     * Represents the salt.
     */
    private String salt = "";
    /**
     * Represents the unique id.
     */
    private int pidn;
    /**
     * Represents the players email address
     */
    private String email;
    /**
     * Whether or not the player has verified their email on the forums
     */
    private boolean emailVerified;
    /**
     * Represents the session.
     */
    private IoSession session;
    /**
     * Represents the given credentials of a player.(default regular)
     */
    private Rights rights = Rights.REGULAR_PLAYER;
    /**
     * Represents the ip address.
     */
    private String ip;
    /**
     * Represents the last ip address logged in from.
     */
    private String lastIp;
    /**
     * Represents the mac address.
     */
    private String macAddress;
    /**
     * Represents the comp name.
     */
    private String computerName;
    /**
     * Represents the last login.
     */
    private long lastLogin = -1;
    /**
     * The player's unread messages.
     */
    private int unreadMessages;
    /**
     * Whether or not this is an invalid login (aka a root login to an account).
     */
    private boolean invalidLogin;
    /**
     * The player's join date.
     */
    public long joinDate;

    /**
     * Constructs a new {@code PlayerDetails}.
     *
     * @param username
     *            the useranme to set.
     * @param password
     *            the password to set.
     * @param session
     *            the session.
     */
    public PlayerDetails(String username, String password, IoSession session) {
        this.username = username;
        this.password = password;
        this.session = session;
        this.setIp();
    }

    /**
     * Saves the player details. TODO Convert to AccountService!
     */
    public void save() {
        if (session != null && session.getPlayer() != null) {
            if (session.getPlayer().isArtificial()) {
                return;
            }
        }

        try (Connection connection = DataSource.getForumConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "INSERT INTO NEWcore_members (member_id, name, members_pass_hash, members_pass_salt, member_group_id, joined, ip_address, last_visit) VALUES(?, ?, ?, ?, ?, ?, ?, ?) "
                     + "ON DUPLICATE KEY UPDATE name=VALUES(name), member_group_id=VALUES(member_group_id), ip_address=VALUES(ip_address), last_visit=VALUES(last_visit)")) {
            preparedStatement.setInt(1, pidn);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, salt);
            preparedStatement.setInt(5, getRights().getMemberGroupId());
            preparedStatement.setLong(6, joinDate / 1000);
            preparedStatement.setString(7, getIp());
            preparedStatement.setLong(8, System.currentTimeMillis() / 1000);
            preparedStatement.executeUpdate();
        } catch (IOException | SQLException ex) {
            log.error("Failed to insert into [NEWcore_members] for [{}].", username, ex);
        }

        try (Connection connection = DataSource.getGameConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "INSERT INTO player_detail (pidn, username, password, salt, rights, joindate) VALUES(?, ?, ?, ?, ?, ?) "
                     + "ON DUPLICATE KEY UPDATE username=VALUES(username), password=VALUES(password), salt=VALUES(salt), rights=VALUES(rights), joindate=VALUES(joindate)")) {
            preparedStatement.setInt(1, pidn);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, salt);
            preparedStatement.setInt(5, getRights().getMemberGroupId());
            preparedStatement.setLong(6, joinDate == 0 ? System.currentTimeMillis() : joinDate);
            preparedStatement.executeUpdate();
        } catch (IOException | SQLException ex) {
            log.error("Failed to insert into [player_detail] for [{}].", username, ex);
        }
    }

    public void parseFromResult(ResultSet res) throws SQLException {
        setUsername(res.getString("name"));
        setPidn(res.getInt("member_id"));
        portal.setPidn(getPidn());
        setPassword(res.getString("members_pass_hash"));

        if (res.getString("mgroup_others") != null && res.getString("mgroup_others").length() > 0) {
            int[] secondaryUserGroups = Arrays.stream(res.getString("mgroup_others").split(",")).mapToInt(Integer::parseInt).toArray();
            setRights(Rights.Companion.forId(res.getByte("member_group_id"), secondaryUserGroups));
        } else {
            setRights(Rights.Companion.forId(res.getByte("member_group_id")));
        }

        setSalt(res.getString("members_pass_salt"));
        setJoinDate(res.getLong("joined"));
        setUnreadMessages(res.getInt("msg_count_new"));
        this.email = res.getString("email");
        this.emailVerified = isEmailVerified();
    }

    public boolean savePassword() {
        if (session != null && session.getPlayer() != null) {
            if (session.getPlayer().isArtificial()) {
                return false;
            }
        }

        try (Connection connection = DataSource.getForumConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "UPDATE NEWcore_members SET members_pass_hash = ?, members_pass_salt = ? WHERE member_id = ?")) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, salt);
            preparedStatement.setInt(3, pidn);
            preparedStatement.executeUpdate();
        } catch (IOException | SQLException ex) {
            log.error("Failed to update [NEWcore_members] for [{}].", username, ex);
            return false;
        }

        try (Connection connection = DataSource.getGameConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "UPDATE player_detail SET password = ?, salt = ? WHERE pidn = ?")) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, salt);
            preparedStatement.setInt(3, pidn);
            preparedStatement.executeUpdate();
        } catch (IOException | SQLException ex) {
            log.error("Failed to update [player_detail] for [{}].", username, ex);
            return false;
        }

        return true;
    }


    /**
     * Method used to parse the details of this class.
     */
    public void parse() {
        try (Connection connection = DataSource.getForumConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM NEWcore_members WHERE name=?")) {
            statement.setString(1, username.replaceAll("_", " "));
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                setSalt(SHA.generateSalt());
                setPassword(SHA.hashPassword(password, getSalt()));
                save();
                return;
            }
            parseFromResult(results);
        } catch (SQLException | IOException ex) {
            log.error("Failed to read player details for [{}].", username, ex);
        }
    }

    private boolean isEmailVerified() {
        try (Connection connection = DataSource.getForumConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM NEWcore_validating WHERE member_id = ?")) {
            statement.setInt(1, pidn);
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                return true;
            }
        } catch (SQLException | IOException ex) {
            log.error("Failed to check if email is verified for [{}].", username, ex);
        }
        return false;
    }

    /**
     * Gets the rights.
     *
     * @return The rights.
     */
    public Rights getRights() {
        return rights;
    }

    /**
     * Sets the credentials.
     *
     * @param rights
     *            The credentials to set.
     */
    public void setRights(Rights rights) {
        this.rights = rights;
    }

    /**
     * Gets the session.
     *
     * @return The session.
     */
    public IoSession getSession() {
        return session;
    }

    /**
     * Sets the session.
     *
     * @param session
     *            The session to set.
     */
    public void setSession(IoSession session) {
        this.session = session;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the password.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the username.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Sets the salt.
     *
     * @param salt
     *            The salt.
     */
    public void setSalt(final String salt) {
        this.salt = salt;
    }

    /**
     * Gets the email address.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Whether or not the email address is verified.
     *
     * @return Email verified true/false.
     */
    public boolean getEmailVerified() {
        return emailVerified;
    }

    /**
     * Gets the ip.
     *
     * @return The ip.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the ip.
     *
     * @param ip
     *            The ip to set.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Method used to set the ip.
     */
    public void setIp() {
        this.ip = session == null ? "127.0.0.1" : session.getRemoteAddress().replaceAll("/", "").split(":")[0];
    }

    /**
     * Gets the last ip.
     *
     * @return The last ip.
     */
    public String getLastIp() {
        return lastIp;
    }

    /**
     * Sets the last ip.
     *
     * @param lastIp
     *            The last ip to set.
     */
    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    /**
     * Gets the lastLogin.
     *
     * @return The lastLogin.
     */
    public long getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets the lastLogin.
     *
     * @param lastLogin
     *            The lastLogin to set.
     */
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Gets the username.
     *
     * @return The username.
     */

    public String getUsername() {
        return username;
    }

    /**
     * Gets the portal.
     *
     * @return The portal.
     */
    public PlayerPortal getPortal() {
        return portal;
    }

    /**
     * Gets the pidn.
     *
     * @return the pidn.
     */
    public int getPidn() {
        return pidn;
    }

    /**
     * Sets the pidn.
     *
     * @param pidn
     *            the pidn.
     */
    public void setPidn(int pidn) {
        this.pidn = pidn;
    }

    /**
     * Gets the password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the salt.
     *
     * @return The salt.
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the join date.
     *
     * @param joinDate
     *            The join date.
     */
    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * Gets the macAddress.
     *
     * @return The macAddress.
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Sets the macAddress.
     *
     * @param macAddress
     *            The macAddress to set.
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * Gets the compName.
     *
     * @return The compName.
     */
    public String getCompName() {
        return computerName;
    }

    /**
     * Sets the compName.
     *
     * @param compName
     *            The compName to set.
     */
    public void setCompName(String compName) {
        this.computerName = compName;
    }

    public int getUnreadMessages() {
        return this.unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    /**
     *
     */
    public void setInvalidLogin(boolean invalidLogin) {
        this.invalidLogin = invalidLogin;
    }

    public boolean isInvalidLogin() {
        return invalidLogin;
    }

}

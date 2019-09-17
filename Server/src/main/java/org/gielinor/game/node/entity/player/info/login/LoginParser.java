package org.gielinor.game.node.entity.player.info.login;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.PlayerDetails;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.system.monitor.PlayerMonitor;
import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.world.WorldList;
import org.gielinor.parser.ParserSequence;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.game.IPBanRepository;
import org.gielinor.utilities.game.MacBanRepository;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a <b>Login</b> from a {@link Player}. A login consists of the
 * client sending data to the server which is used to determine the type of
 * login and any other "flags" that create a "bad" <b>Login</b>.
 *
 * @author Emperor
 * @author 'Vexia
 *
 */
public final class LoginParser implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(LoginParser.class);

    /**
     * The lock used to disable 2 of the same player being logged in.
     */
    private static final Lock LOCK = new ReentrantLock();

    /**
     * The {@link PlayerDetails}, used to load an account.
     */
    private final PlayerDetails details;

    /**
     * The password entered in the login request.
     */
    @SuppressWarnings("unused")
    private final String password;

    /**
     * The login type.
     */
    private final LoginType type;

    /**
     * The player in the game, used for reconnect login type.
     */
    private Player gamePlayer;

    /**
     * The mac address used.
     */
    private String mac;

    /**
     * Constructs a new {@code LoginParser} {@code Object}.
     *
     * @param details
     *            the player details.
     * @param password
     *            The password entered in the login request.
     * @param type
     *            The login type.
     */
    public LoginParser(PlayerDetails details, String password, LoginType type) {
        this.details = details;
        this.password = password;
        this.type = type;
    }

    @Override
    public void run() {
        LOCK.lock();

        try {
            if (validateRequest()) {
                parseDetails();
                handleLogin();
            }
        } catch (Throwable t) {
            log.error("Failed to validate login request for [{}] from [{}].",
                details.getUsername(), details.getIp(), t);
            flag(Response.ERROR_LOADING_PROFILE);
        }

        LOCK.unlock();
    }

    /**
     * Handles the actual login.
     */
    private void handleLogin() {
        Response response;

        try {
            response = World.getWorld().getAccountService().checkLogin(details);
        } catch (Exception ex) {
            log.error("Failed to handle login for [{}].", details.getUsername(), ex);
            flag(Response.ERROR_LOADING_PROFILE);
            return;
        }

        if (response != Response.LOGIN_OK) {
            flag(response);
            return;
        }

        if (details.getPortal().getBan().isPunished()) {
            flag(Response.ACCOUNT_DISABLED);
            return;
        }

        details.setMacAddress(mac);
        Player p = getWorldInstance();

        if (p != null) {
            flag(Response.ALREADY_LOGGED_IN);
            return;
        }

        final Player player = new Player(details);
        int onlineCount = Repository.getPlayers().size() + 1; // +1 is them, since they aren't included yet.
        log.info("Registered player [{}] from [{}] - total online: {}",
            player.getName(), details.getIp(), onlineCount);
        player.setAttribute("login_type", type);
        WorldList.getWorldService().updatePlayers();
        response = World.getWorld().getAccountService().loadPlayer(player);

        if (response != Response.LOGIN_OK) {
            flag(response);
            return;
        }

        Repository.getPlayerNames().put(player.getName().toLowerCase().replace(" ", "_"), player);
        Repository.getPlayerIds().put(player.getPidn(), player);

        World.submit(new Pulse(1) {

            @Override
            public boolean pulse() {
                try {
                    if (details.getSession().isActive()) {
                        if (!Repository.getPlayers().contains(player)) {
                            Repository.getPlayers().add(player);
                        }

                        player.getDetails().getSession().setPlayer(player);
                        flag(Response.LOGIN_OK);
                        player.init();
                        String address = details.getIp() + "|" + details.getMacAddress();
                        player.getMonitor().log(address, PlayerMonitor.ADDRESS_LOG);
                        player.getMonitor().log(address, PlayerMonitor.ADDRESS_HISTORY_LOG, false);
                    } else {
                        Repository.getPlayerNames().remove(player.getName().toLowerCase().replace(" ", "_"));
                        Repository.getPlayerIds().remove(player.getPidn());
                    }
                } catch (Throwable t) {
                    log.error("Failed to init player session for [{}] from [{}].",
                        player.getName(), details.getIp(), t);
                    Repository.getPlayerNames().remove(player.getName().toLowerCase().replace(" ", "_"));
                    Repository.getPlayerIds().remove(player.getPidn());
                }

                return true;
            }
        });
    }

    /**
     * Gets the player instance in the current world.
     *
     * @return The player instance, if found.
     */
    private Player getWorldInstance() {
        Player player = Repository.getDisconnectionQueue().get(details.getUsername());

        if (player == null) {
            player = gamePlayer;
        }

        return player;
    }

    /**
     * Checks if the login request is valid.
     *
     * @return <code>True</code> if the request is valid.
     */
    private boolean validateRequest() {
        if (ParserSequence.getSequence().size() > 0) {
            return flag(Response.LOGIN_SERVER_OFFLINE);
        }

        if (!details.getSession().isActive()) {
            return false;
        }

        if (SystemManager.isUpdating()) {
            return flag(Response.SERVER_UPDATING);
        }

        if (IPBanRepository.getRepository().flagged(details.getIp())
            || MacBanRepository.getRepository().flagged(details.getMacAddress())) {
            return flag(Response.BANNED);
        }

        if (!TextUtils.isValidName(details.getUsername())) {
            return flag(Response.ACCOUNT_DISABLED);
        }

        return !((gamePlayer = Repository.getPlayerByName(details.getUsername())) != null
            && gamePlayer.getSession().isActive()) || flag(Response.ALREADY_LOGGED_IN);
    }

    /**
     * Method used to flag the client with a response code determined by the
     * attempt of login and which "rules" we break.
     *
     * @param response
     *            the {@link Response}.
     * @return <code>True</code> if successfully logged in.
     */
    public boolean flag(Response response) {
        details.getSession().write(response, true);
        return response == Response.LOGIN_OK;
    }

    /**
     * Method used to sync the details.
     */
    public void parseDetails() {
        mac = details.getMacAddress();
        // details.parse();
        // details.setPidn(details.getUsername().hashCode());
    }

}

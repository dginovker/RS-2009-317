package com.runescape.util;

import com.runescape.Constants;
import main.java.com.runescape.Game;

import java.io.IOException;

/**
 * Represents a return response from the server.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public enum Response {
    /**
     * An unexpected server response occurred.
     */
    UNEXPECTED_RESPONSE(0, "Unexpected server response."),
    /**
     * Could not display advertisement video, logging in in x seconds.
     */
    COULD_NOT_DISPLAY_AD(1, "Unexpected server response."),
    /**
     * A successful login.
     */
    LOGIN_OK(2, ""),
    /**
     * Invalid username or password has been entered.
     */
    INVALID_CREDENTIALS(3, "", "Invalid username or password."),
    /**
     * This account is banned.
     */
    ACCOUNT_DISABLED(4, "Your account has been disabled.", "Please check the forums for details."),
    /**
     * This account is already logged in.
     */
    ALREADY_LOGGED_IN(5, "Your account is already logged in.", "Try again in 60 secs..."),
    /**
     * We have updated and client needs to be reloaded.
     */
    UPDATED(6, Constants.CLIENT_NAME + " has been updated!", "Please download the new client."),
    /**
     * The world is full.
     */
    FULL_WORLD(7, "This world is full.", "Please use a different world."),
    /**
     * Login server is offline.
     */
    LOGIN_SERVER_OFFLINE(8, "Unable to connect.", "Login server offline."),
    /**
     * The login limit has been exceeded.
     */
    LOGIN_LIMIT_EXCEEDED(9, "Login limit exceeded.", "Too many connections from your address."),
    /**
     * The session key was invalid.
     */
    BAD_SESSION_ID(10, "Unable to connect.", "Bad session id."),
    /**
     * The password is too weak, and should be improved.
     */
    WEAK_PASSWORD(11, "We suspect someone knows your password.", "Press 'change your password' on the website."),
    /**
     * When trying to connect to a members world while being f2p.
     */
    MEMBERS_WORLD(12, "You need a members account to login to this world.", "Please subscribe, or use a different world."),
    /**
     * Could not login.
     */
    COULD_NOT_LOGIN(13, "Could not complete login.", "Please try using a different world."),
    /**
     * The server is currently updating.
     */
    SERVER_UPDATING(14, "The server is being updated.", "Please wait 1 minute and try again."),
    /**
     * Too many incorrect login attempts from your address.
     */
    TOO_MANY_INCORRECT_LOGINS(16, "Too many incorrect logins from your address.", "Please wait 5 minutes before trying again."),
    /**
     * When logging on a free world while standing in members area.
     */
    STANDING_IN_MEMBER(17, "You are standing in a members-only area.", "To play on this world move to a free area first"),
    /**
     * This account is locked as it might have been stolen.
     */
    LOCKED(18, "Account locked as we suspect it has been stolen.", "Press 'recover a locked account' on the website."),
    /**
     * Closed beta going on.
     */
    CLOSED_BETA(19, "This world is running a closed Beta.", "Sorry invited players only.", "Please use a different world."),
    /**
     * The login server connected to is invalid.
     */
    INVALID_LOGIN_SERVER(20, "Invalid loginserver requested,", "Please try using a different world."),
    /**
     * The player's profile is being transferred.
     */
    TRANSFERRED(21) {
        @Override
        public void handle(Game game) {
            try {
                game.setLoggingIn(true);
                for (int readTime = game.getSocketStream().read(); readTime >= 0; readTime--) {
                    game.setLoginMessage1("You have only just left another world");
                    game.setLoginMessage2("Your profile will be transferred in: " + readTime + " seconds");
                    game.setLoginMessage3("");
                    game.drawLoginScreen(false);
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception _ex) {
                    }
                }
                game.login(game.getLoginUsername(), game.getLoginPassword(), false);
            } catch (IOException e) {
                e.printStackTrace();
                game.setLoggingIn(false);
                game.setLoginMessage1("Unexpected server response.");
                game.setLoginMessage2("Please try using a different world.");
                game.setLoginMessage3("");
            }
        }
    },
    /**
     * When the player's saved file exists, but is unable to be loaded.
     */
    ERROR_LOADING_PROFILE(24, "Error loading your profile.", "Please contact customer support."),
    /**
     * This computer address is disabled as it was used to break our rules.
     */
    BANNED(26, "This computers address has been blocked", "as it was used to break our rules.");

    /**
     * The response id.
     */
    private final int id;
    /**
     * The messages.
     */
    private final String[] messages;

    /**
     * Constructs a new response.
     *
     * @param messages The messages.
     */
    Response(int id, String... messages) {
        this.id = id;
        this.messages = messages;
    }

    /**
     * Gets a {@link com.runescape.util.Response} by id.
     *
     * @param id The response id.
     * @return The response.
     */
    public static Response getResponse(int id) {
        for (Response response : Response.values()) {
            if (response.id == id) {
                return response;
            }
        }
        return null;
    }

    /**
     * Handles the response.
     *
     * @param game The {@link Game} instance.
     */
    public void handle(Game game) {
        if (getMessages() != null) {
            if (getMessages().length >= 1) {
                game.setLoginMessage1(getMessages()[0]);
            }
            if (getMessages().length >= 2) {
                game.setLoginMessage2(getMessages()[1]);
            }
            if (getMessages().length == 3) {
                game.setLoginMessage3(getMessages()[2]);
            }
            return;
        }
        game.setLoginMessage1("Unexpected server response.");
        game.setLoginMessage2("Please try using a different world.");
        game.setLoginMessage3("");
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the messages.
     *
     * @return The messages.
     */
    public String[] getMessages() {
        return messages;
    }
}

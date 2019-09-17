package org.gielinor.game.node.entity.player.link.request;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Ironman;
import org.gielinor.game.node.entity.player.link.request.trade.TradeModule;

/**
 * Represents a request type.
 *
 * @author 'Vexia
 * @author Emperor
 */
public class RequestType {

    /**
     * The trade request type.
     */
    public static final RequestType TRADE = new RequestType("Sending a trade offer...", ":tradereq:", new TradeModule(), "trade");
    /**
     * Represents the message to send for the player when requesting.
     */
    private final String message;
    /**
     * Represents the requesting message type.
     */
    private final String requestMessage;
    /**
     * Represents the module used for this type of requesting.
     */
    private final RequestModule module;
    /**
     * Gets the name of this type.
     */
    public final String name;

    /**
     * Constructs a new {@code RequestManager {@code Object}.
     *
     * @param message        the message.
     * @param requestMessage the requesting message.
     */
    public RequestType(String message, String requestMessage, RequestModule module, String name) {
        this.message = message;
        this.requestMessage = requestMessage;
        this.module = module;
        this.name = name;
    }

    /**
     * Checks if the request can be made.
     *
     * @param player The player.
     * @param target The target.
     * @return <code>True</code> if so.
     */
    public boolean canRequest(Player player, Player target) {
        return Ironman.canRequest(player, target, this);
    }

    /**
     * Gets the requesting message formated with the targets name.
     *
     * @param target the target.
     * @return the message to send.
     */
    public String getRequestMessage(Player target) {
        return target.getUsername() + getRequestMessage();
    }

    /**
     * Method used to get the request type by the option being handled.
     *
     * @param option the option being handled.
     * @return the request type in correlation.
     */
    public static RequestType forOption(String option) {
        return TRADE;
    }

    /**
     * Gets the message.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the requestMessage.
     *
     * @return The requestMessage.
     */
    public String getRequestMessage() {
        return requestMessage;
    }

    /**
     * Gets the request module.
     *
     * @return the module.
     */
    public RequestModule getModule() {
        return module;
    }

    /**
     * Gets the name of this type.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }
}

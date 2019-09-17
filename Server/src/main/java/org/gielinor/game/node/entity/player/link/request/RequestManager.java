package org.gielinor.game.node.entity.player.link.request;

import org.gielinor.game.node.entity.player.Player;

/**
 * Represents a managing class for requests of a player.
 *
 * @author 'Vexia
 */
public final class RequestManager {

    /**
     * Represents the player instance.
     */
    private final Player player;

    /**
     * Represents the target being requested.
     */
    private Player target;

    /**
     * Constructs a new {@code RequestManager} {@code Object}.
     *
     * @param player the player.
     */
    public RequestManager(final Player player) {
        this.player = player;
    }

    /**
     * Method used to send a request type to a target.
     *
     * @param target the target.
     * @param type   the type of request.
     * @return <code>True</code> if successful in requesting.
     */
    public boolean request(final Player target, final RequestType type) {
        if (!canRequest(type, target)) {
            return false;
        }
        if (acceptExisting(target, type)) {
            return true;
        }
        player.getActionSender().sendMessage(type.getMessage());
        target.getActionSender().sendMessage(type.getRequestMessage(player));
        player.setAttribute("lastRequest", type);
        this.target = target;
        return true;
    }

    /**
     * Method used to check if a player can continue with a request.
     *
     * @param type   the type.
     * @param target the target.
     * @return <code>True</code> if they can request.
     */
    private boolean canRequest(RequestType type, Player target) {
        //TODO: is busy.
        if (!target.getLocation().withinDistance(player.getLocation(), 15)) {
            player.getActionSender().sendMessage("Unable to find " + target.getName() + ".");
            return true;
        }
        return type.canRequest(player, target);
    }

    /**
     * Method used to check if we can accept an existing request.
     *
     * @param target the target.
     * @param type   the type.
     * @return <code>True</code> if so.
     */
    public boolean acceptExisting(final Player target, final RequestType type) {
        final RequestType lastType = target.getAttribute("lastRequest", null);
        if (lastType == type && target.getRequestManager().getTarget() != null && player == target.getRequestManager().getTarget()) {
            clear();
            target.getRequestManager().clear();
            type.getModule().open(player, target);
            return true;
        }
        return false;
    }

    /**
     * Method used to clear the cached target.
     */
    public void clear() {
        this.target = null;
    }

    /**
     * Gets the player.
     *
     * @return the player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the target.
     *
     * @return the target.
     */
    public Player getTarget() {
        return target;
    }

}

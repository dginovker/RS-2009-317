package org.gielinor.game.node.entity.player.requirement;

import org.gielinor.game.node.entity.player.Player;

/**
 * Represents a requirement for an action.
 *
 * @author David Insley
 */
public abstract class Requirement {

    /**
     * Whether or not the {@link Player} has this requirement.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public abstract boolean hasRequirement(Player player);

    /**
     * Displays the error message for this requirement.
     *
     * @param player The player.
     */
    public abstract void displayErrorMessage(Player player);

    /**
     * Fulfills this requirement.
     *
     * @param player The player.
     */
    public abstract void fulfill(Player player);

}

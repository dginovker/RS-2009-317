package plugin.interaction.item.rottenpotato.impl;

import org.gielinor.game.node.entity.player.Player;

/**
 * Represents an action class for Rotten Potato actions.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public abstract class ActionClass {

    /**
     * The variable name to call.
     */
    public abstract String getVariableName();

    /**
     * The action to execute.
     *
     * @param player The player.
     */
    public abstract boolean execute(Player player);
}

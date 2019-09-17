package org.gielinor.game.component;

import org.gielinor.game.node.entity.player.Player;

/**
 * An event called when the interface gets closed.
 *
 * @author Emperor
 */
public interface CloseEvent {

    /**
     * Called when the interface gets closed.
     *
     * @param player    The player.
     * @param component The component.
     */
    void close(Player player, Component component);

    /**
     * Whether or not we allow the interface to be closed.
     *
     * @param player    The player.
     * @param component The component.
     * @return <code>True</code> if successful, <code>False</code> if the component should remain open.
     */
    boolean canClose(Player player, Component component);

}
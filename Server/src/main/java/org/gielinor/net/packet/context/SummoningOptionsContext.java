package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The summoning options context.
 *
 * @author <a href="http://Gielinor.org/">Gielinor</a>
 */
public class SummoningOptionsContext implements Context {

    /**
     * The player reference.
     */
    private Player player;

    /**
     * The options.
     */
    private String[] options;

    /**
     * Construct a new {@code SummoningOptionsContext} {@code Object}.
     *
     * @param player  The player.
     * @param options The options.
     */
    public SummoningOptionsContext(Player player, String... options) {
        this.player = player;
        this.options = options;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Transforms this context.
     *
     * @param player  The player.
     * @param options The options.
     */
    public SummoningOptionsContext transform(Player player, String... options) {
        return new SummoningOptionsContext(player, options);
    }

    /**
     * Sets the player.
     *
     * @param player The player.
     * @return This context instance.
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Get the summoning options.
     *
     * @return The options.
     */
    public String[] getOptions() {
        return options;
    }
}

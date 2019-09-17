package org.gielinor.game.content.bot;

import org.gielinor.game.node.entity.player.Player;

/**
 * Represents a {@link org.gielinor.game.content.bot.Script} for the {@link org.gielinor.game.content.bot.Bot}.
 */
public abstract class Script extends Bot {

    public Script(Player player) {
        super(player);
    }
}

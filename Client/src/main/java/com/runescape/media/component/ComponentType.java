package com.runescape.media.component;

import com.runescape.Game;
import com.runescape.cache.media.RSComponent;

/**
 * Represents an {@link com.runescape.cache.media.RSComponent} type to be drawn.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public abstract class ComponentType {
    /**
     * The {@link com.runescape.Game} instance.
     */
    private final Game game;

    /**
     * Constructs a new <code>ComponentType</code>.
     *
     * @param game The {@link com.runescape.Game} instance.
     */
    public ComponentType(Game game) {
        this.game = game;
    }

    /**
     * Draws the component type.
     *
     * @param rsComponent  The parent {@link com.runescape.cache.media.RSComponent}.
     * @param rsComponent1 The child {@link com.runescape.cache.media.RSComponent}.
     * @param interfaceX   The parent x coordinate to draw.
     * @param interfaceY   The parent y coordinate to draw.
     * @param childX       The child x coordinate to draw.
     * @param childY       The child y coordinate to draw.
     */
    public abstract void draw(RSComponent rsComponent, RSComponent rsComponent1, int interfaceX, int interfaceY, int childX, int childY);

    /**
     * Gets the {@link com.runescape.Game} instance.
     *
     * @return The game instance.
     */
    public Game getGame() {
        return game;
    }
}

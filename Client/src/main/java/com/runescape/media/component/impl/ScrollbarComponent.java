package com.runescape.media.component.impl;

import main.java.com.runescape.Game;
import com.runescape.cache.media.RSComponent;
import com.runescape.media.Scrollbar;
import com.runescape.media.component.ComponentType;

/**
 * Represents the scrollbar {@link com.runescape.cache.media.RSComponent} to draw.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ScrollbarComponent extends ComponentType {

    /**
     * Constructs a new <code>ScrollbarComponent</code>.
     *
     * @param game The {@link Game} instance.
     */
    public ScrollbarComponent(Game game) {
        super(game);
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
    @Override
    public void draw(RSComponent rsComponent, RSComponent rsComponent1, int interfaceX, int interfaceY, int childX, int childY) {
        if (rsComponent1.scrollPosition > rsComponent1.scrollMax - rsComponent1.height) {
            rsComponent1.scrollPosition = rsComponent1.scrollMax - rsComponent1.height;
        }
        if (rsComponent1.horizontalScrollPosition > rsComponent1.horizontalScrollMax - rsComponent1.width) {
            rsComponent1.horizontalScrollPosition = rsComponent1.horizontalScrollMax - rsComponent1.width;
        }
        if (rsComponent1.scrollPosition < 0) {
            rsComponent1.scrollPosition = 0;
        }
        if (rsComponent1.horizontalScrollPosition < 0) {
            rsComponent1.horizontalScrollPosition = 0;
        }
        getGame().getComponentDrawing().drawComponent(rsComponent1, childX, childY, rsComponent1.scrollPosition, rsComponent1.horizontalScrollPosition);
        if (rsComponent1.scrollMax > rsComponent1.height) {
            Scrollbar.draw(getGame(), rsComponent1.height, rsComponent1.scrollPosition, childY, childX + rsComponent1.width, rsComponent1.scrollMax, false, rsComponent1.newScroller);
        }
        if (rsComponent1.horizontalScrollMax > rsComponent1.width) {
            Scrollbar.drawHorizontal(getGame(), rsComponent1.width, rsComponent1.horizontalScrollPosition, childY + rsComponent1.height, childX, rsComponent1.horizontalScrollMax);
        }
    }
}

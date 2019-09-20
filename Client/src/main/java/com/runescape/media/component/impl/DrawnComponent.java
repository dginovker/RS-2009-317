package com.runescape.media.component.impl;

import com.runescape.Game;
import com.runescape.cache.media.RSComponent;
import com.runescape.media.Raster;
import com.runescape.media.component.ComponentType;

/**
 * Represents the drawn pixels {@link com.runescape.cache.media.RSComponent} to draw.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class DrawnComponent extends ComponentType {

    /**
     * Constructs a new <code>DrawnComponent</code>.
     *
     * @param game The {@link Game} instance.
     */
    public DrawnComponent(Game game) {
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
        boolean hovered = false;
        if (getGame().getAnInt1039() == rsComponent1.id || getGame().getAnInt1048() == rsComponent1.id || getGame().getAnInt1026() == rsComponent1.id) {
            hovered = true;
        }
        int shownColour;
        if (getGame().interfaceIsSelected(rsComponent1)) {
            shownColour = rsComponent1.enabledColor;
            if (hovered && rsComponent1.enabledMouseOverColor != 0) {
                shownColour = rsComponent1.enabledMouseOverColor;
            }
        } else {
            shownColour = rsComponent1.textColor;
            if (hovered && rsComponent1.disabledMouseOverColor != 0) {
                shownColour = rsComponent1.disabledMouseOverColor;
            }
        }
        if (rsComponent1.opacity == 0) {
            if (rsComponent1.filled) {
                Raster.drawPixels(rsComponent1.height, childY, childX, shownColour, rsComponent1.width);
            } else {
                Raster.fillPixels(childX, rsComponent1.width, rsComponent1.height, shownColour, childY);
            }
        } else if (rsComponent1.filled) {
            Raster.method335(shownColour, childY, rsComponent1.width, rsComponent1.height, 256 - (rsComponent1.opacity & 0xff), childX);
        } else {
            Raster.method338(childY, rsComponent1.height, 256 - (rsComponent1.opacity & 0xff), shownColour, rsComponent1.width, childX);
        }
    }
}

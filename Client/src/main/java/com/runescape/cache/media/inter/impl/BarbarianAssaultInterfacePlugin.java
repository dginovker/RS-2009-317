package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for Barbarian Assault interfaces.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class BarbarianAssaultInterfacePlugin implements InterfacePlugin {
    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(255);
        rsComponent.addText(256, "Points: " + Integer.MAX_VALUE, rsFonts, RSComponent.FANCY, 0xFF981F, true, true, 70, 14, -1);
        rsComponent.addChild(256, 20, 20);
        return rsComponent;
    }
}

package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for dark interfaces.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class DarkInterfacePlugin implements InterfacePlugin {
    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(257);
        RSComponent child = rsComponent.addInterface(258);
        child.type = 3;
        child.textColor = 0;
        child.opacity = 60;
        child.filled = true;
        rsComponent.addChild(258, 0, 0);
        return rsComponent;
    }
}

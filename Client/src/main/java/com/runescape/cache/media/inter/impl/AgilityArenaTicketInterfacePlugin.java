package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for the Brimhaven agility arena ticket exchange.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class AgilityArenaTicketInterfacePlugin implements InterfacePlugin {
    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = RSComponent.forId(8383);
        rsComponent.textColor = 0xFF981F;
        rsComponent.centerText = true;
        rsComponent.gameFont = rsFonts[RSComponent.BOLD];
        rsComponent = RSComponent.forId(8292);
        rsComponent.getInterfaceChild(8383).setX(142);
        return RSComponent.forId(8292);
    }
}

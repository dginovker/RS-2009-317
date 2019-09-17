package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the departure {@link com.runescape.cache.media.inter.InterfacePlugin} for Pest Control.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class PestControlDepartureInterfacePlugin implements InterfacePlugin {
    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(23680);
        rsComponent.addText(23681, "Next Departure: 5 min", rsFonts, RSComponent.REGULAR, 0xCCCCCC, false, true, 251, 14, -1);
        rsComponent.addText(23682, "Players Ready: 5", rsFonts, RSComponent.REGULAR, 0x59D231, false, true, 251, 14, -1);
        rsComponent.addText(23683, "(Need 5 to 25 players)", rsFonts, RSComponent.REGULAR, 0xDED36A, false, true, 249, 14, -1);
        rsComponent.addText(23684, "Points:pest_points", rsFonts, RSComponent.REGULAR, 0x99FFFF, false, true, 249, 14, -1);
        rsComponent.addSprite(23685, ImageLoader.forName("PEST_CONTROL_FLAG")).rgb = true;
        rsComponent.addText(23686, "eeeeeeeeeeeeee", rsFonts, RSComponent.REGULAR, 0xFF9966, true, true, 109, 14, -1);//eeeeeeeeeeeeee
        rsComponent.addChild(23681, 14, 23);
        rsComponent.addChild(23682, 14, 40);
        rsComponent.addChild(23683, 14, 57);
        rsComponent.addChild(23684, 14, 73);
        rsComponent.addChild(23685, 410, 217);
        rsComponent.addChild(23686, 406, 314);
        return rsComponent;
    }
}

package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class DoubleItemMessageInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(250);
        rsComponent.optionType = 6;
        rsComponent.addItem(251);
        rsComponent.addItem(252);
        rsComponent.addText(253, "Line1", rsFonts, 3, 0, true, false, 441, 17, 52);
        rsComponent.addContinue(254, rsFonts[3]);
        rsComponent.addChild(251, 20, 30);
        rsComponent.addChild(252, 430, 28);
        rsComponent.addChild(253, 21, 5);
        rsComponent.addChild(254, 69, 77);
        return rsComponent;
    }
}

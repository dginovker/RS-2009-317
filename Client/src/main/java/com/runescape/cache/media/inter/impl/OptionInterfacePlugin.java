package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * A game screen option {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 */
public class OptionInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addTabInterface(27700);
        rsComponent.addSprite(27701, ImageLoader.forName("OPTION_MENU_BACK"));
        for (int childId = 27703; childId < 27803; childId++) {
            rsComponent.addHoverText(childId, "", rsFonts, 1, 0x46320A, 0x645028, true, false, 230, 16, "Ok");
        }
        rsComponent.addText(27803, "opt1", rsFonts, RSComponent.FANCY, 0x46320A, true, false, 0, 52);
        rsComponent.addChild(27701, 2, 7);
        rsComponent.addChild(27702, 15, 62);
        rsComponent.addChild(27803, 255, 29);
        RSComponent scrollComponent = new RSComponent().addTabInterface(27702);
        int yOffset = 1;
        for (int childId = 0; childId < 100; childId++) {
            scrollComponent.addChild((27703 + childId), 120, yOffset);
            yOffset += 19;
        }
        scrollComponent.width = 429;
        scrollComponent.height = 219;
        scrollComponent.scrollMax = 1910;
        scrollComponent.newScroller = true;
        return rsComponent;
    }
}

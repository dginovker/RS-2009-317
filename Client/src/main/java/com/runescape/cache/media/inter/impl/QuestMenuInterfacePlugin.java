package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * The quest menu {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 */
public class QuestMenuInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addTabInterface(46750);
        rsComponent.addSprite(46751, ImageLoader.forName("OPTION_MENU_BACK"));
        int index = 0;
        for (int childId = 46754; childId < 46855; childId++) {
            rsComponent.addText(childId, "j" + index, rsFonts, 1, 0, true, false, 370, -1);
            index++;
        }
        rsComponent.addHoverButton(46856, SpriteRepository.FANCY_X, 24, 23, "Close", -1, 46857, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(46857, SpriteRepository.FANCY_X_HOVER, 24, 23, 46858);

        rsComponent.addText(46753, "ifquestname", rsFonts, 3, 0, false, false, 0, 52);
        rsComponent.addChild(46751, 2, 7);
        rsComponent.addChild(46752, 25, 62);
        rsComponent.addChild(46753, 40, 29);
        rsComponent.addChild(46856, 453, 23);
        rsComponent.addChild(46857, 453, 23);
        RSComponent scrollComponent = new RSComponent().addTabInterface(46752);
        int yOffset = 1;
        for (int childId = 0; childId < 101; childId++) {
            scrollComponent.addChild((46754 + childId), 40, yOffset);
            yOffset += 19;
        }
        scrollComponent.width = 429;
        scrollComponent.height = 219;
        scrollComponent.scrollMax = 1920;
        scrollComponent.newScroller = true;
        return rsComponent;
    }
}

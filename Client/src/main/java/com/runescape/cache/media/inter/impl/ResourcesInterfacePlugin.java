package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * The "Resources Collected" {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ResourcesInterfacePlugin implements InterfacePlugin {
    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(47900);
        rsComponent.addSprite(47901, ImageLoader.forName("RESOURCES_BACK"));
        rsComponent.addHoverButton(47902, SpriteRepository.FANCY_X_1, 23, 23, "Close", -1, 47903, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(47903, SpriteRepository.FANCY_X_1_HOVER, 23, 23, 47904);
        rsComponent.addText(47905, "Resources collected", rsFonts, 3, 0, false, false, 50, -1);
        rsComponent.addChild(47901, 37, 40);
        rsComponent.addChild(47902, 379, 53);
        rsComponent.addChild(47903, 379, 53);
        rsComponent.addChild(47905, 53, 56);
        rsComponent.addChild(47906, -39, 76);
        RSComponent scrollComponent = rsComponent.addTabInterface(47906, 310, 730);
        for (int index = 0; index <= 90; index++) {
            scrollComponent.addToItemGroup((47907 + index), 32, 32, 0, 0, true, true);
        }
        int xOffset = 100;
        int yOffset = 9;
        int counter = 0;
        for (int index = 0; index <= 90; index++) {
            if (counter == 9) {
                xOffset = 100;
                yOffset += 36;
                counter = 0;
            }
            scrollComponent.addChild((47907 + index), xOffset, yOffset);
            xOffset += 36;
            counter++;
        }
        scrollComponent.width = 429;
        scrollComponent.height = 215;
        scrollComponent.scrollMax = 330;
        return rsComponent;
    }
}

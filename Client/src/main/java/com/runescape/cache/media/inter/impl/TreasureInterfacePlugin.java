package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * The 400+ Construction Treasure {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 */
public class TreasureInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addTabInterface(27135);
        rsComponent.addSprite(27136, ImageLoader.forName("TREASURE_BACK"));
        for (int childId = 27137; childId < 27167; childId++) {
            rsComponent.addActionButton(childId, ImageLoader.forName("TREASURE_OPTION"), 427, 61, "Ok");
            rsComponent.addText((childId + 30), "", rsFonts, 2, 0xFF981F, false, true, 174, -1);
            rsComponent.addToItemGroup((childId + 60), 1, 1, 0, 0, false, false);
        }
        rsComponent.addText(27259, "", rsFonts, 2, 0xFF981F, true, true, 0, -1);
        rsComponent.addHoverButton(27260, SpriteRepository.BIG_X, 21, 21, "Close", 250, 27261, 3);
        rsComponent.addHoveredButton(27261, SpriteRepository.BIG_X_HOVER, 21, 21, 27261);
        rsComponent.addChild(27136, 12, 20);
        rsComponent.addChild(27258, 34, 82);
        rsComponent.addChild(27259, 255, 29);
        rsComponent.addChild(27260, 472, 27);
        //rsComponent.addChild(4, 27261, 472, 27);

        RSComponent scrollComponent = new RSComponent().addTabInterface(27258);
        int yOffset = 1;
        for (int childId = 0; childId < 30; childId++) {
            scrollComponent.addChild((27137 + childId), 0, yOffset); // Button
            scrollComponent.addChild(((27137 + childId) + 30), 75, yOffset + 20); // Text
            scrollComponent.addChild(((27137 + childId) + 60), 25, yOffset + 20); // Item
            yOffset += 63;
        }
        scrollComponent.width = 429;
        scrollComponent.height = 219;
        scrollComponent.scrollMax = 1890;
        return rsComponent;
    }
}

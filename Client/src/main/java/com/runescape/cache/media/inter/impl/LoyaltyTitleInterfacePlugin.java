package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the loyalty title interface.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class LoyaltyTitleInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        // Selection screen
        RSComponent rsComponent = new RSComponent().addInterface(23035);
        rsComponent.addSprite(23036, ImageLoader.forName("TITLES_BACK"));
        rsComponent.addText(23037, "Player Titles", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 0, -1);
        rsComponent.addHoverButton(23038, SpriteRepository.BIG_X, 21, 21, "Close", 250, 23039, 3);
        rsComponent.addHoveredButton(23039, SpriteRepository.BIG_X_HOVER, 21, 21, 23040);
        rsComponent.addHoverButton(23041, ImageLoader.forName("BIG_BUTTON_1"), 90, 56, "Select", -1, 23042,
                RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(23042, ImageLoader.forName("BIG_BUTTON_1_HOVER"), 90, 56, 23043);
        rsComponent.addText(23044, "Regular", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, -1, -1, -1);
        rsComponent.addHoverButton(23045, ImageLoader.forName("BIG_BUTTON_1"), 90, 56, "Select", -1, 23046,
                RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(23046, ImageLoader.forName("BIG_BUTTON_1_HOVER"), 90, 56, 23047);
        rsComponent.addText(23048, "Coins", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, -1, -1, -1);
        rsComponent.addHoverButton(23049, ImageLoader.forName("BIG_BUTTON_1"), 90, 56, "Select", -1, 23050,
                RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(23050, ImageLoader.forName("BIG_BUTTON_1_HOVER"), 90, 56, 23051);
        rsComponent.addText(23052, "Voting", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, -1, -1, -1);
        rsComponent.addHoverButton(23053, ImageLoader.forName("BIG_BUTTON_1"), 90, 56, "Select", -1, 23054,
                RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(23054, ImageLoader.forName("BIG_BUTTON_1_HOVER"), 90, 56, 23055);
        rsComponent.addText(23056, "Misc", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, -1, -1, -1);
        rsComponent.addChild(23036, 12, 70);
        rsComponent.addChild(23037, 255, 79);
        rsComponent.addChild(23038, 472, 77);
        rsComponent.addChild(23039, 472, 77);
        rsComponent.addChild(23041, 61, 153);
        rsComponent.addChild(23042, 61, 153);
        rsComponent.addChild(23044, 105, 173);
        rsComponent.addChild(23045, 161, 153);
        rsComponent.addChild(23046, 161, 153);
        rsComponent.addChild(23048, 205, 173);
        rsComponent.addChild(23049, 261, 153);
        rsComponent.addChild(23050, 261, 153);
        rsComponent.addChild(23052, 305, 173);
        rsComponent.addChild(23053, 361, 153);
        rsComponent.addChild(23054, 361, 153);
        rsComponent.addChild(23056, 405, 173);

        // Title screen
        rsComponent = new RSComponent().addInterface(23057);
        rsComponent.addSprite(23058, ImageLoader.forName("TREASURE_BACK"));
        rsComponent.addText(23059, "Player Titles", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 0, -1);
        rsComponent.addHoverButton(23060, SpriteRepository.BIG_X, 21, 21, "Close", 250, 23061, 3);
        rsComponent.addHoveredButton(23061, SpriteRepository.BIG_X_HOVER, 21, 21, 23062);
        rsComponent.addHoverText(23063, "Previous", rsFonts, RSComponent.BOLD, 0xFF981F, false, true, 60, "Previous Page");
        rsComponent.addHoverText(23064, "Next", rsFonts, RSComponent.BOLD, 0xFF981F, false, true, 60, "Next Page");

        rsComponent.addChild(23058, 12, 20);
        rsComponent.addChild(23059, 255, 29);
        rsComponent.addChild(23060, 472, 27);
        rsComponent.addChild(23061, 472, 27);
        rsComponent.addChild(23063, 31, 60);
        rsComponent.addChild(23064, 449, 60);
        rsComponent.addChild(23065, 34, 82);

        RSComponent scrollComponent = new RSComponent().addTabInterface(23065);
        for (int childId = 0; childId < 11; childId++) {
            rsComponent.addHoverText((23066 + childId), "", rsFonts, RSComponent.BOLD,
                    0xFF981F, 0xFFFFFF, false, true, 174, 14, "View");
            rsComponent.addHoverText((23066 + childId) + 11, "", rsFonts, RSComponent.BOLD,
                    0xFF981F, 0xFFFFFF, false, true, 128, 14, "Purchase");
            rsComponent.addHoverText((23066 + childId) + 22, "", rsFonts, RSComponent.BOLD,
                    0xFF981F, 0xFFFFFF, false, true, 90, 14, "Toggle");
        }
        int yOffset = 1;
        for (int childId = 0; childId < 11; childId++) {
            scrollComponent.addChild((23066 + childId), 4, yOffset);
            scrollComponent.addChild(((23066 + childId) + 11), 210, yOffset);
            scrollComponent.addChild(((23066 + childId) + 22), 370, yOffset);
            yOffset += 20;
        }
        scrollComponent.width = 439;
        scrollComponent.height = 219;
        scrollComponent.scrollMax = 0;
        return null;
    }
}
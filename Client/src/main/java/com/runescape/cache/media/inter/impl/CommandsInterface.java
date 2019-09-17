package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the commands list {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class CommandsInterface implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(26597);
        rsComponent.addSprite(26598, ImageLoader.forName("TREASURE_BACK"));
        rsComponent.addText(26599, "Gielinor Commands", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 0, -1);
        rsComponent.addHoverButton(26600, SpriteRepository.BIG_X, 21, 21, "Close", 250, 26601, 3);
        rsComponent.addHoveredButton(26601, SpriteRepository.BIG_X_HOVER, 21, 21, 26602);
        rsComponent.addHoverText(26704, "Previous", rsFonts, RSComponent.BOLD, 0xFF981F, false, true, 60, "Previous Page");
        rsComponent.addHoverText(26705, "Next", rsFonts, RSComponent.BOLD, 0xFF981F, false, true, 60, "Next Page");
        rsComponent.addChild(26598, 12, 20);
        rsComponent.addChild(26599, 255, 29);
        rsComponent.addChild(26600, 472, 27);
        rsComponent.addChild(26601, 472, 27);
        rsComponent.addChild(26603, 34, 82);
        rsComponent.addChild(26704, 31, 60);
        rsComponent.addChild(26705, 449, 60);
        RSComponent scrollComponent = new RSComponent().addTabInterface(26603);
        for (int childId = 0; childId < 50; childId++) {
            rsComponent.addHoverText((26604 + childId), "Command " + childId, rsFonts, RSComponent.BOLD, 0xFF981F, 0xFFFFFF, false, true, 174, 60, "View usage");
            rsComponent.addText(((26604 + childId) + 50), "Description " + childId + "\\ntest", rsFonts, RSComponent.REGULAR, 0xFF981F, false, true, 174, -1);
        }
        int yOffset = 1;
        for (int childId = 0; childId < 50; childId++) {
            scrollComponent.addChild((26604 + childId), 4, yOffset);
            scrollComponent.addChild(((26604 + childId) + 50), 175, yOffset);
            yOffset += 63;
        }
        scrollComponent.width = 429;
        scrollComponent.height = 219;
        scrollComponent.scrollMax = 3150;
        return rsComponent;
    }
}

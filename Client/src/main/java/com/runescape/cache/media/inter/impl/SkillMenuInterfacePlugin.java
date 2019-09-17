package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the skill menu {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 */
public class SkillMenuInterfacePlugin implements InterfacePlugin {

    /**
     * The child index of the skill menu select bottom.
     */
    public static final int BOTTOM_SCROLL_BAR_CHILD = 3;

    String[] selects = new String[]{"Weapons", "Salamanders", "Surfaces", "Storage", "Decorative", "Trophies", "Games", "Garden", "Dungeon", "Chapel", "Other", "Costume Room", "House Size"};

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(50000);
        rsComponent.addSprite(50001, ImageLoader.forName("SKILL_MENU_BACK"));
        rsComponent.addBorder(50002, 515, 337, true, 70, 0x302313);
        rsComponent.addSprite(50003, ImageLoader.forName("SKILL_MENU_SELECT"), -1, -1);
        rsComponent.addSprite(50004, ImageLoader.forName("SKILL_MENU_SELECT_BOTTOM"), -1, -1);
        for (int index = 0; index < 13; index++) {
            rsComponent.addHoverText(50005 + index, selects[index], rsFonts, 3, 0x46320A, 0x645028, true, false, 120, 19, "Open subsection");
        }

        rsComponent.addText(50021, "Construction", rsFonts, 4, 0x46320A, true, false, 370, -1);
        rsComponent.addText(50022, "Rooms - Members Only", rsFonts, 0, 0x46320A, true, false, 370, -1);
        rsComponent.addHoverButton(50018, SpriteRepository.FANCY_X, 24, 23, "Close", -1, 50019, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(50019, SpriteRepository.FANCY_X_HOVER, 24, 23, 50020);
        rsComponent.addChild(50002, 0, 0);
        rsComponent.addChild(50001, 0, 0);
        rsComponent.addChild(50003, 353, 56);
        rsComponent.addChild(50004, 353, 308);
        int yOffset = 68;
        for (int index = 0; index < 13; index++) {
            rsComponent.addChild(50005 + index, 370, yOffset);
            yOffset += 19;
        }
        rsComponent.addChild(50023, -115, 80);
        // TODO Figure out if these are dead center and compare to OSRS
        rsComponent.addChild(50021, -5, 2);
        rsComponent.addChild(50022, -5, 38);
        for (int childId = 50024; childId < 50124; childId++) {
            rsComponent.addText(childId, "", rsFonts, 1, 0x46320A, false, false, 370, -1);
            rsComponent.addToItemGroup((childId + 100), 1, 1, 0, 0, false, false);
            rsComponent.addText((childId + 200), "", rsFonts, 1, 0x46320A, false, false, 370, -1);
            rsComponent.addText((childId + 300), "", rsFonts, 1, 0x46320A, false, false, 370, -1);
        }
        rsComponent.addChild(50018, 483, 6);
        rsComponent.addChild(50019, 483, 6);
        RSComponent scrollComponent = new RSComponent().addTabInterface(50023, 310, 730);
        scrollComponent.newScroller = true;
        yOffset = 5;
        for (int childId = 0; childId < 100; childId++) {
            scrollComponent.addChild((50024 + childId), 140, yOffset); // Level
            scrollComponent.addChild((50024 + (childId + 100)), 156, yOffset - 6); // Item
            scrollComponent.addChild((50024 + (childId + 200)), 195, yOffset); // Description
            scrollComponent.addChild((50024 + (childId + 300)), 195, yOffset + 16); // Sub description
            yOffset += 36;
        }
        scrollComponent.width = 429;
        scrollComponent.height = 236;
        scrollComponent.scrollMax = 3410;
        return rsComponent;
    }
}

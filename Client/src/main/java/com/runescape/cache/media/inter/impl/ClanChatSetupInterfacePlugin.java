package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the clan chat setup {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ClanChatSetupInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(32320);
        rsComponent.addSprite(32321, ImageLoader.forName("CLAN_CHAT_SETUP_BACK")).addAlphaBox(10, 10, 4, 6, 0, 100);
        rsComponent.addText(32322, "Clan Chat Setup", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 0, 0);
        rsComponent.addText(32323, "Friends List", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 0, 0);
        rsComponent.addText(32324, "Name:", rsFonts, RSComponent.BOLD, 0xFF981F, false, true, 0, 0);
        rsComponent.addText(32325, "Rank:", rsFonts, RSComponent.BOLD, 0xFF981F, false, true, 0, 0);
        String[] tooltips = new String[]{"Set prefix", "Anyone", "Anyone", "Corporal+"};
        RSComponent setupButton = rsComponent.addHoverButton(32326, ImageLoader.forName("CLAN_CHAT_SETUP_BUTTON"), 150, 43, tooltips[0], -1, 32327, RSComponent.BUTTON_ACTION_TYPE);
        setupButton.tooltips = new String[]{"Disable", "Set prefix"};
        rsComponent.addHoveredButton(32327, ImageLoader.forName("CLAN_CHAT_SETUP_BUTTON_HOVER"), 150, 43, 32328);
        setupButton = rsComponent.addHoverButton(32329, ImageLoader.forName("CLAN_CHAT_SETUP_BUTTON"), 150, 43, tooltips[1], -1, 32330, RSComponent.BUTTON_ACTION_TYPE);
        setupButton.tooltips = new String[]{"Only me", "General+", "Captain+", "Lieutenant+", "Sergeant+", "Corporal+", "Recruit+", "Any friends", "Anyone"};
        rsComponent.addHoveredButton(32330, ImageLoader.forName("CLAN_CHAT_SETUP_BUTTON_HOVER"), 150, 43, 32331);
        setupButton = rsComponent.addHoverButton(32332, ImageLoader.forName("CLAN_CHAT_SETUP_BUTTON"), 150, 43, tooltips[2], -1, 32333, RSComponent.BUTTON_ACTION_TYPE);
        setupButton.tooltips = new String[]{"Only me", "General+", "Captain+", "Lieutenant+", "Sergeant+", "Corporal+", "Recruit+", "Any friends", "Anyone"};
        rsComponent.addHoveredButton(32333, ImageLoader.forName("CLAN_CHAT_SETUP_BUTTON_HOVER"), 150, 43, 32334);
        setupButton = rsComponent.addHoverButton(32335, ImageLoader.forName("CLAN_CHAT_SETUP_BUTTON"), 150, 43, tooltips[3], -1, 32336, RSComponent.BUTTON_ACTION_TYPE);
        setupButton.tooltips = new String[]{"Only me", "General+", "Captain+", "Lieutenant+", "Sergeant+", "Corporal+"};
        rsComponent.addHoveredButton(32336, ImageLoader.forName("CLAN_CHAT_SETUP_BUTTON_HOVER"), 150, 43, 32337);
        rsComponent.addText(32338, "Right click on\\nwhite text to\\nchange options.", rsFonts, RSComponent.BOLD, 0xFFFF9D, true, true, 0, 0);
        rsComponent.addText(32339, "Clan name:", rsFonts, RSComponent.SMALL, 0xFF981F, true, true, 0, 0);
        rsComponent.addText(32340, "Who can enter chat?", rsFonts, RSComponent.SMALL, 0xFF981F, true, true, 0, 0);
        rsComponent.addText(32341, "Who can talk on chat?", rsFonts, RSComponent.SMALL, 0xFF981F, true, true, 0, 0);
        rsComponent.addText(32342, "Who can kick on chat?", rsFonts, RSComponent.SMALL, 0xFF981F, true, true, 0, 0);
        rsComponent.addText(32343, "Chat disabled", rsFonts, RSComponent.BOLD, 0xFFFFFF, true, true, 0, 0);
        rsComponent.addText(32344, "Any friends", rsFonts, RSComponent.BOLD, 0xFFFFFF, true, true, 0, 0);
        rsComponent.addText(32345, "Any friends", rsFonts, RSComponent.BOLD, 0xFFFFFF, true, true, 0, 0);
        rsComponent.addText(32346, "Only me", rsFonts, RSComponent.BOLD, 0xFFFFFF, true, true, 0, 0);
        rsComponent.addHoverButton(32759, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 32760, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(32760, SpriteRepository.SMALL_X_HOVER, 16, 16, 32761);
        rsComponent.addChild(32321, 13, 20);
        rsComponent.addChild(32322, 254, 30);
        rsComponent.addChild(32323, 333, 61);
        rsComponent.addChild(32324, 198, 83);
        rsComponent.addChild(32325, 335, 83);
        int xOffset = 25;
        rsComponent.addChild(32326, xOffset, 62);
        rsComponent.addChild(32327, xOffset, 62);
        rsComponent.addChild(32329, xOffset, 109);
        rsComponent.addChild(32330, xOffset, 109);
        rsComponent.addChild(32332, xOffset, 156);
        rsComponent.addChild(32333, xOffset, 156);
        rsComponent.addChild(32335, xOffset, 203);
        rsComponent.addChild(32336, xOffset, 203);
        rsComponent.addChild(32338, 97, 261);
        rsComponent.addChild(32339, 99, 69);
        rsComponent.addChild(32340, 99, 116);
        rsComponent.addChild(32341, 99, 163);
        rsComponent.addChild(32342, 99, 210);
        rsComponent.addChild(32343, 99, 86);
        rsComponent.addChild(32344, 99, 133);
        rsComponent.addChild(32345, 99, 179);
        rsComponent.addChild(32346, 99, 226);
        rsComponent.addChild(32759, 473, 30);
        rsComponent.addChild(32760, 473, 30);
        rsComponent.addChild(32347, 198, 100);
        RSComponent scrollComponent = rsComponent.addTabInterface(32347, 310, 730);
        for (int childId = 32348; childId <= 32748; childId++) {
            rsComponent.addText(childId, "", rsFonts, RSComponent.BOLD, 0xFFFF64, false, true, 135, 0);
            childId += 1;
            rsComponent.addHoverText(childId, "Not in clan", rsFonts, RSComponent.BOLD, 0xFFFFFF, 0xFFFFFF, false, true, 135, 14, "General", "Captain", "Lieutenant", "Sergeant", "Corporal", "Recruit", "Not in clan");
        }
        int yOffset = 0;
        for (int index = 0; index <= 400; index++) {
            scrollComponent.addChild((32348 + index), 0, yOffset);
            index += 1;
            scrollComponent.addChild((32348 + index), 137, yOffset);
            yOffset += 14;
        }
        scrollComponent.width = 267;
        scrollComponent.height = 214;
        scrollComponent.scrollMax = 196;
        return rsComponent;
    }

}

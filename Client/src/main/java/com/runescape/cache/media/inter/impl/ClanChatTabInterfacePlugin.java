package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for the Clan chat tab.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ClanChatTabInterfacePlugin implements InterfacePlugin {
    // HEIGHT MAY BE 10 FOR USERS IN CHAT!
    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(32100);
        rsComponent.addSprite(32101, ImageLoader.forName("CLAN_CHAT_BACK"));
        rsComponent.addHoverButton(32102, ImageLoader.forName("CLAN_CHAT_BUTTON"), 72, 32, "Join Chat", -1, 32103, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(32103, ImageLoader.forName("CLAN_CHAT_BUTTON_HOVER"), 72, 32, 32104);
        rsComponent.addText(32105, "Join Chat", rsFonts, 0, 0xFF9B00, true, true, 0, 0);
        rsComponent.addHoverButton(32106, ImageLoader.forName("CLAN_CHAT_BUTTON"), 72, 32, "Clan Setup", -1, 32107, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(32107, ImageLoader.forName("CLAN_CHAT_BUTTON_HOVER"), 72, 32, 32108);
        rsComponent.addText(32109, "Clan Setup", rsFonts, 0, 0xFF9B00, true, true, 0, 0);
        rsComponent.addText(32110, "Clan Chat", rsFonts, 2, 0xFF9B00, true, true, 0, 0);
        rsComponent.addText(32111, "Talking in: Not in chat", rsFonts, 0, 0xFF9B00, false, true, 0, 0);
        rsComponent.addText(32112, "Owner: None", rsFonts, 0, 0xFF9B00, false, true, 0, 0);
        rsComponent.addChild(32101, 3, 56);
        rsComponent.addChild(32102, 15, 226);
        rsComponent.addChild(32103, 15, 226);
        rsComponent.addChild(32105, 51, 236);
        rsComponent.addChild(32106, 95, 226);
        rsComponent.addChild(32107, 95, 226);
        rsComponent.addChild(32109, 131, 236);
        rsComponent.addChild(32110, 96, 4);
        rsComponent.addChild(32111, 10, 26);
        rsComponent.addChild(32112, 25, 41);
        rsComponent.addChild(32113, 8, 62);
        RSComponent scrollComponent = rsComponent.addTabInterface(32113);
        for (int childId = 32114; childId <= 32214; childId++) {
            scrollComponent.addHoverText(childId, "", rsFonts, 0, 0xFFFFFF, false, true, 145);
        }
        for (int childId = 32215; childId <= 32315; childId++) {
            scrollComponent.addHoverText(childId, "", rsFonts, 0, 0xFFFFFF, false, true, 55);
        }
        int yOffset = 2;
        for (int childId = 32114; childId <= 32214; childId++) {
            scrollComponent.addChild(childId, 5, yOffset);
            yOffset += 14;
        }
        yOffset = 2;
        for (int childId = 32215; childId <= 32315; childId++) {
            scrollComponent.addChild(childId, 85, yOffset);
            yOffset += 14;
        }
        scrollComponent.height = 156;
        scrollComponent.width = 162;
        scrollComponent.scrollMax = 157;
        return rsComponent;
    }
}

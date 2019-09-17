package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the summoning creation interface plugin.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class SummoningCreationPlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25717);
        rsComponent.addSprite(25718, ImageLoader.forName("summoning-creation-back")).addAlphaBox(10, 10, 4, 6, 0, 100);
        rsComponent.addHoverButton(25719, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 25720, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25720, SpriteRepository.SMALL_X_HOVER, 16, 16, 25721);
        rsComponent.addText(25722, "Summoning Pouch Creation", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 200, 14, -1);
        rsComponent.addButton(25725, ImageLoader.forName("summoning-tab-back-off"), 79, 20, "Transform Scrolls", RSComponent.BUTTON_ACTION_TYPE).popupString = "Swap between\\ncreating pouches and\\nscrolls";
        ;
        rsComponent.addSprite(25726, ImageLoader.forName("summoning-pouch-off"));
        rsComponent.addSprite(25723, ImageLoader.forName("summoning-tab-back-on"));
        rsComponent.addSprite(25724, ImageLoader.forName("summoning-wolf-on"));
        RSComponent scrollComponent = rsComponent.addInterface(25727);
        scrollComponent.height = 257;
        scrollComponent.width = 445;
        scrollComponent.scrollMax = 1400;
        scrollComponent.addToItemGroup(25728, 4, 20, 79, 38, false, true, false, true, "Infuse<col=FF9040>", "Infuse-5<col=FF9040>", "Infuse-10<col=FF9040>", "Infuse-All<col=FF9040>", "List<col=FF9040>");
        scrollComponent.addChild(25728, 29, 0);
        for (int childId = 25729; childId < 25810; childId++) {
            scrollComponent.addText(childId, "", gameFonts, RSComponent.BOLD, 0x111111, true, true, 90, 40, -1);
        }

        rsComponent.addChild(25718, 13, 20);
        rsComponent.addChild(25719, 474, 30);
        rsComponent.addChild(25720, 474, 30);
        rsComponent.addChild(25722, 242, 30);
        rsComponent.addChild(25725, 92, 29);
        rsComponent.addChild(25726, 119, 30);
        rsComponent.addChild(25723, 22, 29);
        rsComponent.addChild(25724, 42, 30);
        rsComponent.addChild(25727, 29, 59);
        int index = 0;
        int xOffset = 4;
        int yOffset = 32;
        for (int childId = 25729; childId < 25810; childId++) {
            scrollComponent.addChild(childId, xOffset, yOffset);
            xOffset += 111;
            index++;
            if (index % 4 == 0) {
                xOffset = 4;
                yOffset += 70;
            }
        }
        return loadScrollCreation(gameFonts);
    }

    public RSComponent loadScrollCreation(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25810);
        rsComponent.addSprite(25811, ImageLoader.forName("summoning-creation-back")).addAlphaBox(10, 10, 4, 6, 0, 100);
        rsComponent.addHoverButton(25812, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 25813, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25813, SpriteRepository.SMALL_X_HOVER, 16, 16, 25814);
        rsComponent.addText(25815, "Scroll Creation", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 200, 14, -1);
        rsComponent.addButton(25818, ImageLoader.forName("summoning-tab-back-on"),
                79, 20, "Infuse Pouches", RSComponent.BUTTON_ACTION_TYPE).popupString = "Swap between\\ncreating pouches and\\nscrolls";
        rsComponent.addSprite(25819, ImageLoader.forName("summoning-wolf-off"));
        rsComponent.addSprite(25816, ImageLoader.forName("summoning-tab-back-off"));
        rsComponent.addSprite(25817, ImageLoader.forName("summoning-pouch-on"));
        RSComponent scrollComponent = rsComponent.addInterface(25820);
        scrollComponent.height = 257;
        scrollComponent.width = 445;
        scrollComponent.scrollMax = 1400;
        scrollComponent.addToItemGroup(25821, 4, 20, 79, 38, false, true, false, true, "Transform<col=ff9040>", "Transform-5<col=ff9040>", "Transform-10<col=ff9040>", "Transform-All<col=ff9040>", "Transform-X<col=ff9040>");
        scrollComponent.addChild(25821, 29, 0);
        for (int childId = 25822; childId < 25904; childId++) {
            scrollComponent.addText(childId, "", gameFonts, RSComponent.BOLD, 0xFFFF64, true, true, 90, 40, -1);
        }

        rsComponent.addChild(25811, 13, 20);
        rsComponent.addChild(25812, 474, 30);
        rsComponent.addChild(25813, 474, 30);
        rsComponent.addChild(25815, 242, 30);
        rsComponent.addChild(25818, 22, 29);
        rsComponent.addChild(25819, 42, 30);
        rsComponent.addChild(25816, 92, 29);
        rsComponent.addChild(25817, 119, 30);
        rsComponent.addChild(25820, 29, 59);
        int index = 0;
        int xOffset = 4;
        int yOffset = 32;
        for (int childId = 25822; childId < 25904; childId++) {
            scrollComponent.addChild(childId, xOffset, yOffset);
            xOffset += 111;
            index++;
            if (index % 4 == 0) {
                xOffset = 4;
                yOffset += 70;
            }
        }
        return rsComponent;
    }
}
package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * The {@link com.runescape.cache.media.inter.InterfacePlugin} for guide prices.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class GuidePricesInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25626);
        rsComponent.addSprite(25627, ImageLoader.forName("GE_GUIDE_PRICE_BACK"));//.addAlphaBox(5, 5, 3, 3, 0, 100);
        rsComponent.addHoverButton(25628, SpriteRepository.BIG_X, 21, 21, "Close", 250, 25629, 3);
        rsComponent.addHoveredButton(25629, SpriteRepository.BIG_X_HOVER, 21, 21, 25630);
        rsComponent.addText(25631, "Grand Exchange guide prices", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 120, 14, -1);
        rsComponent.addText(25632, "Total guide price:", gameFonts, RSComponent.REGULAR, 0xFF981F, true, true, 120, 14, -1);
        rsComponent.addText(25633, "0", gameFonts, RSComponent.REGULAR, 0xFFFFFF, true, true, 120, 14, -1);
        rsComponent.addHoverButton(25634, ImageLoader.forName("GE_SEARCH_BOX"), 40, 36, "Search for item", 0, 25635, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(25635, ImageLoader.forName("GE_SEARCH_BOX_OPACITY"), 40, 36, 25636);
        rsComponent.addToItemGroup(25637, 1, 1, 0, 0, true, false, false, false);
        rsComponent.addText(25638, "", gameFonts, RSComponent.REGULAR, 0xFF981F, false, true, 120, 14, -1);
        rsComponent.addText(25639, "", gameFonts, RSComponent.REGULAR, 0xFFFFFF, false, true, 120, 14, -1);
        rsComponent.addHoverButton(25640, ImageLoader.forName("DEPOSIT_INVENTORY"), 36, 36, "Add all", 0, 25641, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(25641, ImageLoader.forName("DEPOSIT_INVENTORY_HOVER"), 36, 36, 25642);
        rsComponent.addChild(25627, 16, 10);
        rsComponent.addChild(25628, 468, 17);
        rsComponent.addChild(25629, 468, 17);
        rsComponent.addChild(25631, 196, 20);
        rsComponent.addChild(25632, 197, 282);
        rsComponent.addChild(25633, 198, 297);
        rsComponent.addChild(25634, 26, 280);
        rsComponent.addChild(25635, 26, 280);
        rsComponent.addChild(25637, 30, 281);
        rsComponent.addChild(25638, 71, 282);
        rsComponent.addChild(25639, 71, 297);
        rsComponent.addChild(25640, 450, 280);
        rsComponent.addChild(25641, 450, 280);
        rsComponent.addChild(25643, 30, 50);
        RSComponent scrollComponent = rsComponent.addInterface(25643);
        scrollComponent.height = 222;
        scrollComponent.width = 440;
        scrollComponent.scrollMax = 223;
        scrollComponent.addToItemGroup(25644, 5, 6, 57, 35, true, true, true, "Remove", "Remove-5", "Remove-10", "Remove-All", "Remove-X");

        int index = 0;
        int xOffset = 1;
        int yOffset = 0;
        for (int childIndex = 25645; childIndex <= 25675; childIndex++) {
            if (index != 0 && (index % 5) == 0) {
                xOffset = 1;
                yOffset += 67;
            }
            scrollComponent.addText(childIndex, "", gameFonts, RSComponent.SMALL, 0xFFFFFF, true, true, 120, 14, -1);
            scrollComponent.addChild(childIndex, xOffset - 21, 32 + yOffset);
            xOffset += 89;
            index++;
        }
        scrollComponent.addChild(25644, 22, 0);
        return loadInventoryInterface(gameFonts);
    }

    public RSComponent loadInventoryInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25676);
        rsComponent.addToItemGroup(25677, 4, 7, 10, 4, true, true, true, "Add", "Add-5", "Add-10", "Add-All", "Add-X");
        rsComponent.addChild(25677, 16, 8);
        return rsComponent;
    }
}

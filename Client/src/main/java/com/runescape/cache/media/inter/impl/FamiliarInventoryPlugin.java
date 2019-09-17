package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the beast of burden inventory plugin for Summoning.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class FamiliarInventoryPlugin implements InterfacePlugin {
    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25699);
        rsComponent.addSprite(25700, ImageLoader.forName("bob_inventory_back")).addAlphaBox(5, 5, 3, 3, 0, 100);
        rsComponent.addText(25701, "Familiar Inventory", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 120, 14, -1);
        rsComponent.addHoverButton(25702, SpriteRepository.SMALL_X, 16, 16, "Close", -1, 25703, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25703, SpriteRepository.SMALL_X_HOVER, 16, 16, 25704);
        rsComponent.addButton(25705, new SpriteSet("dump_bob"), 21, 21, "Take BoB", 1).popupString = "Take Beast of\\nBurden items.";
        rsComponent.addToItemGroup(25706, 6, 5, 22, 21, true, true, true, "Withdraw-1", "Withdraw-5", "Withdraw-10", "Withdraw-All", "Withdraw-X");
        rsComponent.addChild(25700, 70, 19);
        rsComponent.addChild(25701, 204, 28);
        rsComponent.addChild(25702, 413, 29);
        rsComponent.addChild(25703, 413, 29);
        rsComponent.addChild(25705, 407, 293);
        rsComponent.addChild(25706, 88, 65);
        loadInventoryInterface();
        return rsComponent;
    }

    public RSComponent loadInventoryInterface() {
        RSComponent rsComponent = new RSComponent().addInterface(25707);
        rsComponent.addToItemGroup(25708, 4, 7, 10, 4, true, true, true, "Store-1", "Store-5", "Store-10", "Store-All", "Store-X");
        rsComponent.addChild(25708, 16, 8);
        return rsComponent;
    }
}

package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for the rune pouch.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class RunePouchInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25030);
        rsComponent.addSprite(25031, ImageLoader.forName("rune-pouch-0"));
        rsComponent.addSprite(25032, ImageLoader.forName("rune-pouch-2"));
        rsComponent.addSprite(25033, ImageLoader.forName("rune-pouch-1"));

        rsComponent.addText(25034, "Rune pouch", gameFonts, RSComponent.BOLD, 0xFFA500, true, true, 0, 14, -1);
        rsComponent.addText(25035, "Pouch", gameFonts, RSComponent.BOLD, 0xFFA500, true, true, 0, 14, -1);
        rsComponent.addText(25036, "Inventory", gameFonts, RSComponent.BOLD, 0xFFA500, true, true, 0, 14, -1);

        rsComponent.addHoverButton(25037, SpriteRepository.BIG_X, 21, 21, "Close", 0, 25038, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25038, SpriteRepository.BIG_X_HOVER, 21, 21, 25039);

        rsComponent.addChild(25031, 0, 0);
        rsComponent.addChild(25032, 105, 57);
        rsComponent.addChild(25033, 342, 57);

        rsComponent.addChild(25034, 257, 29);
        rsComponent.addChild(25035, 257, 62);
        rsComponent.addChild(25036, 257, 137);

        rsComponent.addChild(25037, 406, 26);
        rsComponent.addChild(25038, 406, 26);

        RSComponent rsComponent1 = rsComponent.addInterface(25040);
        rsComponent1.addToItemGroup(25040, 3, 1, 26, 1, true, true, true, "Withdraw-1", "Withdraw-5", "Withdraw-All", "Withdraw-X");

        rsComponent1 = rsComponent.addInterface(25041);
        rsComponent1.addToItemGroup(25041, 7, 4, 16, 4, true, true, true, "Store-1", "Store-5", "Store-All", "Store-X");

        rsComponent.addChild(25040, 186, 86);
        rsComponent.addChild(25041, 98, 154);
        return rsComponent;
    }
}

package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * 11942
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class PlayerStorePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25709);
        rsComponent.addSprite(25710, ImageLoader.forName("PLAYER_STORE_BACK")).addAlphaBox(9, 10, 4, 6, 0, 80);
        rsComponent.addHoverButton(25711, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 25712, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25712, SpriteRepository.SMALL_X_HOVER, 16, 16, 25713);
        rsComponent.addText(25714, "Credit Shop", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 200, 16, -1);
        rsComponent.addChild(25710, 13, 20);
        rsComponent.addChild(25711, 474, 30);
        rsComponent.addChild(25712, 474, 30);
        rsComponent.addChild(25714, 155, 30);
        rsComponent.addChild(25715, 29, 62);
        RSComponent scrollComponent = rsComponent.addInterface(25715);
        scrollComponent.height = 250;
        scrollComponent.width = 445;
        scrollComponent.scrollMax = 820;
        scrollComponent.addToItemGroup(25716, 10, 14, 12, 26, false, true, false, true, "Claim");
        scrollComponent.addChild(25716, 0, 0);
        return rsComponent;
    }
}

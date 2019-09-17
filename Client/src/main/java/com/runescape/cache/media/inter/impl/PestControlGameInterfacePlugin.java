package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.*;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the departure {@link com.runescape.cache.media.inter.InterfacePlugin} for Pest Control.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class PestControlGameInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(24525);
        rsComponent.addSprite(24526, ImageLoader.forName("PEST_CONTROL_BACK"));
        Sprite shield = ImageLoader.forName("PEST_CONTROL_SHIELD");
        rsComponent.addSprite(24527, new SpriteSet(shield, SpriteRepository.EMPTY), 0, 203, false, 32, 13, true);
        rsComponent.addSprite(24528, new SpriteSet(shield, SpriteRepository.EMPTY), 0, 204, false, 32, 13, true);
        rsComponent.addSprite(24529, new SpriteSet(shield, SpriteRepository.EMPTY), 0, 205, false, 32, 13, true);
        rsComponent.addSprite(24530, new SpriteSet(shield, SpriteRepository.EMPTY), 0, 206, false, 32, 13, true);
        Sprite redX = ImageLoader.forName("PEST_CONTROL_X");
        rsComponent.addSprite(24531, new SpriteSet(redX, SpriteRepository.EMPTY), 1, 207, false, 32, 13, true);
        rsComponent.addSprite(24532, new SpriteSet(redX, SpriteRepository.EMPTY), 1, 208, false, 32, 13, true);
        rsComponent.addSprite(24533, new SpriteSet(redX, SpriteRepository.EMPTY), 1, 209, false, 32, 13, true);
        rsComponent.addSprite(24534, new SpriteSet(redX, SpriteRepository.EMPTY), 1, 210, false, 32, 13, true);
        rsComponent.addText(24535, "0", rsFonts, RSComponent.SMALL, 0x00F800, false, true, 20, 14, -1);
        rsComponent.addText(24536, "0", rsFonts, RSComponent.SMALL, 0x00F800, false, true, 20, 14, -1);
        rsComponent.addText(24537, "-1 min", rsFonts, RSComponent.SMALL, 0xCCCCCC, false, true, 20, 14, -1);
        rsComponent.addText(24538, "200", rsFonts, RSComponent.SMALL, 0x00F800, false, true, 20, 14, -1);
        rsComponent.addText(24539, "200", rsFonts, RSComponent.SMALL, 0x00F800, false, true, 20, 14, -1);
        rsComponent.addText(24540, "200", rsFonts, RSComponent.SMALL, 0x00F800, false, true, 20, 14, -1);
        rsComponent.addText(24541, "200", rsFonts, RSComponent.SMALL, 0x00F800, false, true, 20, 14, -1);
        rsComponent.addText(24542, "W", rsFonts, RSComponent.REGULAR, 0xFF00FF, false, true, 20, 14, -1);
        rsComponent.addText(24543, "E", rsFonts, RSComponent.REGULAR, 0x6666FF, false, true, 20, 14, -1);
        rsComponent.addText(24544, "SE", rsFonts, RSComponent.REGULAR, 0xFFFF00, false, true, 20, 14, -1);
        rsComponent.addText(24545, "SW", rsFonts, RSComponent.REGULAR, 0xFF3333, false, true, 20, 14, -1);
        rsComponent.addChild(24526, 0, 0);
        rsComponent.addChild(24527, 350, 44);
        rsComponent.addChild(24528, 387, 44);
        rsComponent.addChild(24529, 425, 44);
        rsComponent.addChild(24530, 463, 44);
        rsComponent.addChild(24531, 357, 30);
        rsComponent.addChild(24532, 394, 30);
        rsComponent.addChild(24533, 432, 30);
        rsComponent.addChild(24534, 470, 30);
        rsComponent.addChild(24535, 29, 32);
        rsComponent.addChild(24536, 29, 60);
        rsComponent.addChild(24537, 5, 87);
        rsComponent.addChild(24538, 356, 12);
        rsComponent.addChild(24539, 393, 12);
        rsComponent.addChild(24540, 431, 12);
        rsComponent.addChild(24541, 469, 12);
        rsComponent.addChild(24542, 361, 61);
        rsComponent.addChild(24543, 399, 61);
        rsComponent.addChild(24544, 433, 61);
        rsComponent.addChild(24545, 470, 61);
        return rsComponent;
    }
}

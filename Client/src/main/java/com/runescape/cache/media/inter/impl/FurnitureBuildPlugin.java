package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class FurnitureBuildPlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25977);
        rsComponent.addSprite(25978, ImageLoader.forName("furniture-back"));
        rsComponent.addToItemGroup(25979, 2, 4, 214, 26, false, true, false, true, "Build");

        rsComponent.addChild(25978, 12, 20);
        rsComponent.addChild(25979, 12, 20);

        return rsComponent;
    }
}
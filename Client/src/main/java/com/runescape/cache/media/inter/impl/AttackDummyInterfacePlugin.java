package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the Attack Dummy Key {@link com.runescape.cache.media.inter.InterfacePlugin} for the Warriors Guild mini-game.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 * TODO Defensive :         //15376, 15373, 15375, 15374
 */
public class AttackDummyInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(575);
        rsComponent.addSprite(576, ImageLoader.forName("DUMMY_KEY_BACK"));
        rsComponent.addHoverButton(577, SpriteRepository.FANCY_X_RED, 24, 23, "Close", -1, 578, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(578, SpriteRepository.FANCY_X_RED_HOVER, 24, 23, 579);
        rsComponent.addChild(576, 8, 0);
        rsComponent.addChild(577, 451, 23);
        rsComponent.addChild(578, 451, 23);
        return rsComponent;
    }
}

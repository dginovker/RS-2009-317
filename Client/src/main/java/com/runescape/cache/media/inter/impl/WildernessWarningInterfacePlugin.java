package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the wilderness warning {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 * TODO Implement interface config
 */
public class WildernessWarningInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(46900);
        rsComponent.addSprite(46901, ImageLoader.forName("WILDERNESS_WARNING_BACK"));
        rsComponent.addHoverButton(46902, ImageLoader.forName("ENTER_WILDERNESS"), 90, 56, "Close", -1, 46903, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(46903, ImageLoader.forName("ENTER_WILDERNESS_HOVER"), 90, 56, 46904);
        rsComponent.addHoverButton(46905, ImageLoader.forName("CANCEL_WILDERNESS"), 90, 56, "Close", -1, 46906, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(46906, ImageLoader.forName("CANCEL_WILDERNESS_HOVER"), 90, 56, 46907);
        rsComponent.addHoverButton(46908, SpriteRepository.SMALL_X, 16, 16, "Close", -1, 46909, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(46909, SpriteRepository.SMALL_X_HOVER, 16, 16, 46910);
        rsComponent.addConfigHover(46911, 4, 46912, new SpriteSet(ImageLoader.forName("CHECKMARK_2_ON"), ImageLoader.forName("CHECKMARK_2_OFF")), 16, 16, 1, 1, "Off/On", 46913, 46914, "", "", 164, 178, true);
        rsComponent.addChild(46901, 70, 20);
        rsComponent.addChild(46902, 144, 200);
        rsComponent.addChild(46903, 144, 200);
        rsComponent.addChild(46905, 275, 200);
        rsComponent.addChild(46906, 275, 200);
        rsComponent.addChild(46908, 417, 30);
        rsComponent.addChild(46909, 417, 30);
        rsComponent.addChild(46911, 164, 178);
        return rsComponent;
    }
}

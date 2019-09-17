package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the warning {@link com.runescape.cache.media.inter.InterfacePlugin} for climbing down an area.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ClimbDownWarningInterfacePlugin implements InterfacePlugin {
    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(259);
        rsComponent.addSprite(260, ImageLoader.forName("DOWN_WARNING_BACK"));
        rsComponent.addHoverButton(261, SpriteRepository.SMALL_X, 16, 16, "Close", -1, 262, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(262, SpriteRepository.SMALL_X_HOVER, 16, 16, 263);
        rsComponent.addText(264, "Warning!", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 120, 14, -1);
        rsComponent.addText(265, "Are you sure you want to climb down?", rsFonts, RSComponent.REGULAR, 0xFF7D1F, true, true, 319, 14, -1);
        rsComponent.addHoverText(266, "Yes - I know that it may be\\n  dangerous down there", rsFonts, RSComponent.REGULAR, 0xFF0000, 0x01FF00, true, true, 319, 29, "Yes");
        rsComponent.addHoverText(267, "No thanks - I don't want to die", rsFonts, RSComponent.REGULAR, 0xFF0000, 0x01FF00, true, true, 319, 29, "No");
        rsComponent.addChild(260, 98, 34);
        rsComponent.addChild(261, 405, 43);
        rsComponent.addChild(262, 405, 43);
        rsComponent.addChild(264, 205, 43);
        rsComponent.addChild(265, 106, 103);
        rsComponent.addChild(266, 104, 157);
        rsComponent.addChild(267, 106, 189);
        return rsComponent;
    }
}
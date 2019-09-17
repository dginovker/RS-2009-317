package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * The sign post {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class SignPostInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(46950);
        rsComponent.addSprite(46951, ImageLoader.forName("SIGN_POST_BACK"));
        for (int childId = 46952; childId < 46956; childId++) {
            rsComponent.addText(childId, "To the " + (childId == 46952 ? "NORTH" : childId == 46953 ? "SOUTH" : childId == 46954 ? "EAST" : "WEST"), rsFonts, 2, 0x381f0A, true, false, 180, -1);
        }
        for (int childId = 46957; childId < 46962; childId++) {
            rsComponent.addText(childId, "", rsFonts, 1, 0x381f0A, true, false, 180, -1);
        }
        rsComponent.addChild(46951, 0, 0);
        for (int childId = 46952; childId < 46956; childId++) {
            rsComponent.addChild(childId,
                    (childId == 46952 || childId == 46953) ? 168 : childId == 46954 ? 350 : -15,
                    childId == 46952 ? 16 : childId == 46953 ? 240 : 88);
        }
        for (int childId = 46957; childId < 46961; childId++) {
            rsComponent.addChild(childId,
                    (childId == 46957 || childId == 46958) ? 168 : childId == 46959 ? 350 : -15,
                    childId == 46957 ? 36 : childId == 46958 ? 260 : 118);
        }
        return rsComponent;
    }
}

package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for quick prayers.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class QuickPrayersInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(17200);
        rsComponent.addSprite(17232, ImageLoader.forName("QUICK_PRAY_BACK")).opacity = 80; // TODO
        int prayerId = 17202;
        for (int prayerConfig = 205; prayerId < 17231 || prayerConfig < 234; prayerConfig++) {
            RSComponent rsComponent1 = rsComponent.addConfigButton(prayerId, new SpriteSet(ImageLoader.forName("CHECKMARK_3_ON"),
                            ImageLoader.forName("CHECKMARK_3_OFF")),
                    14, 15, new String[]{"Select"}, 1, prayerConfig, -1);
            rsComponent1.interfaceConfig = true;
            rsComponent1.tooltip = "Select";
            prayerId++;
        }
        rsComponent.addHoverButton(17241, ImageLoader.forName("QUICK_PRAY_CONFIRM"), 190, 24, "Confirm Selection", -1, 17242, 4, false);
        rsComponent.addHoveredButton(17242, ImageLoader.forName("QUICK_PRAY_CONFIRM_HOVER"), /*5,*/ 190, 24, 17243);

        rsComponent.setChildBounds(17232, 0, 7);
        rsComponent.setChildBounds(5632, 5, 8 + 3);
        rsComponent.setChildBounds(5633, 44, 8 + 3);
        rsComponent.setChildBounds(5634, 79, 11 + 3);
        rsComponent.setChildBounds(19813, 116, 10 + 3);
        rsComponent.setChildBounds(19815, 153, 9 + 3);
        rsComponent.setChildBounds(5635, 5, 48 + 3);
        rsComponent.setChildBounds(5636, 44, 47 + 3);
        rsComponent.setChildBounds(5637, 79, 49 + 3);
        rsComponent.setChildBounds(5638, 116, 50 + 3);
        rsComponent.setChildBounds(5639, 154, 50 + 3);
        rsComponent.setChildBounds(5640, 4, 84 + 3);
        rsComponent.setChildBounds(19817, 44, 87 + 3);
        rsComponent.setChildBounds(19820, 81, 85 + 3);
        rsComponent.setChildBounds(5641, 117, 85 + 3);
        rsComponent.setChildBounds(5642, 156, 87 + 3);
        rsComponent.setChildBounds(5643, 5, 125 + 3);
        rsComponent.setChildBounds(5644, 43, 124 + 3);
        rsComponent.setChildBounds(13984, 83, 124 + 3);
        rsComponent.setChildBounds(5645, 115, 121 + 3);
        rsComponent.setChildBounds(19822, 154, 124 + 3);
        rsComponent.setChildBounds(19824, 5, 160 + 3);
        rsComponent.setChildBounds(5649, 41, 158 + 3);
        rsComponent.setChildBounds(5647, 79, 163 + 3);
        rsComponent.setChildBounds(5648, 116, 158 + 3);
        rsComponent.setChildBounds(19826, 161, 160 + 3);
        rsComponent.setChildBounds(19828, 4, 202);
        rsComponent.setChildBounds(17202, 5 - 3, 8);
        rsComponent.setChildBounds(17203, 44 - 3, 8);
        rsComponent.setChildBounds(17204, 79 - 3, 8);
        rsComponent.setChildBounds(17205, 116 - 3, 8);
        rsComponent.setChildBounds(17206, 153 - 3, 8);
        rsComponent.setChildBounds(17207, 5 - 3, 48);
        rsComponent.setChildBounds(17208, 44 - 3, 48);
        rsComponent.setChildBounds(17209, 79 - 3, 48);
        rsComponent.setChildBounds(17210, 116 - 3, 48);
        rsComponent.setChildBounds(17211, 153 - 3, 48);
        rsComponent.setChildBounds(17212, 5 - 3, 85);
        rsComponent.setChildBounds(17213, 44 - 3, 85);
        rsComponent.setChildBounds(17214, 79 - 3, 85);
        rsComponent.setChildBounds(17215, 116 - 3, 85);
        rsComponent.setChildBounds(17216, 153 - 3, 85);
        rsComponent.setChildBounds(17217, 5 - 3, 124);
        rsComponent.setChildBounds(17218, 44 - 3, 124);
        rsComponent.setChildBounds(17219, 79 - 3, 124);
        rsComponent.setChildBounds(17220, 116 - 3, 124);
        rsComponent.setChildBounds(17221, 153 - 3, 124);
        rsComponent.setChildBounds(17222, 5 - 3, 160);
        rsComponent.setChildBounds(17223, 44 - 3, 160);
        rsComponent.setChildBounds(17224, 79 - 3, 160);
        rsComponent.setChildBounds(17225, 116 - 3, 160);
        rsComponent.setChildBounds(17226, 153 - 3, 160);
        rsComponent.setChildBounds(17227, 4 - 3, 194);
        rsComponent.setChildBounds(17241, 0, 237);
        rsComponent.setChildBounds(17242, 0, 237);
        return rsComponent;
    }
}

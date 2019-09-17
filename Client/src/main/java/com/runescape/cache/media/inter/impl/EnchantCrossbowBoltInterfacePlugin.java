package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.Sprite;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for bolt enchantments.
 *
 * @author Vincent M. <admin@Gielinor.org>
 */
public class EnchantCrossbowBoltInterfacePlugin implements InterfacePlugin {
    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(47200);
        rsComponent.addSprite(47201, ImageLoader.forName("ENCHANT_CROSSBOW_BOLT_BACK"));
        int[][] itemCounts = new int[][]{
                {1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 3, 1}, // Top
                {1, 2, 1, 5, 1, 1, 10, 2, 1, 15, 1, 1, 20, 1} // Bottom
        };
        int[][] binds = new int[][]{
                {
                        30, 63,
                        112, 143, 174,
                        224, 255,
                        322, 352,
                        406, 435, 464
                }, // Top
                {
                        32, 64,
                        108, 138, 169,
                        208, 238, 269,
                        305, 332, 364,
                        403, 435, 464
                } // Bottom
        };
        int[] offsetsX = new int[]{23, 107, 214, 308, 400, 23, 107, 214, 308, 400};
        int[] levels = new int[]{4, 7, 14, 24, 27, 29, 49, 57, 68, 87};
        int childId = 47202;
        for (int[] itemCountArray : itemCounts) {
            for (int itemCount : itemCountArray) {
                rsComponent.addText(childId, "0/" + itemCount, rsFonts, 0, 0xC00000, true, true, 20, -1);
                childId++;
            }
        }
        int offset = 0;
        for (childId = 47228; childId < 47238; childId++) {
            rsComponent.addActionButton(childId, Sprite.NULL_TYPE, (offset == 1 || offset == 4 || offset >= 6) ? 100 : 73, 120, "Enchant <col=FF9040>Bolts", -1);
            offset++;
        }
        childId = 47238;
        for (int level : levels) {
            rsComponent.addText(childId++, "Magic " + level, rsFonts, 0, 0x00CC00, true, true, 55, -1);
        }
        rsComponent.addHoverButton(47248, SpriteRepository.SMALL_X, 16, 16, "Close", -1, 47249, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(47249, SpriteRepository.SMALL_X_HOVER, 16, 16, 47250);
        rsComponent.addChild(47201, 13, 16);
        childId = 47202;
        for (int index = 0; index < binds.length; index++) {
            for (int xOffset : binds[index]) {
                rsComponent.addChild(childId, xOffset, index == 0 ? 168 : 298);
                childId++;
            }
        }
        childId = 47228;
        int offsetY = 61;
        int offX = 0;
        for (int offsetX : offsetsX) {
            if (offX == 5) {
                offsetY = 185;
            }
            rsComponent.addChild(childId, offsetX, offsetY);
            childId++;
            offX++;
        }
        childId = 47238;
        binds = new int[][]{
                {36, 136, 231, 327, 418},
                {48, 129, 226, 323, 418}
        };
        for (int index = 0; index < binds.length; index++) {
            for (int xOffset : binds[index]) {
                rsComponent.addChild(childId, xOffset - 5, index == 0 ? 79 : 208);
                childId++;
            }
        }
        rsComponent.addChild(47248, 471, 27);
        rsComponent.addChild(47249, 471, 27);
        return rsComponent;
    }
}

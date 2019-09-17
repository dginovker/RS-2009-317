package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * The {@link com.runescape.cache.media.inter.InterfacePlugin} for items kept on death.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ItemsOnDeathInterfacePlugin implements InterfacePlugin {
    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25609);
        rsComponent.addSprite(25610, ImageLoader.forName("ITEM_ON_DEATH_BACK")).addAlphaBox(5, 5, 3, 3, 0, 100);
        rsComponent.addHoverButton(25611, SpriteRepository.SMALL_X, 16, 16, "Close", 250, 25612, 3);
        rsComponent.addHoveredButton(25612, SpriteRepository.SMALL_X_HOVER, 16, 16, 25613);
        rsComponent.addText(25614, "Items Kept on Death", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 120, 14, -1);
        rsComponent.addText(25615, "Items you will keep on death if not skulled:", gameFonts, RSComponent.REGULAR, 0xFF981F, false, true, 120, 14, -1);
        rsComponent.addText(25616, "Items you will lose on death if not skulled:", gameFonts, RSComponent.REGULAR, 0xFF981F, false, true, 120, 14, -1);
        rsComponent.addText(25617, "Information:", gameFonts, RSComponent.REGULAR, 0xFF981F, false, true, 120, 14, -1);
        rsComponent.addText(25618, "The normal amount of items\\nkept is three.", gameFonts, RSComponent.SMALL, 0xFF981F, false, true, 149, 14, -1);
        rsComponent.addText(25619, "You have no factors affecting\\nthe items you keep.", gameFonts, RSComponent.SMALL, 0xFF981F, false, true, 149, 14, -1);
        rsComponent.addText(25620, "Value of lost items:", gameFonts, RSComponent.SMALL, 0xFF981F, true, true, 149, 14, -1);
        rsComponent.addText(25621, "12,900 gp", gameFonts, RSComponent.SMALL, 0xFF981F, true, true, 149, 14, -1);
        rsComponent.addText(25622, "Max items kept on death :", gameFonts, RSComponent.REGULAR, 0xffCC33, true, true, 149, 14, -1);
        rsComponent.addText(25623, "~ 3 ~", gameFonts, RSComponent.REGULAR, 0xffCC33, true, true, 149, 14, -1);
        rsComponent.addToItemGroup(25624, 8, 6, 8, 0, false, true, true, "Item :");
        rsComponent.addToItemGroup(25625, 8, 6, 6, 6, false, true, true, "Item :");

        rsComponent.addChild(25610, 8, 9);
        rsComponent.addChild(25611, 481, 18);
        rsComponent.addChild(25612, 481, 18);
        rsComponent.addChild(25614, 194, 19);
        rsComponent.addChild(25615, 24, 50);
        rsComponent.addChild(25616, 24, 110);
        rsComponent.addChild(25617, 348, 47);
        rsComponent.addChild(25618, 348, 66);
        rsComponent.addChild(25619, 348, 102);
        rsComponent.addChild(25620, 345, 249);
        rsComponent.addChild(25621, 346, 264);
        rsComponent.addChild(25622, 348, 277);
        rsComponent.addChild(25623, 348, 301);
        rsComponent.addChild(25624, 24, 72);
        rsComponent.addChild(25625, 24, 132);
        return rsComponent;
    }
}

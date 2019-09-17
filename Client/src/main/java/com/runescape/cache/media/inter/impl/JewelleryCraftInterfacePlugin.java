package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class JewelleryCraftInterfacePlugin implements InterfacePlugin {
    /**
     * The name types of jewellery that can be crafted.
     */
    private static final String[] NAMES = new String[]{"Gold", "Sapphire", "Emerald", "Ruby", "Diamond", "Dragonstone", "Onyx"};

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        // TODO Remove "X" on JEWELLERY_CRAFT_BACK
        RSComponent rsComponent = new RSComponent().addInterface(23335);
        rsComponent.addSprite(23336, ImageLoader.forName("JEWELLERY_CRAFT_BACK"));
        rsComponent.addHoverButton(23337, SpriteRepository.SMALL_X, 16, 16, "Close", -1, 23338, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(23338, SpriteRepository.SMALL_X_HOVER, 16, 16, 23339);
        rsComponent.addText(23340, "What would you like to make?", rsFonts, RSComponent.BOLD, 0xFF991F, true, true, 378, -1);
        rsComponent.addText(23341, "You need a ring mould to craft rings.", rsFonts, RSComponent.REGULAR, 0xFF991F, false, true, 378, -1);
        rsComponent.addText(23342, "Rings", rsFonts, RSComponent.BOLD, 0xFF991F, false, true, 394, -1);
        rsComponent.addSprite(23343, ImageLoader.forName("RING_MOULD"));
        int index = 0;
        for (int childId = 23344; childId < (23344 + (NAMES.length)); childId++) {
            rsComponent.addToItemGroup(childId, 35, 32, 0, 0, false, false, false, "Make 1 <col=FF9040>" + NAMES[index] + " Ring", "Make 5 <col=FF9040>" + NAMES[index] + " Ring",
                    "Make All <col=FF9040>" + NAMES[index] + " Ring", "Make X <col=FF9040>" + NAMES[index] + " Ring");
            index++;
        }
        rsComponent.addText(23351, "You need a necklace mould to craft necklaces.", rsFonts, RSComponent.REGULAR, 0xFF991F, false, true, 378, -1);
        rsComponent.addText(23352, "Necklaces", rsFonts, RSComponent.BOLD, 0xFF991F, false, true, 394, -1);
        rsComponent.addSprite(23353, ImageLoader.forName("NECKLACE_MOULD"));
        index = 0;
        for (int childId = 23354; childId < (23354 + (NAMES.length)); childId++) {
            rsComponent.addToItemGroup(childId, 35, 32, 0, 0, false, false, false, "Make 1 <col=FF9040>" + NAMES[index] + " Necklace", "Make 5 <col=FF9040>" + NAMES[index] + " Necklace",
                    "Make All <col=FF9040>" + NAMES[index] + " Necklace", "Make X <col=FF9040>" + NAMES[index] + " Necklace");
            index++;
        }
        rsComponent.addText(23361, "You need an amulet mould to craft amulets.", rsFonts, RSComponent.REGULAR, 0xFF991F, false, true, 378, -1);
        rsComponent.addText(23362, "Amulets", rsFonts, RSComponent.BOLD, 0xFF991F, false, true, 394, -1);
        rsComponent.addSprite(23363, ImageLoader.forName("NECKLACE_MOULD"));
        index = 0;
        for (int childId = 23364; childId < (23364 + (NAMES.length)); childId++) {
            rsComponent.addToItemGroup(childId, 35, 32, 0, 0, false, false, false, "Make 1 <col=FF9040>" + NAMES[index] + " Amulet", "Make 5 <col=FF9040>" + NAMES[index] + " Amulet",
                    "Make All <col=FF9040>" + NAMES[index] + " Amulet", "Make X <col=FF9040>" + NAMES[index] + " Amulet");
            index++;
        }
        rsComponent.addText(23371, "You need a bracelets mould to craft bracelets.", rsFonts, RSComponent.REGULAR, 0xFF991F, false, true, 378, -1);
        rsComponent.addText(23372, "Bracelets", rsFonts, RSComponent.BOLD, 0xFF991F, false, true, 394, -1);
        rsComponent.addSprite(23373, ImageLoader.forName("BRACELET_MOULD"));
        index = 0;
        for (int childId = 23374; childId < (23374 + (NAMES.length)); childId++) {
            rsComponent.addToItemGroup(childId, 35, 32, 0, 0, false, false, false, "Make 1 <col=FF9040>" + NAMES[index] + " Bracelet", "Make 5 <col=FF9040>" + NAMES[index] + " Bracelet",
                    "Make All <col=FF9040>" + NAMES[index] + " Bracelet", "Make X <col=FF9040>" + NAMES[index] + " Bracelet");
            index++;
        }

        rsComponent.addChild(23336, 13, 23);
        rsComponent.addChild(23337, 471, 32);
        rsComponent.addChild(23338, 471, 32);
        rsComponent.addChild(23340, 66, 33);
        rsComponent.addChild(23341, 96, 90);
        rsComponent.addChild(23342, 80, 66);
        rsComponent.addChild(23343, 35, 74);
        int xOffset = 111;
        for (int childId = 23344; childId < (23344 + (NAMES.length)); childId++) {
            rsComponent.addChild(childId, xOffset, 88);
            xOffset += 50;
        }
        rsComponent.addChild(23351, 96, 151);
        rsComponent.addChild(23352, 80, 127);
        rsComponent.addChild(23353, 35, 135);
        xOffset = 111;
        for (int childId = 23354; childId < (23354 + (NAMES.length)); childId++) {
            rsComponent.addChild(childId, xOffset, 149);
            xOffset += 50;
        }
        rsComponent.addChild(23361, 96, 212);
        rsComponent.addChild(23362, 80, 188);
        rsComponent.addChild(23363, 35, 196);
        xOffset = 111;
        for (int childId = 23364; childId < (23364 + (NAMES.length)); childId++) {
            rsComponent.addChild(childId, xOffset, 210);
            xOffset += 50;
        }
        rsComponent.addChild(23371, 96, 273);
        rsComponent.addChild(23372, 80, 249);
        rsComponent.addChild(23373, 35, 257);
        xOffset = 111;
        for (int childId = 23374; childId < (23374 + (NAMES.length)); childId++) {
            rsComponent.addChild(childId, xOffset, 271);
            xOffset += 50;
        }
        return rsComponent;
    }
}

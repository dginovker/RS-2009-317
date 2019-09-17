package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for the Stat spy interface.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class StatSpyInterfacePlugin implements InterfacePlugin {

    private final int[][] OFFSETS = new int[][]{
            {20, 3}, {48, 7}, {68, 16}, {64, 12}, {95, 2}, {123, 6}, {143, 15}, {139, 11}, {20, 30}, {48, 34},
            {68, 43}, {64, 39}, {95, 30}, {123, 34}, {143, 43}, {139, 39}, {20, 58}, {48, 62}, {68, 71}, {64, 67},
            {94, 58}, {122, 62}, {142, 71}, {138, 67}, {20, 86}, {48, 90}, {68, 99}, {64, 95}, {94, 85}, {122, 89},
            {142, 98}, {138, 94}, {20, 114}, {48, 118}, {68, 127}, {64, 123}, {94, 113}, {122, 117}, {142, 126},
            {138, 122}, {20, 142}, {48, 146}, {68, 155}, {64, 151}, {94, 141}, {122, 145}, {142, 154}, {138, 150},
            {20, 170}, {48, 174}, {68, 183}, {64, 179}, {94, 169}, {122, 173}, {142, 182}, {138, 178}, {20, 198},
            {48, 202}, {68, 211}, {64, 207}, {94, 198}, {122, 202}, {142, 211}, {138, 207}, {20, 226}, {48, 230},
            {68, 239}, {64, 235}, {94, 226}, {122, 230}, {142, 239}, {138, 235}, {20, 254}, {48, 258}, {68, 267},
            {64, 263}, {94, 254}, {122, 258}, {142, 267}, {138, 263}, {20, 282}, {48, 286}, {68, 295}, {64, 291},
            {94, 282}, {122, 286}, {142, 295}, {138, 291}, {20, 310}, {48, 314}, {68, 323}, {64, 319},
    };

    private final String[] SKILL_ICONS = new String[]{
            "ATTACK", "HITPOINTS", "MINING", "STRENGTH", "AGILITY", "SMITHING",
            "DEFENCE", "HERBLORE", "FISHING", "RANGED", "THIEVING", "COOKING", "PRAYER",
            "CRAFTING", "FIREMAKING", "MAGIC", "FLETCHING", "WOODCUTTING", "RUNECRAFT",
            "SLAYER", "FARMING", "CONSTRUCTION", "HUNTER"
    };

    // {94, 310},
    // {121, 314}, {142, 324}, {137, 320}, {-2, 48}, {165, 49}, {12, 9}, {1, 31}, {165, 8}, {160, 4}

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addTabInterface(49000);
        rsComponent.addText(49001, "Player :", rsFonts, RSComponent.BOLD, 16750623, true, true, 169, -1);
        rsComponent.addHoverButton(49002, SpriteRepository.SMALL_X, 16, 16, "Close", -1, 49003, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(49003, SpriteRepository.SMALL_X_HOVER, 16, 16, 49004);
        rsComponent.addText(49005, "Player ~ Name", rsFonts, RSComponent.BOLD, 16750623, true, true, 183, -1);
        rsComponent.addChild(49001, 12, 9);
        rsComponent.addChild(49002, 165, 8);
        rsComponent.addChild(49003, 165, 8);
        rsComponent.addChild(49005, 1, 31);
        rsComponent.addChild(49006, -2, 48);
        RSComponent scrollComponent = rsComponent.addInterface(49006, 168, 207);
        scrollComponent.scrollMax = 350;

        int index = 0;
        for (int j = 0; j < 23; j++) {
            scrollComponent.addSprite(49007 + index, ImageLoader.forName("SKILL_ICON_" + SKILL_ICONS[j]));
            scrollComponent.addText(49007 + index + 1, "00", rsFonts, RSComponent.REGULAR, 16750623, false, true, 15, -1);
            scrollComponent.addText(49007 + index + 2, "00", rsFonts, RSComponent.REGULAR, 16750623, true, true, 15, -1);
            scrollComponent.addText(49007 + index + 3, "/", rsFonts, RSComponent.REGULAR, 16750623, false, true, 8, -1);
            index += 4;
        }
        index = 0;
        for (int[] offsets : OFFSETS) {
            scrollComponent.addChild(49007 + index, offsets[0], offsets[1]);
            index++;
        }
        return rsComponent;
    }
}

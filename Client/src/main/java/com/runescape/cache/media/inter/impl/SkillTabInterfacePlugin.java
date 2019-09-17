package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;
import com.runescape.util.Skills;

/**
 * The {@link com.runescape.cache.media.inter.InterfacePlugin} for fixing the skill tab.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class SkillTabInterfacePlugin implements InterfacePlugin {
    private static final int[] SKILL_ORDER = {Skills.ATTACK, Skills.HITPOINTS, Skills.MINING, Skills.STRENGTH,
            Skills.AGILITY, Skills.SMITHING, Skills.DEFENCE, Skills.HERBLORE, Skills.FISHING, Skills.RANGE,
            Skills.THIEVING, Skills.COOKING, Skills.PRAYER, Skills.CRAFTING, Skills.FIREMAKING, Skills.MAGIC,
            Skills.FLETCHING, Skills.WOODCUTTING, Skills.RUNECRAFTING, Skills.SLAYER, Skills.FARMING,
            Skills.CONSTRUCTION, Skills.HUNTER, Skills.SUMMONING};

    private static final String[] SKILL_NAME = {
            "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching",
            "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming",
            "Runecraft", "Construction", "Hunter", "Summoning"
    };

    private static final int[] SKILL_ICON_X = {2, 5, 4, 57, 3, 2, 2, 112, 112, 57, 112, 111, 58, 111, 112, 57, 58, 57, 58, 110, 3, 3, 57, 111};

    private static final int[] SKILL_ICON_Y = {6, 70, 40, 7, 103, 135, 166, 100, 164, 164, 68, 131, 134, 38, 5, 70, 35, 106, 196, 198, 195, 228, 229, 226};

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addTabInterface(3917);
        RSComponent totalLevel = rsComponent.addText(24607, "Total level: %1", rsFonts, 2, 0xFFFF00, false, true, 100, 0);
        totalLevel.scripts = new int[][]{
                {9, 0}
        };
        rsComponent.addSprite(24608, ImageLoader.forName("SKILL_TAB_BORDER"));

        rsComponent.addChild(24607, 3, 2);
        rsComponent.addChild(24608, 3, 17);
        rsComponent.addChild(24609, 4, 21);

        RSComponent scrollComponent = rsComponent.addTabInterface(24609);
        scrollComponent.height = 227;
        scrollComponent.width = 164;
        scrollComponent.scrollMax = 260;
        for (int id = 0; id < 24; id++) {
            scrollComponent.addActionButton(24610 + id,
                    ImageLoader.forName("SKILL_ICON_BACK").toSpriteSet(), 56, 32, -1, "View");
            RSComponent min = scrollComponent.addText(24610 + 24 + id, "%1", rsFonts, RSComponent.SMALL, 0xFFFF00, true, true, 15, 0);
            min.scripts = new int[][]{{1, SKILL_ORDER[id], 0}};
            RSComponent max = scrollComponent.addText(24610 + 48 + id, "%1", rsFonts, RSComponent.SMALL, 0xFFFF00, true, true, 15, 0);
            max.scripts = new int[][]{
                    {2, SKILL_ORDER[id], 0},
                    {2, SKILL_ORDER[id], 0}
            };
            scrollComponent.addSprite(24610 + id + 72, ImageLoader.forName("SKILL_ICON_" + SKILL_NAME[id].toUpperCase()));
            scrollComponent.addHoverBox(24610 + id + 96,
                    SKILL_NAME[SKILL_ORDER[id]] + ": %1/%2\\nCurrent XP: %4\\nNext Level at: %3\\nRemaining XP: %5",
                    54, 32, true).scripts = new int[][]{
                    {1, SKILL_ORDER[id], 0},
                    {2, SKILL_ORDER[id], 0},
                    {6, SKILL_ORDER[id], 0},
                    {3, SKILL_ORDER[id], 0},
                    {21, SKILL_ORDER[id], 0}
            };
        }
        int xOffset = -1;
        int yOffset = 0;
        for (int index = 0; index < 24; index++) {
            scrollComponent.addChild(24610 + index, xOffset + 1, yOffset);
            scrollComponent.addChild(24610 + 24 + index, xOffset + 25, yOffset + 4);
            scrollComponent.addChild(24610 + 48 + index, xOffset + 9 + 25, yOffset + 14 + 4);
            if (xOffset == 107) {
                xOffset = -1;
                yOffset += 32;
            } else {
                xOffset += 54;
            }
        }
        xOffset = -1;
        yOffset = 0;
        for (int index = 0; index < 24; index++) {
            scrollComponent.addChild(24610 + 72 + index, SKILL_ICON_X[index], SKILL_ICON_Y[index] + 3);

            if (xOffset == 107) {
                xOffset = -1;
                yOffset += 32;
            } else {
                xOffset += 54;
            }
        }
        xOffset = -1;
        yOffset = 0;
        for (int index = 0; index < 24; index++) {
            scrollComponent.addChild(24610 + 96 + index, xOffset, yOffset);
            if (xOffset == 107) {
                xOffset = -1;
                yOffset += 32;
            } else {
                xOffset += 54;
            }
        }
        return rsComponent;
    }
}

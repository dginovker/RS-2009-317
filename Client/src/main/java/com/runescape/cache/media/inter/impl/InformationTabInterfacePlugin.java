package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the Quest diary {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 */
public class InformationTabInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(22697);
        rsComponent.addText(22698, "Information", rsFonts, 2, 0xFF981F, false, true, 0, 0);
        rsComponent.addSprite(22699, new SpriteSet("QUEST_TAB_BACKGROUND").toSprite()); // TODO
        rsComponent.addButton(22700, new SpriteSet("ACHIEVEMENT_DIARY_ICON"), 18, 18, "Swap to Achievement Diaries", 1);
        //   rsComponent.addButton(22701, new SpriteSet("MINIGAME_ICON"), 18, 18, "Swap to Minigames", 1);
        rsComponent.addButton(22702, new SpriteSet("QUEST_DIARY_ICON"), 18, 18, "Swap to Quests", 1);
        int[][] questBinds = new int[][]{
                {22698, 10, 5},
                {22699, 3, 24},
                {22700, 154, 4},
                //  {22701, 172, 4},
                {22702, 136, 4},
                {22703, 8, 29}
        };
        for (int[] questBind : questBinds) {
            rsComponent.setChildBounds(questBind[0], questBind[1], questBind[2]);
        }
        rsComponent = rsComponent.addInterface(22703);
        rsComponent.height = 214;
        rsComponent.width = 165;
        rsComponent.scrollMax = 260;
        int yOffset = 5;
        for (int id = 22704; id <= 22774; id++) {
            if (id == 22712 || id == 22718 || id == 22719 || id == 22720 || id == 22721) {
                rsComponent.addHoverText(id, "" + id, rsFonts, RSComponent.SMALL, 0xFF991F, false, true, 150, "View");
            } else {
                rsComponent.addText(id, "", rsFonts, RSComponent.SMALL, 0xFF991F, false, true, 150, 52);
            }
            rsComponent.setChildBounds(id, 8, yOffset);
            yOffset += 14;
        }
        return rsComponent;
    }
}

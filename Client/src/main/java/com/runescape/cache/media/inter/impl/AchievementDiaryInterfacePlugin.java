package com.runescape.cache.media.inter.impl;

import com.runescape.Constants;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the Achievement diary {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 */
public class AchievementDiaryInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(29300);
        rsComponent.addText(29301, "Achievement Diary", rsFonts, 2, 0xFF981F, false, true, 0, 0);
        rsComponent.addSprite(29302, new SpriteSet("QUEST_TAB_BACKGROUND").toSprite()); // TODO
        rsComponent.addButton(29303, new SpriteSet("QUEST_DIARY_ICON"), 18, 18, "Swap to Quests", 1);
        //rsComponent.addButton(29304, new SpriteSet("MINIGAME_ICON"), 18, 18, "Swap to Minigames", 1);
        rsComponent.addButton(22695, new SpriteSet("INFORMATION_ICON"), 18, 18, "Swap to Information", 1);
        int[][] questBinds = new int[][]{
                {29301, 10, 5},
                {29302, 3, 24},
                {29303, 154, 4},
                // {29304, 172, 4},
                {22695, 136, 4},
                {29305, 8, 29}
        };
        for (int[] questBind : questBinds) {
            rsComponent.addChild(questBind[0], questBind[1], questBind[2]);
        }
        RSComponent scrollComponent = rsComponent.addInterface(29305);
        scrollComponent.height = 214;
        scrollComponent.width = 165;
        scrollComponent.scrollMax = Constants.ACHIEVEMENT_AMOUNT * 14;
        int yOffset = -10;
        for (int id = 29306; id <= (29306 + Constants.ACHIEVEMENT_AMOUNT); id++) {
            scrollComponent.addHoverText(id, "", rsFonts, 0, 0xC00000, 0xFFFFFF, false, true, 150, "View");
            scrollComponent.addChild(id, 8, yOffset);
            yOffset += 14;
        }
        return rsComponent;
    }
}

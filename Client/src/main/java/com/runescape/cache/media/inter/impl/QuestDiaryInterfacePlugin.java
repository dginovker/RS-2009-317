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
public class QuestDiaryInterfacePlugin implements InterfacePlugin {
    /**
     * Represents quests.
     */
    private static final String[] QUESTS = new String[]{"Curse of the Undead", "Gertrude's Cat", "The Lost Kingdom"};

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(638);
        rsComponent.addText(29155, "Quests", rsFonts, 2, 0xFF981F, false, true, 0, 0);
        rsComponent.addSprite(29156, new SpriteSet("QUEST_TAB_BACKGROUND").toSprite());
        rsComponent.addButton(29157, new SpriteSet("ACHIEVEMENT_DIARY_ICON"), 18, 18, "Swap to Achievement Diaries", 1);
        // rsComponent.addButton(29270, new SpriteSet("MINIGAME_ICON"), 18, 18, "Swap to Minigames", 1);
        rsComponent.addButton(22694, new SpriteSet("INFORMATION_ICON"), 18, 18, "Swap to Information", 1);
        int[][] questBinds = new int[][]{
                {29155, 10, 5},
                {29156, 3, 24},
                {29157, 154, 4},
                // {29270, 172, 4},
                {22694, 136, 4},
                {29160, 8, 29}
        };
        for (int[] questBind : questBinds) {
            rsComponent.setChildBounds(questBind[0], questBind[1], questBind[2]);
        }
        rsComponent = rsComponent.addInterface(29160);
        rsComponent.height = 214;
        rsComponent.width = 165;
        rsComponent.scrollMax = QUESTS.length * 14;
        int yOffset = 5;
        int questIndex = 0;
        for (int id = 29161; id <= (29160 + QUESTS.length); id++) {
            if (id == 29163) {
                id = 29164;
            }
            rsComponent.addHoverText(id, QUESTS[questIndex], rsFonts, RSComponent.SMALL, 0xC00000, false, true, 150, "View");
            rsComponent.setChildBounds(id, 8, yOffset);
            yOffset += 14;
            questIndex++;
        }
        return rsComponent;
    }
}

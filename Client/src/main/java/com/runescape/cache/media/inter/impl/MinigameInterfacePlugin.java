package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the Minigame selection {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 */
public class MinigameInterfacePlugin implements InterfacePlugin {

    /**
     * The minigame teleports.
     */
    private final String[] MINIGAMES = new String[]{
            "Castle Wars",
            "Clan Wars",
            "Barbarian Assault",
            "Barrows",
            "Burthorpe Games Room",
            "Duel Arena",
            "Pest Control",
            "TzHaar Fight Cave",
            "TzHaar Fight Pit",
            "Warriors' Guild"
    };

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(23235);
        rsComponent.addText(23236, "Minigames", rsFonts, 2, 0xFF981F, false, true, 0, 0);
        rsComponent.addSprite(23237, new SpriteSet("QUEST_TAB_BACKGROUND").toSprite()); // TODO
        rsComponent.addButton(23238, new SpriteSet("ACHIEVEMENT_DIARY_ICON"), 18, 18, "Swap to Achievement Diaries", 1);
        rsComponent.addButton(23239, new SpriteSet("QUEST_DIARY_ICON"), 18, 18, "Swap to Quests", 1);
        rsComponent.addButton(22696, new SpriteSet("INFORMATION_ICON"), 18, 18, "Swap to Information", 1);
        int[][] questBinds = new int[][]{
                {23236, 10, 5},
                {23237, 3, 24},
                {23238, 172, 4},
                {23239, 154, 4},
                {22696, 136, 4},
                {23240, 8, 29},
        };
        for (int[] questBind : questBinds) {
            rsComponent.setChildBounds(questBind[0], questBind[1], questBind[2]);
        }
        rsComponent = rsComponent.addInterface(23240);
        rsComponent.height = 214;
        rsComponent.width = 165;
        rsComponent.scrollMax = MINIGAMES.length * 14;
        int yOffset = 5;
        int index = 0;
        for (int id = 23241; id <= (23240 + MINIGAMES.length); id++) {
            rsComponent.addHoverText(id, MINIGAMES[index], rsFonts, RSComponent.REGULAR, 0xFF981F, false, true, 150, "Teleport");
            rsComponent.setChildBounds(id, 8, yOffset);
            yOffset += 14;
            index++;
        }
        return rsComponent;
    }
}

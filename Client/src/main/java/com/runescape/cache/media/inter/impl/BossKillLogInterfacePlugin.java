package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * The {@link com.runescape.cache.media.inter.InterfacePlugin} for the Boss kill log.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class BossKillLogInterfacePlugin implements InterfacePlugin {

    /**
     * Represents bosses killed.
     */
    private final String[] BOSSES_KILLED = new String[]{
            "Kree'Arra", "Commander Zilyana", "General Graardor", "K'ril Tsutsaroth", "Dagannoth Rex", "Dagannoth Prime",
            "Dagannoth Supreme", "Giant Mole", "Kalphite Queen", "King Black Dragon", "Chaos Elemental", "Bork", "Barrelchest", "Barrows Chests"
    };

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(580);
        rsComponent.addSprite(581, ImageLoader.forName("BOSS_KILL_LOG_BACK"));
        rsComponent.addChild(581, 2, 7);
        int index = 0;
        int yOffset = 68;
        for (int childId = 582; childId < (582 + BOSSES_KILLED.length); childId++) {
            rsComponent.addText(childId, BOSSES_KILLED[index], rsFonts, RSComponent.REGULAR, 0x46320A, false, false, 80, 14, -1);
            rsComponent.addText((childId + 30), "0", rsFonts, RSComponent.REGULAR, 0x46320A, false, false, 80, 14, -1);
            rsComponent.addChild(childId, 153, yOffset);
            rsComponent.addChild((childId + 30), 303, yOffset);
            yOffset += 15;
            index++;
        }
        rsComponent.addHoverButton(597, SpriteRepository.FANCY_X, 24, 23, "Close", -1, 598, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(598, SpriteRepository.FANCY_X_HOVER, 24, 23, 599);
        rsComponent.addChild(597, 450, 30);
        rsComponent.addChild(598, 450, 30);
        return null;
    }
}

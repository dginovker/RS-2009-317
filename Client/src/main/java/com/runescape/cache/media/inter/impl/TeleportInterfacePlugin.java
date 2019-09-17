package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.*;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class TeleportInterfacePlugin implements InterfacePlugin {
    String[] teleports = new String[]{
            "Training", "Monsters", "Minigames", "Bossing", "Wilderness", "Skilling","Cities", "Donor"
    };

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(24794);
        SpriteSet spriteSet = new SpriteSet(ImageLoader.forName("TELE_BUTTON_BACK_ON"), ImageLoader.forName("TELE_BUTTON_BACK_OFF"));

        rsComponent.addSprite(24795, ImageLoader.forName("TELEPORT_BACK")).addAlphaBox(10, 10, 4, 6, 0, 100);
        rsComponent.addHoverButton(24796, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 24797, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(24797, SpriteRepository.SMALL_X_HOVER, 16, 16, 24798);
        rsComponent.addText(24799, "Gielinor Teleports", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 0, 0);
        RSComponent scrollComponent = rsComponent.addInterface(24800);

        scrollComponent.type = 0;
        scrollComponent.height = 255;
        scrollComponent.width = (100 + (teleports.length < 10 ? 8 : 0));
        scrollComponent.scrollMax = teleports.length * 26;
        for (int fillerId = 24813; fillerId < 24823; fillerId++) {
            RSComponent filler = rsComponent.addFiller(fillerId);
            filler.addChild(fillerId, 0, 0);
        }
        int configId = 1;
        for (int buttonId = 24801; buttonId < (24801 + (teleports.length * 2)); buttonId++) {
            RSComponent button = scrollComponent.addActionButton(buttonId++, spriteSet, 83, 20, -1, teleports[configId - 1] + " teleports");
            button.scriptOperators = new int[1];
            button.scriptDefaults = new int[1];
            button.scriptOperators[0] = 1;
            button.scriptDefaults[0] = configId;
            button.scripts = new int[1][3];
            button.scripts[0][0] = 5;
            button.scripts[0][1] = 235;
            button.scripts[0][2] = 0;
            button.interfaceConfig = true;
            button = scrollComponent.addText(buttonId, teleports[configId - 1], rsFonts, RSComponent.REGULAR, 0xBF751D, 0xFF981F, true, true, 32, 12, -1);
            button.scriptOperators = new int[1];
            button.scriptDefaults = new int[1];
            button.scriptOperators[0] = 1;
            button.scriptDefaults[0] = configId;
            button.scripts = new int[1][3];
            button.scripts[0][0] = 5;
            button.scripts[0][1] = 235;
            button.scripts[0][2] = 0;
            button.interfaceConfig = true;
            configId++;
        }

        rsComponent.addChild(24795, 13, 20);
        rsComponent.addChild(24796, 474, 30);
        rsComponent.addChild(24797, 474, 30);
        rsComponent.addChild(24799, 254, 30);
        rsComponent.addChild(24800, 9, 60);
        int xOffset = teleports.length > 10 ? 14 : 23;
        int textXOffset = teleports.length > 10 ? 40 : 49;
        int yOffset = 0;
        int textYOffset = 2;
        for (int buttonId = 24801; buttonId < (24801 + (teleports.length * 2)); buttonId++) {
            scrollComponent.addChild(buttonId++, xOffset, yOffset);
            scrollComponent.addChild(buttonId, textXOffset, textYOffset);
            yOffset += 26;
            textYOffset += 26;
        }

        // Buttons start
        Sprite button = ImageLoader.forName("TELEPORT_BUTTON");
        Sprite buttonHover = ImageLoader.forName("TELEPORT_BUTTON_HOVER");
        int index = 0;
        for (int buttonId = 24823; buttonId < (24823 + 60); buttonId++) {
            rsComponent.addText(buttonId, buttonId + "", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, -1, -1, -1);
            rsComponent.addHoverButton(buttonId + 1, button, 109, 43, "Teleport", -1, buttonId + 2,
                    RSComponent.BUTTON_ACTION_TYPE);
            rsComponent.addHoveredButton(buttonId + 2, buttonHover, 109, 43, buttonId + 4);
            index++;
            buttonId += 4;
        }
        int textX = 197 + 1;
        int textY = 79;
        int buttonX = 143 + 1;
        int buttonY = 65;
        index = 0;
        for (int buttonId = 24823; buttonId < (24823 + 60); buttonId++) {
            rsComponent.addChild(buttonId + 1, buttonX, buttonY);
            rsComponent.addChild(buttonId + 2, buttonX, buttonY);
            rsComponent.addChild(buttonId, textX, textY);
            textX += 112 + 1;
            buttonX += 112 + 1;
            buttonId += 4;
            if ((++index % 3) == 0) {
                textX = 197 + 1;
                textY += 60;
                buttonX = 143 + 1;
                buttonY += 60;
            }
        }
        return rsComponent;
    }
}

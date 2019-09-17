package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the Graphics settings {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Vincent M. <admin@Gielinor.org>
 */
public class GraphicSettingsInterfacePlugin implements InterfacePlugin {
    /**
     * The options for the settings screen.
     */
    private final String[][] OPTIONS = new String[][]{
            {"HITICON", "Hit icons", "Toggles damage hit icons on or off", "Hit icons"},
            {"HEALTH", "Health Bars", "Toggles new health bars on or off", "Health bars"},
            {"TWEENING", "Tweening", "Toggles animation tweening on or off", "Tweening"},
            {"GAMEFRAME", "Gameframe", "Sets the current gameframe revision", "Gameframe"},
            {"HITMARKS", "New Hitsplats", "Toggles new hitsplats on or off", "Hitsplats"},
            {"CURSOR", "Cursors", "Toggles new cursors on or off", "Cursors"},
            {"HD", "High Detail Textures", "Toggles high detail textures on or off", "HD textures"},
            {"FOG", "Fog", "Toggles fog on or off", "Fog"},
            {"CONTEXT", "Context Menu", "Toggles the new context menu on or off", "Context menu"},
    };

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(23580);
        rsComponent.addSprite(23581, ImageLoader.forName("GRAPHIC_SETTINGS_BACK"));
        rsComponent.addHoverButton(23582, SpriteRepository.BIG_X, 21, 21, "Close", 0, 23583, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(23583, SpriteRepository.BIG_X_HOVER, 21, 21, 25384);
        rsComponent.addText(23585, "Graphics Settings", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 200, 14, -1);
        rsComponent.addConfigHover(23586, RSComponent.BUTTON_ACTION_TYPE, 23587, new SpriteSet(ImageLoader.forName("FIXED_MODE_ON"), ImageLoader.forName("FIXED_MODE")), 54, 46, 100, 1, "Fixed", 23588, 23589, null, null, 0, 0, true, "The game client is fixed to a\\nstatic size.");
        rsComponent.addConfigHover(23590, RSComponent.BUTTON_ACTION_TYPE, 23591, new SpriteSet(ImageLoader.forName("RESIZE_MODE_ON"), ImageLoader.forName("RESIZE_MODE")), 54, 46, 101, 1, "Resizable", 23592, 23593, null, null, 0, 0, true, "The game client expands to fill\\nthe window.");
        rsComponent.addConfigHover(23594, RSComponent.BUTTON_ACTION_TYPE, 23595, new SpriteSet(ImageLoader.forName("FULL_MODE_ON"), ImageLoader.forName("FULL_MODE")), 54, 46, 102, 1, "Full screen", 23596, 23597, null, null, 0, 0, true, "The game client fills the entire\\nmonitor.");

        int buttonId = 24883;
        int configId = 238;
        for (String[] option : OPTIONS) {
            String spriteName = option[0];
            String tooltip = option[1];
            String popupString = option[2];
            String message = option[3];
            RSComponent button = rsComponent.addActionButton(buttonId++, new SpriteSet(ImageLoader.forName(spriteName + "_ON"), ImageLoader.forName(spriteName + "_OFF")), 40, 40, tooltip, -1, popupString);
            button.optionType = 4;
            button.automaticConfig = false;
            addConfig(button, configId);
            RSComponent text = rsComponent.addText(buttonId++, message, rsFonts, RSComponent.REGULAR, (spriteName.equals("GAMEFRAME") ? 0xFF981F : 0x8F8F8F), 0xFF981F, false, true, 90, 25, -1);
            addConfig(text, configId++);
        }
        rsComponent.addText(24901, "474", rsFonts, RSComponent.FANCY, 0xFFDF00, true, true, 50, 16, -1);
        rsComponent.addChild(23581, 48, 18); // was 100 - 82
        rsComponent.addChild(23582, 436, 25); // 107
        rsComponent.addChild(23583, 436, 25); // 107
        rsComponent.addChild(23585, 155, 27);
        rsComponent.addChild(23586, 109, 66);
        rsComponent.addChild(23590, 229, 66);
        rsComponent.addChild(23594, 349, 66);
        // new children
        buttonId = 24883;
        int xTextOffset = 112;
        int yTextOffset = 146;
        int index = 0;
        for (int i = 0; i < OPTIONS.length; i++) {
            rsComponent.addChild(buttonId, xTextOffset - 46, yTextOffset - 12); // button
            rsComponent.addChild(buttonId + 1, xTextOffset, yTextOffset); // text
            xTextOffset += 133;
            if (++index % 3 == 0) {
                xTextOffset = 112;
                yTextOffset += 42;
            }
            buttonId += 2;
        }
        rsComponent.addChild(24901, 60, 186);
        return rsComponent;
    }

    /**
     * Adds a configuration.
     *
     * @param rsComponent The {@link com.runescape.cache.media.RSComponent}.
     * @param configId    The configuration id.
     */
    public RSComponent addConfig(RSComponent rsComponent, int configId) {
        rsComponent.scriptOperators = new int[1];
        rsComponent.scriptDefaults = new int[1];
        rsComponent.scriptOperators[0] = 1;
        rsComponent.scriptDefaults[0] = 1;
        rsComponent.scripts = new int[1][3];
        rsComponent.scripts[0][0] = 5;
        rsComponent.scripts[0][1] = configId;
        rsComponent.scripts[0][2] = 0;
        rsComponent.interfaceConfig = true;
        return rsComponent;
    }
}

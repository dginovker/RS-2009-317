package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the advanced options {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Vincent M. <admin@Gielinor.org>
 */
public class AdvancedOptionsInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(26387);
        rsComponent.addSprite(26388, ImageLoader.forName("ADVANCED_OPTIONS_BACK"));
        rsComponent.addHoverButton(26389, SpriteRepository.BIG_X, 21, 21, "Close", 0, 26390, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(26390, SpriteRepository.BIG_X_HOVER, 21, 21, 25391);
        rsComponent.addText(26392, "Advanced Options", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 200, 14, -1);
        RSComponent transparentChatbox = rsComponent.addHoverText(26413, "Transparent chatbox...", rsFonts, RSComponent.REGULAR, 0xFF981F, 0xFFB82F, false, true, 170, "Click through chatbox");
        transparentChatbox.height = 36;
        transparentChatbox.popupString = "In resizable mode, if the chatbox is transparent, should it be possible to click through the\\nchatbox on the ground beneath?";
        RSComponent clickThrough =
                rsComponent.addHoverText(26418, "Can be clicked through.", rsFonts, RSComponent.SMALL, 0xFF981F, 0xFF981F, false, true, 0);
        clickThrough.enabledColor = 0xFF981F;
        clickThrough.addSprite(26414, new SpriteSet(ImageLoader.forName("CHECKMARK_4_ON"), ImageLoader.forName("CHECKMARK_4_X"))).addInterfaceConfig(234, 1);
        clickThrough.height = 36;
        clickThrough.addInterfaceConfig(234, 1);
        rsComponent.addConfigHover(26419, 4, 26420, new SpriteSet(ImageLoader.forName("SIDE_PANELS_ON"), ImageLoader.forName("SIDE_PANELS")), 40, 40, 109, 1, "Side-stones arrangement", 26421, 26422, null, null, 0, 0, true, "Resizable mode stone buttons:\\n'Bottom Line'\\nThe hotkeys will not close the side-panels.");
        RSComponent sidePanels = rsComponent.addHoverText(26423, "Side-panels...", rsFonts, RSComponent.REGULAR, 0xFF981F, 0xFFB82F, false, true, 170, "Hotkeys Behaviour");
        sidePanels.height = 36;
        sidePanels.enabledColor = 0xFF981F;
        sidePanels.popupString = "In resizable mode, if the stone buttons are arranged along the bottom of the screen,\\nshould it be possible to shut a side-panel by pressing its hotkey?";
        RSComponent hotkey = rsComponent.addSprite(26424,
                new SpriteSet(ImageLoader.forName("CHECKMARK_4_ON"), ImageLoader.forName("CHECKMARK_4_X")));
        hotkey.addInterfaceConfig(110, 1);
        RSComponent hotkeys = rsComponent.addHoverText(26425, "Can be closed by the hotkeys.", rsFonts, RSComponent.SMALL, 0xFF981F, 0xFFB82F, false, true, 0);
        hotkeys.height = 36;
        hotkeys.enabledColor = 0xFF981F;
        rsComponent.addConfigHover(26393, 4, 26394, new SpriteSet(ImageLoader.forName("SIDE_PANEL_TRANSPARENT_ON"), ImageLoader.forName("SIDE_PANEL_TRANSPARENT")), 40, 40, 103, 1, "Transparent side-panel", 26395, 26396, null, null, 0, 0, true, "Resizable mode side-panel:\\nOpaque");
        rsComponent.addConfigHover(26397, 4, 26398, new SpriteSet(ImageLoader.forName("XP_REMAINING_ON"), ImageLoader.forName("XP_REMAINING")), 40, 40, 104, 1, "'Remaining XP' tooltips", 26399, 26400, null, null, 0, 0, true, "Stats panel shows XP to next level (currently off)");
        rsComponent.addConfigHover(26401, 4, 26402, new SpriteSet(ImageLoader.forName("ROOF_REMOVAL_ON"), ImageLoader.forName("ROOF_REMOVAL")), 40, 40, 105, 1, "Roof-removal", 26403, 26404, null, null, 0, 0, true, "Always hide roofs (currently off)");
        rsComponent.addConfigHover(26405, 4, 26406, new SpriteSet(ImageLoader.forName("DATA_ORBS_ON"), ImageLoader.forName("DATA_ORBS")), 40, 40, 106, 1, "Data orbs", 26407, 26408, null, null, 0, 0, true, "Data orbs (currently off)");
        rsComponent.addConfigHover(26409, 4, 26410, new SpriteSet(ImageLoader.forName("TRANSPARENT_CHAT_ON"), ImageLoader.forName("TRANSPARENT_CHAT")), 40, 40, 107, 1, "Transparent Chatbox", 26411, 26412, null, null, 0, 0, true, "Resizable mode chatbox:\\nOpaque");
        rsComponent.addChild(26388, 134, 67);
        rsComponent.addChild(26389, 350, 74);
        rsComponent.addChild(26390, 350, 74);
        rsComponent.addChild(26392, 155, 77);
        rsComponent.addChild(26414, 190, 184);
        rsComponent.addChild(26418, 209, 185);
        rsComponent.addChild(26419, 144, 217);
        rsComponent.addChild(26423, 189, 219);
        rsComponent.addChild(26413, 189, 164);
        rsComponent.addChild(26397, 205, 107);
        rsComponent.addChild(26401, 267, 107);
        rsComponent.addChild(26405, 328, 107);
        rsComponent.addChild(26409, 144, 162);
        rsComponent.addChild(26393, 144, 107);
        rsComponent.addChild(26424, 190, 239);
        rsComponent.addChild(26425, 209, 240);
        return rsComponent;
    }
}

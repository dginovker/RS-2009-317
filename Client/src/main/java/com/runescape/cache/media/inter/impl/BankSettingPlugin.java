package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the bank setting interface plugin.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class BankSettingPlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25682);
        rsComponent.addSprite(25683, ImageLoader.forName("BANK_SETTING_BACK"));
        rsComponent.addHoverButton(25684, SpriteRepository.BIG_X, 21, 21, "Close", 0, 25685, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25685, SpriteRepository.BIG_X_HOVER, 21, 21, 25686);
        rsComponent.addText(25687, "Bank settings menu", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 151, 14, -1);
        rsComponent.addButton(25688, new SpriteSet(ImageLoader.forName("miscgraphics3_14"), ImageLoader.forName("miscgraphics3_14")), 25, 25, "Dismiss menu", 1);
        rsComponent.addSprite(25689, ImageLoader.forName("BANK_SETTINGS"));
        rsComponent.addBorder(25690, 278, 75, false, 0, 0x2E2B23, false);
        rsComponent.addBorder(25691, 276, 73, false, 0, 0x726451, false);
        rsComponent.addText(25692, "Tab display:", gameFonts, RSComponent.BOLD, 0xFF981F, false, true, 272, 14, -1);
        RSComponent rsComponent1 = rsComponent.addConfigButton(25693, new SpriteSet(ImageLoader.forName("CHECKMARK_1_OFF"), ImageLoader.forName("CHECKMARK_1_ON")), 21, 15, null, 0, InterfaceConfiguration.BANK_ICON_TYPE.getId(), 25694, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.interfaceConfig = true;
        rsComponent1.automaticConfig = false;
        rsComponent1.tooltip = "First item";
        rsComponent.addHoverText(25694, "First item in tab", gameFonts, RSComponent.REGULAR, 0xFF981F, false, true, 250, "First item");
        rsComponent1 = rsComponent.addConfigButton(25695, new SpriteSet(ImageLoader.forName("CHECKMARK_1_OFF"), ImageLoader.forName("CHECKMARK_1_ON")), 21, 15, null, 0, InterfaceConfiguration.BANK_NUMERAL_TYPE.getId(), 25696, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.interfaceConfig = true;
        rsComponent1.automaticConfig = false;
        rsComponent1.tooltip = "Digit";
        rsComponent.addHoverText(25696, "Digit (1, 2, 3)", gameFonts, RSComponent.REGULAR, 0xFF981F, false, true, 250, "Digit");
        rsComponent1 = rsComponent.addConfigButton(25697, new SpriteSet(ImageLoader.forName("CHECKMARK_1_OFF"), ImageLoader.forName("CHECKMARK_1_ON")), 21, 15, null, 0, InterfaceConfiguration.BANK_ROMAN_TYPE.getId(), 25698, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.interfaceConfig = true;
        rsComponent1.automaticConfig = false;
        rsComponent1.tooltip = "Roman numeral";
        rsComponent.addHoverText(25698, "Roman numeral (I, II, III)", gameFonts, RSComponent.REGULAR, 0xFF981F, false, true, 250, "Roman numeral");
        rsComponent.addChild(25683, 12, 2);
        rsComponent.addChild(25684, 472, 9);
        rsComponent.addChild(25685, 472, 9);
        rsComponent.addChild(25687, 180, 12);
        rsComponent.addChild(BankInterfacePlugin.BANK_INTERFACE_ID + 53, 30, 8);
        rsComponent.addChild(BankInterfacePlugin.BANK_INTERFACE_ID + 54, 24, 19);
        rsComponent.addChild(BankInterfacePlugin.BANK_INTERFACE_ID + 55, 30, 20);
        rsComponent.addChild(25688, 463, 44);
        rsComponent.addChild(25689, 463, 44);
        rsComponent.addChild(25690, 117, 87);
        rsComponent.addChild(25691, 118, 88);
        rsComponent.addChild(25692, 121, 91);
        rsComponent.addChild(25693, 122, 109);
        rsComponent.addChild(25694, 142, 108);
        rsComponent.addChild(25695, 122, 126);
        rsComponent.addChild(25696, 142, 125);
        rsComponent.addChild(25697, 122, 143);
        rsComponent.addChild(25698, 142, 142);
        return rsComponent;
    }
}

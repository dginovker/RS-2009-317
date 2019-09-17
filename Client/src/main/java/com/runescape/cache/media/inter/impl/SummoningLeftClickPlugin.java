package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the interface plugin for the Summoning left click option.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class SummoningLeftClickPlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addTabInterface(25951, 249, 335);
        rsComponent.addSprite(25952, ImageLoader.forName("summoning-left-click-header"));
        rsComponent.addText(25953, "Left-click option:", gameFonts, RSComponent.BOLD, 0xFFCC00, true, true, 161, 15, -1);
        rsComponent.addButton(25954,
                new SpriteSet(ImageLoader.forName("summoning-left-click-on"), ImageLoader.forName("summoning-left-click-off")), 22, 22,
                "Follower details", RSComponent.BUTTON_ACTION_TYPE, 25955)
                .addInterfaceConfig(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION.getId(), 1);
        rsComponent.addHoverText(25955, "Follower details", gameFonts, RSComponent.REGULAR, 0xFF9833, 0xFFFFFF, false, true, 130, "Follower details");
        rsComponent.addButton(25956,
                new SpriteSet(ImageLoader.forName("summoning-left-click-on"), ImageLoader.forName("summoning-left-click-off")), 22, 22,
                "Special attack", RSComponent.BUTTON_ACTION_TYPE, 25957)
                .addInterfaceConfig(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION.getId(), 2);
        rsComponent.addHoverText(25957, "Special attack", gameFonts, RSComponent.REGULAR, 0xFF9833, 0xFFFFFF, false, true, 130, "Special attack");
        rsComponent.addButton(25958,
                new SpriteSet(ImageLoader.forName("summoning-left-click-on"), ImageLoader.forName("summoning-left-click-off")), 22, 22,
                "Attack", RSComponent.BUTTON_ACTION_TYPE, 25959)
                .addInterfaceConfig(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION.getId(), 3);
        rsComponent.addHoverText(25959, "Attack", gameFonts, RSComponent.REGULAR, 0xFF9833, 0xFFFFFF, false, true, 130, "Attack");
        rsComponent.addButton(25960,
                new SpriteSet(ImageLoader.forName("summoning-left-click-on"), ImageLoader.forName("summoning-left-click-off")), 22, 22,
                "Call follower", RSComponent.BUTTON_ACTION_TYPE, 25961)
                .addInterfaceConfig(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION.getId(), 4);
        rsComponent.addHoverText(25961, "Call follower", gameFonts, RSComponent.REGULAR, 0xFF9833, 0xFFFFFF, false, true, 130, "Call follower");
        rsComponent.addButton(25962,
                new SpriteSet(ImageLoader.forName("summoning-left-click-on"), ImageLoader.forName("summoning-left-click-off")), 22, 22,
                "Dismiss follower", RSComponent.BUTTON_ACTION_TYPE, 25963)
                .addInterfaceConfig(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION.getId(), 5);
        rsComponent.addHoverText(25963, "Dismiss follower", gameFonts, RSComponent.REGULAR, 0xFF9833, 0xFFFFFF, false, true, 130, "Dismiss follower");
        rsComponent.addButton(25964,
                new SpriteSet(ImageLoader.forName("summoning-left-click-on"), ImageLoader.forName("summoning-left-click-off")), 22, 22,
                "Take BoB", RSComponent.BUTTON_ACTION_TYPE, 25965)
                .addInterfaceConfig(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION.getId(), 6);
        rsComponent.addHoverText(25965, "Take BoB", gameFonts, RSComponent.REGULAR, 0xFF9833, 0xFFFFFF, false, true, 130, "Take BoB");
        rsComponent.addButton(25966,
                new SpriteSet(ImageLoader.forName("summoning-left-click-on"), ImageLoader.forName("summoning-left-click-off")), 22, 22,
                "Renew familiar", RSComponent.BUTTON_ACTION_TYPE, 25967)
                .addInterfaceConfig(InterfaceConfiguration.SUMMONING_LEFT_CLICK_OPTION.getId(), 7);
        rsComponent.addHoverText(25967, "Renew familiar", gameFonts, RSComponent.REGULAR, 0xFF9833, 0xFFFFFF, false, true, 130, "Renew familiar");
        rsComponent.addHoverButton(25968, ImageLoader.forName("summoning-confirm"), 114, 20, "Confirm", 0, 25969, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(25969, ImageLoader.forName("summoning-confirm-hover"), 114, 20, 25970);
        rsComponent.addText(25971, "Confirm", gameFonts, RSComponent.BOLD, 0xFFCC00, true, true, 114, -1);
        rsComponent.addChild(25952, 12, 7);
        rsComponent.addChild(25953, 14, 13);
        int buttonId = 25954;
        int yOffset = 0;
        for (int index = 0; index < 7; index++) {
            rsComponent.addChild(buttonId++, 12, 41 + yOffset);
            rsComponent.addChild(buttonId++, 43, 46 + yOffset);
            yOffset += 26;
        }
        rsComponent.addChild(25968, 38, 230);
        rsComponent.addChild(25969, 38, 230);
        rsComponent.addChild(25971, 38, 232);
        return rsComponent;
    }

}

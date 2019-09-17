package com.runescape.cache.media.inter.impl;

import com.runescape.Game;
import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the Summoning tab {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class SummoningTabPlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addTabInterface(25904, 249, 335);
        rsComponent.addSprite(25905, ImageLoader.forName("summoning-tab-npc"));
        rsComponent.addNPCModel(25906);
        rsComponent.addSprite(25907, new SpriteSet(ImageLoader.forName("familiar-special-move-back"), SpriteRepository.EMPTY), 1, InterfaceConfiguration.SUMMONING_SPECIAL_MOVE.getId(), true);
        scrollConfiguration(rsComponent.addText(25915, "%1", gameFonts, RSComponent.SMALL, 0xFF9933, true, true, 32, 12, -1));
        rsComponent.addHoverBox(25916, "Scrolls remaining", 32, 12, true).addInterfaceConfig(InterfaceConfiguration.SUMMONING_SPECIAL_MOVE.getId(), 1);
        rsComponent.addSprite(25917, ImageLoader.forName("familiar-name"));
        rsComponent.addText(25918, "Spirit wolf", gameFonts, RSComponent.BOLD, 0xCC9900, true, false, 160, 15, -1);
        rsComponent.addSprite(25919, ImageLoader.forName("summoning-details-back"));
        rsComponent.addSprite(25920, new SpriteSet(ImageLoader.forName("summoning-tab-dish"), SpriteRepository.EMPTY), 1, InterfaceConfiguration.SUMMONING_PET.getId(), false, 23, 12, true);
        rsComponent.addSprite(25921, new SpriteSet(ImageLoader.forName("summoning-tab-hunger"), SpriteRepository.EMPTY), 1, InterfaceConfiguration.SUMMONING_PET.getId(), false, 25, 24, true);
        rsComponent.addSprite(25922, new SpriteSet(ImageLoader.forName("summoning-tab-level"), SpriteRepository.EMPTY), 2, InterfaceConfiguration.SUMMONING_PET.getId(), false, 24, 25, true);
        rsComponent.addSprite(25923, new SpriteSet(ImageLoader.forName("summoning-tab-time"), SpriteRepository.EMPTY), 2, InterfaceConfiguration.SUMMONING_PET.getId(), false, 17, 25, true);
        rsComponent.addText(25924, "%1/%2", gameFonts, RSComponent.SMALL, 0xC4B074, true, true, 34, 9, -1).scripts = new int[][]{
                {1, 23, 0},
                {2, 23, 0}
        };
        rsComponent.addText(25925, "1.00", gameFonts, RSComponent.SMALL, 0xC4B074, true, true, 30, 9, -1);
        rsComponent.addHoverButton(25928, ImageLoader.forName("call-familiar-button"), 38, 38, "Call Follower", 0, 25929, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(25929, ImageLoader.forName("call-familiar-button-hover"), 38, 38, 25930);
        rsComponent.addHoverButton(25931, ImageLoader.forName("take-bob-button"), 38, 38, "Take BoB", 0, 25932, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(25932, ImageLoader.forName("take-bob-button-hover"), 38, 38, 25933);
        rsComponent.addHoverButton(25934, ImageLoader.forName("dismiss-familiar-button"), 38, 38, "Dismiss familiar", 0, 25935, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(25935, ImageLoader.forName("dismiss-familiar-button-hover"), 38, 38, 25936);
        rsComponent.addHoverBox(25937, "Call familiar", 38, 38, true);
        rsComponent.addHoverBox(25938, "Take Beast of Burden\\nitems", 38, 38, true);
        rsComponent.addHoverBox(25939, "Dismiss familiar", 38, 38, true);
        rsComponent.addHoverBox(25940, "Summoning points\\nremaining", 34, 42, true);
        rsComponent.addHoverBox(25941, "Familiar time\\nremaining", 34, 42, true);
        rsComponent.drawRequirementBox(25942, 12425, 1, 1, "Howl", "Makes NPCs flee for a short time", gameFonts,
                new SpriteSet(ImageLoader.forName("familiar-special-on"), ImageLoader.forName("familiar-special-off")), 14, 2);

        rsComponent.addSprite(25973, Game.INSTANCE.getSummoning().getSpecialMoveBar(false));
        rsComponent.addSprite(25974, Game.INSTANCE.getSummoning().getSpecialMoveBar(true));
        rsComponent.addHoverBox(25975, "60/60 special move\\npoints remaining", 142, 12, true);
        rsComponent.addText(25976, "S P E C I A L   M O V E", gameFonts, RSComponent.SMALL, 0xFFFFCC, true, false, 142, 8, -1);

        rsComponent.addChild(25905, 5, 25);
        rsComponent.addChild(25906, 88, 68);
        rsComponent.addChild(25907, 5, 25);
        rsComponent.addChild(25915, 6, 62);
        rsComponent.addChild(25916, 8, 61);
        rsComponent.addChild(25917, 13, 145);
        rsComponent.addChild(25918, 17, 146);
        rsComponent.addChild(25919, 12, 169);
        rsComponent.addChild(25920, 34, 180);
        rsComponent.addChild(25921, 128, 174);
        rsComponent.addChild(25922, 33, 173);
        rsComponent.addChild(25923, 138, 173);
        rsComponent.addChild(25924, 28, 201);
        rsComponent.addChild(25925, 132, 201);
        rsComponent.addChild(25928, 23, 218);
        rsComponent.addChild(25929, 23, 218);
        rsComponent.addChild(25931, 76, 218);
        rsComponent.addChild(25932, 76, 218);
        rsComponent.addChild(25934, 129, 218);
        rsComponent.addChild(25935, 129, 218);
        rsComponent.addChild(25937, 23, 218);
        rsComponent.addChild(25938, 76, 218);
        rsComponent.addChild(25939, 129, 218);
        rsComponent.addChild(25940, 25, 172);
        rsComponent.addChild(25941, 129, 172);
        rsComponent.addChild(25942, 8, 29);
        rsComponent.addChild(25943, 5, 163);
        rsComponent.addChild(25973, 24, 5);
        rsComponent.addChild(25974, 24, 5);
        rsComponent.addChild(25975, 24, 5);
        rsComponent.addChild(25976, 23, 7);
        return rsComponent;
    }

    public void scrollConfiguration(RSComponent rsComponent) {
        rsComponent.scripts = new int[][]{
                {4, 3214, 12425, 0},
        };
        rsComponent.interfaceConfig = true;
    }
}

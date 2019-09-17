package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the Wrench tab {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Vincent M. <admin@Gielinor.org>
 */
public class WrenchTabInterfacePlugin implements InterfacePlugin {

    /**
     * Binds for positions on the interface.
     */
    private static final int[][][] BINDS = new int[][][]{
            {
                    {31001, 3, 45, 0}, {31002, 6, 3, 1}, {31003, 6, 3, 2}, {31005, 52, 3, 3},
                    {31006, 52, 3, 4}, {31008, 98, 3, 5}, {31009, 98, 3, 6}, {31011, 144, 3, 7},
                    {31012, 144, 3, 8}, {31014, 6, 221, 9}, {31016, 6, 221, 10}, {31018, 52, 221, 11},
                    {31020, 52, 221, 12}, {31022, 98, 221, 13}, {31023, 144, 221, 14}, /*{31024, 23, 119, 15},*/
                    /*{31026, 23, 119, 16},*/ {31028, 24, 130, 17}, {31029, 24, 137, 18}, {31032, 24, 175, 19},
                    //31035, 31036, 31037, 31109, 31113, 31117
                    {31033, 24, 182, 20}, {149, 57, 241, 21}, {31035, 9, 89, 22}, {31036, 53, 97, 23},
                    {31037, 61, 98, 24}, {31039, 61, 98, 25}, {31109, 92, 98, 26}, {31111, 92, 98, 27},
                    {31113, 124, 98, 28}, {31115, 124, 98, 29}, {31117, 156, 98, 30}, {31119, 156, 98, 31}
            },
            {
                    {31001, 3, 45, 0}, {31041, 6, 3, 1}, {31042, 6, 3, 2}, {31044, 52, 3, 3},
                    {31045, 52, 3, 4}, {31008, 98, 3, 5}, {31009, 98, 3, 6}, {31011, 144, 3, 7},
                    {31012, 144, 3, 8}, {31014, 6, 221, 9}, {31016, 6, 221, 10}, {31018, 52, 221, 11},
                    {31020, 52, 221, 12}, {31022, 98, 221, 13}, {31023, 144, 221, 14}, {149, 57, 241, 15},
                    {31121, 12, 68, 16}, {31122, 12, 117, 17}, {31123, 12, 166, 18}, {31124, 51, 76, 19},
                    {31125, 51, 124, 20}, {31126, 51, 173, 21}, {31128, 59, 77, 22}, {31130, 59, 77, 23},
                    {31132, 90, 77, 24}, {31134, 90, 77, 25}, {31136, 121, 77, 26}, {31138, 121, 77, 27},
                    {31140, 152, 77, 28}, {31142, 152, 77, 29}, {31144, 59, 125, 30}, {31146, 59, 125, 31},
                    {31148, 90, 125, 32}, {31150, 90, 125, 33}, {31152, 121, 125, 34}, {31154, 121, 125, 35},
                    {31156, 152, 125, 36}, {31158, 152, 125, 37}, {31160, 59, 173, 38}, {31162, 59, 173, 39},
                    {31164, 90, 173, 40}, {31166, 90, 173, 41}, {31168, 121, 173, 42}, {31170, 121, 173, 43},
                    {31172, 152, 173, 44}, {31174, 152, 173, 45}
            },
            {
                    {31001, 3, 45, 0}, {31041, 6, 3, 1}, {31042, 6, 3, 2}, {31005, 52, 3, 3},
                    {31006, 52, 3, 4}, {31061, 98, 3, 5}, {31062, 98, 3, 6}, {31011, 144, 3, 7},
                    {31012, 144, 3, 8}, {31014, 6, 221, 9}, {31016, 6, 221, 10}, {31018, 52, 221, 11},
                    {31020, 52, 221, 12}, {31022, 98, 221, 13}, {31023, 144, 221, 14}, {149, 57, 241, 15},
                    {31064, 39, 81, 16}, {31066, 39, 81, 17}, {31068, 111, 81, 18}, {31070, 111, 81, 19},
                    {31072, 39, 144, 20}, {31074, 39, 144, 21}, {31076, 111, 144, 22}, {31078, 111, 144, 23}
            },
            {
                    {31001, 3, 45, 0}, {31041, 6, 3, 1}, {31042, 6, 3, 2}, {31005, 52, 3, 3},
                    {31006, 52, 3, 4}, {31008, 98, 3, 5}, {31009, 98, 3, 6}, {31081, 144, 3, 7},
                    {31082, 144, 3, 8}, {31014, 6, 221, 9}, {31016, 6, 221, 10}, {31018, 52, 221, 11},
                    {31020, 52, 221, 12}, {31022, 98, 221, 13}, {31023, 144, 221, 14}, {149, 57, 241, 15},
                    {31084, 40, 71, 16}, {31086, 40, 71, 17}, {31088, 110, 71, 18}, {31090, 110, 71, 19},
                    {31092, 9, 126, 20}, {31093, 10, 145, 21}, {31095, 10, 145, 22}, {31097, 10, 162, 23},
                    {31099, 10, 162, 24}, {31101, 10, 179, 25}, {31103, 10, 179, 26}, {31105, 26, 145, 27},
                    {31106, 26, 162, 28}, {31107, 26, 179, 29}
            }
    };

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(31000);
        rsComponent.addSprite(31001, ImageLoader.forName("WRENCH_TAB_BORDER"));
        rsComponent.addHoverButton(31002, ImageLoader.forName("DISPLAY_SELECTED"), 40, 40, "Display Settings", -1, 31003, 1);
        rsComponent.addHoveredButton(31003, ImageLoader.forName("DISPLAY_SELECTED_HOVER"), 40, 40, 31004);
        rsComponent.addHoverButton(31005, ImageLoader.forName("AUDIO_SETTINGS"), 40, 40, "Audio Settings", -1, 31006, 1);
        rsComponent.addHoveredButton(31006, ImageLoader.forName("AUDIO_SETTINGS_HOVER"), 40, 40, 31007);
        rsComponent.addHoverButton(31008, ImageLoader.forName("CHAT_SETTINGS"), 40, 40, "Chat Settings", -1, 31009, 1);
        rsComponent.addHoveredButton(31009, ImageLoader.forName("CHAT_SETTINGS_HOVER"), 40, 40, 31010);
        rsComponent.addHoverButton(31011, ImageLoader.forName("CONTROLS_SETTINGS"), 40, 40, "Controls Settings", -1, 31012, 1);
        rsComponent.addHoveredButton(31012, ImageLoader.forName("CONTROLS_SETTINGS_HOVER"), 40, 40, 31013);
        rsComponent.addConfigHover(31014, 4, 31015, new SpriteSet(ImageLoader.forName("ACCEPT_AID_ON"), ImageLoader.forName("ACCEPT_AID_OFF")), 40, 40, 304, 1, "Toggle Accept Aid", 31016, 31017, "", "", 12, 20);
        rsComponent.addConfigHover(31018, 4, 31019, new SpriteSet(ImageLoader.forName("RUN_ON"), ImageLoader.forName("RUN_OFF")), 40, 40, 173, 1, "Toggle Run", 31020, 31021, "", "", 12, 20);
        rsComponent.addButton(31022, ImageLoader.forName("HOUSE_ICON"), 40, 40, "Open House Options", 1);
        rsComponent.addButton(31023, ImageLoader.forName("BOND_ICON"), 40, 40, "View Membership Bonds", 1);
        //  rsComponent.addConfigHover(31024, 4, 31025, new SpriteSet(ImageLoader.forName("ORBS_ON"), ImageLoader.forName("ORBS_OFF")), 40, 40, 306, 1, "Toggle Data Orbs", 31026, 31027, "", "", 12, 20);
        //rsComponent.addConfigHover(31028, 4, 31029, new SpriteSet(ImageLoader.forName("ROOF_ON"), ImageLoader.forName("ROOF_OFF")), 40, 40, 307, 1, "Toggle roof-removal", 31030, 31031, "", "", 12, 20);
        // rsComponent.addConfigHover(31032, 4, 31033, new SpriteSet(ImageLoader.forName("XP_ON"), ImageLoader.forName("XP_OFF")), 40, 40, 308, 1, "Toggle 'Remaining XP'", 31034, 31035, "", "", 12, 20);
        rsComponent.addButton(31028, ImageLoader.forName("STONE_BUTTON_BACK"), 140, 30, "View Graphics Settings", RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoverText(31029, "Graphics settings", rsFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, true, true, 140, 30);
        rsComponent.addButton(31032, ImageLoader.forName("STONE_BUTTON_BACK"), 140, 30, "View Advanced Options", RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoverText(31033, "Advanced options", rsFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, true, true, 140, 30);
        rsComponent.addText(149, "", rsFonts, 1, 16750623, false, true, 0, 52);
        rsComponent.addSprite(31035, ImageLoader.forName("SUN_ICON"));//.popupString = "Adjust Screen Brightness";
        rsComponent.addSprite(31036, ImageLoader.forName("SETTING_BAR"));
        rsComponent.addConfigHover(31037, 4, 31038, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 166, 1, "Adjust Screen Brightness", 31039, 31108, "", "", 12, 20);
        rsComponent.addConfigHover(31109, 4, 31110, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 166, 2, "Adjust Screen Brightness", 31111, 31112, "", "", 12, 20);
        rsComponent.addConfigHover(31113, 4, 31114, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 166, 3, "Adjust Screen Brightness", 31115, 31116, "", "", 12, 20);
        rsComponent.addConfigHover(31117, 4, 31118, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 166, 4, "Adjust Screen Brightness", 31119, 31120, "", "", 12, 20);
        for (int[] bind : BINDS[0]) {
            rsComponent.addChild(bind[0], bind[1], bind[2]);
        }

        rsComponent = rsComponent.addInterface(31040);
        rsComponent.addHoverButton(31041, ImageLoader.forName("DISPLAY_SETTINGS"), 40, 40, "Display Settings", -1, 31042, 1);
        rsComponent.addHoveredButton(31042, ImageLoader.forName("DISPLAY_SETTINGS_HOVER"), 40, 40, 31043);
        rsComponent.addHoverButton(31044, ImageLoader.forName("AUDIO_SELECTED"), 40, 40, "Audio Settings", -1, 31045, 1);
        rsComponent.addHoveredButton(31045, ImageLoader.forName("AUDIO_SELECTED_HOVER"), 40, 40, 31046);
        rsComponent.addSprite(31121, ImageLoader.forName("MUSIC_ICON"));
        rsComponent.addSprite(31122, ImageLoader.forName("SOUND_ICON"));
        rsComponent.addSprite(31123, ImageLoader.forName("AREA_SOUND_ICON"));
        rsComponent.addSprite(31124, ImageLoader.forName("SETTING_BAR"));
        rsComponent.addSprite(31125, ImageLoader.forName("SETTING_BAR"));
        rsComponent.addSprite(31126, ImageLoader.forName("SETTING_BAR"));
        rsComponent.addConfigHover(31128, 4, 31129, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 168, 4, "Adjust Music Volume", 31130, 31131, "", "", 12, 20);
        rsComponent.addConfigHover(31132, 4, 31133, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 168, 3, "Adjust Music Volume", 31134, 31135, "", "", 12, 20);
        rsComponent.addConfigHover(31136, 4, 31137, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 168, 2, "Adjust Music Volume", 31138, 31139, "", "", 12, 20);
        rsComponent.addConfigHover(31140, 4, 31141, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 168, 1, "Adjust Music Volume", 31142, 31143, "", "", 12, 20);
        rsComponent.addConfigHover(31144, 4, 31145, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 169, 4, "Adjust Sound Effect Volume", 31146, 31147, "", "", 12, 20);
        rsComponent.addConfigHover(31148, 4, 31149, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 169, 3, "Adjust Sound Effect Volume", 31150, 31151, "", "", 12, 20);
        rsComponent.addConfigHover(31152, 4, 31153, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 169, 2, "Adjust Sound Effect Volume", 31154, 31155, "", "", 12, 20);
        rsComponent.addConfigHover(31156, 4, 31157, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 169, 1, "Adjust Sound Effect Volume", 31158, 31159, "", "", 12, 20);
        rsComponent.addConfigHover(31160, 4, 31161, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 872, 4, "Adjust Area Sound Effect Volume", 31162, 31163, "", "", 12, 20);
        rsComponent.addConfigHover(31164, 4, 31165, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 872, 3, "Adjust Area Sound Effect Volume", 31166, 31167, "", "", 12, 20);
        rsComponent.addConfigHover(31168, 4, 31169, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 872, 2, "Adjust Area Sound Effect Volume", 31170, 31171, "", "", 12, 20);
        rsComponent.addConfigHover(31172, 4, 31173, new SpriteSet(ImageLoader.forName("BAR_FILL"), ImageLoader.forName("EMPTY")), 16, 16, 872, 1, "Adjust Area Sound Effect Volume", 31174, 31175, "", "", 12, 20);
        for (int[] bind : BINDS[1]) {
            rsComponent.addChild(bind[0], bind[1], bind[2]);
        }

        rsComponent = rsComponent.addInterface(31060);
        rsComponent.addHoverButton(31061, ImageLoader.forName("CHAT_SELECTED"), 40, 40, "Chat Settings", -1, 31062, 1);
        rsComponent.addHoveredButton(31062, ImageLoader.forName("CHAT_SELECTED_HOVER"), 40, 40, 31063);
        rsComponent.addConfigHover(31064, 4, 31065, new SpriteSet(ImageLoader.forName("CHAT_EFFECTS_ON"), ImageLoader.forName("CHAT_EFFECTS_OFF")), 40, 40, 171, 0, "Toggle Chat Effects", 31066, 31067, "", "", 12, 20);
        rsComponent.addConfigHover(31068, 4, 31069, new SpriteSet(ImageLoader.forName("PRIVATE_CHAT_ON"), ImageLoader.forName("PRIVATE_CHAT_OFF")), 40, 40, 287, 1, "Toggle Split Chat", 31070, 31071, "", "", 12, 20);
        rsComponent.addConfigHover(31072, 4, 31073, new SpriteSet(ImageLoader.forName("PROFANITY_ON"), ImageLoader.forName("PROFANITY_OFF")), 40, 40, 311, 1, "Toggle Profanity Filter", 31074, 31075, "", "", 12, 20);
        rsComponent.addConfigHover(31076, 4, 31077, new SpriteSet(ImageLoader.forName("LOGOUT_NOTIFICATION_ON"), ImageLoader.forName("LOGOUT_NOTIFICATION_OFF")), 40, 40, 312, 1, "Toggle Login/Logout notification timeout", 31078, 31079, "", "", 12, 20);
        for (int[] bind : BINDS[2]) {
            rsComponent.addChild(bind[0], bind[1], bind[2]);
        }

        rsComponent = rsComponent.addInterface(31080);
        rsComponent.addHoverButton(31081, ImageLoader.forName("CONTROLS_SELECTED"), 40, 40, "Controls Settings", -1, 31082, 1);
        rsComponent.addHoveredButton(31082, ImageLoader.forName("CONTROLS_SELECTED_HOVER"), 40, 40, 31083);
        rsComponent.addConfigHover(31084, 4, 31085, new SpriteSet(ImageLoader.forName("MOUSE_OFF"), ImageLoader.forName("MOUSE_ON")), 40, 40, 313, 1, "Toggle number of mouse buttons", 31086, 31087, "", "", 12, 20);
        rsComponent.addConfigHover(31088, 4, 31089, new SpriteSet(ImageLoader.forName("MOUSE_CAMERA_ON"), ImageLoader.forName("MOUSE_CAMERA_OFF")), 40, 40, 314, 1, "Toggle Mouse Camera", 31090, 31091, "", "", 12, 20);
        rsComponent.addText(31092, "Attack option priority:", rsFonts, 1, 0xFF981F, false, true, 0, 52);
        rsComponent.addConfigHover(31093, 4, 31094, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 15, 15, 315, 1, "Auto", 31095, 31096, "", "", 12, 20, true, false);
        rsComponent.addConfigHover(31097, 4, 31098, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 15, 15, 315, 2, "Left-click ", 31099, 31100, "", "", 12, 20, true, false);
        rsComponent.addConfigHover(31101, 4, 31102, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 15, 15, 315, 3, "Right-click ", 31103, 31104, "", "", 12, 20, true, false);
        rsComponent.addText(31105, "Depends on combat levels.", rsFonts, 0, 0xFF981F, false, true, 150, 14);
        rsComponent.addText(31106, "Left-click where available.", rsFonts, 0, 0xFF981F, false, true, 150, 14);
        rsComponent.addText(31107, "Always right-click.", rsFonts, 0, 0xFF981F, false, true, 150, 14);
        for (int[] bind : BINDS[3]) {
            rsComponent.addChild(bind[0], bind[1], bind[2]);
        }
        return rsComponent;
    }
}

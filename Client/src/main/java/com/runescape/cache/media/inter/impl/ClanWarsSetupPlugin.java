package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the clan wars setup interface plugin.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ClanWarsSetupPlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(24126);
        rsComponent.addSprite(24127, ImageLoader.forName("clan-wars-setup-back"));
        rsComponent.addHoverButton(24128, SpriteRepository.BIG_X, 21, 21, "Close", 0, 24129, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(24129, SpriteRepository.BIG_X_HOVER, 21, 21, 24130);
        rsComponent.addText(24131, "Clan Wars Setup: Challenging xxxxxxxxxxxx", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 200, 14, -1);

        // Game end box
        rsComponent.addText(24132, "Game end", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 156, 14, -1);
        RSComponent scrollComponent = rsComponent.addInterface(24133);
        scrollComponent.height = 55;
        scrollComponent.width = 137;
        scrollComponent.scrollMax = 290;
        String[] gameEnds = new String[]{
                "Last team standing",
                "25 kills", "50 kills", "100 kills", "200 kills", "500 kills", "1,000 kills", "5,000 kills", "10,000 kills",
                "Most kills: 5 mins", "Most kills: 10 mins", "Most kills: 20 mins", "Most kills: 60 mins", "Most kills: 120 mins",
                "Oddskull: 100 points", "Oddskull: 300 points", "Oddskull: 500 points"
        };
        int id = 24134;
        int yOffset = 1;
        int configFrame = 1;
        String popupString;
        for (String gameEnd : gameEnds) {
            if (configFrame == 1) {
                popupString = "The battle ends when all members of a team have been defeated. Fighters may not join\\nor re-join the battle after it has begun.";
            } else if (configFrame > 1 && configFrame <= 9) {
                popupString = "The first team to score " + gameEnds[configFrame - 1] + " will win.\\nFighters may enter the battle at any time.";
            } else if (configFrame >= 10 && configFrame < 15) {
                popupString = "Most kills: The team with the most kills after " + gameEnds[configFrame - 1].replace("Most kills: ", "").replace(" mins", "") + " minutes will win.\\nFighters may enter the battle at any time.";
            } else if (configFrame >= 15 && configFrame < 18) {
                popupString = "The first team to hold the oddskull for " + gameEnds[configFrame - 1].replace("Oddskull: ", "") + " will win.";
            } else {
                popupString = "";
            }
            scrollComponent.addSprite(id, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), configFrame, 800, false, 15, 15, true).popupString = popupString;
            scrollComponent.addHoverText(id + 1, gameEnd, gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 120, gameEnds[configFrame - 1]).popupString = popupString;
            scrollComponent.addChild(id, 0, yOffset);
            scrollComponent.addChild(id + 1, 16, yOffset);
            yOffset += 17;
            id += 2;
            configFrame += 1;
        }
        // Arena box
        rsComponent.addText(24168, "Arena", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 156, 14, -1);
        scrollComponent = rsComponent.addInterface(24169);
        scrollComponent.height = 59;
        scrollComponent.width = 137;
        scrollComponent.scrollMax = 170;
        String[] arenas = new String[]{
                "Wasteland", "Plateau", "Sylvan Glade", "Forsaken Quarry", "Turrets", "Ghastly Swamp", "Northleach Quell", "Gridlock", "Ethereal", "Clan Cup Arena"
        };
        id = 24170;
        yOffset = 1;
        configFrame = 1;
        for (String arena : arenas) {
            scrollComponent.addSprite(id, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), configFrame, 801, false, 15, 15, true).popupString = arena;
            scrollComponent.addHoverText(id + 1, arena, gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 120, "Ok").popupString = arena;
            scrollComponent.addChild(id, 0, yOffset);
            scrollComponent.addChild(id + 1, 16, yOffset);
            yOffset += 17;
            id += 2;
            configFrame += 1;
        }
        // Miscellaneous box
        rsComponent.addText(24190, "Miscellaneous", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 156, 14, -1);
        rsComponent.addSprite(24191, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_4_X")), 1, 802, false, 15, 15, true).popupString = "If freezing is ignored, spells such as Entangle and Ice Barrage will not prevent their\\ntargets from moving. Damage dealth by the spells will be applied normally.";
        rsComponent.addHoverText(24192, "Ignore freezing", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 120, "Ok").popupString = "If freezing is ignored, spells such as Entangle and Ice Barrage will not prevent their\\ntargets from moving. Damage dealth by the spells will be applied normally.";
        rsComponent.addSprite(24193, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_4_X")), 1, 803, false, 15, 15, true).popupString = "In single-way combat areas, the PJ timer prevents players from being attacked for 10\\nsecs after they have been attacking someone-else.";
        rsComponent.addHoverText(24194, "PJ timer", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 120, "Ok").popupString = "In single-way combat areas, the PJ timer prevents players from being attacked for 10\\nsecs after they have been attacking someone-else.";
        rsComponent.addSprite(24195, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_4_X")), 1, 804, false, 15, 15, true).popupString = "If Single Spells is enabled, multi-target attacks (such as chinchompas) will hit only one\\ntarget, even in multi-way combat areas.";
        rsComponent.addHoverText(24196, "Single Spells", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 120, "Ok").popupString = "If Single Spells is enabled, multi-target attacks (such as chinchompas) will hit only one\\ntarget, even in multi-way combat areas.";
        rsComponent.addSprite(24197, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_4_X")), 1, 805, false, 15, 15, true).popupString = "Should the Trident of the Seas be able to cast its spell on players? Normally it works\\nonly against monsters.";
        rsComponent.addHoverText(24198, "Allow Trident in PvP", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 120, "Ok").popupString = "Should the Trident of the Seas be able to cast its spell on players? Normally it works\\nonly against monsters.";
        // Big box
        rsComponent.addSprite(24199, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 1, 806, false, 15, 15, true).popupString = "Melee combat is allowed.";
        rsComponent.addHoverText(24200, "Allowed", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "Melee combat is allowed.";
        rsComponent.addSprite(24201, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 2, 806, false, 15, 15, true).popupString = "Melee combat is disabled.";
        rsComponent.addHoverText(24202, "Disabled", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "Melee combat is disabled.";

        rsComponent.addSprite(24203, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 1, 807, false, 15, 15, true).popupString = "Ranging is allowed.";
        rsComponent.addHoverText(24204, "Allowed", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "Ranging is allowed.";
        rsComponent.addSprite(24205, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 2, 807, false, 15, 15, true).popupString = "Ranging is disabled.";
        rsComponent.addHoverText(24206, "Disabled", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "Ranging is disabled.";

        rsComponent.addSprite(24207, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 1, 808, false, 15, 15, true).popupString = "Food may be eaten during the battle.";
        rsComponent.addHoverText(24208, "Allowed", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Allowed").popupString = "Food may be eaten during the battle.";
        rsComponent.addSprite(24209, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 2, 808, false, 15, 15, true).popupString = "No food may be eaten during the battle.";
        rsComponent.addHoverText(24210, "Disabled", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Disabled").popupString = "No food may be eaten during the battle.";

        rsComponent.addSprite(24211, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 1, 809, false, 15, 15, true).popupString = "Drinks, such as potions, may be used during the battle.";
        rsComponent.addHoverText(24212, "Allowed", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Allowed").popupString = "Drinks, such as potions, may be used during the battle.";
        rsComponent.addSprite(24213, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 2, 809, false, 15, 15, true).popupString = "No drinks may be consumed during the battle.";
        rsComponent.addHoverText(24214, "Disabled", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Disabled").popupString = "No drinks may be consumed during the battle.";

        rsComponent.addSprite(24215, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 1, 810, false, 15, 15, true).popupString = "Special attacks are allowed.";
        rsComponent.addHoverText(24216, "Allowed", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "Special attacks are allowed.";
        rsComponent.addSprite(24217, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 2, 810, false, 15, 15, true).popupString = "The Staff of the Dead cannot use its special attack, but all other special attacks are\\nallowed.";
        rsComponent.addHoverText(24218, "No Staff of the Dead", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 150, "Ok").popupString = "The Staff of the Dead cannot use its special attack, but all other special attacks are\\nallowed.";
        rsComponent.addSprite(24219, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 3, 810, false, 15, 15, true).popupString = "Special attacks are forbidden.";
        rsComponent.addHoverText(24220, "Disabled", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "Special attacks are forbidden.";

        rsComponent.addSprite(24221, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 1, 811, false, 15, 15, true).popupString = "A team will lose the battle if it has no fighters in the arena.";
        rsComponent.addHoverText(24222, "Kill 'em all", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 85, "Ok").popupString = "A team will lose the battle if it has no fighters in the arena.";
        rsComponent.addSprite(24223, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 2, 811, false, 15, 15, true).popupString = "A team will lose the battle if it has 5 fighters or fewer in the arena.";
        rsComponent.addHoverText(24224, "Ignore 5", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 85, "Ok").popupString = "A team will lose the battle if it has 5 fighters or fewer in the arena.";

        rsComponent.addSprite(24225, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 1, 812, false, 15, 15, true).popupString = "All the spellbooks are allowed.";
        rsComponent.addHoverText(24226, "All spellbooks", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "All the spellbooks are allowed.";
        rsComponent.addSprite(24227, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 2, 812, false, 15, 15, true).popupString = "Only the standard spellbook is allowed.";
        rsComponent.addHoverText(24228, "Standard spells", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "Only the standard spellbook is allowed.";
        rsComponent.addSprite(24229, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 3, 812, false, 15, 15, true).popupString = "Only the Bind, Snare and Entangle spells are allowed.";
        rsComponent.addHoverText(24230, "Binding only", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "Only the Bind, Snare and Entangle spells are allowed.";
        rsComponent.addSprite(24231, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 4, 812, false, 15, 15, true).popupString = "No magical combat is allowed.";
        rsComponent.addHoverText(24232, "Disabled", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Ok").popupString = "No magical combat is allowed.";

        rsComponent.addSprite(24233, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 1, 813, false, 15, 15, true).popupString = "All prayers are allowed.";
        rsComponent.addHoverText(24234, "All allowed", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "All allowed").popupString = "All prayers are allowed.";
        rsComponent.addSprite(24235, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 2, 813, false, 15, 15, true).popupString = "Prayers that use an overhead icon are forbidden. Other prayers are allowed.";
        rsComponent.addHoverText(24236, "No overheads", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "No overheads").popupString = "Prayers that use an overhead icon are forbidden. Other prayers are allowed.";
        rsComponent.addSprite(24237, new SpriteSet(ImageLoader.forName("CHECKMARK_1_ON"), ImageLoader.forName("CHECKMARK_1_OFF")), 3, 813, false, 15, 15, true).popupString = "No prayers are allowed.";
        rsComponent.addHoverText(24238, "Disabled", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 70, "Disabled").popupString = "No prayers are allowed.";
        // End big box
        RSComponent rsComponent1 = rsComponent.addButton(24239, new SpriteSet(ImageLoader.forName("clan-wars-button-on"), ImageLoader.forName("clan-wars-button")), 94, 40, "Accept", RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.hoverType = 24240;
        rsComponent1.addInterfaceConfig(814, 1);
        rsComponent1 = scrollComponent.addHoverText(24240, "Accept", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 94, "Accept");
        rsComponent1.enabledMessage = "Waiting";
        rsComponent1.enabledColor = 0xFFFFFF;
        rsComponent1.textColor = 0xFF981F;
        rsComponent1.optionType = 0;
        rsComponent1.addInterfaceConfig(814, 1);

        scrollComponent.addText(24241, "Your opponent has made\\nchanges!\\n5", gameFonts, RSComponent.REGULAR, 0xFF981F, true, true, 165, -1);

        rsComponent.addChild(24127, 6, 6);
        rsComponent.addChild(24128, 478, 13);
        rsComponent.addChild(24129, 478, 13);
        rsComponent.addChild(24131, 156, 16);
        rsComponent.addChild(24132, 17, 50);
        rsComponent.addChild(24133, 20, 71);
        rsComponent.addChild(24168, 17, 136);
        rsComponent.addChild(24169, 20, 157);
        rsComponent.addChild(24190, 17, 226);
        rsComponent.addChild(24191, 20, 248);
        rsComponent.addChild(24192, 36, 248);
        rsComponent.addChild(24193, 20, 265);
        rsComponent.addChild(24194, 36, 265);
        rsComponent.addChild(24195, 20, 282);
        rsComponent.addChild(24196, 36, 282);
        rsComponent.addChild(24197, 20, 299);
        rsComponent.addChild(24198, 36, 299);
        rsComponent.addChild(24199, 184, 76);
        rsComponent.addChild(24200, 200, 76);
        rsComponent.addChild(24201, 184, 93);
        rsComponent.addChild(24202, 200, 93);
        rsComponent.addChild(24203, 276, 76);
        rsComponent.addChild(24204, 292, 76);
        rsComponent.addChild(24205, 276, 93);
        rsComponent.addChild(24206, 292, 93);
        rsComponent.addChild(24207, 184, 137);
        rsComponent.addChild(24208, 200, 137);
        rsComponent.addChild(24209, 184, 154);
        rsComponent.addChild(24210, 200, 154);
        rsComponent.addChild(24211, 276, 137);
        rsComponent.addChild(24212, 292, 137);
        rsComponent.addChild(24213, 276, 154);
        rsComponent.addChild(24214, 292, 154);
        rsComponent.addChild(24215, 184, 198);
        rsComponent.addChild(24216, 200, 198);
        rsComponent.addChild(24217, 184, 215);
        rsComponent.addChild(24218, 200, 215);
        rsComponent.addChild(24219, 184, 232);
        rsComponent.addChild(24220, 200, 232);
        rsComponent.addChild(24221, 183, 282);
        rsComponent.addChild(24222, 199, 282);
        rsComponent.addChild(24223, 183, 299);
        rsComponent.addChild(24224, 199, 299);
        rsComponent.addChild(24225, 368, 76);
        rsComponent.addChild(24226, 384, 76);
        rsComponent.addChild(24227, 368, 93);
        rsComponent.addChild(24228, 384, 93);
        rsComponent.addChild(24229, 368, 110);
        rsComponent.addChild(24230, 384, 110);
        rsComponent.addChild(24231, 368, 127);
        rsComponent.addChild(24232, 384, 127);
        rsComponent.addChild(24233, 368, 185);
        rsComponent.addChild(24234, 384, 185);
        rsComponent.addChild(24235, 368, 202);
        rsComponent.addChild(24236, 384, 202);
        rsComponent.addChild(24237, 368, 219);
        rsComponent.addChild(24238, 384, 219);
        rsComponent.addChild(24239, 352, 267);
        rsComponent.addChild(24240, 379, 280);
        rsComponent.addChild(24241, 317, 267);
        return clanWarsConfirm(gameFonts);
    }

    /**
     * The confirmation interface.
     *
     * @param gameFonts The {@link com.runescape.media.font.GameFont} variables.
     * @return The {@link com.runescape.cache.media.RSComponent}.
     */
    public RSComponent clanWarsConfirm(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(24242);
        rsComponent.addSprite(24243, ImageLoader.forName("clan-wars-confirm-back"));
        rsComponent.addHoverButton(24244, SpriteRepository.BIG_X, 21, 21, "Close", 0, 24245, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(24245, SpriteRepository.BIG_X_HOVER, 21, 21, 24246);
        rsComponent.addText(24247, "Clan Wars Setup: Challenging xxxxxxxxxxxx", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 200, 14, -1);
        RSComponent rsComponent1 = rsComponent.addInterface(24248, 315, 215);
        rsComponent1.scrollMax = 450;
        rsComponent1.addText(24249, "", gameFonts, RSComponent.REGULAR, 0xFF981F, true, true, 307, 440, -1);
        rsComponent1.addChild(24249, 5, 0);
        //24250
        rsComponent1 = rsComponent.addButton(24250, new SpriteSet(ImageLoader.forName("clan-wars-confirm-button-on"), ImageLoader.forName("clan-wars-confirm-button")), 100, 30, "Accept", RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.hoverType = 24251;
        rsComponent1.addInterfaceConfig(815, 1);
        rsComponent1 = rsComponent.addHoverText(24251, "  Okay", gameFonts, RSComponent.REGULAR, 0xFF981F, 0xFFFFFF, false, true, 110, "Okay");
        rsComponent1.enabledMessage = "Waiting";
        rsComponent1.enabledColor = 0x534A3E;
        rsComponent1.textColor = 0xFF981F;
        rsComponent1.optionType = 0;
        rsComponent1.addInterfaceConfig(815, 1);
        rsComponent1.addText(24252, "Check the\\noptions!", gameFonts, RSComponent.REGULAR, 0xFF0000, true, true, 54, -1);

        rsComponent.addChild(24243, 81, 17);
        rsComponent.addChild(24244, 403, 24);
        rsComponent.addChild(24245, 403, 24);
        rsComponent.addChild(24247, 155, 27);
        rsComponent.addChild(24248, 90, 57);
        rsComponent.addChild(24250, 206, 277);
        rsComponent.addChild(24251, 235, 284);
        rsComponent.addChild(24252, 228, 278);
        return rsComponent;
    }
}
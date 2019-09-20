package com.runescape.cache.media.inter.impl;

import main.java.com.runescape.Game;
import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for the Duel Arena option and conformation screens.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class DuelArenaInterfacePlugin implements InterfacePlugin {
    /**
     * Represents text for rules in a duel.
     */
    private static final String[][] DUEL_RULE_TEXT = new String[][]{
            {"No Ranged", "Neither player is allowed to use ranged\\nattacks."},
            {"No Melee", "Neither player is allowed to use melee\\nattacks."},
            {"No Magic", "Neither player is allowed to use magic\\nattacks."},
            {"No Sp. Atk", "Neither player will be allowed to use special\\nattacks."},
            {"Fun Weapons", "Both players will use a 'fun weapon', such\\as: flowers or rubber chicken."},
            {"No Forfeit", "Neither player is allowed to forfeit the\\n duel."},
            {"No Prayer", "Neither player will be allowed to use prayer."},
            {"No Drinks", "Neither player will be allowed to use drinks."},
            {"No Food", "Neither player will be allowed to use food."},
            {"No Movement", "Players stand next to each other and\\naren't allowed to move or use hold spells."},
            {"Obstacles", "The duel will be in an arena with obstacles."}
    };

    /**
     * Represents text and offsets for equipment rules in a duel.
     */
    private static final Object[][] DUEL_EQUIPMENT_RULES = new Object[][]{
            {242, 57, "Neither player will be allowed to wear items\\non their head."},
            {201, 96, "Neither player will be allowed to wear items\\non their back."},
            {242, 96, "Neither player will be allowed to wear items\\naround their neck."},
            {284, 96, "Neither player will be allowed to have items\\nin their quiver."},
            {186, 135, "Neither player will be allowed to use 2-\\nhanded items or items in their right hand."},
            {242, 135, "Neither player will be allowed to wear items on their torso."},
            {298, 135, "Neither player will be allowed to use 2-\\nhanded items or items in their left hand."},
            {242, 175, "Neither player will be allowed to wear items\\non their legs."},
            {186, 215, "Neither player will be allowed to wear items\\non their hands."},
            {242, 215, "Neither player will be allowed to wear items\\non their feet."},
            {298, 215, "Neither player will be allowed to wear items\\non their fingers."}
    };

    /**
     * Represents text for preset settings.
     */
    private static final String[][] PRESET_TEXT = new String[][]{
            {"Save as preset", "Save the current settings."},
            {"Load preset", "Load the saved preset settings."},
            {"Load last duel", "Load the previous duel settings."}
    };

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(48000);
        rsComponent.addSprite(48001, ImageLoader.forName("DUEL_ARENA_OPTION_BACK"));
        rsComponent.addText(48002, "Dueling with:", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 137, 0);
        rsComponent.addText(48003, "nameeeeeeeee", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 140, 0);
        rsComponent.addText(48004, "Opponent's combat level:", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 142, 0);
        rsComponent.addText(48005, "126", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 32, 0);
        rsComponent.addHoverButton(48006, SpriteRepository.SMALL_X, 16, 16, "Close", 0, 48007, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(48007, SpriteRepository.SMALL_X_HOVER, 16, 16, 48008);
        rsComponent.addText(48009, "Your stake:", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 32, 12, -1);
        rsComponent.addText(48010, "Opponent's stake:", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 32, 12, -1);
        int index;
        int childId = 48011;
        int xOffset = 29;
        int yOffset = 186;
        for (index = 0; index < DUEL_RULE_TEXT.length; index++) {
            rsComponent.addText(childId, DUEL_RULE_TEXT[index][0], rsFonts, RSComponent.REGULAR, 0xBF751D, false, true, 42, 0);
            rsComponent.addConfigHover((childId + 1), 4, (childId + 2), new SpriteSet(ImageLoader.forName("CHECKMARK_3_ON"), ImageLoader.forName("CHECKMARK_3_OFF")), 17 + Game.INSTANCE.regularFont.getTextWidth(DUEL_RULE_TEXT[index][0]), 16, (5 + index), 1, "Ok", (childId + 3), (childId + 4), null, null, xOffset, yOffset, true, DUEL_RULE_TEXT[index][1]);
            yOffset += 18;
            if (index == 6) {
                xOffset = 374;
                yOffset = 186;
            }
            childId += 5;
        }
        childId = 48066;
        for (index = 0; index < PRESET_TEXT.length; index++) {
            rsComponent.addText(childId, PRESET_TEXT[index][0], rsFonts, RSComponent.REGULAR, 0xBF751D, false, true, 42, 0);
            SpriteSet spriteSet = index == 0 ? new SpriteSet(ImageLoader.forName("SAVE_ICON"), ImageLoader.forName("SAVE_ICON")) :
                    new SpriteSet(ImageLoader.forName("LOAD_ICON"), ImageLoader.forName("LOAD_ICON"));
            rsComponent.addActionButton((childId + 1), spriteSet, 17 + Game.INSTANCE.regularFont.getTextWidth(PRESET_TEXT[index][0]), 16, (index == 0 ? "Save Settings" : index == 1 ? "Load Preset Settings" : "Load Previous Settings"), -1, PRESET_TEXT[index][1]);
            childId += 2;
        }
        rsComponent.addHoverButton(48169, SpriteRepository.ACCEPT_BUTTON, 76, 42, "Accept", 0, 48170, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(48170, SpriteRepository.ACCEPT_BUTTON_HOVER, 76, 42, 48171);
        rsComponent.addHoverText(48073, "Decline", rsFonts, RSComponent.REGULAR, 0x800000, 0xAB0000, true, false, 62, 28, "Decline");
        childId = 48074;
        for (index = 0; index < DUEL_EQUIPMENT_RULES.length; index++) {
            rsComponent.addConfigHover(childId, 4, (childId + 1), new SpriteSet(ImageLoader.forName("RED_CROSS_OUT"), ImageLoader.forName(SpriteSet.EMPTY_STRING)), 36, 35, (16 + index), 1, "Ok", (childId + 2), (childId + 3), null, null, (int) DUEL_EQUIPMENT_RULES[index][0], (int) DUEL_EQUIPMENT_RULES[index][1], true, (String) DUEL_EQUIPMENT_RULES[index][2]);
            childId += 3;
        }
        rsComponent.addText(48108, "Other player has accepted.", rsFonts, RSComponent.SMALL, 0xF80000, true, true, 135, 0);
        rsComponent.addChild(48001, 13, 20);
        rsComponent.addChild(48002, 28, 30);
        rsComponent.addChild(48003, 104, 30);
        rsComponent.addChild(48004, 192, 30);
        rsComponent.addChild(48005, 339, 30);
        rsComponent.addChild(48006, 474, 30);
        rsComponent.addChild(48007, 474, 30);
        rsComponent.addChild(48009, 23, 59);
        rsComponent.addChild(48010, 357, 59);

        childId = 48011;
        xOffset = 29;
        yOffset = 186;
        for (int i = 0; i < DUEL_RULE_TEXT.length; i++) {
            rsComponent.addChild((childId + 1), xOffset, yOffset);
            rsComponent.addChild(childId, xOffset + 18, yOffset);
            yOffset += 18;
            if (i == 6) {
                xOffset = 374;
                yOffset = 186;
            }
            childId += 5;

        }
        childId = 48066;
        yOffset = 258;
        for (int i = 0; i < PRESET_TEXT.length; i++) {
            rsComponent.addChild((childId + 1), 374, yOffset);
            rsComponent.addChild(childId, 392, yOffset);
            yOffset += 18;
            childId += 2;
        }
        rsComponent.addChild(48073, 268, 261);
        rsComponent.addChild(1688, 186, 95);        // TODO Remove "Examine" from equipment
        childId = 48074;
        for (Object[] DUEL_EQUIPMENT_RULE : DUEL_EQUIPMENT_RULES) {
            rsComponent.addChild(childId, (int) DUEL_EQUIPMENT_RULE[0], (int) DUEL_EQUIPMENT_RULE[1]);
            childId += 3;
        }
        rsComponent.addChild(48108, 191, 299);
        rsComponent.addChild(48109, 24, 75);
        rsComponent.addChild(48139, 342, 75);
        addStakeScrollbar(rsComponent, rsFonts, true);
        addOpponentStakeScrollbar(rsComponent, rsFonts, true);
        rsComponent.addChild(48169, 175, 249);
        rsComponent.addChild(48170, 175, 249);
        conformationScreen(rsFonts);
        return rsComponent;
    }

    public RSComponent conformationScreen(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(48172);
        rsComponent.addSprite(48173, ImageLoader.forName("DUEL_ARENA_CONFIRM_BACK"));
        rsComponent.addText(48174, "Dueling with:", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 137, 0);
        rsComponent.addText(48175, "nameeeeeeeee", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 140, 0);
        rsComponent.addText(48176, "Opponent's combat level:", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 142, 0);
        rsComponent.addText(48177, "126", rsFonts, RSComponent.REGULAR, 0xE1981F, false, true, 32, 0);
        rsComponent.addHoverButton(48178, SpriteRepository.SMALL_X, 16, 16, "Close", 0, 48179, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(48179, SpriteRepository.SMALL_X_HOVER, 16, 16, 48180);
        rsComponent.addText(48181, "Your stake:", rsFonts, RSComponent.REGULAR, 0xBF751D, false, true, 32, 12, -1);
        rsComponent.addText(48182, "Opponent's stake:", rsFonts, RSComponent.REGULAR, 0xBF751D, false, true, 32, 12, -1);
        rsComponent.addText(48183, "0 gp", rsFonts, RSComponent.REGULAR, 0xBF751D, false, true, 32, 12, -1);
        rsComponent.addText(48184, "0 gp", rsFonts, RSComponent.REGULAR, 0xBF751D, false, true, 32, 12, -1);
        rsComponent.addText(48185, "Before the duel starts:", rsFonts, RSComponent.REGULAR, 0xBF7500, false, true, 32, 12, -1);
        rsComponent.addText(48186, "Some worn items will be\\ntaken off.\\nBoosted stats will be\\nrestored.\\nExisting prayers will\\n be stopped.", rsFonts, RSComponent.SMALL, 0xAAAAAA, false, true, 144, 88, -1);
        rsComponent.addText(48187, "During the duel:", rsFonts, RSComponent.REGULAR, 0xBF7500, false, true, 32, 12, -1);
        rsComponent.addText(48188, "You cannot forfeit the duel.\\nYou cannot move.\\nYou cannot used Ranged\\nattacks.\\nYou cannot use Magic attacks.\\nYou cannot use special\\nattacks.\\nYou cannot use drinks.\\nYou cannot use food.\\nYou cannot use Prayer.\\nYou can't use 2H weapons\\nsuch as bows.", rsFonts, RSComponent.SMALL, 0xAAAAAA, false, true, 144, 88, -1);
        rsComponent.addHoverButton(48189, SpriteRepository.ACCEPT_BUTTON, 76, 42, "Accept", 0, 48190, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(48190, SpriteRepository.ACCEPT_BUTTON_HOVER, 76, 42, 48191);
        rsComponent.addHoverButton(48192, SpriteRepository.DECLINE_BUTTON, 76, 42, "Decline", 0, 48193, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(48193, SpriteRepository.DECLINE_BUTTON_HOVER, 76, 42, 48194);
        int childId = 48195;
        for (int index = 0; index < DUEL_EQUIPMENT_RULES.length; index++) {
            rsComponent.addSprites(childId, new SpriteSet(ImageLoader.forName("RED_CROSS_OUT"), ImageLoader.forName(SpriteSet.EMPTY_STRING)), 1, 16 + index, true);
            childId += 1;
        }

        rsComponent.addChild(48173, 13, 20);
        rsComponent.addChild(48174, 28, 30);
        rsComponent.addChild(48175, 104, 30);
        rsComponent.addChild(48176, 192, 30);
        rsComponent.addChild(48177, 339, 30);
        rsComponent.addChild(48178, 476, 28);
        rsComponent.addChild(48179, 476, 28);
        rsComponent.addChild(48181, 23, 54);
        rsComponent.addChild(48182, 23, 188);
        rsComponent.addChild(48183, 22, 165);
        rsComponent.addChild(48184, 22, 302);
        rsComponent.addChild(48185, 343, 55);
        rsComponent.addChild(48186, 344, 70);
        rsComponent.addChild(48187, 343, 157);
        rsComponent.addChild(48188, 344, 172);
        rsComponent.addChild(48189, 261, 275);
        rsComponent.addChild(48190, 261, 275);
        rsComponent.addChild(48192, 185, 275);
        rsComponent.addChild(48193, 185, 275);
        rsComponent.addChild(1688, 188, 93);        // TODO Remove "Examine" from equipment
        childId = 48195;
        for (Object[] DUEL_EQUIPMENT_RULE : DUEL_EQUIPMENT_RULES) {
            rsComponent.addChild(childId, (int) DUEL_EQUIPMENT_RULE[0] + 2, (int) DUEL_EQUIPMENT_RULE[1] - 1);
            childId += 1;
        }
        rsComponent.addChild(48206, 31, 69);
        rsComponent.addChild(48236, 31, 205);
        addStakeScrollbar(rsComponent, rsFonts, false);
        addOpponentStakeScrollbar(rsComponent, rsFonts, false);
        return rsComponent;
    }

    public RSComponent addStakeScrollbar(RSComponent rsComponent, GameFont[] rsFonts, boolean firstScreen) {
        int interfaceId = firstScreen ? 48109 : 48206;
        int childId = firstScreen ? 48110 : 48207;
        RSComponent stakeInterface = rsComponent.addTabInterface(interfaceId, 132, firstScreen ? 100 : 96);
        for (int index = 0; index <= 28; index++) {
            if (firstScreen) {
                stakeInterface.addToItemGroup((childId + index), 32, 32, 0, 0, true, true, firstScreen ? new String[]{"Remove-1", "Remove-5", "Remove-10", "Remove-All", "Remove-X"} : new String[5]);
            } else {
                rsComponent.addText((childId + index), "", rsFonts, RSComponent.SMALL, 0xFF9040, false, true, 135, 0);
            }
        }

        int xOffset = firstScreen ? 2 : 0;
        int yOffset = firstScreen ? 5 : 7;
        int counter = 0;
        for (int index = 0; index <= 28; index++) {
            if (counter == 4 && firstScreen) {
                xOffset = 2;
                yOffset += 36;
                counter = 0;
            }
            stakeInterface.addChild((childId + index), xOffset, yOffset);
            xOffset += firstScreen ? 33 : 0;
            yOffset += firstScreen ? 0 : 16;
            counter++;
        }

        stakeInterface.width = 132;
        stakeInterface.height = firstScreen ? 100 : 94;
        stakeInterface.scrollMax = 252;
        return stakeInterface;
    }

    public RSComponent addOpponentStakeScrollbar(RSComponent rsComponent, GameFont[] rsFonts, boolean firstScreen) {
        int interfaceId = firstScreen ? 48139 : 48236;
        int childId = firstScreen ? 48140 : 48237;
        RSComponent opponentStakeInterface = rsComponent.addTabInterface(interfaceId, 132, firstScreen ? 100 : 96);
        for (int index = 0; index <= 28; index++) {
            if (firstScreen) {
                opponentStakeInterface.addToItemGroup((childId + index), 32, 32, 0, 0, true, true);
            } else {
                rsComponent.addText((childId + index), "", rsFonts, RSComponent.SMALL, 0xFF9040, false, true, 135, 0);
            }
        }
        int xOffset = firstScreen ? 2 : 0;
        int yOffset = firstScreen ? 5 : 7;
        int counter = 0;
        for (int index = 0; index <= 28; index++) {
            if (counter == 4 && firstScreen) {
                xOffset = 2;
                yOffset += 36;
                counter = 0;
            }
            opponentStakeInterface.addChild((childId + index), xOffset, yOffset);
            xOffset += firstScreen ? 33 : 0;
            yOffset += firstScreen ? 0 : 16;
            counter++;
        }
        opponentStakeInterface.width = 132;
        opponentStakeInterface.height = firstScreen ? 100 : 94;
        opponentStakeInterface.scrollMax = 252;
        return opponentStakeInterface;
    }
}

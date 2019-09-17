package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.Sprite;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class SlayerRewardInterface implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        buildLearnComponent(rsFonts);
        buildAssignComponent(rsFonts);
        return buildBuyComponent(rsFonts);
    }

    /**
     * Builds the Buy component.
     *
     * @return The {@link com.runescape.cache.media.RSComponent}.
     */
    public RSComponent buildBuyComponent(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(24546);
        rsComponent.addSprite(24547, ImageLoader.forName("SLAYER_BUY_BACK"));
        rsComponent.addHoverButton(24548, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 24549, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(24549, SpriteRepository.SMALL_X_HOVER, 16, 16, 24550);
        rsComponent.addActionButton(24551, Sprite.NULL_TYPE, 86, 20, -1, "Learn").popupString = "Learn new abilities";
        rsComponent.addActionButton(24552, Sprite.NULL_TYPE, 86, 20, -1, "Assignments").popupString = "Manage Slayer\\nassignments";
        rsComponent.addActionButton(24553, Sprite.NULL_TYPE, 28, 28, -1, "Buy XP").popupString = "Buy 50,000 Slayer XP";
        rsComponent.addActionButton(24554, Sprite.NULL_TYPE, 28, 28, -1, "Buy Ring").popupString = "Buy a ring which enables\\nteleports to Slayer\\nlocations (8 charges).";
        rsComponent.addActionButton(24555, Sprite.NULL_TYPE, 48, 28, -1, "Buy Runes").popupString = "Buy runes for 250 casts\\nof Slayer Dart.";
        rsComponent.addActionButton(24556, Sprite.NULL_TYPE, 28, 28, -1, "Buy Bolts").popupString = "Buy 250 broad bolts.";
        rsComponent.addActionButton(24557, Sprite.NULL_TYPE, 28, 28, -1, "Buy Arrows").popupString = "Buy 250 broad arrows.";
        rsComponent.addHoverText(24558, "Buy Slayer XP", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 83, 12, "Buy XP").popupString = "Buy 50,000 Slayer XP";
        rsComponent.addHoverText(24559, "Buy ring of slaying", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 93, 12, "Buy Ring").popupString = "Buy a ring which enables\\nteleports to Slayer\\nlocations (8 charges).";
        rsComponent.addHoverText(24560, "Buy runes for Slayer Dart", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 126, 12, "Buy Runes").popupString = "Buy runes for 250 casts\\nof Slayer Dart.";
        rsComponent.addHoverText(24561, "Buy broad bolts", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 92, 12, "Buy Bolts").popupString = "Buy 250 broad bolts.";
        rsComponent.addHoverText(24562, "Buy broad arrows", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 97, 12, "Buy Arrows").popupString = "Buy 250 broad arrows.";
        rsComponent.addHoverText(24563, "400 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Buy XP").popupString = "Buy 50,000 Slayer XP";
        rsComponent.addHoverText(24564, "75 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Buy Ring").popupString = "Buy a ring which enables\\nteleports to Slayer\\nlocations (8 charges).";
        rsComponent.addHoverText(24565, "35 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Buy Runes").popupString = "Buy runes for 250 casts\\nof Slayer Dart.";
        rsComponent.addHoverText(24566, "35 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Buy Bolts").popupString = "Buy 250 broad bolts.";
        rsComponent.addHoverText(24567, "35 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Buy Arrows").popupString = "Buy 250 broad arrows.";
        rsComponent.addText(24568, "Current points = 133", rsFonts, RSComponent.BOLD, 0xD7B011, false, true, 0, 0);
        rsComponent.addChild(24547, 13, 20);
        rsComponent.addChild(24548, 474, 30);
        rsComponent.addChild(24549, 474, 30);
        rsComponent.addChild(24551, 99, 33);
        rsComponent.addChild(24552, 187, 33);
        rsComponent.addChild(24553, 80, 135);
        rsComponent.addChild(24554, 80, 167);
        rsComponent.addChild(24555, 60, 198);
        rsComponent.addChild(24556, 80, 231);
        rsComponent.addChild(24557, 80, 261);
        rsComponent.addChild(24558, 118, 138);
        rsComponent.addChild(24559, 118, 171);
        rsComponent.addChild(24560, 118, 202);
        rsComponent.addChild(24561, 118, 233);
        rsComponent.addChild(24562, 118, 261);
        rsComponent.addChild(24563, 341, 139);
        rsComponent.addChild(24564, 341, 169);
        rsComponent.addChild(24565, 340, 202);
        rsComponent.addChild(24566, 340, 233);
        rsComponent.addChild(24567, 340, 261);
        rsComponent.addChild(24568, 95, 87);
        return rsComponent;
    }

    /**
     * Builds the Learn component.
     *
     * @return The {@link com.runescape.cache.media.RSComponent}.
     */
    public RSComponent buildLearnComponent(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(24569);
        rsComponent.addSprite(24570, ImageLoader.forName("SLAYER_LEARN_BACK"));
        rsComponent.addHoverButton(24572, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 24573, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(24573, SpriteRepository.SMALL_X_HOVER, 16, 16, 24574);
        rsComponent.addActionButton(24575, Sprite.NULL_TYPE, 80, 20, -1, "Buy").popupString = "Buy Slayer equipment";
        rsComponent.addActionButton(24576, Sprite.NULL_TYPE, 86, 20, -1, "Assignments").popupString = "Manage Slayer\\nassignments";
        rsComponent.addActionButton(24577, Sprite.NULL_TYPE, 41, 33, -1, "Buy Ability").popupString =
                "Learn how to fletch broad\\narrows (with 52 Fletching)\\nand bolts (with 55\\nFletching).";
        rsComponent.addActionButton(24578, Sprite.NULL_TYPE, 41, 37, -1, "Buy Ability").popupString =
                "Learn how to craft rings\\nof slaying (with 75\\nCrafting).";
        rsComponent.addActionButton(24579, Sprite.NULL_TYPE, 41, 54, -1, "Buy Ability").popupString =
                "Learn how to combine the \\nnosepeg, earmuffs, \\nspiny helm, facemask and\\nuncharged black mask\\n(with 55 Crafting).";
        rsComponent.addHoverText(24580, "Learn how to fletch broad arrows/bolts", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 224, 12, "Buy Ability").popupString =
                "Learn how to fletch broad\\narrows (with 52 Fletching)\\nand bolts (with 55\\nFletching).";
        rsComponent.addHoverText(24581, "Learn how to craft rings of slaying", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 197, 12, "Buy Ability").popupString =
                "Learn how to craft rings\\nof slaying (with 75\\nCrafting).";
        rsComponent.addHoverText(24582, "Learn how to craft Slayer helmets", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 197, 12, "Buy Ability").popupString =
                "Learn how to combine the \\nnosepeg, earmuffs, \\nspiny helm, facemask and\\nuncharged black mask\\n(with 55 Crafting).";
        rsComponent.addHoverText(24583, "300 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Buy Ability").popupString =
                "Learn how to fletch broad\\narrows (with 52 Fletching)\\nand bolts (with 55\\nFletching).";
        rsComponent.addHoverText(24584, "300 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Buy Ability").popupString =
                "Learn how to craft rings\\nof slaying (with 75\\nCrafting).";
        rsComponent.addHoverText(24585, "400 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Buy Ability").popupString =
                "Learn how to combine the \\nnosepeg, earmuffs, \\nspiny helm, facemask and\\nuncharged black mask\\n(with 55 Crafting).";
        rsComponent.addChild(24570, 13, 20);
        rsComponent.addChild(24568, 95, 87);
        rsComponent.addChild(24572, 474, 30);
        rsComponent.addChild(24573, 474, 30);
        rsComponent.addChild(24575, 16, 33);
        rsComponent.addChild(24576, 187, 33);
        rsComponent.addChild(24577, 71, 138);
        rsComponent.addChild(24578, 71, 190);
        rsComponent.addChild(24579, 71, 235);
        rsComponent.addChild(24580, 118, 138);
        rsComponent.addChild(24581, 118, 191);
        rsComponent.addChild(24582, 118, 241);
        rsComponent.addChild(24583, 365, 138);
        rsComponent.addChild(24584, 365, 191);
        rsComponent.addChild(24585, 365, 241);
        return rsComponent;
    }

    /**
     * Builds the Assignment component.
     * <p/>
     * TODO Cancel removal - (Does not\\nrefund points)
     *
     * @return The {@link com.runescape.cache.media.RSComponent}.
     */
    public RSComponent buildAssignComponent(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(24586);
        rsComponent.addSprite(24587, ImageLoader.forName("SLAYER_ASSIGN_BACK"));
        rsComponent.addHoverButton(24589, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 24590, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(24590, SpriteRepository.SMALL_X_HOVER, 16, 16, 24591);
        rsComponent.addActionButton(24592, Sprite.NULL_TYPE, 80, 20, -1, "Buy").popupString = "Buy Slayer equipment";
        rsComponent.addActionButton(24593, Sprite.NULL_TYPE, 86, 20, -1, "Learn").popupString = "Learn new abilities";
        rsComponent.addHoverText(24594, "Reassign current mission", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 147, "Reassign current task").popupString =
                "Cancels your current\\nmission. You may be\\nreassigned this creature\\nin the future.";
        rsComponent.addHoverText(24595, "Permanently remove current", rsFonts,
                RSComponent.REGULAR, 0xD7B011, 0xD7B011, false, true, 164, "Permanently remove current task").popupString =
                "Cancels your current\\nmission. You will never be\\nassigned this creature in\\nthe future.";
        rsComponent.addHoverText(24596, "20 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Reassign Current").popupString =
                "Cancels your current\\nmission. You may be\\nreassigned this creature\\nin the future.";
        rsComponent.addHoverText(24597, "70 points", rsFonts,
                RSComponent.REGULAR, 0xFF981F, 0xFF981F, false, true, 68, 12, "Permanently Remove Current").popupString =
                "Cancels your current\\nmission. You will never be\\nassigned this creature in\\nthe future.";
        rsComponent.addText(24598, "Currently removed", rsFonts, RSComponent.SMALL, 0xBF751D, false, true, 97, -1);
        rsComponent.addText(24599, "nothing", rsFonts, RSComponent.REGULAR, 0xFF981F, false, true, 97, -1);
        rsComponent.addText(24600, "nothing", rsFonts, RSComponent.REGULAR, 0xFF981F, false, true, 97, -1);
        rsComponent.addText(24601, "nothing", rsFonts, RSComponent.REGULAR, 0xFF981F, false, true, 97, -1);
        rsComponent.addText(24602, "nothing", rsFonts, RSComponent.REGULAR, 0xFF981F, false, true, 97, -1);
        rsComponent.addActionButton(24603, SpriteRepository.SMALL_X, 16, 16, "Cancel Removal", -1).popupString =
                "Cancels this removal. You\\nwill not be refunded the\\npoints you paid.";
        rsComponent.addActionButton(24604, SpriteRepository.SMALL_X, 16, 16, "Cancel Removal", -1).popupString =
                "Cancels this removal. You\\nwill not be refunded the\\npoints you paid.";
        rsComponent.addActionButton(24605, SpriteRepository.SMALL_X, 16, 16, "Cancel Removal", -1).popupString =
                "Cancels this removal. You\\nwill not be refunded the\\npoints you paid.";
        rsComponent.addActionButton(24606, SpriteRepository.SMALL_X, 16, 16, "Cancel Removal", -1).popupString =
                "Cancels this removal. You\\nwill not be refunded the\\npoints you paid.";

        rsComponent.addChild(24587, 13, 20);
        rsComponent.addChild(24568, 95, 87);
        rsComponent.addChild(24589, 474, 30);
        rsComponent.addChild(24590, 474, 30);
        rsComponent.addChild(24592, 16, 33);
        rsComponent.addChild(24593, 99, 33);
        rsComponent.addChild(24594, 55, 133);
        rsComponent.addChild(24595, 55, 156);
        rsComponent.addChild(24596, 375, 133);
        rsComponent.addChild(24597, 375, 154);
        rsComponent.addChild(24598, 130, 203);
        rsComponent.addChild(24599, 55, 225);
        rsComponent.addChild(24600, 55, 243);
        rsComponent.addChild(24601, 55, 261);
        rsComponent.addChild(24602, 55, 279);
        rsComponent.addChild(24603, 304, 225);
        rsComponent.addChild(24604, 304, 243);
        rsComponent.addChild(24605, 304, 261);
        rsComponent.addChild(24606, 304, 279);
        return rsComponent;
    }

}

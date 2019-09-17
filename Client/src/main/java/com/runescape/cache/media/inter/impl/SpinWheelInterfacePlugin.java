package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for bolt enchantments.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class SpinWheelInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(22653);
        rsComponent.addSprite(22654, ImageLoader.forName("JEWELLERY_CRAFT_BACK"));
        rsComponent.addHoverButton(22655, SpriteRepository.SMALL_X, 16, 16, "Close", -1, 22656, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(22656, SpriteRepository.SMALL_X_HOVER, 16, 16, 22657);
        rsComponent.addText(22658, "What would you like to spin?", rsFonts, RSComponent.BOLD, 0xFF981F, true, true, 378, -1);
        rsComponent.addActionButton(22659, ImageLoader.forName("WOOL").toSpriteSet(), 96, 99, -1, "Make X <col=FF9040>Ball of Wool");
        rsComponent.addActionButton(22660, ImageLoader.forName("WOOL").toSpriteSet(), 96, 99, -1, "Make All <col=FF9040>Ball of Wool");
        rsComponent.addActionButton(22661, ImageLoader.forName("WOOL").toSpriteSet(), 96, 99, -1, "Make 5 <col=FF9040>Ball of Wool");
        rsComponent.addActionButton(22662, ImageLoader.forName("WOOL").toSpriteSet(), 96, 99, -1, "Make 1 <col=FF9040>Ball of Wool");
        rsComponent.addText(22663, "Ball of Wool\\n(Wool)", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        rsComponent.addActionButton(22664, ImageLoader.forName("BOWSTRING").toSpriteSet(), 96, 99, -1, "Make X <col=FF9040>Bow String");
        rsComponent.addActionButton(22665, ImageLoader.forName("BOWSTRING").toSpriteSet(), 96, 99, -1, "Make All <col=FF9040>Bow String");
        rsComponent.addActionButton(22666, ImageLoader.forName("BOWSTRING").toSpriteSet(), 96, 99, -1, "Make 5 <col=FF9040>Bow String");
        rsComponent.addActionButton(22667, ImageLoader.forName("BOWSTRING").toSpriteSet(), 96, 99, -1, "Make 1 <col=FF9040>Bow String");
        rsComponent.addText(22668, "Bow String\\n(Flax)", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        rsComponent.addActionButton(22669, ImageLoader.forName("BOWSTRING").toSpriteSet(), 96, 99, -1, "Make X <col=FF9040>Magic String");
        rsComponent.addActionButton(22670, ImageLoader.forName("BOWSTRING").toSpriteSet(), 96, 99, -1, "Make All <col=FF9040>Magic String");
        rsComponent.addActionButton(22671, ImageLoader.forName("BOWSTRING").toSpriteSet(), 96, 99, -1, "Make 5 <col=FF9040>Magic String");
        rsComponent.addActionButton(22672, ImageLoader.forName("BOWSTRING").toSpriteSet(), 96, 99, -1, "Make 1 <col=FF9040>Magic String");
        rsComponent.addText(22673, "Magic Amulet", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        rsComponent.addText(22674, "String", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        rsComponent.addText(22675, "(Magic Roots)", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        // Next row
        rsComponent.addActionButton(22676, ImageLoader.forName("CBOWSTRING").toSpriteSet(), 96, 99, -1, "Make X <col=FF9040>Crossbow String");
        rsComponent.addActionButton(22677, ImageLoader.forName("CBOWSTRING").toSpriteSet(), 96, 99, -1, "Make All <col=FF9040>Crossbow String");
        rsComponent.addActionButton(22678, ImageLoader.forName("CBOWSTRING").toSpriteSet(), 96, 99, -1, "Make 5 <col=FF9040>Crossbow String");
        rsComponent.addActionButton(22679, ImageLoader.forName("CBOWSTRING").toSpriteSet(), 96, 99, -1, "Make 1 <col=FF9040>Crossbow String");
        rsComponent.addText(22680, "C'Bow String", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        rsComponent.addText(22681, "(Tree Roots)", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        rsComponent.addActionButton(22682, ImageLoader.forName("CBOWSTRING").toSpriteSet(), 96, 99, -1, "Make X <col=FF9040>Crossbow String");
        rsComponent.addActionButton(22683, ImageLoader.forName("CBOWSTRING").toSpriteSet(), 96, 99, -1, "Make All <col=FF9040>Crossbow String");
        rsComponent.addActionButton(22684, ImageLoader.forName("CBOWSTRING").toSpriteSet(), 96, 99, -1, "Make 5 <col=FF9040>Crossbow String");
        rsComponent.addActionButton(22685, ImageLoader.forName("CBOWSTRING").toSpriteSet(), 96, 99, -1, "Make 1 <col=FF9040>Crossbow String");
        rsComponent.addText(22686, "C'Bow String", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        rsComponent.addText(22687, "(Sinew)", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        rsComponent.addActionButton(22688, ImageLoader.forName("ROPE").toSpriteSet(), 96, 99, -1, "Make X <col=FF9040>Rope");
        rsComponent.addActionButton(22689, ImageLoader.forName("ROPE").toSpriteSet(), 96, 99, -1, "Make All <col=FF9040>Rope");
        rsComponent.addActionButton(22690, ImageLoader.forName("ROPE").toSpriteSet(), 96, 99, -1, "Make 5 <col=FF9040>Rope");
        rsComponent.addActionButton(22691, ImageLoader.forName("ROPE").toSpriteSet(), 96, 99, -1, "Make 1 <col=FF9040>Rope");
        rsComponent.addText(22692, "Rope", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);
        rsComponent.addText(22693, "(Yak Hair)", rsFonts, RSComponent.REGULAR, 0xFF981F, true, true, 96, -1);

        rsComponent.addChild(22654, 13, 23);
        rsComponent.addChild(22655, 471, 32);
        rsComponent.addChild(22656, 471, 32);
        rsComponent.addChild(22658, 66, 32);
        rsComponent.addChild(22659, 113, 85);
        rsComponent.addChild(22660, 113, 85);
        rsComponent.addChild(22661, 113, 85);
        rsComponent.addChild(22662, 113, 85);
        rsComponent.addChild(22663, 79, 125);
        rsComponent.addChild(22664, 243, 88);
        rsComponent.addChild(22665, 243, 88);
        rsComponent.addChild(22666, 243, 88);
        rsComponent.addChild(22667, 243, 88);
        rsComponent.addChild(22668, 214, 128);
        rsComponent.addChild(22669, 362, 87);
        rsComponent.addChild(22670, 362, 87);
        rsComponent.addChild(22671, 362, 87);
        rsComponent.addChild(22672, 362, 87);
        rsComponent.addChild(22673, 338, 130);
        rsComponent.addChild(22674, 338, 145);
        rsComponent.addChild(22675, 338, 160);
        rsComponent.addChild(22676, 101, 197);
        rsComponent.addChild(22677, 101, 197);
        rsComponent.addChild(22678, 101, 197);
        rsComponent.addChild(22679, 101, 197);
        rsComponent.addChild(22680, 80, 251);
        rsComponent.addChild(22681, 80, 266);
        rsComponent.addChild(22682, 234, 200);
        rsComponent.addChild(22683, 234, 200);
        rsComponent.addChild(22684, 234, 200);
        rsComponent.addChild(22685, 234, 200);
        rsComponent.addChild(22686, 213, 252);
        rsComponent.addChild(22687, 212, 267);
        rsComponent.addChild(22688, 357, 206);
        rsComponent.addChild(22689, 357, 206);
        rsComponent.addChild(22690, 357, 206);
        rsComponent.addChild(22691, 357, 206);
        rsComponent.addChild(22692, 334, 252);
        rsComponent.addChild(22693, 334, 267);
        return rsComponent;
    }
}

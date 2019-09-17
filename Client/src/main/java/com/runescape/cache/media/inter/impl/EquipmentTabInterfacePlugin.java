package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * @author Gielinor
 */
public class EquipmentTabInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent = RSComponent.getComponentCache()[1644];
        rsComponent.addSprite(15101, SpriteRepository.EMPTY);
        rsComponent.addSprite(15102, SpriteRepository.EMPTY);
        rsComponent.addSprite(15109, SpriteRepository.EMPTY);
        RSComponent.removeConfig(21338);
        RSComponent.removeConfig(21344);
        RSComponent.removeConfig(21343);
        RSComponent.removeConfig(21342);
        RSComponent.removeConfig(21341);
        RSComponent.removeConfig(21340);
        RSComponent.removeConfig(15103);
        RSComponent.removeConfig(15104);
        rsComponent.setChildBounds(15102, 110, 205);
        rsComponent.setChildBounds(15109, 39, 240);
        rsComponent.setChildBounds(27650, 0, 0);
        rsComponent = new RSComponent().addInterface(27650);
        rsComponent.addHoverButton(27651, ImageLoader.forName("PRICE_CHECK"), 40, 40, "Price-checker", -1, 27652, 1);
        rsComponent.addHoveredButton(27652, ImageLoader.forName("PRICE_CHECK_HOVER"), 40, 40, 27658);
        rsComponent.addHoverButton(27653, ImageLoader.forName("EQUIPMENT_STATS"), 40, 40, "Show Equipment Stats", -1, 27655, 1);
        rsComponent.addHoveredButton(27655, ImageLoader.forName("EQUIPMENT_STATS_HOVER"), 40, 40, 27665);
        rsComponent.addHoverButton(27654, ImageLoader.forName("ITEMS_ON_DEATH"), 40, 40, "Show items kept on death", -1, 27657, 1);
        rsComponent.addHoveredButton(27657, ImageLoader.forName("ITEMS_ON_DEATH_HOVER"), 40, 40, 27666);
        rsComponent.setChildBounds(27651, 75, 205);
        rsComponent.setChildBounds(27652, 75, 205);
        rsComponent.setChildBounds(27653, 23, 205);
        rsComponent.setChildBounds(27654, 127, 205);
        rsComponent.setChildBounds(27655, 23, 205);
        rsComponent.setChildBounds(27657, 127, 205);
        rsComponent = RSComponent.forId(27651);
        rsComponent.tooltip = "View guide prices";
        rsComponent = RSComponent.forId(27653);
        rsComponent.tooltip = "View equipment stats";
        rsComponent = RSComponent.forId(27654);
        rsComponent.tooltip = rsComponent.tooltip.replace("Show ", "View ");
        return rsComponent;
    }
}

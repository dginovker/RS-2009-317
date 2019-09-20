package com.runescape.media.component.impl;

import com.runescape.Game;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.media.RSComponent;
import com.runescape.media.component.ComponentType;

/**
 * Represents the item stack {@link com.runescape.cache.media.RSComponent} to draw.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ItemStackComponent extends ComponentType {

    /**
     * Constructs a new <code>ItemStackComponent</code>.
     *
     * @param game The {@link Game} instance.
     */
    public ItemStackComponent(Game game) {
        super(game);
    }

    /**
     * Draws the component type.
     *
     * @param rsComponent  The parent {@link com.runescape.cache.media.RSComponent}.
     * @param rsComponent1 The child {@link com.runescape.cache.media.RSComponent}.
     * @param interfaceX   The parent x coordinate to draw.
     * @param interfaceY   The parent y coordinate to draw.
     * @param childX       The child x coordinate to draw.
     * @param childY       The child y coordinate to draw.
     */
    @Override
    public void draw(RSComponent rsComponent, RSComponent rsComponent1, int interfaceX, int interfaceY, int childX, int childY) {
        int itemIndex = 0;
        for (int height = 0; height < rsComponent1.height; height++) {
            for (int width = 0; width < rsComponent1.width; width++) {
                if (rsComponent1.inventory[itemIndex] > 0) {
                    ItemDefinition itemDef = ItemDefinition.forId(rsComponent1.inventory[itemIndex] - 1);
                    String itemName = itemDef.name;
                    if (itemDef.stackingType || rsComponent1.inventoryValue[itemIndex] != 1) {
                        itemName = itemName + " x" + Game.intToKOrMilLongName(rsComponent1.inventoryValue[itemIndex]);
                    }
                    int paddingX = childX + width * (115 + rsComponent1.invSpritePadX);
                    int paddingY = childY + height * (12 + rsComponent1.invSpritePadY);
                    if (rsComponent1.centerText) {
                        rsComponent1.gameFont.drawCenteredString(itemName, paddingX + rsComponent1.width / 2, paddingY, rsComponent1.textColor, rsComponent1.textShadow);
                    } else {
                        rsComponent1.gameFont.drawBasicString(itemName, paddingX, paddingY, rsComponent1.textColor, rsComponent1.textShadow);
                    }
                }
                itemIndex++;
            }
        }
    }
}

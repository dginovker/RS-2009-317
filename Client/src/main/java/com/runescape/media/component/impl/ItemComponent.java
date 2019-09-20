package com.runescape.media.component.impl;

import main.java.com.runescape.Game;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.Sprite;
import com.runescape.media.Raster;
import com.runescape.media.component.ComponentType;

/**
 * Represents the item {@link com.runescape.cache.media.RSComponent} to draw.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ItemComponent extends ComponentType {

    /**
     * Constructs a new <code>ItemComponent</code>.
     *
     * @param game The {@link Game} instance.
     */
    public ItemComponent(Game game) {
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
        for (int heightIndex = 0; heightIndex < rsComponent1.height; heightIndex++) {
            for (int widthIndex = 0; widthIndex < rsComponent1.width; widthIndex++) {
                int itemSpriteX = childX + widthIndex * (32 + rsComponent1.invSpritePadX);
                int itemSpriteY = childY + heightIndex * (32 + rsComponent1.invSpritePadY);
                if (itemIndex < 20) {
                    itemSpriteX += rsComponent1.spritesX[itemIndex];
                    itemSpriteY += rsComponent1.spritesY[itemIndex];
                }
                if (rsComponent1.inventory[itemIndex] > 0) {
                    if (rsComponent1.drawSprite) {
                        continue;
                    }
                    int xCoordinate = 0;
                    int yCoordinate = 0;
                    int itemId = rsComponent1.inventory[itemIndex] - 1;
                    if (itemSpriteX > Raster.topX - 32 && itemSpriteX < Raster.bottomX && itemSpriteY > Raster.topY - 32 &&
                            itemSpriteY < Raster.bottomY || getGame().activeInterfaceType != 0 && getGame().dragFromSlot == itemIndex) {
                        int outlineColor = 0;
                        if (getGame().itemSelected == 1 && getGame().lastItemSelectedSlot == itemIndex && getGame().lastItemSelectedInterface == rsComponent1.id) {
                            outlineColor = 0xFFFFFF;
                        }
                        Sprite itemSprite = ItemDefinition.getSprite(itemId, rsComponent1.inventoryValue[itemIndex], outlineColor);
                        if (itemSprite != null) {
                            if (getGame().activeInterfaceType != 0 && getGame().dragFromSlot == itemIndex && getGame().focusedComponent == rsComponent1.id) {
                                xCoordinate = getGame().mouseX - getGame().pressX;
                                yCoordinate = getGame().mouseY - getGame().pressY;
                                if (xCoordinate < 5 && xCoordinate > -5) {
                                    xCoordinate = 0;
                                }
                                if (yCoordinate < 5 && yCoordinate > -5) {
                                    yCoordinate = 0;
                                }
                                if (getGame().dragCycle < 10) {
                                    xCoordinate = 0;
                                    yCoordinate = 0;
                                }
                                itemSprite.drawSprite(itemSpriteX + xCoordinate, itemSpriteY + yCoordinate);
                                if (itemSpriteY + yCoordinate < Raster.topY && rsComponent.scrollPosition > 0) {
                                    int currentCycleScroll = (getGame().cycleTimer * (Raster.topY - itemSpriteY - yCoordinate)) / 3;
                                    if (currentCycleScroll > getGame().cycleTimer * 10) {
                                        currentCycleScroll = getGame().cycleTimer * 10;
                                    }
                                    if (currentCycleScroll > rsComponent.scrollPosition) {
                                        currentCycleScroll = rsComponent.scrollPosition;
                                    }
                                    rsComponent.scrollPosition -= currentCycleScroll;
                                    getGame().pressY += currentCycleScroll;
                                }
                                if (itemSpriteY + yCoordinate + 32 > Raster.bottomY && rsComponent.scrollPosition < rsComponent.scrollMax - rsComponent.height) {
                                    int j10 = (getGame().cycleTimer * ((itemSpriteY + yCoordinate + 32) - Raster.bottomY)) / 3;
                                    if (j10 > getGame().cycleTimer * 10) {
                                        j10 = getGame().cycleTimer * 10;
                                    }
                                    if (j10 > rsComponent.scrollMax - rsComponent.height - rsComponent.scrollPosition) {
                                        j10 = rsComponent.scrollMax - rsComponent.height - rsComponent.scrollPosition;
                                    }
                                    rsComponent.scrollPosition += j10;
                                    getGame().pressY -= j10;
                                }
                            } else if (getGame().atInventoryInterfaceType != 0 && getGame().atInventoryIndex == itemIndex
                                    && getGame().atInventoryInterface == rsComponent1.id) {
                                itemSprite.drawSprite1(itemSpriteX, itemSpriteY);
                            } else {
                                itemSprite.drawSprite(itemSpriteX, itemSpriteY);
                            }
                            if (itemSprite.maxWidth == 33 || rsComponent1.inventoryValue[itemIndex] != 1) {
                                if (rsComponent1.hasItemCount) {
                                    int itemAmount = rsComponent1.inventoryValue[itemIndex];
                                    if (itemAmount >= 10000000) {
                                        getGame().smallFont.drawBasicString(Game.intToKOrMil(itemAmount), itemSpriteX + xCoordinate, itemSpriteY + 9 + yCoordinate, 0x00FF80, 0);
                                    } else if (itemAmount >= 100000) {
                                        getGame().smallFont.drawBasicString(Game.intToKOrMil(itemAmount), itemSpriteX + xCoordinate, itemSpriteY + 9 + yCoordinate, 0xFFFFFF, 0);
                                    } else if (itemAmount >= 1) {
                                        getGame().smallFont.drawBasicString(Game.intToKOrMil(itemAmount), itemSpriteX + xCoordinate, itemSpriteY + 9 + yCoordinate, 0xFFFF00, 0);
                                    } else {
                                        getGame().smallFont.drawBasicString(Game.intToKOrMil(itemAmount), itemSpriteX + 1 + xCoordinate, itemSpriteY + 10 + yCoordinate, 0xFFFF00, 0);
                                    }
                                }
                            }
                        }
                    }
                } else if (rsComponent1.sprites != null && itemIndex < 20) {
                    Sprite backgroundSprite = rsComponent1.sprites[itemIndex];
                    if (backgroundSprite != null) {
                        backgroundSprite.drawSprite(itemSpriteX, itemSpriteY);
                    }
                }
                itemIndex++;
            }
        }
    }
}

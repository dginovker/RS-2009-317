package com.runescape.media.component.impl;

import com.runescape.Game;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.Sprite;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.cache.media.inter.impl.BankInterfacePlugin;
import com.runescape.media.Raster;
import com.runescape.media.component.ComponentType;

/**
 * Represents the container {@link com.runescape.cache.media.RSComponent} to draw.
 * <p/>
 * TODO Create class specifically for bank, com.runescape.media.component.bank.BankComponent
 * TODO Move GrandExchange classes to com.runescape.media.component.ge.*
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ContainerComponent extends ComponentType {

    /**
     * Constructs a new <code>ContainerComponent</code>.
     *
     * @param game The {@link com.runescape.Game} instance.
     */
    public ContainerComponent(Game game) {
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
        if (true) {
            int itemIndex = 0;
            int tabAmountInv = 0;
            int tabSlot = -1;
            int tabHeightY = 2;
            if (rsComponent1.contentType == 206) {
                int tabHeight = 0;
                for (int i = 0; i < getGame().tabAmounts.length; i++) {
                    if (tabSlot + 1 < getGame().tabAmounts.length && getGame().tabAmounts[tabSlot + 1] > 0) {
                        tabSlot++;
                        if (getGame().tabAmounts[tabSlot] == 32768) {
                            getGame().tabAmounts[tabSlot] = tabSlot == 0 ? 1 : 0;
                        }
                        tabAmountInv += getGame().tabAmounts[tabSlot];
                        tabHeight += (getGame().tabAmounts[tabSlot] >> 3) + (getGame().tabAmounts[tabSlot] % 8 == 0 ? 0 : 1);
                        if (tabSlot + 1 < getGame().tabAmounts.length && getGame().tabAmounts[tabSlot + 1] > 0 && InterfaceConfiguration.CURRENT_BANK_TAB.get() == 0 && getGame().settings[1012] == 0) {
                            Raster.drawLinePixels((childY + tabHeight * (32 + rsComponent1.invSpritePadY) + tabHeightY) - 1,
                                    0x3E3529, ((32 + rsComponent1.invSpritePadX) << 3) - 10, childX);
                            Raster.drawLinePixels((childY + tabHeight * (32 + rsComponent1.invSpritePadY) + tabHeightY),
                                    0x3E3529, ((32 + rsComponent1.invSpritePadX) << 3) - 10, childX);
                        }
                        tabHeightY += 8;
                    }

                    if (i > 0) {
                        int itemSlot = tabAmountInv - getGame().tabAmounts[i];
                        int xOffset = (Game.getFrameWidth() - 237 - RSComponent.getComponentCache()[5292].width) / 2;
                        int yOffset = 36 + ((Game.getFrameHeight() - 503) / 2);
                        int x = xOffset + 77;
                        int y = yOffset + 25;
                        try {
                            int item = RSComponent.getComponentCache()[5382].inventory[itemSlot];
                            if (getGame().tabAmounts[i] > 0 && item > 0) {
                                Sprite icon = ItemDefinition.getSprite(item - 1, rsComponent1.inventoryValue[itemSlot], 0);
                                if (InterfaceConfiguration.BANK_ROMAN_TYPE.get() == 1) {
                                    icon = ImageLoader.forName("BANK_ROMAN_" + i);
                                }
                                if (InterfaceConfiguration.BANK_NUMERAL_TYPE.get() == 1) {
                                    icon = ImageLoader.forName("BANK_NUMERIC_" + i);
                                }
                                if (icon != null) {
                                    icon.drawSprite1((Game.isFixed() ? 60 : x + 4) + 40 * i, (Game.isFixed() ? 41 : y + 2), 255, true);
                                }
                                RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 13 + i * 4].yOffset = 0;
                                RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].yOffset = 0;
                                RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].tooltip = "View tab <col=FF9040>" + i;
                                if (RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].getSpriteSet() == null) {
                                    RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].setSpriteSet(new SpriteSet("BANK_TAB_EMPTY"));
                                } else {
                                    RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].getSpriteSet().setDisabled(ImageLoader.forName("BANK_TAB_EMPTY"));
                                }
                            } else if (getGame().tabAmounts[i - 1] <= 0) {
                                RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 13 + i * 4].yOffset = -500;
                                if (i > 1) {
                                    RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].yOffset = -500;
                                } else {
                                    getGame().bankNewTab.drawSprite1((Game.isFixed() ? 59 : x) + 40 * i, (Game.isFixed() ? 41 : y), 255, true);
                                }
                                RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].tooltip = "New tab";
                            } else {
                                RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 13 + i * 4].yOffset = -500;
                                RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].yOffset = 0;
                                RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].tooltip = "New tab";
                                if (RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].getSpriteSet() == null) {
                                    RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].setSpriteSet(new SpriteSet("BANK_TAB_2"));
                                } else {
                                    RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + i * 4].getSpriteSet().setDisabled(ImageLoader.forName("BANK_TAB_2"));
                                }
                                getGame().bankNewTab.drawSprite1((Game.isFixed() ? 59 : x) + 40 * i, (Game.isFixed() ? 41 : y), 255, true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                Raster.bottomY += 3;
            }

            tabAmountInv = getGame().tabAmounts[0];
            tabSlot = 0;
            tabHeightY = 0;

            int dragX = 0, dragY = 0;
            Sprite draggedItem = null;

            int newSlot = 0;
            if (rsComponent1.contentType == 206 && InterfaceConfiguration.CURRENT_BANK_TAB.get() != 0 && getGame().settings[1012] == 0) {
                for (int i = 0; i < getGame().tabAmounts.length; i++) {
                    if (i == InterfaceConfiguration.CURRENT_BANK_TAB.get()) {
                        break;
                    }
                    newSlot += getGame().tabAmounts[i];
                }
                itemIndex = newSlot;
            }

            heightLoop:
            for (int height = 0; height < rsComponent1.height; height++) {
                for (int width = 0; width < rsComponent1.width; width++) {
                    int w = childX + width * (32 + rsComponent1.invSpritePadX);
                    int h = childY + height * (32 + rsComponent1.invSpritePadY) + tabHeightY;
                    if (rsComponent1.contentType == 206 && getGame().settings[1012] == 0) {
                        if (InterfaceConfiguration.CURRENT_BANK_TAB.get() == 0) {
                            if (itemIndex == tabAmountInv) {
                                if (tabSlot + 1 < getGame().tabAmounts.length) {
                                    tabAmountInv += getGame().tabAmounts[++tabSlot];
                                    if (tabSlot > 0 && getGame().tabAmounts[tabSlot - 1] % 8 == 0) {
                                        height--;
                                    }
                                    tabHeightY += 8;
                                }
                                break;
                            }
                        } else if (InterfaceConfiguration.CURRENT_BANK_TAB.get() <= 9) {
                            if (itemIndex >= getGame().tabAmounts[InterfaceConfiguration.CURRENT_BANK_TAB.get()] + newSlot) {
                                break heightLoop;
                            }
                        }
                    }
                    if (itemIndex < 20) {
                        w += rsComponent1.spritesX[itemIndex];
                        h += rsComponent1.spritesY[itemIndex];
                    }
                    int itemId = rsComponent1.inventory[itemIndex] - 1;
                    int itemCount = rsComponent1.inventoryValue[itemIndex];
                    if (getGame().settings[1012] == 1 && rsComponent1.contentType == 206 && getGame().bankInvTemp.length > itemIndex) {
                        itemId = getGame().bankInvTemp[itemIndex] - 1;
                        itemCount = getGame().bankStackTemp[itemIndex];
                    }
                    if (itemId + 1 > 0) {
                        int x = 0;
                        int y = 0;
                        if (w > Raster.topX - 32 && w < Raster.bottomX && h > Raster.topY - 32 && h < Raster.bottomY || getGame().activeInterfaceType != 0 && getGame().dragFromSlot == itemIndex) {
                            int outlineColor = 0;
                            if (getGame().itemSelected == 1 && getGame().lastItemSelectedSlot == itemIndex && getGame().lastItemSelectedInterface == rsComponent1.id) {
                                outlineColor = 0xFFFFFF;
                            }
                            Sprite itemSprite = ItemDefinition.getSprite(itemId, getGame().settings[1012] == 1 && rsComponent1.contentType == 206 ? getGame().bankStackTemp[itemIndex] : rsComponent1.inventoryValue[itemIndex], outlineColor);
                            if (itemSprite != null) {
                                if (getGame().activeInterfaceType != 0 && getGame().dragFromSlot == itemIndex &&
                                        getGame().focusedComponent == rsComponent1.id) {
                                    draggedItem = itemSprite;
                                    x = getGame().mouseX - getGame().pressX;
                                    y = getGame().mouseY - getGame().pressY;
                                    if (x < 5 && x > -5) {
                                        x = 0;
                                    }
                                    if (y < 5 && y > -5) {
                                        y = 0;
                                    }
                                    if (getGame().dragCycle < 15) {
                                        x = 0;
                                        y = 0;
                                    }
                                    dragX = w + x;
                                    dragY = h + y;
                                    if (dragY > 50 && h + y < Raster.topY && rsComponent.scrollPosition > 0) {
                                        int dragLength = (getGame().cycleTimer * (Raster.topY - h - y)) / 3;
                                        if (dragLength > getGame().cycleTimer * 10) {
                                            dragLength = getGame().cycleTimer * 10;
                                        }
                                        if (dragLength > rsComponent.scrollPosition) {
                                            dragLength = rsComponent.scrollPosition;
                                        }
                                        rsComponent.scrollPosition -= dragLength;
                                        getGame().pressY += dragLength;
                                    }

                                    if (h + y + 32 > Raster.bottomY && rsComponent.scrollPosition < rsComponent.scrollMax - rsComponent.height) {
                                        int j10 = (getGame().cycleTimer * ((h + y + 32) - Raster.bottomY)) / 3;
                                        if (j10 > getGame().cycleTimer * 10) {
                                            j10 = getGame().cycleTimer * 10;
                                        }
                                        if (j10 > rsComponent.scrollMax - rsComponent.height - rsComponent.scrollPosition) {
                                            j10 = rsComponent.scrollMax - rsComponent.height - rsComponent.scrollPosition;
                                        }
                                        rsComponent.scrollPosition += j10;
                                        getGame().pressY -= j10;
                                    }
                                } else if (getGame().atInventoryInterfaceType != 0 && getGame().atInventoryIndex == itemIndex &&
                                        getGame().atInventoryInterface == rsComponent1.id) {
                                    itemSprite.drawSprite1(w, h);
                                } else {
                                    itemSprite.drawSprite(w, h);
                                }
                                if (rsComponent1.parentId != 42752) {
                                    if (itemSprite.maxWidth == 33 || itemCount != 1) {
                                        if (rsComponent1.hasItemCount) {
                                            if (itemCount >= 10000000) {
                                                getGame().smallFont.drawBasicString(Game.intToKOrMil(itemCount), w + x, h + 9 + y, 0x00FF80, 0);
                                            } else if (itemCount >= 100000) {
                                                getGame().smallFont.drawBasicString(Game.intToKOrMil(itemCount), w + x, h + 9 + y, 0xFFFFFF, 0);
                                            } else if (itemCount >= 1) {
                                                getGame().smallFont.drawBasicString(Game.intToKOrMil(itemCount), w + x, h + 9 + y, 0xFFFF00, 0);
                                            } else {
                                                getGame().smallFont.drawBasicString(Game.intToKOrMil(itemCount), w + x + 1, h + 10 + y, 0xFFFF00, 0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (rsComponent1.sprites != null && itemIndex < 20) {
                        Sprite childSprite = rsComponent1.sprites[itemIndex];
                        if (childSprite != null) {
                            childSprite.drawSprite(w, h);
                        }
                    }
                    itemIndex++;
                }
            }
            if (draggedItem != null) {
                draggedItem.drawSprite1(dragX, dragY, 200 + (int) (50 * Math.sin(Game.loopCycle / 10.0)), rsComponent1.contentType == 206);
            }
            return;
        }
        int itemIndex = 0;
        int tabAmountInv = 0;
        int tabSlot = -1;
        int tabHeightY = 2;
        for (int index = 0; index < getGame().tabFirstItems.length; index++) {
            if (index > 0) {
                int xOffset = (Game.getFrameWidth() - 237 - RSComponent.getComponentCache()[5292].width) / 2;
                int yOffset = 36 + ((Game.getFrameHeight() - 503) / 2);
                int x = xOffset + 77;
                int y = yOffset + 25;
                try {
                    int itemId = getGame().tabFirstItems[index];
                    if (itemId >= 0) {
                        Sprite icon = null;
                        if (getGame().settings[1011] == 0) {
                            icon = ItemDefinition.getSprite(itemId, 1, 0);
                        }
                        if (getGame().settings[1011] == 1) {
                            icon = ImageLoader.forName("BANK_ROMAN_" + index); // Roman
                        }
                        if (getGame().settings[1011] == 2) {
                            icon = ImageLoader.forName("BANK_NUMERIC_" + index);// Numeric
                        }
                        if (icon != null) {
                            icon.drawSprite1((Game.isFixed() ? 60 : x + 4) + 40 * index, (Game.isFixed() ? 41 : y + 2), 255, true);
                        }
                        Sprite s = InterfaceConfiguration.CURRENT_BANK_TAB.get() == index ? ImageLoader.forName("BANK_TAB_2") : ImageLoader.forName("BANK_TAB_EMPTY");
                        RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 13 + index * 4].yOffset = 0;
                        RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].yOffset = 0;
                        RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].tooltip = "View tab <col=FF9040>" + index;
                        if (RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].getSpriteSet() == null) {
                            RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].setSpriteSet(new SpriteSet(s, s));
                        } else {
                            RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].getSpriteSet().setDisabled(s);
                        }
                    } else if (InterfaceConfiguration.BANK_TAB_COUNT.get() < index && itemId < 0) {
                        RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 13 + index * 4].yOffset = -500;
                        if (index > 1) {
                            RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].yOffset = -500;
                        } else {
                            getGame().bankNewTab.drawSprite1((Game.isFixed() ? 59 : x) + 40 * index, (Game.isFixed() ? 41 : y), 255, true);
                        }
                        RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].tooltip = "New tab";
                    } else {
                        RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 13 + index * 4].yOffset = -500;
                        RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].yOffset = 0;
                        RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].tooltip = "New tab";
                        if (RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].getSpriteSet() == null) {
                            RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].setSpriteSet(new SpriteSet("BANK_TAB_2"));
                        } else {
                            RSComponent.getComponentCache()[BankInterfacePlugin.BANK_INTERFACE_ID + 14 + index * 4].setSpriteSet(new SpriteSet("BANK_TAB_2"));
                        }
                        getGame().bankNewTab.drawSprite1((Game.isFixed() ? 59 : x) + 40 * index, (Game.isFixed() ? 41 : y), 255, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        int dragX = 0, dragY = 0;
        Sprite draggedItem = null;
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
                                draggedItem = itemSprite;
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
                                dragX = itemSpriteX + xCoordinate;
                                dragY = itemSpriteY + yCoordinate;
                                if (itemSpriteY + yCoordinate < Raster.topY && rsComponent.scrollPosition > 0) {
                                    int i10 = (getGame().cycleTimer * (Raster.topY - itemSpriteY - yCoordinate)) / 3;
                                    if (i10 > getGame().cycleTimer * 10) {
                                        i10 = getGame().cycleTimer * 10;
                                    }
                                    if (i10 > rsComponent.scrollPosition) {
                                        i10 = rsComponent.scrollPosition;
                                    }
                                    rsComponent.scrollPosition -= i10;
                                    getGame().pressY += i10;
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
                            } else if (getGame().atInventoryInterfaceType != 0 && getGame().atInventoryIndex == itemIndex && getGame().atInventoryInterface == rsComponent1.id) {
                                itemSprite.drawSprite1(itemSpriteX, itemSpriteY);
                            } else {
                                itemSprite.drawSprite(itemSpriteX, itemSpriteY);
                            }
                            if (itemSprite.maxWidth == 33 || rsComponent1.inventoryValue[itemIndex] != 1) {
                                if (rsComponent1.hasItemCount) {
                                    int itemCount = rsComponent1.inventoryValue[itemIndex];
                                    if (itemCount >= 10000000) {
                                        getGame().smallFont.drawBasicString(Game.intToKOrMil(itemCount), itemSpriteX + xCoordinate, itemSpriteY + 9 + yCoordinate, 0x00FF80, 0);
                                    } else if (itemCount >= 100000) {
                                        getGame().smallFont.drawBasicString(Game.intToKOrMil(itemCount), itemSpriteX + xCoordinate, itemSpriteY + 9 + yCoordinate, 0xFFFFFF, 0);
                                    } else if (itemCount >= 1) {
                                        getGame().smallFont.drawBasicString(Game.intToKOrMil(itemCount), itemSpriteX + xCoordinate, itemSpriteY + 9 + yCoordinate, 0xFFFF00, 0);
                                    } else {
                                        getGame().smallFont.drawBasicString(Game.intToKOrMil(itemCount), itemSpriteX + xCoordinate + 1, itemSpriteY + 10 + yCoordinate, 0xFFFF00, 0);
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
        if (draggedItem != null) {
            draggedItem.drawSprite1(dragX, dragY, 200 + (int) (50 * Math.sin(Game.loopCycle / 10.0)), rsComponent1.contentType == 206);
        }
    }
}

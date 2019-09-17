package com.runescape.media.component.impl;

import com.runescape.Game;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.Sprite;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.media.Raster;
import com.runescape.media.component.ComponentType;

/**
 * Represents the tooltip {@link com.runescape.cache.media.RSComponent} to draw.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class TooltipComponent extends ComponentType {

    /**
     * Constructs a new <code>TooltipComponent</code>.
     *
     * @param game The {@link com.runescape.Game} instance.
     */
    public TooltipComponent(Game game) {
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
        Sprite sprite = getGame().interfaceIsSelected(rsComponent1) ?
                rsComponent1.getSpriteSet().getEnabled() :
                rsComponent1.getSpriteSet().getDisabled();
        if (rsComponent1.id == 23586 || rsComponent1.id == 23590 || rsComponent1.id == 23594) {
            sprite = ((getGame().anInt1500 == rsComponent1.id || getGame().anInt1044 == rsComponent1.id || getGame().anInt1129 == rsComponent1.id) &&
                    getGame().anInt1501 == 0 && !getGame().isMenuOpen()) ? rsComponent1.getSpriteSet().getEnabled() : sprite;
        }
        if (sprite != null) {
            if (rsComponent1.drawsTransparent) {
                sprite.drawTransparentSprite(childX, childY, rsComponent1.transparency);
            } else {
                sprite.drawSprite(childX, childY);
            }
        }
        if (rsComponent1.interfaceConfig && !getGame().interfaceIsSelected(rsComponent1)) {
            return;
        }
        if (rsComponent1.disabledMessage != null && (getGame().anInt1500 == rsComponent1.id ||
                getGame().anInt1044 == rsComponent1.id || getGame().anInt1129 == rsComponent1.id) && getGame().anInt1501 == 0 &&
                !getGame().isMenuOpen() && rsComponent1.popupString == null) {
            // TODO Script
            if (rsComponent1.disabledMessage.toLowerCase().contains("next level at:")) {
                if (!getGame().getInterfaceConfiguration(InterfaceConfiguration.REMAINING_XP) &&
                        rsComponent1.disabledMessage.contains("Remaining XP:")) {
                    rsComponent1.disabledMessage = rsComponent1.disabledMessage.substring(0,
                            rsComponent1.disabledMessage.indexOf("\\nRemaining"));
                }
                if (getGame().getInterfaceConfiguration(InterfaceConfiguration.REMAINING_XP) &&
                        !rsComponent1.disabledMessage.contains("Remaining XP:")) {
                    rsComponent1.disabledMessage = rsComponent1.disabledMessage + "\\nRemaining XP: %3";
                }
            }
            int boxWidth = 0;
            int boxHeight = 0;
            String tooltipMessage = rsComponent1.disabledMessage;
            for (String s1 = tooltipMessage; s1.length() > 0; ) {
                if (s1.indexOf("%") != -1) {
                    do {
                        int k7 = s1.indexOf("%1");
                        if (k7 == -1) {
                            break;
                        }
                        s1 = s1.substring(0, k7) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 0)) + s1.substring(k7 + 2);
                    } while (true);
                    do {
                        int l7 = s1.indexOf("%2");
                        if (l7 == -1) {
                            break;
                        }
                        s1 = s1.substring(0, l7) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 1)) + s1.substring(l7 + 2);
                    } while (true);
                    do {
                        int i8 = s1.indexOf("%3");
                        if (i8 == -1) {
                            break;
                        }
                        s1 = s1.substring(0, i8) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 2)) + s1.substring(i8 + 2);
                    } while (true);
                    do {
                        int j8 = s1.indexOf("%4");
                        if (j8 == -1) {
                            break;
                        }
                        s1 = s1.substring(0, j8) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 3)) + s1.substring(j8 + 2);
                    } while (true);
                    do {
                        int k8 = s1.indexOf("%5");
                        if (k8 == -1) {
                            break;
                        }
                        s1 = s1.substring(0, k8) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 4)) + s1.substring(k8 + 2);
                    } while (true);
                    do {
                        int i8 = s1.indexOf("%6");
                        if (i8 == -1) {
                            break;
                        }
                        s1 = s1.substring(0, i8) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 21)) + s1.substring(i8 + 2);
                    } while (true);
                }
                int l7 = s1.indexOf("\\n");
                String s4;
                if (l7 != -1) {
                    s4 = s1.substring(0, l7);
                    s1 = s1.substring(l7 + 2);
                } else {
                    s4 = s1;
                    s1 = "";
                }
                int j10 = getGame().regularFont.getTextWidth(s4);
                if (j10 > boxWidth) {
                    boxWidth = j10;
                }
                boxHeight += getGame().regularFont.baseCharacterHeight + 1;
            }
            boxWidth += 6;
            boxHeight += 7;
            int xPos = (childX + rsComponent1.width) - 5 - boxWidth;
            int yPos = childY + rsComponent1.height + 5;
            if (xPos < childX + 5) {
                xPos = childX + 5;
            }
            if (Game.isFixed()) {
                if (xPos < childX + 5) {
                    xPos = childX + 5;
                }
                if (xPos + boxWidth > interfaceX + rsComponent.width) {
                    xPos = (interfaceX + rsComponent.width) - boxWidth;
                }
                if (yPos + boxHeight > interfaceY + rsComponent.height) {
                    yPos = (childY - boxHeight);
                }
            } else {
                if (xPos < childX + 5) {
                    xPos = childX + 5;
                }
                if (xPos > 1560 && xPos < 1600) {
                    xPos -= 40;
                } else if (xPos >= 1600) {
                    xPos -= 140;
                }
            }
            Raster.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0, boxWidth);
            Raster.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
            String s2 = tooltipMessage;
            for (int j11 = yPos + getGame().regularFont.baseCharacterHeight + 2; s2.length() > 0; j11 += getGame().regularFont.baseCharacterHeight + 1) {
                if (s2.indexOf("%") != -1) {
                    do {
                        int k7 = s2.indexOf("%1");
                        if (k7 == -1) {
                            break;
                        }
                        s2 = s2.substring(0, k7) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 0)) + s2.substring(k7 + 2);
                    } while (true);
                    do {
                        int l7 = s2.indexOf("%2");
                        if (l7 == -1) {
                            break;
                        }
                        s2 = s2.substring(0, l7) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 1)) + s2.substring(l7 + 2);
                    } while (true);
                    do {
                        int i8 = s2.indexOf("%3");
                        if (i8 == -1) {
                            break;
                        }
                        s2 = s2.substring(0, i8) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 2)) + s2.substring(i8 + 2);
                    } while (true);
                    do {
                        int j8 = s2.indexOf("%4");
                        if (j8 == -1) {
                            break;
                        }
                        s2 = s2.substring(0, j8) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 3)) + s2.substring(j8 + 2);
                    } while (true);
                    do {
                        int k8 = s2.indexOf("%5");
                        if (k8 == -1) {
                            break;
                        }
                        s2 = s2.substring(0, k8) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 4)) + s2.substring(k8 + 2);
                    } while (true);
                }
                int l11 = s2.indexOf("\\n");
                String s5;
                if (l11 != -1) {
                    s5 = s2.substring(0, l11);
                    s2 = s2.substring(l11 + 2);
                } else {
                    s5 = s2;
                    s2 = "";
                }
                if (rsComponent1.centerText) {
                    getGame().regularFont.drawCenteredString(s5, xPos, yPos + rsComponent1.width / 2, j11, -1);
                } else {
                    if (s5.contains("\\r")) {
                        String text = s5.substring(0, s5.indexOf("\\r"));
                        String text2 = s5.substring(s5.indexOf("\\r") + 2);
                        getGame().regularFont.drawBasicString(text, xPos + 3, j11, 0, -1);
                        int rightX = boxWidth + xPos - getGame().regularFont.getTextWidth(text2) - 2;
                        getGame().regularFont.drawBasicString(text2, rightX, j11, 0, -1);
                    } else {
                        getGame().regularFont.drawBasicString(s5, xPos + 3, j11, 0, -1);
                    }
                }
            }
        }
    }
}

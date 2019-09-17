package com.runescape.media.component.impl;

import com.runescape.Game;
import com.runescape.cache.media.RSComponent;
import com.runescape.media.Raster;
import com.runescape.media.component.ComponentType;
import com.runescape.media.font.GameFont;

/**
 * Represents the string {@link com.runescape.cache.media.RSComponent} to draw.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class StringComponent extends ComponentType {

    /**
     * Constructs a new <code>StringComponent</code>.
     *
     * @param game The {@link com.runescape.Game} instance.
     */
    public StringComponent(Game game) {
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
        if (rsComponent1.hidden) {
            return;
        }
        GameFont gameFont = rsComponent1.gameFont;
        String shownMessage = rsComponent1.disabledMessage;
        boolean hovered = false;
        if (getGame().getAnInt1039() == rsComponent1.id || getGame().getAnInt1048() == rsComponent1.id || getGame().getAnInt1026() == rsComponent1.id) {
            hovered = true;
        }
        int shownColour;
        if (getGame().interfaceIsSelected(rsComponent1)) {
            shownColour = rsComponent1.enabledColor;
            if (hovered && rsComponent1.enabledMouseOverColor != 0) {
                shownColour = rsComponent1.enabledMouseOverColor;
            }
            if (rsComponent1.enabledMessage.length() > 0) {
                shownMessage = rsComponent1.enabledMessage;
            }
        } else {
            shownColour = rsComponent1.textColor;
            if (hovered && rsComponent1.disabledMouseOverColor != 0) {
                shownColour = rsComponent1.disabledMouseOverColor;
            }
        }
        if (rsComponent1.optionType == 6 && getGame().continuedDialogue) {
            shownMessage = "Please wait...";
            shownColour = rsComponent1.textColor;
        }
        if (Raster.width == 519) {
            if (shownColour == 0xFFFF00) {
                shownColour = 255;
            }
            if (shownColour == 49152) {
                shownColour = 0xFFFFFF;
            }
        }
        if (!Game.isFixed()) {
            if ((getGame().backDialogueId != -1 || getGame().dialogueId != -1 || rsComponent1.disabledMessage.contains("Click here to continue")) && (rsComponent.id == getGame().backDialogueId || rsComponent.id == getGame().dialogueId)) {
                if (shownColour == 0xFFFF00) {
                    shownColour = 255;
                }
                if (shownColour == 49152) {
                    shownColour = 0xFFFFFF;
                }
            }
        }
        if ((rsComponent1.parentId == 1151) || (rsComponent1.parentId == 12855)) {
            switch (shownColour) {
                case 16773120:
                    shownColour = 0xFE981F;
                    break;
                case 7040819:
                    shownColour = 0xAF6A1A;
                    break;
            }
        }
        if (gameFont == null) {
            gameFont = getGame().regularFont;
        }
        for (int drawY = childY + gameFont.baseCharacterHeight; shownMessage.length() > 0; drawY += gameFont.baseCharacterHeight) {
            if (shownMessage.contains("%")) {
                if (shownMessage.equals("Combat Lvl: %1") && Game.getLocalPlayer().summoningLevel > 0) {
                    shownMessage = "Combat Lvl: %1+" + Game.getLocalPlayer().summoningLevel;
                }
                do {
                    int index1 = shownMessage.indexOf("%1");
                    if (index1 == -1) {
                        break;
                    }
                    if (rsComponent1.id < 4000 || rsComponent1.id > 5000 && rsComponent1.id != 13921 && rsComponent1.id != 13922 && rsComponent1.id != 12171 && rsComponent1.id != 12172) {
                        if (rsComponent1.id == 25915) {
                            shownMessage = shownMessage.substring(0, index1) + getGame().formatStackable(getGame().executeCS1Script(rsComponent1, 0), true) + shownMessage.substring(index1 + 2);
                        } else {
                            shownMessage = shownMessage.substring(0, index1) + getGame().formatStackable(getGame().executeCS1Script(rsComponent1, 0), false) + shownMessage.substring(index1 + 2);
                        }
                    } else {
                        shownMessage = shownMessage.substring(0, index1) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 0)) + shownMessage.substring(index1 + 2);
                    }
                } while (true);
                do {
                    int index2 = shownMessage.indexOf("%2");
                    if (index2 == -1) {
                        break;
                    }
                    shownMessage = shownMessage.substring(0, index2) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 1)) + shownMessage.substring(index2 + 2);
                } while (true);
                do {
                    int index3 = shownMessage.indexOf("%3");
                    if (index3 == -1) {
                        break;
                    }
                    shownMessage = shownMessage.substring(0, index3) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 2)) + shownMessage.substring(index3 + 2);
                } while (true);
                do {
                    int index4 = shownMessage.indexOf("%4");
                    if (index4 == -1) {
                        break;
                    }
                    shownMessage = shownMessage.substring(0, index4) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 3)) + shownMessage.substring(index4 + 2);
                } while (true);
                do {
                    int k8 = shownMessage.indexOf("%5");
                    if (k8 == -1) {
                        break;
                    }
                    shownMessage = shownMessage.substring(0, k8) + getGame().interfaceIntToString(getGame().executeCS1Script(rsComponent1, 4)) + shownMessage.substring(k8 + 2);
                } while (true);
            }
            int newLineIndex = shownMessage.indexOf("\\n");
            String drawnString;
            if (newLineIndex != -1) {
                drawnString = shownMessage.substring(0, newLineIndex);
                shownMessage = shownMessage.substring(newLineIndex + 2);
            } else {
                drawnString = shownMessage;
                shownMessage = "";
            }
            if (rsComponent1.centerText) {
                gameFont.drawCenteredString(drawnString, childX + rsComponent1.width / 2, drawY, shownColour, rsComponent1.textShadow);
            } else {
                gameFont.drawBasicString(drawnString, childX, drawY, shownColour, rsComponent1.textShadow);
            }
        }
    }
}

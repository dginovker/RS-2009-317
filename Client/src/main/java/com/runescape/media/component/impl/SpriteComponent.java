package com.runescape.media.component.impl;

import com.runescape.Game;
import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.Sprite;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.media.Raster;
import com.runescape.media.component.ComponentType;

/**
 * Represents the sprite {@link com.runescape.cache.media.RSComponent} to draw.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class SpriteComponent extends ComponentType {

    /**
     * Constructs a new <code>SpriteComponent</code>.
     *
     * @param game The {@link Game} instance.
     */
    public SpriteComponent(Game game) {
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
        Sprite sprite = getGame().interfaceIsSelected(rsComponent1) ? rsComponent1.getSpriteSet().getEnabled() : rsComponent1.getSpriteSet().getDisabled();
        if (rsComponent1.hidden) {
            sprite = SpriteRepository.EMPTY;
        }
        if (getGame().spellSelected == 1 && rsComponent1.id == Game.spellId && Game.spellId != 0 && sprite != null) {
            sprite.drawSprite(childX, childY, 0xFFFFFF);
            if (rsComponent1.drawsTransparent) {
                sprite.drawTransparentSprite(childX, childY, rsComponent.transparency);
            } else {
                sprite.drawSprite(childX, childY);
            }
        } else {
            if (rsComponent1.alphaBox) {
                Raster.drawAlphaFilledPixels(childX + rsComponent1.boxOffsetX, childY + rsComponent1.boxOffsetY,
                        rsComponent1.boxX, rsComponent1.boxY,
                        rsComponent1.boxColour, rsComponent1.boxAlpha);
            }
            if (sprite != null) {
                if (rsComponent1.drawsTransparent) {
                    sprite.drawTransparentSprite(childX, childY, rsComponent.transparency);
                } else {
                    if (rsComponent1.id == 50003 && getGame().getInterfaceConfig(InterfaceConfiguration.SKILL_MENU_SUBSECTION) > 2) { // TODO Convert to CS2
                        sprite = getGame().getInterfaceConfig(InterfaceConfiguration.SKILL_MENU_SUBSECTION) < 1 ? sprite : sprite.scale(sprite.myWidth, (getGame().getInterfaceConfig(InterfaceConfiguration.SKILL_MENU_SUBSECTION) * 19));
                        getGame().setInterfaceConfig(InterfaceConfiguration.SKILL_MENU_SUBSECTION_HEIGHT, 311 - (252 - (getGame().getInterfaceConfig(InterfaceConfiguration.SKILL_MENU_SUBSECTION) * 19)));
                    }
                    if (rsComponent1.id == 50004 && getGame().getInterfaceConfig(InterfaceConfiguration.SKILL_MENU_SUBSECTION) > 2) {
                        if (getGame().getInterfaceConfig(InterfaceConfiguration.SKILL_MENU_SUBSECTION) > 0) {
                            rsComponent.getInterfaceChild(50004).setY(getGame().getInterfaceConfig(InterfaceConfiguration.SKILL_MENU_SUBSECTION_HEIGHT) - 10);
                        }
                    }

                    if (sprite.spriteName != null && sprite.spriteName.contains("sworddecor") && (childX == 146 || childX == 318)) {
                        //  sprite.drawSprite(childX + ((childX == 146 || childX == 318) ? (sprite.spriteIndex == 1 ? -94 : 94) : 0), childY);
                    } else {
                        try {
                            sprite.drawSprite(childX, childY);
                        } catch (Exception e) {
                        }
                        // TODO Alpha values handled by RSComponent, values adjusted in #ComponentDrawing
                        if (((rsComponent1.id == 25054 || rsComponent1.id == 25055) ||
                                ((rsComponent1.id == 25634 || rsComponent1.id == 25635)) && !rsComponent1.hidden)) {
                            int alphaFlash = getGame().interfaceIsSelected(rsComponent1) ? 255 : getGame().spriteAlphaValues[0][0];
                            ImageLoader.forName("GE_SEARCH_BOX_OPACITY").drawTransparentSprite(childX, childY, alphaFlash);
                        }
                    }
                }
            }
        }
    }
}

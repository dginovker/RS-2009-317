package com.runescape.media.component.impl;

import com.runescape.Game;
import com.runescape.cache.media.RSComponent;
import com.runescape.media.Animation;
import com.runescape.media.component.ComponentType;
import com.runescape.media.renderable.Model;
import com.runescape.scene.graphic.Rasterizer;

/**
 * Represents the model {@link com.runescape.cache.media.RSComponent} to draw.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ModelComponent extends ComponentType {

    /**
     * Constructs a new <code>ModelComponent</code>.
     *
     * @param game The {@link Game} instance.
     */
    public ModelComponent(Game game) {
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
        int textureInt1 = Rasterizer.textureInt1;
        int textureInt2 = Rasterizer.textureInt2;
        Rasterizer.textureInt1 = childX + rsComponent1.width / 2;
        Rasterizer.textureInt2 = childY + rsComponent1.height / 2;
        int sineZoom = Rasterizer.SINE[rsComponent1.modelRotation2] * rsComponent1.modelZoom >> 16;
        int cosineZoom = Rasterizer.COSINE[rsComponent1.modelRotation2] * rsComponent1.modelZoom >> 16;
        boolean hover = getGame().interfaceIsSelected(rsComponent1);
        int emoteAnimation;
        if (hover) {
            emoteAnimation = rsComponent1.enabledAnimationId;
        } else {
            emoteAnimation = rsComponent1.disabledAnimationId;
        }
        Model model = null;
        if (emoteAnimation == -1) {
            model = rsComponent1.getAnimatedModel(-1, -1, hover);
        } else {
            Animation animation = Animation.animations[emoteAnimation];
            try {
                model = rsComponent1.getAnimatedModel(animation.modelFrames[rsComponent1.animationFrames], animation.frames[rsComponent1.animationFrames], hover);
            } catch (Exception e) {

            }
        }
        if (model != null) {
            model.renderSingle(rsComponent1.modelRotation1, 0, rsComponent1.modelRotation2, 0, sineZoom, cosineZoom);
        }
        Rasterizer.textureInt1 = textureInt1;
        Rasterizer.textureInt2 = textureInt2;
    }
}

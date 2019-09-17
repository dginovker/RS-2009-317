package com.runescape.media.renderable.entity;

import com.runescape.cache.def.npc.NPCDefinition;
import com.runescape.cache.def.SpotAnimation;
import com.runescape.cache.media.SequenceFrame;
import com.runescape.media.Animation;
import com.runescape.media.renderable.Model;

public final class NPC extends Entity {

    public NPCDefinition desc;

    private Model getAnimatedModel() {
        if (super.emoteAnimation >= 0 && super.animationDelay == 0) {
            int emote = Animation.animations[super.emoteAnimation].frames[super.displayedEmoteFrames];
            int movement = -1;
            if (super.movementAnimation >= 0 && super.movementAnimation != super.standAnimIndex) {
                movement = Animation.animations[super.movementAnimation].frames[super.displayedMovementFrames];
            }
            return desc.getAnimatedModel(movement, emote, Animation.animations[super.emoteAnimation].animationFlowControl);
        }
        int movement = -1;
        if (super.movementAnimation >= 0) {
            movement = Animation.animations[super.movementAnimation].frames[super.displayedMovementFrames];
        }
        return desc.getAnimatedModel(-1, movement, null);
    }

    public Model getRotatedModel() {
        if (desc == null) {
            return null;
        }
        Model animatedModel = getAnimatedModel();
        if (animatedModel == null) {
            return null;
        }
        super.height = animatedModel.modelHeight;
        if (super.gfxId != -1 && super.currentAnimation != -1) {
            SpotAnimation spotAnim = SpotAnimation.cache[super.gfxId];
            Model graphicModel = spotAnim.getModel();
            if (graphicModel != null) {
                int frame = spotAnim.animationSequence.frames[super.currentAnimation];
                Model model = new Model(true, SequenceFrame.method532(frame),
                        false, graphicModel);
                model.translate(0, -super.graphicHeight, 0);
                model.skin();
                model.apply(frame, spotAnim.animationSequence.osrs);
                model.faceGroups = null;
                model.vertexGroups = null;
                if (spotAnim.resizeXY != 128 || spotAnim.resizeZ != 128) {
                    model.scale(spotAnim.resizeXY, spotAnim.resizeXY,
                            spotAnim.resizeZ);
                }
                model.light(64 + spotAnim.modelBrightness,
                        850 + spotAnim.modelShadow, -30, -50, -30, true);
                Model models[] = {animatedModel, model};
                animatedModel = new Model(models);
            }
        }
        if (desc.size == 1) {
            animatedModel.fitsOnSingleSquare = true;
        }
        return animatedModel;
    }

    public boolean isVisible() {
        return desc != null;
    }
}

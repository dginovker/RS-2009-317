package com.runescape.scene;

import com.runescape.cache.def.SpotAnimation;
import com.runescape.cache.media.SequenceFrame;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.Renderable;

public final class SceneSpotAnim extends Renderable {

    public final int anInt1560;
    public final int anInt1561;
    public final int anInt1562;
    public final int anInt1563;
    public final int anInt1564;
    private final SpotAnimation currentGraphic;
    public boolean aBoolean1567;
    private int anInt1569;
    private int anInt1570;

    public SceneSpotAnim(int i, int j, int l, int i1, int j1, int k1, int l1) {
        aBoolean1567 = false;
        currentGraphic = SpotAnimation.cache[i1];
        anInt1560 = i;
        anInt1561 = l1;
        anInt1562 = k1;
        anInt1563 = j1;
        anInt1564 = j + l;
        aBoolean1567 = false;
    }

    public Model getRotatedModel() {
        Model model = currentGraphic.getModel();
        if (model == null) {
            return null;
        }
        int seqAnimId = currentGraphic.animationSequence.frames[anInt1569];
        Model model_1 = new Model(true, SequenceFrame.method532(seqAnimId), false, model);
        if (!aBoolean1567) {
            model_1.skin();
            model_1.apply(seqAnimId, currentGraphic.animationSequence.osrs);
            model_1.faceGroups = null;
            model_1.vertexGroups = null;
        }
        if (currentGraphic.resizeXY != 128 || currentGraphic.resizeZ != 128) {
            model_1.scale(currentGraphic.resizeXY, currentGraphic.resizeXY, currentGraphic.resizeZ);
        }
        if (currentGraphic.rotation != 0) {
            if (currentGraphic.rotation == 90) {
                model_1.method473();
            }
            if (currentGraphic.rotation == 180) {
                model_1.method473();
                model_1.method473();
            }
            if (currentGraphic.rotation == 270) {
                model_1.method473();
                model_1.method473();
                model_1.method473();
            }
        }
        model_1.light(64 + currentGraphic.modelBrightness, 850 + currentGraphic.modelShadow, -30, -50, -30, true);
        return model_1;
    }

    public void method454(int i) {
        for (anInt1570 += i; anInt1570 > currentGraphic.animationSequence.fetchAnimationLength(anInt1569); ) {
            anInt1570 -= currentGraphic.animationSequence.fetchAnimationLength(anInt1569) + 1;
            anInt1569++;
            if (anInt1569 >= currentGraphic.animationSequence.frameCount && (anInt1569 < 0 || anInt1569 >= currentGraphic.animationSequence.frameCount)) {
                anInt1569 = 0;
                aBoolean1567 = true;
            }
        }
    }
}
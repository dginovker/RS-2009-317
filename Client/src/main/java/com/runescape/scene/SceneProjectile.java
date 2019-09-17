package com.runescape.scene;

import com.runescape.cache.def.SpotAnimation;
import com.runescape.cache.media.SequenceFrame;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.Renderable;

public final class SceneProjectile extends Renderable {

    public final int anInt1571;
    public final int anInt1572;
    public final int anInt1583;
    public final int anInt1590;
    public final int anInt1597;
    private final int anInt1580;
    private final int anInt1581;
    private final int anInt1582;
    private final int initialAngle;
    private final int initialDistance;
    private final SpotAnimation currentGraphic;
    public double aDouble1585;
    public double aDouble1586;
    public double aDouble1587;
    public int anInt1595;
    private double aDouble1574;
    private double aDouble1575;
    private double aDouble1576;
    private double aDouble1577;
    private double aDouble1578;
    private boolean aBoolean1579;
    private int anInt1593;
    private int anInt1594;
    private int anInt1596;

    public SceneProjectile(int initialAngle, int j, int l, int i1, int initialDistance, int k1, int l1, int i2, int j2, int k2, int l2) {
        aBoolean1579 = false;
        currentGraphic = SpotAnimation.cache[l2];
        anInt1597 = k1;
        anInt1580 = j2;
        anInt1581 = i2;
        anInt1582 = l1;
        anInt1571 = l;
        anInt1572 = i1;
        this.initialAngle = initialAngle;
        this.initialDistance = initialDistance;
        anInt1590 = k2;
        anInt1583 = j;
        aBoolean1579 = false;
    }

    public void method455(int i, int j, int k, int l) {
        if (!aBoolean1579) {
            double d = l - anInt1580;
            double d2 = j - anInt1581;
            double d3 = Math.sqrt(d * d + d2 * d2);
            aDouble1585 = (double) anInt1580 + (d * (double) initialDistance) / d3;
            aDouble1586 = (double) anInt1581 + (d2 * (double) initialDistance) / d3;
            aDouble1587 = anInt1582;
        }
        double d1 = (anInt1572 + 1) - i;
        aDouble1574 = ((double) l - aDouble1585) / d1;
        aDouble1575 = ((double) j - aDouble1586) / d1;
        aDouble1576 = Math.sqrt(aDouble1574 * aDouble1574 + aDouble1575 * aDouble1575);
        if (!aBoolean1579) {
            aDouble1577 = -aDouble1576 * Math.tan((double) initialAngle * 0.02454369D);
        }
        aDouble1578 = (2D * ((double) k - aDouble1587 - aDouble1577 * d1)) / (d1 * d1);
    }

    public Model getRotatedModel() {
        Model model = currentGraphic.getModel();
        if (model == null) {
            return null;
        }
        int j = -1;
        if (currentGraphic.animationSequence != null) {
            j = currentGraphic.animationSequence.frames[anInt1593];
        }
        Model model_1 = new Model(true, SequenceFrame.method532(j), false, model);
        if (j != -1) {
            model_1.skin();
            model_1.apply(j, currentGraphic.animationSequence.osrs);
            model_1.faceGroups = null;
            model_1.vertexGroups = null;
        }
        if (currentGraphic.resizeXY != 128 || currentGraphic.resizeZ != 128) {
            model_1.scale(currentGraphic.resizeXY, currentGraphic.resizeXY, currentGraphic.resizeZ);
        }
        model_1.method474(anInt1596);
        model_1.light(64 + currentGraphic.modelBrightness, 850 + currentGraphic.modelShadow, -30, -50, -30, true);
        return model_1;
    }

    public void method456(int i) {
        aBoolean1579 = true;
        aDouble1585 += aDouble1574 * (double) i;
        aDouble1586 += aDouble1575 * (double) i;
        aDouble1587 += aDouble1577 * (double) i + 0.5D * aDouble1578 * (double) i * (double) i;
        aDouble1577 += aDouble1578 * (double) i;
        anInt1595 = (int) (Math.atan2(aDouble1574, aDouble1575) * 325.94900000000001D) + 1024 & 0x7ff;
        anInt1596 = (int) (Math.atan2(aDouble1577, aDouble1576) * 325.94900000000001D) & 0x7ff;
        if (currentGraphic != null && currentGraphic.animationSequence != null) {
            for (anInt1594 += i; anInt1594 > currentGraphic.animationSequence.fetchAnimationLength(anInt1593); ) {
                anInt1594 -= currentGraphic.animationSequence.fetchAnimationLength(anInt1593) + 1;
                anInt1593++;
                if (anInt1593 >= currentGraphic.animationSequence.frameCount) {
                    anInt1593 = 0;
                }
            }
        }
    }
}
package com.runescape.scene;

import main.java.com.runescape.Game;
import com.runescape.cache.config.VariableBits;
import com.runescape.cache.def.object.ObjectDefinition;
import com.runescape.media.Animation;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.Renderable;

public final class SceneObject extends Renderable {

    public static Game clientInstance;
    private final int[] anIntArray1600;
    private final int anInt1601;
    private final int anInt1602;
    private final int anInt1603;
    private final int anInt1604;
    private final int anInt1605;
    private final int anInt1606;
    private final int objectId;
    private final int anInt1611;
    private final int anInt1612;
    private int anInt1599;
    private Animation objectAnimation;
    private int anInt1608;

    public SceneObject(int objectId, int j, int k, int l, int i1, int j1, int k1, int animation, boolean flag) {
        this.objectId = objectId;
        anInt1611 = k;
        anInt1612 = j;
        anInt1603 = j1;
        anInt1604 = l;
        anInt1605 = i1;
        anInt1606 = k1;
        if (animation != -1) {
            objectAnimation = Animation.animations[animation];
            anInt1599 = 0;
            anInt1608 = Game.loopCycle;
            if (flag && objectAnimation.loopDelay != -1) {
                anInt1599 = (int) (Math.random() * (double) objectAnimation.frameCount);
                anInt1608 -= (int) (Math.random() * (double) objectAnimation.fetchAnimationLength(anInt1599));
            }
        }
        ObjectDefinition objectDefinition = ObjectDefinition.forId(this.objectId);
        anInt1601 = objectDefinition.configFileId;
        anInt1602 = objectDefinition.configId;
        anIntArray1600 = objectDefinition.childrenIds;
    }

    private ObjectDefinition getConfigChild() {
        int i = -1;
        if (anInt1601 != -1) {
            try {
                VariableBits varBit = VariableBits.cache[anInt1601];
                int k = varBit.getSetting();
                int l = varBit.getLow();
                int i1 = varBit.getHigh();
                int j1 = Game.BIT_MASKS[i1 - l];
                i = clientInstance.settings[k] >> l & j1;
            } catch (Exception ex) {
            }
        } else if (anInt1602 != -1) {
            i = clientInstance.settings[anInt1602];
        }
        if (i < 0 || i >= anIntArray1600.length || anIntArray1600[i] == -1) {
            return null;
        } else {
            return ObjectDefinition.forId(anIntArray1600[i]);
        }
    }

    public Model getRotatedModel() {
        int j = -1;
        if (objectAnimation != null) {
            int k = Game.loopCycle - anInt1608;
            if (k > 100 && objectAnimation.loopDelay > 0) {
                k = 100;
            }
            while (k > objectAnimation.fetchAnimationLength(anInt1599)) {
                k -= objectAnimation.fetchAnimationLength(anInt1599);
                anInt1599++;
                if (anInt1599 < objectAnimation.frameCount) {
                    continue;
                }
                anInt1599 -= objectAnimation.loopDelay;
                if (anInt1599 >= 0 && anInt1599 < objectAnimation.frameCount) {
                    continue;
                }
                objectAnimation = null;
                break;
            }
            anInt1608 = Game.loopCycle - k;
            if (objectAnimation != null) {
                j = objectAnimation.frames[anInt1599];
            }
        }
        ObjectDefinition objectDefinition;
        if (anIntArray1600 != null) {
            objectDefinition = getConfigChild();
        } else {
            objectDefinition = ObjectDefinition.forId(objectId);
        }
        if (objectDefinition == null) {
            return null;
        } else {
            return objectDefinition.modelAt(anInt1611, anInt1612, anInt1603, anInt1604, anInt1605, anInt1606, j);
        }
    }
}
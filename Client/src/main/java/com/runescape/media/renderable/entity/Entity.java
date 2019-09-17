package com.runescape.media.renderable.entity;

import com.runescape.media.Animation;
import com.runescape.media.renderable.Renderable;

public class Entity extends Renderable {

    public final int[] pathX;
    public final int[] pathY;
    public final int[] hitDamages;
    public final int[] hitIcons;
    public final int[] hitMarkTypes;
    public int[] hitmarkMove = new int[4];
    public int[] moveTimer = new int[4];
    public int[] hitmarkTrans = new int[4];
    public int[] hitIcon = new int[4];
    public int[] hitMarkPos = new int[4];
    public final int[] hitsLoopCycle;
    public final boolean[] pathRun;
    public final int index = -1;
    public int interactingEntity;
    public int anInt1503;
    public int degreesToTurn;
    public int runAnimIndex;
    public String spokenText;
    public int height;
    public int turnDirection;
    public int standAnimIndex;
    public int standTurnAnimIndex;
    public int textColour;
    public int movementAnimation;
    public int displayedMovementFrames;
    public int anInt1519;
    public int gfxId;
    public int currentAnimation;
    public int anInt1522;
    public int graphicDelay;
    public int graphicHeight;
    public int smallXYIndex;
    public int emoteAnimation;
    public int displayedEmoteFrames;
    public int emoteTimeRemaining;
    public int animationDelay;
    public int currentAnimationLoops;
    public int textEffect;
    public int loopCycleStatus;
    public int currentHealth;
    public int maxHealth;
    public int textCycle;
    public int anInt1537;
    public int faceX;
    public int faceY;
    public int boundDim;
    public boolean aBoolean1541;
    public int anInt1542;
    public int initialX;
    public int destinationX;
    public int initialY;
    public int destinationY;
    public int startForceMovement;
    public int endForceMovement;
    public int direction;
    public int x;
    public int y;
    public int anInt1552;
    public int walkAnimIndex;
    public int turn180AnimIndex;
    public int turn90CWAnimIndex;
    public int turn90CCWAnimIndex;
    public int nextAnimationFrame;
    public int nextGraphicsAnimationFrame;
    public int nextIdleAnimationFrame;
    public int entScreenX;
    public int entScreenY;

    public Entity() {
        pathX = new int[10];
        pathY = new int[10];
        interactingEntity = -1;
        degreesToTurn = 32;
        runAnimIndex = -1;
        height = 200;
        standAnimIndex = -1;
        standTurnAnimIndex = -1;
        hitDamages = new int[4];
        hitIcons = new int[4];
        hitMarkTypes = new int[4];
        hitsLoopCycle = new int[4];
        movementAnimation = -1;
        gfxId = -1;
        emoteAnimation = -1;
        loopCycleStatus = -1000;
        textCycle = 100;
        boundDim = 1;
        aBoolean1541 = false;
        pathRun = new boolean[10];
        walkAnimIndex = -1;
        turn180AnimIndex = -1;
        turn90CWAnimIndex = -1;
        turn90CCWAnimIndex = -1;
    }

    public final void setPos(int x, int y, boolean flag) {
        if (emoteAnimation != -1 && Animation.animations[emoteAnimation].anInt364 == 1) {
            emoteAnimation = -1;
        }
        if (!flag) {
            int dx = x - pathX[0];
            int dy = y - pathY[0];
            if (dx >= -8 && dx <= 8 && dy >= -8 && dy <= 8) {
                if (smallXYIndex < 9) {
                    smallXYIndex++;
                }
                for (int i1 = smallXYIndex; i1 > 0; i1--) {
                    pathX[i1] = pathX[i1 - 1];
                    pathY[i1] = pathY[i1 - 1];
                    pathRun[i1] = pathRun[i1 - 1];
                }

                pathX[0] = x;
                pathY[0] = y;
                pathRun[0] = false;
                return;
            }
        }
        smallXYIndex = 0;
        anInt1542 = 0;
        anInt1503 = 0;
        pathX[0] = x;
        pathY[0] = y;
        this.x = pathX[0] * 128 + boundDim * 64;
        this.y = pathY[0] * 128 + boundDim * 64;
    }

    public final void resetPath() {
        smallXYIndex = 0;
        anInt1542 = 0;
    }

    public final void updateHitData(int hitType, int hitDamage, int currentTime, int hitIcon) {
        for (int hitPtr = 0; hitPtr < 4; hitPtr++) {
            if (hitsLoopCycle[hitPtr] <= currentTime) {
                hitDamages[hitPtr] = hitDamage;
                hitIcons[hitPtr] = hitIcon;
                hitmarkTrans[hitPtr] = 0;
                hitMarkTypes[hitPtr] = hitType;
                hitsLoopCycle[hitPtr] = currentTime + 70;
                return;
            }
        }
    }

    public final void moveInDir(boolean run, int direction) {
        int x = pathX[0];
        int y = pathY[0];
        if (direction == 0) {
            x--;
            y++;
        }
        if (direction == 1) {
            y++;
        }
        if (direction == 2) {
            x++;
            y++;
        }
        if (direction == 3) {
            x--;
        }
        if (direction == 4) {
            x++;
        }
        if (direction == 5) {
            x--;
            y--;
        }
        if (direction == 6) {
            y--;
        }
        if (direction == 7) {
            x++;
            y--;
        }
        if (emoteAnimation != -1 && Animation.animations[emoteAnimation].anInt364 == 1) {
            emoteAnimation = -1;
        }
        if (smallXYIndex < 9) {
            smallXYIndex++;
        }
        for (int l = smallXYIndex; l > 0; l--) {
            pathX[l] = pathX[l - 1];
            pathY[l] = pathY[l - 1];
            pathRun[l] = pathRun[l - 1];
        }
        pathX[0] = x;
        pathY[0] = y;
        pathRun[0] = run;
    }

    public boolean isVisible() {
        return false;
    }
}

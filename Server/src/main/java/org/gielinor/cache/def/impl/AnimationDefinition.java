package org.gielinor.cache.def.impl;


import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import org.gielinor.game.world.callback.CallBack;
import org.gielinor.utilities.RSStream;
import org.gielinor.utilities.misc.FileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AnimationDefinition implements CallBack {

    private static final Logger log = LoggerFactory.getLogger(AnimationDefinition.class);

    private static final HashMap<Integer, AnimationDefinition> animations = new HashMap<>();
    public int frameCount;
    public int[] frames;
    public int[] modelFrames;
    public int[] delays;
    public int loopDelay;
    public int[] animationFlowControl;
    public boolean aBoolean358;
    public int anInt359;
    public int anInt360;
    public int anInt361;
    public int anInt362;
    public int anInt363;
    public int anInt364;
    public int anInt365;
    public boolean osrs;

    public AnimationDefinition() {
        loopDelay = -1;
        aBoolean358 = false;
        anInt359 = 5;
        anInt360 = -1;
        anInt361 = -1;
        anInt362 = 99;
        anInt363 = -1;
        anInt364 = -1;
        anInt365 = 1;
        osrs = false;
    }

    @Override
    public boolean call() {
        RSStream rsStream = new RSStream(FileOperations.getBytes(new File("data" + File.separator + "animation_data.dat")));
        int length = rsStream.getShort();
        animations.put(-1, new AnimationDefinition());
        for (int index = 0; index < length; index++) {
            if (animations.get(index) == null) {
                AnimationDefinition animationDefinition = new AnimationDefinition();
                animationDefinition.readValues(rsStream);
                animations.put(index, new AnimationDefinition());
            }
            if (index < 16000) {
                animations.get(index).osrs = false;
            }
        }
        return true;
    }

    public static AnimationDefinition forId(int emoteId) {
        return animations.get(emoteId);
    }

    /**
     * Gets the duration of this animation in milliseconds.
     *
     * @return The duration.
     */
    public int getDuration() {
        if (delays == null) {
            return 0;
        }
        int duration = 0;
        for (int i : delays) {
            duration += i * 30;
        }
        return duration;
    }

    /**
     * Gets the duration of this animation in (600ms) ticks.
     *
     * @return The duration in ticks.
     */
    public int getDurationTicks() {
        return Math.round(getDuration() / 1200);
    }


    public void readValues(RSStream stream) {
        int[] opcodeHistory = new int[5];
        do {
            int opcode = stream.getByte();
            if (opcode == 0) {
                break;
            }
            switch (opcode) {
                case 1:
                    frameCount = stream.getShort();
                    frames = new int[frameCount];
                    modelFrames = new int[frameCount];
                    delays = new int[frameCount];
                    for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
                        frames[frameIndex] = stream.getInt();
                        modelFrames[frameIndex] = -1;
                    }
                    for (int delayIndex = 0; delayIndex < frameCount; delayIndex++) {
                        delays[delayIndex] = stream.getByte();
                    }
                    break;

                case 2:
                    loopDelay = stream.getShort();
                    break;
                case 3:
                    int flowCount = stream.getByte();
                    animationFlowControl = new int[flowCount + 1];
                    for (int flowIndex = 0; flowIndex < flowCount; flowIndex++) {
                        animationFlowControl[flowIndex] = stream.getByte();
                    }
                    animationFlowControl[flowCount] = 0x98967f;
                    break;
                case 4:
                    aBoolean358 = true;
                    break;
                case 5:
                    anInt359 = stream.getByte();
                    break;
                case 6:
                    anInt360 = stream.getShort();
                    break;
                case 7:
                    anInt361 = stream.getShort();
                    break;
                case 8:
                    anInt362 = stream.getByte();
                    break;
                case 9:
                    anInt363 = stream.getByte();
                    break;
                case 10:
                    anInt364 = stream.getByte();
                    break;
                case 11:
                    anInt365 = stream.getByte();
                    break;
                case 12:
                    osrs = true;
                    break;
                default:
                    log.warn("Unknown animation opcode: {}. Previous: {}.",
                        opcode, Arrays.toString(opcodeHistory));
                    break;
            }
            System.arraycopy(opcodeHistory, 0, opcodeHistory, 1, opcodeHistory.length - 2 + 1);
            opcodeHistory[0] = opcode;
        } while (true);
        if (frameCount == 0) {
            frameCount = 1;
            frames = new int[1];
            frames[0] = -1;
            modelFrames = new int[1];
            modelFrames[0] = -1;
            delays = new int[1];
            delays[0] = -1;
        }
        if (anInt363 == -1) {
            if (animationFlowControl != null) {
                anInt363 = 2;
            } else {
                anInt363 = 0;
            }
        }
        if (anInt364 == -1) {
            if (animationFlowControl != null) {
                anInt364 = 2;
                return;
            }
            anInt364 = 0;
        }
    }
}

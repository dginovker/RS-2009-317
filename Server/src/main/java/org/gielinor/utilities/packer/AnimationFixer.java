package org.gielinor.utilities.packer;

import java.util.Arrays;

import org.gielinor.utilities.RSStream;
import org.gielinor.utilities.misc.FileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AnimationFixer {

    private static final Logger log = LoggerFactory.getLogger(AnimationFixer.class);

    public static AnimationFixer[] animations;
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

    public AnimationFixer() {
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

    public static void main(String[] args) {
        RSStream rsStream = new RSStream(FileOperations.readFile("oseq.dat"));
        int length = rsStream.getShort();
        log.info("Oseq length={}", length);
        if (animations == null) {
            animations = new AnimationFixer[length];
        }
        for (int index = 0; index < length; index++) {
            if (animations[index] == null) {
                animations[index] = new AnimationFixer();
            }
            animations[index].readValues(rsStream);
        }
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
                    log.info("Unknown animation opcode [{}]. History: {}.",
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

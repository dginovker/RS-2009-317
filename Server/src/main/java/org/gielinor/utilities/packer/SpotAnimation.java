package org.gielinor.utilities.packer;


import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.gielinor.utilities.RSStream;
import org.gielinor.utilities.misc.FileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Refactored SpotAnimation using rename317
 * https://code.google.com/p/rename317/source/browse/trunk/src/rs2/SpotAnim.java?r=168
 */
public final class SpotAnimation {

    private static final Logger log = LoggerFactory.getLogger(SpotAnimation.class);

    public static SpotAnimation[] cache;
    private static int spotAnimationCount;
    private final int[] originalModelColours;
    private final int[] modifiedModelColours;
    public int animationId;
    public int resizeXY;
    public int resizeZ;
    public int rotation;
    public int modelBrightness;
    public int modelShadow;
    int colourCount;
    private int id;
    private int modelId;

    private SpotAnimation() {
        modelId = -1;
        animationId = -1;
        originalModelColours = new int[6];
        modifiedModelColours = new int[6];
        resizeXY = 128;
        resizeZ = 128;
    }


    public static int getSpotAnimationCount() {
        return spotAnimationCount;
    }

    public static void main(String[] args) {
        RSStream stream = new RSStream(FileOperations.readFile("spotanim.dat"));
        spotAnimationCount = stream.getShort();
        if (cache == null) {
            cache = new SpotAnimation[spotAnimationCount];
        }
        for (int index = 0; index < (spotAnimationCount); index++) {
            if (cache[index] == null) {
                cache[index] = new SpotAnimation();
            }
            cache[index].id = index;
            cache[index].readValues(stream);
            // Find data here
            if (cache[index].modelId == 21102) {
                System.out.println(cache[index].animationId);
            }
        }
    }

    public static void save() {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("spotanim_new.dat"))) {
            out.writeShort(spotAnimationCount);
            for (SpotAnimation animation : cache) {
                if (animation == null) {
                    out.writeByte(0);
                    continue;
                }
                if (animation.modelId != -1) {
                    out.writeByte(1);
                    out.writeShort(animation.modelId);
                }
                if (animation.animationId != -1) {
                    out.writeByte(2);
                    out.writeShort(animation.animationId);
                }
                if (animation.resizeXY != 128) {
                    out.writeByte(3);
                    out.writeShort(animation.resizeXY);
                }
                if (animation.resizeZ != 128) {
                    out.writeByte(4);
                    out.writeShort(animation.resizeZ);
                }
                if (animation.rotation != -1) {
                    out.writeByte(5);
                    out.writeShort(animation.rotation);
                }
                if (animation.modelBrightness != -1) {
                    out.writeByte(6);
                    out.writeByte(animation.modelBrightness);
                }
                if (animation.modelShadow != -1) {
                    out.writeByte(7);
                    out.writeByte(animation.modelShadow);
                }
                if (animation.colourCount != -1) {
                    out.writeByte(8);
                    out.writeByte(animation.colourCount);
                    for (int colourIndex = 0; colourIndex < animation.colourCount; colourIndex++) {
                        out.writeShort(animation.originalModelColours[colourIndex]);
                        out.writeShort(animation.modifiedModelColours[colourIndex]);
                    }
                }
                out.writeByte(0);
            }
        } catch (IOException ex) {
            log.error("Failed to write SpotAnimation.", ex);
        }
    }

    public void readValues(RSStream stream) {
        int[] opcodeHistory = new int[5];
        do {
            int opcode = stream.getByte();
            if (opcode == 0) {
                return;
            }
            switch (opcode) {
                case 1:
                    modelId = stream.getShort();
                    break;
                case 2:
                    animationId = stream.getShort();
                    break;
                case 4:
                    resizeXY = stream.getShort();
                    break;
                case 5:
                    resizeZ = stream.getShort();
                    break;
                case 6:
                    rotation = stream.getShort();
                    break;
                case 7:
                    modelBrightness = stream.getShort();// stream.getByte();
                    break;
                case 8:
                    modelShadow = stream.getShort();// stream.getByte();
                    break;
                case 40:
                    colourCount = stream.getByte();
                    for (int colourIndex = 0; colourIndex < colourCount; colourIndex++) {
                        originalModelColours[colourIndex] = stream.getShort();
                        modifiedModelColours[colourIndex] = stream.getShort();
                    }
                    break;
                default:
                    log.warn("Unknown spot animation opcode [{}]. History: {}.",
                        opcode, Arrays.toString(opcodeHistory));
                    break;
            }
            System.arraycopy(opcodeHistory, 0, opcodeHistory, 1, opcodeHistory.length - 2 + 1);
            opcodeHistory[0] = opcode;
        } while (true);
    }

}

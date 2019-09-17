package com.runescape.cache.def;

import com.runescape.Constants;
import com.runescape.collection.ReferenceCache;
import com.runescape.media.Animation;
import com.runescape.media.renderable.Model;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.util.FileUtility;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Refactored SpotAnimation using rename317
 * https://code.google.com/p/rename317/source/browse/trunk/src/rs2/SpotAnim.java?r=168
 */
public final class SpotAnimation {
    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(SpotAnimation.class.getName());
    public static SpotAnimation[] cache;
    public static ReferenceCache models = new ReferenceCache(30);
    public static ReferenceCache osrsModels = new ReferenceCache(30);
    private static int spotAnimationCount;
    private static int spotAnimationCountOSRS;

    public int animationId;
    public Animation animationSequence;
    public int resizeXY;
    public int resizeZ;
    public int rotation;
    public int modelBrightness;
    public int modelShadow;
    int colourCount;
    boolean osrs;
    private short[] originalModelColours;
    private short[] modifiedModelColours;
    private int id;
    private int modelId;
    private short[] originalModelTextures;
    private short[] modifiedModelTextures;

    private SpotAnimation() {
        modelId = -1;
        animationId = -1;
        resizeXY = 128;
        resizeZ = 128;
        originalModelColours = new short[6];
        modifiedModelColours = new short[6];
    }

    public static int getSpotAnimationCount() {
        return spotAnimationCount;
    }

    public static void unpackConfig(CacheArchive cacheArchive) {
        RSStream rsStream = new RSStream(FileUtility.getBytes(Constants.getCachePath(true) + "/config/498spotanim.dat"));
        spotAnimationCount = rsStream.getShort();

        RSStream osrsStream = new RSStream(FileUtility.getBytes(Constants.getCachePath(true) + "/config/spotanim159.dat"));
        spotAnimationCountOSRS = osrsStream.getShort();

        if (cache == null) {
            cache = new SpotAnimation[20_000 + spotAnimationCountOSRS]; // TODO Count after adding new
        }
        for (int index = 0; index < 20_000 + spotAnimationCountOSRS; index++) {
            if (cache[index] == null) {
                cache[index] = new SpotAnimation();
            }
            cache[index].id = index;
            if (index < spotAnimationCount) {
                cache[index].osrs = false;
                cache[index].readValues(rsStream);
            } else if (index >= 20_000 && index < 20_000 + spotAnimationCountOSRS) {
                cache[index].osrs = true;
                cache[index].decode(osrsStream);
            }
        }
    }

    public static void save() {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("spotanim_new.dat"));
            dataOutputStream.writeShort(cache.length);
            for (SpotAnimation animation : cache) {
                if (animation == null) {
                    dataOutputStream.writeByte(0);
                    continue;
                }
                if (animation.modelId != -1) {
                    dataOutputStream.writeByte(1);
                    dataOutputStream.writeShort(animation.modelId);
                }
                if (animation.animationId != -1) {
                    dataOutputStream.writeByte(2);
                    dataOutputStream.writeShort(animation.animationId);
                }
                if (animation.resizeXY != 128) {
                    dataOutputStream.writeByte(3);
                    dataOutputStream.writeShort(animation.resizeXY);
                }
                if (animation.resizeZ != 128) {
                    dataOutputStream.writeByte(4);
                    dataOutputStream.writeShort(animation.resizeZ);
                }
                if (animation.rotation != -1) {
                    dataOutputStream.writeByte(5);
                    dataOutputStream.writeShort(animation.rotation);
                }
                if (animation.modelBrightness != -1) {
                    dataOutputStream.writeByte(6);
                    dataOutputStream.writeByte(animation.modelBrightness);
                }
                if (animation.modelShadow != -1) {
                    dataOutputStream.writeByte(7);
                    dataOutputStream.writeByte(animation.modelShadow);
                }
                if (animation.colourCount != -1) {
                    dataOutputStream.writeByte(8);
                    dataOutputStream.writeByte(animation.colourCount);
                    for (int colourIndex = 0; colourIndex < animation.colourCount; colourIndex++) {
                        dataOutputStream.writeShort(animation.originalModelColours[colourIndex]);
                        dataOutputStream.writeShort(animation.modifiedModelColours[colourIndex]);
                    }
                }
                if (animation.osrs) {
                    dataOutputStream.writeByte(9);
                }
                dataOutputStream.writeByte(0);
            }
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Packed {0} spot animations.", cache.length);
    }
    
    private void readValues(RSStream stream) {
        do {
            int i = stream.getByte();
            if (i == 0)
                return;
            if (i == 1)
                modelId = stream.getShort();
            else if (i == 2) {
                animationId = stream.getShort();
                if (Animation.animations != null)
                    animationSequence = Animation.animations[animationId];
            } else if (i == 4)
                resizeXY = stream.getShort();
            else if (i == 5)
                resizeZ = stream.getShort();
            else if (i == 6)
                rotation = stream.getShort();
            else if (i == 7)
                modelBrightness = stream.getByte();
            else if (i == 8)
                modelShadow = stream.getByte();
            else if (i == 40) {
                colourCount = stream.getByte();
                for (int k = 0; k < colourCount; k++) {
                    originalModelColours[k] = (short) stream.getShort();
                    modifiedModelColours[k] = (short) stream.getShort();
                }
            } else
                System.out.println("Error unrecognised spotanim config code: "
                        + i);
        } while (true);
    }
    

    void decode(RSStream buffer) {
        while (true) {
            int opcode = buffer.getByte();
            if (opcode == 0)
                return;

            this.decode(buffer, opcode);
        }
    }

    void decode(RSStream buffer, int opcode) {
        if (opcode == 1)
            modelId = buffer.getShort();
        else if (opcode == 2)
            animationId = osrs ? buffer.getShort() + 20_000 : buffer.getShort();
        else if (opcode == 4)
            resizeXY = buffer.getShort();
        else if (opcode == 5)
            resizeZ = buffer.getShort();
        else if (opcode == 6)
            rotation = buffer.getShort();
        else if (opcode == 7)
            modelBrightness = buffer.getByte();
        else if (opcode == 8)
            modelShadow = buffer.getByte();
        else if (opcode == 9) {

        } else if (opcode == 10) {

        } else {
            int i_4;
            int i_5;
            if (opcode == 40) {
                i_4 = buffer.getByte();

                for (i_5 = 0; i_5 < i_4; i_5++) {
                    originalModelColours[i_5] = (short) buffer.getShort();
                    modifiedModelColours[i_5] = (short) buffer.getShort();
                }
            } else if (opcode == 41) {
                i_4 = buffer.getByte();
                originalModelTextures = new short[i_4];
                modifiedModelTextures = new short[i_4];

                for (i_5 = 0; i_5 < i_4; i_5++) {
                    originalModelTextures[i_5] = (short) buffer.getShort();
                    modifiedModelTextures[i_5] = (short) buffer.getShort();
                }
            } else {
                System.out.println("Fucked: " + opcode + " " + osrs);
            }
        }

    }

    public Model getModel() {
        Model model = (Model) (osrs ? osrsModels.get(id) : models.get(id));
        if (model != null) {
            return model;
        }
        model = Model.getModel(modelId, osrs);
        if (model == null) {
            return null;
        }
        for (int i = 0; i < 6; i++) {
            if (originalModelColours[0] != 0) {
                model.recolor(originalModelColours[i], modifiedModelColours[i]);
            }
        }
        if (osrs) {
            osrsModels.put(model, id);
            return model;
        }
        models.put(model, id);
        return model;
    }
}

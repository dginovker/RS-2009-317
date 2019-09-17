package com.runescape.cache.def;

import com.runescape.Constants;
import com.runescape.media.renderable.Model;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.util.FileUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class IdentityKit {
    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(IdentityKit.class.getName());
    public static int length;
    public static IdentityKit[] kits;
    private final int[] headModels = {-1, -1, -1, -1, -1};
    public int part;
    public boolean validStyle;
    private short[] originalColors;
    private short[] replacementColors;
    private int[] bodyModels;
    private short[] originalTexture;
    private short[] replacementTexture;

    private IdentityKit() {
        part = -1;
        originalColors = new short[6];
        replacementColors = new short[6];
        validStyle = false;
    }

    public static void unpackConfig(CacheArchive cacheArchive) {
        RSStream stream = new RSStream(FileUtility.getBytes(Constants.getCachePath(true) + "/config/idk159.dat"));
        length = stream.getShort();
        if (kits == null) {
            kits = new IdentityKit[length];
        }
        for (int id = 0; id < length; id++) {
            if (kits[id] == null) {
                kits[id] = new IdentityKit();
            }
            kits[id].readValues(stream);
            kits[id].originalColors[0] = (short) 55232;
            kits[id].replacementColors[0] = 6798;
        }
    }

    private void readValues(RSStream buffer) {
        do {
            int opcode = buffer.getByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                part = buffer.getByte();
            } else if (opcode == 2) {
                int count = buffer.getByte();
                bodyModels = new int[count];
                for (int part = 0; part < count; part++) {
                    bodyModels[part] = buffer.getShort();
                }

            } else if (opcode == 3) {
                validStyle = true;
            } else if (opcode == 40) {
                int count = buffer.getByte();
                for (int index = 0; index < count; index++) {
                    originalColors[index] = (short) buffer.getShort();
                    replacementColors[index] = (short) buffer.getShort();
                }
            } else if (opcode == 41) {
                int count = buffer.getByte();
                originalTexture = new short[count];
                replacementTexture = new short[count];

                for (int index = 0; index < count; index++) {
                    originalTexture[index] = (short) buffer.getShort();
                    replacementTexture[index] = (short) buffer.getShort();
                }
            } else if (opcode >= 60 && opcode < 70) {
                headModels[opcode - 60] = buffer.getShort();
            } else {
                if (Constants.DEBUG_MODE) {
                    logger.log(Level.WARNING, "Invalid identity kit opcode: {0}!", opcode);
                }
            }
        } while (true);
    }

    public boolean bodyLoaded() {
        if (bodyModels == null) {
            return true;
        }
        boolean ready = true;
        for (int bodyModel : bodyModels) {
            if (!Model.isCached(bodyModel, true)) {
                ready = false;
            }
        }
        return ready;
    }

    public Model bodyModel() {
        if (bodyModels == null) {
            return null;
        }
        Model models[] = new Model[bodyModels.length];
        for (int part = 0; part < bodyModels.length; part++) {
            models[part] = Model.getModel(bodyModels[part], true);
        }

        Model model;
        if (models.length == 1) {
            model = models[0];
        } else {
            model = new Model(models.length, models);
        }
        for (int part = 0; part < 6; part++) {
            if (originalColors[part] == 0) {
                break;
            }
            model.recolor(originalColors[part], replacementColors[part]);
        }

        return model;
    }

    public boolean headLoaded() {
        boolean ready = true;
        for (int part = 0; part < 5; part++) {
            if (headModels[part] != -1 && !Model.isCached(headModels[part], true)) {
                ready = false;
            }
        }

        return ready;
    }

    public Model headModel() {
        Model models[] = new Model[5];
        int count = 0;
        for (int part = 0; part < 5; part++) {
            if (headModels[part] != -1) {
                models[count++] = Model.getModel(headModels[part], true);
            }
        }

        Model model = new Model(count, models);
        for (int part = 0; part < 6; part++) {
            if (originalColors[part] == 0) {
                break;
            }
            model.recolor(originalColors[part], replacementColors[part]);
        }

        return model;
    }
}

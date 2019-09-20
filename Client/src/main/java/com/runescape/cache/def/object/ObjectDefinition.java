package com.runescape.cache.def.object;

import com.runescape.Game;
import com.runescape.cache.config.VariableBits;
import com.runescape.cache.def.DefinitionData;
import com.runescape.cache.def.impl.ObjectRepository;
import com.runescape.cache.media.SequenceFrame;
import com.runescape.collection.ReferenceCache;
import com.runescape.media.renderable.Model;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.net.requester.ResourceProvider;

import java.io.IOException;

public class ObjectDefinition {
    public short[] textureToFind;
    public short[] textureToReplace;
    public static final Model[] modelArray = new Model[4];
    public static final Model[] osrsModelArray = new Model[4];
    public static ReferenceCache models = new ReferenceCache(30);
    public static ReferenceCache baseModels = new ReferenceCache(500);
    public static ReferenceCache osrsModels = new ReferenceCache(30);
    public static ReferenceCache osrsBaseModels = new ReferenceCache(500);
    public static boolean lowMemory;
    public static Game clientInstance;
    public static ObjectDefinition[] cache;
    private static DefinitionData definitionData = new DefinitionData();
    public boolean obstructsGround;
    public byte ambientLighting;
    public int translateX;
    public String name;
    public int scaleZ;
    public byte lightDiffusion;
    public int sizeX;
    public int translateY;
    public int mapIcon;
    public int[] originalModelColors;
    public int scaleX;
    public int configId;
    public boolean inverted;
    public int type;
    public boolean impenetrable;
    public int mapscene;
    public int[] childrenIds;
    public int supportItems;
    public int sizeY;
    public boolean contouredGround;
    public boolean occludes;
    public boolean hollow;
    public boolean projectileClipped;
    public int surroundings;
    public boolean delayShading;
    public int scaleY;
    public int[] modelIds;
    public int configFileId;
    public int decorDisplacement;
    public int[] modelTypes;
    public byte[] description;
    public boolean isInteractive;
    public boolean castsShadow;
    public int animation;
    public int translateZ;
    public int[] modifiedModelColors;
    public String[] interactions;
    public boolean osrs;
    public int clipType;

    public ObjectDefinition() {
        type = -1;
    }

    public static ObjectDefinition forId(int objectId) {
        if (objectId >= 0) {
            return OSRSObjectDefinition.forId(objectId);
        }
        if (objectId > definitionData.getIndices().length) {
            objectId = definitionData.getIndices().length - 1;
        }
        for (int index = 0; index < 20; index++) {
            if (cache[index].type == objectId) {
                return cache[index];
            }
        }
        definitionData.setCacheIndex((definitionData.getCacheIndex() + 1) % 20);
        ObjectDefinition objectDefinition = cache[definitionData.getCacheIndex()];
        definitionData.getBuffer().currentPosition = definitionData.getOffset(objectId);
        objectDefinition.type = objectId;
        objectDefinition.reset();
        if (objectId >= 28266 && objectId <= 28295) {
            objectDefinition.name = "Snowman";
            objectDefinition.modelIds = new int[]{45693 + (objectId - 28266)};
            objectDefinition.interactions = new String[]{"Add-to", null, null, null, null};
            objectDefinition.isInteractive = true;
            return objectDefinition;
        }
        switch (objectId) {
            case 28296:
            case 28297:
                objectDefinition.name = "Snow";
                objectDefinition.modelIds = new int[]{objectId == 28296 ? 45724 : 45723};
                objectDefinition.interactions = new String[]{"Collect", null, null, null, null};
                objectDefinition.isInteractive = true;
                return objectDefinition;

            case 28717:
                objectDefinition.name = "Inert obelisk";
                objectDefinition.modelIds = new int[]{45727};
                objectDefinition.isInteractive = true;
                objectDefinition.animation = 8510;
                return objectDefinition;

            case 28718:
                objectDefinition.name = "Inert obelisk";
                objectDefinition.modelIds = new int[]{45728};
                objectDefinition.isInteractive = true;
                objectDefinition.animation = 8510;
                return objectDefinition;

            case 28716:
                objectDefinition.name = "Obelisk";
                objectDefinition.interactions = new String[]{"Infuse-Pouch", "Renew-Points", null, null, null,};
                objectDefinition.modelIds = new int[]{45729};
                objectDefinition.sizeX = 2;
                objectDefinition.sizeY = 2;
                objectDefinition.isInteractive = true;
                objectDefinition.animation = 8510;
                return objectDefinition;

            case 28714:
                objectDefinition.name = "Ladder";
                objectDefinition.interactions = new String[]{"Climb", null, null, null, null,};
                objectDefinition.modelIds = new int[]{45730};
                objectDefinition.isInteractive = true;
                return objectDefinition;

            case 29882:
                objectDefinition.name = "Small obelisk";
                objectDefinition.interactions = new String[]{"Renew-points", null, null, null, null};
                objectDefinition.modelIds = new int[]{45728};
                objectDefinition.isInteractive = true;
                objectDefinition.animation = 8510;
                return objectDefinition;
        }
        objectDefinition.readValues(definitionData.getBuffer());
        objectDefinition = ObjectRepository.forId(objectId, objectDefinition);
        return objectDefinition;
    }

    public static void nullLoader() {
        baseModels = null;
        models = null;
        osrsBaseModels = null;
        osrsModels = null;
        cache = null;
        definitionData.clear();
    }

    public static void unpackConfig(CacheArchive cacheArchive) throws IOException {
        //definitionData.load(cacheArchive, "loc", false, 5000);

        cache = new ObjectDefinition[20];
        for (int index = 0; index < 20; index++) {
            cache[index] = new ObjectDefinition();
        }
    }

    public void readValues(RSStream buffer) {
        while (true) {
            final int opcode = buffer.getByte();
            if (opcode == 0) {
                return;
            }

            readValues(buffer, opcode);
        }
    }

    public void readValues(RSStream buffer, final int opcode) {
        int count;
        int index;
        if (opcode == 1) {
            count = buffer.getByte();
            if (count > 0) {
                if ((modelIds == null) || lowMemory) {
                    modelTypes = new int[count];
                    modelIds = new int[count];

                    for (index = 0; index < count; index++) {
                        modelIds[index] = buffer.getShort();
                        modelTypes[index] = buffer.getByte();
                    }

                    return;
                }

                buffer.currentPosition += count * 3;
            }
        } else {
            if (opcode == 2) {
                name = buffer.getNewString();
                return;
            }

            if (opcode == 5) {
                count = buffer.getByte();
                if (count > 0) {
                    if ((modelIds == null) || lowMemory) {
                        modelTypes = null;
                        modelIds = new int[count];

                        for (index = 0; index < count; index++) {
                            modelIds[index] = buffer.getShort();
                        }

                        return;
                    }

                    buffer.currentPosition += count * 2;
                }

                return;
            }

            if (opcode == 14) {
                sizeX = buffer.getByte();
                return;
            }

            if (opcode == 15) {
                sizeY = buffer.getByte();
                return;
            }

            if (opcode == 17) {
                clipType = 0;
                projectileClipped = false;
                return;
            }

            if (opcode == 18) {
                impenetrable = false;
                return;
            }

            if (opcode == 19) {
                isInteractive = buffer.getByte() == 1;
                return;
            }

            if (opcode == 21) {
                //clipType = 0;
                contouredGround = true;
                return;
            }

            if (opcode == 22) {
                delayShading = true;
                return;
            }

            if (opcode == 23) {
                occludes = true;
                return;
            }

            if (opcode == 24) {
                animation = buffer.getShort() + 20_000;
                if (animation == 65535) {
                    animation = -1;
                    return;
                }
            } else {
                if (opcode == 27) {
                    clipType = 1;
                    return;
                }

                if (opcode == 28) {
                    decorDisplacement = buffer.getByte();
                    return;
                }

                if (opcode == 29) {
                    ambientLighting = buffer.getSignedByte();
                    return;
                }

                if (opcode == 39) {
                    lightDiffusion = (byte) (buffer.getSignedByte() * 25);
                    return;
                }

                if ((opcode >= 30) && (opcode < 35)) {
                    if (interactions == null) {
                        interactions = new String[5];
                    }
                    interactions[opcode - 30] = buffer.getNewString();
                    if (interactions[opcode - 30].equalsIgnoreCase("Hidden")) {
                        interactions[opcode - 30] = null;
                        return;
                    }
                } else {
                    if (opcode == 40) {
                        count = buffer.getByte();
                        modifiedModelColors = new int[count];
                        originalModelColors = new int[count];

                        for (index = 0; index < count; index++) {
                            modifiedModelColors[index] = (short) buffer.getShort();
                            originalModelColors[index] = (short) buffer.getShort();
                        }

                        return;
                    }

                    if (opcode == 41) {
                        count = buffer.getByte();
                        textureToFind = new short[count];
                        textureToReplace = new short[count];

                        for (index = 0; index < count; index++) {
                            textureToFind[index] = (short) buffer.getShort();
                            textureToReplace[index] = (short) buffer.getShort();
                        }

                        return;
                    }

                    if (opcode == 62) {
                        inverted = true;
                        return;
                    }

                    if (opcode == 64) {
                        castsShadow = false;
                        return;
                    }

                    if (opcode == 65) {
                        scaleX = buffer.getShort();
                        return;
                    }

                    if (opcode == 66) {
                        scaleY = buffer.getShort();
                        return;
                    }

                    if (opcode == 67) {
                        scaleZ = buffer.getShort();
                        return;
                    }

                    if (opcode == 68) {
                        mapscene = buffer.getShort();
                        return;
                    }

                    if (opcode == 69) {
                        surroundings = buffer.getByte();
                        return;
                    }

                    if (opcode == 70) {
                        translateX = buffer.getSignedShort();
                        return;
                    }

                    if (opcode == 71) {
                        translateY = buffer.getSignedShort();
                        return;
                    }

                    if (opcode == 72) {
                        translateZ = buffer.getSignedShort();
                        return;
                    }

                    if (opcode == 73) {
                        obstructsGround = true;
                        return;
                    }

                    if (opcode == 74) {
                        hollow = true;
                        return;
                    }

                    if (opcode == 75) {
                        supportItems = buffer.getByte();
                        return;
                    }

                    if ((opcode == 77) || (opcode == 92)) {
                        configFileId = buffer.getShort();
                        if (configFileId == 65535) {
                            configFileId = -1;
                        }

                        configId = buffer.getShort();
                        if (configId == 65535) {
                            configId = -1;
                        }

                        count = -1;
                        if (opcode == 92) {
                            count = buffer.getShort();
                            if (count == 65535) {
                                count = -1;
                            }
                        }

                        index = buffer.getByte();
                        childrenIds = new int[index + 2];

                        for (int int_3 = 0; int_3 <= index; int_3++) {
                            childrenIds[int_3] = buffer.getShort();
                            if (childrenIds[int_3] == 65535) {
                                childrenIds[int_3] = -1;
                            }
                        }

                        childrenIds[index + 1] = count;
                        return;
                    }

                    if (opcode == 78) {
                        buffer.getShort();
                        buffer.getByte();
                        return;
                    }

                    if (opcode == 79) {
                        buffer.getShort();
                        buffer.getShort();
                        buffer.getByte();
                        count = buffer.getByte();
                        //anIntArray100 = new int[count];

                        for (index = 0; index < count; index++) {
                           /* anIntArray100[index] = */buffer.getShort();
                        }

                        return;
                    }

                    if (opcode == 81) {
                        buffer.getByte() /** 256*/;
                        return;
                    }

                    if (opcode == 82) {
                        mapIcon = buffer.getShort();
                        return;
                    }

                    if (opcode == 249) {
                        buffer.staticMethod195();
                        return;
                    } else {
                        System.out.println("uh oh" + opcode);
                    }
                }
            }
        }

    }


    public static int getObjectCount() {
        return definitionData.getCount();
    }

    public boolean hasAction(String action) {
        if (interactions == null) {
            return false;
        }
        for (String interaction : interactions) {
            if (interaction == null) {
                continue;
            }
            if (interaction.equalsIgnoreCase(action)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        modelIds = null;
        modelTypes = null;
        name = null;
        description = null;
        modifiedModelColors = null;
        originalModelColors = null;
        sizeX = 1;
        sizeY = 1;
        projectileClipped = true;
        impenetrable = true;
        isInteractive = false;
        contouredGround = false;
        delayShading = false;
        occludes = false;
        animation = -1;
        decorDisplacement = 16;
        ambientLighting = 0;
        lightDiffusion = 0;
        interactions = null;
        mapIcon = -1;
        mapscene = -1;
        inverted = false;
        castsShadow = true;
        scaleX = 128;
        scaleY = 128;
        scaleZ = 128;
        surroundings = 0;
        translateX = 0;
        translateY = 0;
        translateZ = 0;
        obstructsGround = false;
        hollow = false;
        supportItems = -1;
        configFileId = -1;
        configId = -1;
        childrenIds = null;
        osrs = false;
    }

    public void loadModels(ResourceProvider resourceProvider) {
        if (modelIds == null) {
            return;
        }
        for (int modelId : modelIds) {
            resourceProvider.loadExtra(modelId & 0xffff, osrs ? 6 : 0);
        }
    }

    public boolean isCached(int modelType) {
        if (modelTypes == null) {
            if (modelIds == null) {
                return true;
            }
            if (modelType != 10) {
                return true;
            }
            boolean flag1 = true;
            for (int modelId : modelIds) {
                flag1 &= Model.isCached(modelId & 0xffff, osrs);
            }
            return flag1;
        }
        for (int index = 0; index < modelTypes.length; index++) {
            if (modelTypes[index] == modelType) {
                return Model.isCached(modelIds[index] & 0xffff, osrs);
            }
        }
        return true;
    }

    public Model modelAt(int type, int orientation, int aY, int bY, int cY, int dY, int frameId) {
        Model model = model(type, frameId, orientation);
        if (model == null) {
            return null;
        }
        if (contouredGround || delayShading) {
            model = new Model(contouredGround, delayShading, model);
            model.osrs = osrs;
        }
        if (contouredGround) {
            int y = (aY + bY + cY + dY) / 4;
            for (int vertex = 0; vertex < model.vertexCount; vertex++) {
                int x = model.verticesX[vertex];
                int z = model.verticesZ[vertex];
                int l2 = aY + ((bY - aY) * (x + 64)) / 128;
                int i3 = dY + ((cY - dY) * (x + 64)) / 128;
                int j3 = l2 + ((i3 - l2) * (z + 64)) / 128;
                model.verticesY[vertex] += j3 - y;
            }
            model.computeSphericalBounds();
        }
        return model;
    }

    public boolean isModelCached() {
        if (modelIds == null) {
            return true;
        }
        boolean cachedModel = true;
        for (int modelId : modelIds) {
            cachedModel &= Model.isCached(modelId & 0xffff, osrs);
        }
        return cachedModel;
    }

    public ObjectDefinition getChild() {
        int i = -1;
        if (configFileId != -1) {
            VariableBits varBit = VariableBits.cache[configFileId];
            int j = varBit.getSetting();
            int k = varBit.getLow();
            int l = varBit.getHigh();
            int i1 = Game.BIT_MASKS[l - k];
            i = clientInstance.settings[j] >> k & i1;
        } else if (configId != -1) {
            i = clientInstance.settings[configId];
        }
        if (i < 0 || i >= childrenIds.length || childrenIds[i] == -1) {
            return null;
        } else {
            return forId(childrenIds[i]);
        }
    }

    public Model model(int type, int frameId, int orientation) {
        Model model = null;
        long modelId;
        if (modelTypes == null) {
            if (type != 10) {
                return null;
            }
            modelId = (long) ((this.type << 6) + orientation) + ((long) (frameId + 1) << 32);
            Model model1 = (Model) (osrs ? osrsModels.get(modelId) : models.get(modelId));
            if (model1 != null) {
                return model1;
            }
            if (modelIds == null) {
                return null;
            }
            boolean flag1 = inverted ^ (orientation > 3);
            int k1 = modelIds.length;
            for (int modelIndex = 0; modelIndex < k1; modelIndex++) {
                int modelId1 = modelIds[modelIndex];
                if (flag1) {
                    modelId1 += 0x10000;
                }
                model = (Model) (osrs ? osrsBaseModels.get(modelId1) : baseModels.get(modelId1));
                if (model == null) {
                    model = Model.getModel(modelId1 & 0xffff, osrs);
                    if (model == null) {
                        return null;
                    }
                    if (flag1) {
                        model.method477();
                    }
                    model.osrs = osrs;
                    (osrs ? osrsBaseModels : baseModels).put(model, modelId1);
                }
                if (k1 > 1) {
                    if (osrs) {
                        model.osrs = true;
                        osrsModelArray[modelIndex] = model;
                    } else {
                        modelArray[modelIndex] = model;
                    }
                }
            }

            if (k1 > 1) {
                model = new Model(k1, osrs ? osrsModelArray : modelArray);
                model.osrs = osrs;
            }
        } else {
            int i1 = -1;
            for (int j1 = 0; j1 < modelTypes.length; j1++) {
                if (modelTypes[j1] != type) {
                    continue;
                }
                i1 = j1;
                break;
            }

            if (i1 == -1) {
                return null;
            }
            modelId = (long) ((this.type << 8) + (i1 << 3) + orientation) + ((long) (frameId + 1) << 32);
            Model model_2 = (Model) (osrs ? osrsModels.get(modelId) : models.get(modelId));
            if (model_2 != null) {
                return model_2;
            }
            int j2 = modelIds[i1];
            boolean flag3 = inverted ^ (orientation > 3);
            if (flag3) {
                j2 += 0x10000;
            }
            model = (Model) (osrs ? osrsBaseModels.get(j2) : baseModels.get(j2));
            if (model == null) {
                model = Model.getModel(j2 & 0xffff, osrs);
                if (model == null) {
                    return null;
                }
                if (flag3) {
                    model.method477();
                }
                model.osrs = osrs;
                (osrs ? osrsBaseModels : baseModels).put(model, j2);
            }
        }
        boolean flag;
        flag = scaleX != 128 || scaleY != 128 || scaleZ != 128;
        boolean flag2;
        flag2 = translateX != 0 || translateY != 0 || translateZ != 0;
        Model model_3 = new Model(modifiedModelColors == null,
                SequenceFrame.method532(frameId), orientation == 0 && frameId == -1 && !flag
                && !flag2, model);
        model_3.osrs = osrs;
        if (frameId != -1) {
            model_3.skin();
            model_3.apply(frameId, false);
            model_3.faceGroups = null;
            model_3.vertexGroups = null;
        }
        while (orientation-- > 0) {
            model_3.method473();
        }
        if (modifiedModelColors != null) {
            for (int k2 = 0; k2 < modifiedModelColors.length; k2++) {
                model_3.recolor(modifiedModelColors[k2],
                        originalModelColors[k2]);
            }

        }
        if (textureToFind != null) {
            for (int k2 = 0; k2 < textureToFind.length; k2++) {
                model_3.retexture(textureToFind[k2],
                        textureToReplace[k2]);
            }

        }
        if (flag) {
            model_3.scale(scaleX, scaleZ, scaleY);
        }
        if (flag2) {
            model_3.translate(translateX, translateY, translateZ);
        }
        try {
            model_3.light(84, 1500, -90, -280, -70, !delayShading);
        } catch (Exception e) {

        }
        if (supportItems == 1) {
            model_3.myPriority = model_3.modelHeight;
        }
        model_3.osrs = osrs;
        (osrs ? osrsModels : models).put(model_3, modelId);
        return model_3;
    }
}
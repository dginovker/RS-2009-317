package com.runescape.cache.def.object;

import com.runescape.cache.def.DefinitionData;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents an old-school RuneScape object definition.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class OSRSObjectDefinition extends ObjectDefinition {
    public static OSRSObjectDefinition[] cache;
    private static DefinitionData definitionData = new DefinitionData();

    public OSRSObjectDefinition() {
        super();
        osrs = true;
    }

    public static OSRSObjectDefinition forId(int objectId) {
        if (objectId > definitionData.getIndices().length) {
            objectId = definitionData.getIndices().length - 1;
        }
        for (int index = 0; index < 20; index++) {
            if (cache[index].type == objectId) {
                return cache[index];
            }
        }
        definitionData.setCacheIndex((definitionData.getCacheIndex() + 1) % 20);
        OSRSObjectDefinition osrsObjectDefinition = cache[definitionData.getCacheIndex()];
        definitionData.getBuffer().currentPosition = definitionData.getOffset(objectId);
        osrsObjectDefinition.type = objectId;
        osrsObjectDefinition.osrs = true;
        osrsObjectDefinition.reset();

        if (objectId >= 28266 && objectId <= 28295) {
            osrsObjectDefinition.name = "Snowman";
            osrsObjectDefinition.modelIds = new int[]{45693 + (objectId - 28266)};
            osrsObjectDefinition.interactions = new String[]{"Add-to", null, null, null, null};
            osrsObjectDefinition.isInteractive = true;
            return osrsObjectDefinition;
        }
        switch (objectId) {
            case 28296:
            case 28297:
                osrsObjectDefinition.name = "Snow";
                osrsObjectDefinition.modelIds = new int[]{objectId == 28296 ? 45724 : 45723};
                osrsObjectDefinition.interactions = new String[]{"Collect", null, null, null, null};
                osrsObjectDefinition.isInteractive = true;
                return osrsObjectDefinition;

            case 28717:
                osrsObjectDefinition.name = "Inert obelisk";
                osrsObjectDefinition.modelIds = new int[]{45727};
                osrsObjectDefinition.isInteractive = true;
                osrsObjectDefinition.animation = 8510;
                return osrsObjectDefinition;

            case 28718:
                osrsObjectDefinition.name = "Inert obelisk";
                osrsObjectDefinition.modelIds = new int[]{45728};
                osrsObjectDefinition.isInteractive = true;
                osrsObjectDefinition.animation = 8510;
                return osrsObjectDefinition;

            case 28716:
                osrsObjectDefinition.name = "Obelisk";
                osrsObjectDefinition.interactions = new String[]{"Infuse-Pouch", "Renew-Points", null, null, null,};
                osrsObjectDefinition.modelIds = new int[]{45729};
                osrsObjectDefinition.sizeX = 2;
                osrsObjectDefinition.sizeY = 2;
                osrsObjectDefinition.isInteractive = true;
                osrsObjectDefinition.animation = 8510;
                return osrsObjectDefinition;

            case 28714:
                osrsObjectDefinition.name = "Ladder";
                osrsObjectDefinition.interactions = new String[]{"Climb", null, null, null, null,};
                osrsObjectDefinition.modelIds = new int[]{45730};
                osrsObjectDefinition.isInteractive = true;
                return osrsObjectDefinition;

            case 29882:
                osrsObjectDefinition.name = "Small obelisk";
                osrsObjectDefinition.interactions = new String[]{"Renew-points", null, null, null, null};
                osrsObjectDefinition.modelIds = new int[]{45728};
                osrsObjectDefinition.isInteractive = true;
                osrsObjectDefinition.animation = 8510;
                return osrsObjectDefinition;

            case 8772:
                osrsObjectDefinition.isInteractive = true;
                osrsObjectDefinition.interactions = new String[]{"Sit", null, null, null, null};
                break;
        }

        osrsObjectDefinition.readValues(definitionData.getBuffer());
        osrsObjectDefinition.post();
        return osrsObjectDefinition;
    }

    public static void nullLoader() {
        cache = null;
        definitionData.clear();
    }

    public static void unpackConfig(CacheArchive cacheArchive) throws IOException {
        definitionData.load(cacheArchive, "loc", true);

        cache = new OSRSObjectDefinition[20];
        for (int index = 0; index < 20; index++) {
            cache[index] = new OSRSObjectDefinition();
        }
        //dumpDefinitions();
    }

    public static int getObjectCount() {
        return definitionData.getCount();
    }

    void post() {
        if (!Objects.equals(name, "null") && name != null) {
            isInteractive = modelIds != null
                    && (modelTypes == null || modelTypes[0] == 10);
            if (interactions != null) {
                isInteractive = true;
            }
        }
        if (hollow) {
            projectileClipped = false;
            impenetrable = false;
        }
        if (supportItems == -1) {
            supportItems = projectileClipped ? 1 : 0;
        }

    }

    static final void method177(RSStream buffer) {
        final int count = buffer.getByte();
        int int_1 = method386(count);
        for (int_1 = 0; int_1 < count; int_1++) {
            final boolean bool_0 = buffer.getByte() == 1;
            buffer.getTri();
            if (bool_0) {
                buffer.getNewString();
            } else {
                buffer.getInt();
            }

        }
    }

    public static int method386(int int_0) {
        --int_0;
        int_0 |= int_0 >>> 1;
        int_0 |= int_0 >>> 2;
        int_0 |= int_0 >>> 4;
        int_0 |= int_0 >>> 8;
        int_0 |= int_0 >>> 16;
        return int_0 + 1;
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
        osrs = true;
    }
}
package com.runescape.cache.def.npc;

import com.runescape.Game;
import com.runescape.cache.config.VariableBits;
import com.runescape.cache.def.DefinitionData;
import com.runescape.cache.def.impl.NPCRepository;
import com.runescape.cache.media.SequenceFrame;
import com.runescape.collection.ReferenceCache;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.entity.Player;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Refactored reference:
 * http://www.rune-server.org/runescape-development/rs2-client/downloads/575183-almost-fully-refactored-317-client.html
 */
public class NPCDefinition {

    public static NPCDefinition[] cache;
    public static Game clientInstance;
    public static ReferenceCache models = new ReferenceCache(30);
    public static boolean FILE = true;
    private static DefinitionData definitionData = new DefinitionData();
    public boolean osrs;
    public int turn90CCWAnimation;
    public int varBitID;
    public int turn180Animation;
    public int settingId;
    public int combatLevel;
    public String name;
    public String[] actions;
    public int walkAnimationId;
    public byte size;
    public short[] recolourTarget;
    public int[] headModelIds;
    public int headIcon;
    public short[] recolourOriginal;
    public int standAnimation;
    public long npcId;
    public int degreesToTurn;
    public int turn90CWAnimation;
    public boolean clickable;
    public int lightModifier;
    public int height;
    public boolean drawMinimapDot;
    public int childrenIDs[];
    public byte description[];
    public int width;
    public int shadowModifier;
    public boolean priorityRender;
    public int[] modelIds;
    private short[] retextureTarget;
    private short[] retextureOriginal;

    public NPCDefinition() {
        turn90CCWAnimation = -1;
        varBitID = -1;
        turn180Animation = -1;
        settingId = -1;
        combatLevel = -1;
        walkAnimationId = -1;
        size = 1;
        headIcon = -1;
        standAnimation = -1;
        npcId = -1L;
        degreesToTurn = 32;
        turn90CWAnimation = -1;
        clickable = true;
        height = 128;
        drawMinimapDot = true;
        width = 128;
        priorityRender = false;
    }

    /**
     * Gets an {@link NPCDefinition} by id.
     *
     * @param npcId The id.
     */
    public static NPCDefinition forId(int npcId) {
        if (npcId >= 20000) {
            return OSRSNPCDefinition.forId(npcId);
        }
        for (int index = 0; index < 20; index++) {
            if (cache[index].npcId == (long) npcId) {
                return cache[index];
            }
        }
        definitionData.setCacheIndex((definitionData.getCacheIndex() + 1) % 20);
        NPCDefinition npcDefinition = cache[definitionData.getCacheIndex()] = new NPCDefinition();
        definitionData.getBuffer().currentPosition = definitionData.getOffset(npcId);
        npcDefinition.npcId = npcId;
        npcDefinition.osrs = false;
        npcDefinition.readValues(definitionData.getBuffer());
        npcDefinition = NPCRepository.forId(npcId, npcDefinition);
        return npcDefinition;
    }

    public static void unpackConfig(CacheArchive cacheArchive) {
        definitionData.load(cacheArchive, "npc", false, 20000);
        cache = new NPCDefinition[20];
        for (int count = 0; count < 20; count++) {
            cache[count] = new NPCDefinition();
        }
    }

    public static void writeToFile(String line, String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            file.createNewFile();
        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), charset, StandardOpenOption.APPEND)) {
            writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void nullLoader() {
        models = null;
        cache = null;
        definitionData.clear();
    }

    public boolean hasAction(String action) {
        if (actions == null) {
            return false;
        }
        for (String interaction : actions) {
            if (interaction == null) {
                continue;
            }
            if (interaction.equalsIgnoreCase(action)) {
                return true;
            }
        }
        return false;
    }

    public Model model() {
        /*if (npcId > 30000) {
            return getPetModel();
        }*/
        if (childrenIDs != null) {
            NPCDefinition entityDef = morph();
            if (entityDef == null) {
                return null;
            } else {
                return entityDef.model();
            }
        }
        if (headModelIds == null) {
            return null;
        }
        boolean flag1 = false;
        for (int additionalModel : headModelIds) {
            if (!Model.isCached(additionalModel, osrs)) {
                flag1 = true;
            }
        }

        if (flag1) {
            return null;
        }
        Model models[] = new Model[headModelIds.length];
        for (int index = 0; index < headModelIds.length; index++) {
            models[index] = Model.getModel(headModelIds[index], osrs);
        }

        Model model;
        if (models.length == 1) {
            model = models[0];
        } else {
            model = new Model(models.length, models);
        }
        if (recolourOriginal != null) {
            for (int index = 0; index < recolourOriginal.length; index++) {
                model.recolor(recolourOriginal[index], recolourTarget[index]);
            }

        }
        return model;
    }

    public NPCDefinition morph() {
        int child = -1;
        if (varBitID != -1) {
            if (varBitID > VariableBits.cache.length) {
                return null;
            }
            VariableBits varBit = VariableBits.cache[varBitID];
            int variable = varBit.getSetting();
            int low = varBit.getLow();
            int high = varBit.getHigh();
            int mask = Game.BIT_MASKS[high - low];
            child = clientInstance.settings[variable] >> low & mask;
        } else if (settingId != -1) {
            child = clientInstance.settings[settingId];
        }
        if (child < 0 || child >= childrenIDs.length
                || childrenIDs[child] == -1) {
            return null;
        } else {
            return forId(childrenIDs[child]);
        }
    }

    public Model modelAt(int j, int frame, int ai[], int nextFrame, int idk, int idk2) {
        // osrs = Constants.isOSRSAnimation(j);
        if (childrenIDs != null) {
            NPCDefinition entityDef = morph();
            if (entityDef == null) {
                return null;
            } else {
                return entityDef.modelAt(j, frame, ai, nextFrame, idk, idk2);
            }
        }
        Model model = (Model) models.get(npcId);
        if (model == null) {
            boolean flag = false;
            for (int modelId : modelIds) {
                if (!Model.isCached(modelId, this.osrs)) {
                    flag = true;
                }
            }

            if (flag) {
                return null;
            }
            Model models[] = new Model[modelIds.length];
            for (int j1 = 0; j1 < modelIds.length; j1++) {
                models[j1] = Model.getModel(modelIds[j1], this.osrs);
            }

            if (models.length == 1) {
                model = models[0];
            } else {
                model = new Model(models.length, models);
            }
            if (recolourOriginal != null) {
                for (int k1 = 0; k1 < recolourOriginal.length; k1++) {
                    model.recolor(recolourOriginal[k1], recolourTarget[k1]);
                }

            }
            model.skin();
            model.scale(132, 132, 132);
            model.light(84 + lightModifier, 1000 + shadowModifier, -90, -580,
                    -90, true);
            NPCDefinition.models.put(model, npcId);
        }
        Model empty = Model.EMPTY_MODEL;
        empty.method464(model,
                SequenceFrame.method532(frame) & SequenceFrame.method532(j)
                        & SequenceFrame.method532(nextFrame));
        if (frame != -1 && j != -1) {
            empty.method471(ai, j, frame, this.osrs);
        } else if (frame != -1 && nextFrame != -1) {
            empty.method470(frame, nextFrame, idk, idk2, this.osrs);
        } else if (frame != -1) {
            empty.apply(frame, this.osrs);
        }
        if (width != 128 || height != 128) {
            empty.scale(width, width, height);
        }
        empty.method466();
        empty.faceGroups = null;
        empty.vertexGroups = null;
        if (size == 1) {
            empty.fitsOnSingleSquare = true;
        }
        return empty;
    }

    public Model getPetModel() {
        int otherPidn = ((int) npcId) - 30001;
        Player[] players = Game.INSTANCE.players;
        if (otherPidn > players.length) {
            return null;
        }
        Player player = otherPidn == (Game.getLocalPlayer().pidn - 1) ? Game.getLocalPlayer() : players[otherPidn];
        if (player == null) {
            return null;
        }
        Model newModel = player.getRotatedModel();
        newModel.transformScale(0, -40, 0);
        return newModel;
    }

    public Model getAnimatedModel(int primaryFrame, int secondaryFrame, int[] interleaveOrder) {
   /*     if (npcId > 30000) {
            return getPetModel();
        }*/
        if (childrenIDs != null) {
            NPCDefinition definition = morph();
            if (definition == null) {
                return null;
            } else {
                return definition.getAnimatedModel(primaryFrame, secondaryFrame, interleaveOrder);
            }
        }
        Model model = (Model) models.get(npcId);
        if (model == null) {
            boolean flag = false;
            if (modelIds == null) {
                return null;
            }
            for (int modelId : modelIds) {
                if (!Model.isCached(modelId, this.osrs)) {
                    flag = true;
                }
            }

            if (flag) {
                return null;
            }
            Model models[] = new Model[modelIds.length];
            for (int index = 0; index < modelIds.length; index++) {
                models[index] = Model.getModel(modelIds[index], this.osrs);
            }

            if (models.length == 1) {
                model = models[0];
            } else {
                model = new Model(models.length, models);
            }
            if (recolourOriginal != null) {
                for (int index = 0; index < recolourOriginal.length; index++) {
                    model.recolor(recolourOriginal[index],
                            recolourTarget[index]);
                }

            }
            model.skin();
            model.light(64 + lightModifier, 850 + shadowModifier, -30, -50,
                    -30, true);
            NPCDefinition.models.put(model, npcId);
        }
        Model model_1 = Model.EMPTY_MODEL;
        model_1.method464(model, SequenceFrame.method532(secondaryFrame)
                & SequenceFrame.method532(primaryFrame));
        if (secondaryFrame != -1 && primaryFrame != -1) {
            model_1.method471(interleaveOrder, primaryFrame, secondaryFrame, this.osrs);
        } else if (secondaryFrame != -1) {
            model_1.apply(secondaryFrame, this.osrs);
        }
        if (width != 128 || height != 128) {
            model_1.scale(width, width, height);
        }
        model_1.method466();
        model_1.faceGroups = null;
        model_1.vertexGroups = null;
        if (size == 1) {
            model_1.fitsOnSingleSquare = true;
        }
        return model_1;
    }
    public void decode(RSStream buffer) {
        while (true) {
            int opcode = buffer.getByte();
            if (opcode == 0) {
                break;
            }
            decode(buffer, opcode);
        }
    }
    void decode(RSStream buffer, int opcode) {
        int i_4;
        int i_5;
        if (opcode == 1) {
            i_4 = buffer.getByte();
            modelIds = new int[i_4];

            for (i_5 = 0; i_5 < i_4; i_5++)
                modelIds[i_5] = buffer.getShort();
        } else if (opcode == 2)
            name = buffer.getNewString();
        else if (opcode == 12)
            size = (byte) buffer.getByte();
        else if (opcode == 13) {
            /*-*/standAnimation = buffer.getShort() + 20_000;
        } else if (opcode == 14)
            walkAnimationId = buffer.getShort() + 20_000;
        else if (opcode == 15)
            buffer.getShort();
        else if (opcode == 16)
            buffer.getShort();
        else if (opcode == 17) {
            walkAnimationId = buffer.getShort() + 20_000;
            turn180Animation = -buffer.getShort() + 20_000;
            turn90CWAnimation = buffer.getShort() + 20_000;
            turn90CCWAnimation = buffer.getShort() + 20_000;
        } else if (opcode >= 30 && opcode < 35) {
            if (actions == null) {
                actions = new String[5];
            }
            actions[opcode - 30] = buffer.getNewString();
            if (actions[opcode - 30].equalsIgnoreCase("Hidden"))
                actions[opcode - 30] = null;
        } else if (opcode == 40) {
            i_4 = buffer.getByte();
            recolourOriginal = new short[i_4];
            recolourTarget = new short[i_4];

            for (i_5 = 0; i_5 < i_4; i_5++) {
                recolourOriginal[i_5] = (short) buffer.getShort();
                recolourTarget[i_5] = (short) buffer.getShort();
            }
        } else if (opcode == 41) {
            i_4 = buffer.getByte();
            retextureOriginal = new short[i_4];
            retextureTarget = new short[i_4];

            for (i_5 = 0; i_5 < i_4; i_5++) {
                retextureOriginal[i_5] = (short) buffer.getShort();
                retextureTarget[i_5] = (short) buffer.getShort();
            }
        } else if (opcode == 60) {
            i_4 = buffer.getByte();
            headModelIds = new int[i_4];

            for (i_5 = 0; i_5 < i_4; i_5++)
                headModelIds[i_5] = buffer.getShort();
        } else if (opcode == 93)
            drawMinimapDot = false;
        else if (opcode == 95)
            combatLevel = buffer.getShort();
        else if (opcode == 97)
            width = buffer.getShort();
        else if (opcode == 98)
            height = buffer.getShort();
        else if (opcode == 99)
            priorityRender = true;
        else if (opcode == 100)
            lightModifier = buffer.getSignedByte();
        else if (opcode == 101)
            shadowModifier = buffer.getSignedByte() * 5;
        else if (opcode == 102)
            headIcon = buffer.getShort();
        else if (opcode == 103)
            degreesToTurn = buffer.getShort();
        else if (opcode != 106 && opcode != 118) {
            if (opcode == 107)
                clickable = false;
            else if (opcode == 109) {
             //   boolean bool64 = false;
            } else if (opcode == 111) {
              //  boolean bool63 = true;
            } else if (opcode == 249)
                buffer.staticMethod195();
        } else {
            varBitID = buffer.getShort();
            if (varBitID == 65535)
                varBitID = -1;

            settingId = buffer.getShort();
            if (settingId == 65535)
                settingId = -1;

            i_4 = -1;
            if (opcode == 118) {
                i_4 = buffer.getShort();
                if (i_4 == 65535)
                    i_4 = -1;
            }

            i_5 = buffer.getByte();
            childrenIDs = new int[i_5 + 2];

            for (int i_6 = 0; i_6 <= i_5; i_6++) {
                childrenIDs[i_6] = buffer.getShort();
                if (childrenIDs[i_6] == 65535)
                    childrenIDs[i_6] = -1;
            }

            childrenIDs[i_5 + 1] = i_4;
        }

    }
    public void readValues(RSStream rsStream) {
        do {
            int opcode = rsStream.getByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                int modelLength = rsStream.getByte();
                modelIds = new int[modelLength];
                for (int modelIndex = 0; modelIndex < modelLength; modelIndex++) {
                    modelIds[modelIndex] = rsStream.getShort();
                }
            } else if (opcode == 2) {
                name = FILE ? rsStream.getString() : rsStream.getNewString();
            } else if (opcode == 3) {
                description = rsStream.readBytes();
            } else if (opcode == 12) {
                size = rsStream.getSignedByte();
            } else if (opcode == 13) {
                standAnimation = osrs ? rsStream.getShort() + 20_000 : rsStream.getShort();
            } else if (opcode == 14) {
                walkAnimationId = osrs ? rsStream.getShort() + 20_000 : rsStream.getShort();
            } else if (opcode == 17) {
                walkAnimationId = osrs ? rsStream.getShort() + 20_000 : rsStream.getShort();
                turn180Animation = osrs ? rsStream.getShort() + 20_000 : rsStream.getShort();
                turn90CWAnimation = osrs ? rsStream.getShort() + 20_000 : rsStream.getShort();
                turn90CCWAnimation = osrs ? rsStream.getShort() + 20_000 : rsStream.getShort();
            } else if (opcode >= 30 && opcode < 40) {
                if (actions == null) {
                    actions = new String[5];
                }
                actions[opcode - 30] = FILE ? rsStream.getString() : rsStream.getNewString();
                if (actions[opcode - 30].equalsIgnoreCase("hidden")) {
                    actions[opcode - 30] = null;
                }
            } else if (opcode == 40) {
                int colours = rsStream.getByte();
                recolourOriginal = new short[colours];
                recolourTarget = new short[colours];
                for (int k1 = 0; k1 < colours; k1++) {
                    recolourOriginal[k1] = (short) rsStream.getShort();
                    recolourTarget[k1] = (short) rsStream.getShort();
                }
            } else if (opcode == 60) {
                int modelLength = rsStream.getByte();
                headModelIds = new int[modelLength];
                for (int modelIndex = 0; modelIndex < modelLength; modelIndex++) {
                    headModelIds[modelIndex] = rsStream.getShort();
                }
            } else if (opcode == 90) {
                rsStream.getShort();
            } else if (opcode == 91) {
                rsStream.getShort();
            } else if (opcode == 92) {
                rsStream.getShort();
            } else if (opcode == 93) {
                drawMinimapDot = false;
            } else if (opcode == 95) {
                combatLevel = rsStream.getShort();
            } else if (opcode == 97) {
                width = rsStream.getShort();
            } else if (opcode == 98) {
                height = rsStream.getShort();
            } else if (opcode == 99) {
                priorityRender = true;
            } else if (opcode == 100) {
                lightModifier = rsStream.getSignedByte();
            } else if (opcode == 101) {
                shadowModifier = rsStream.getSignedByte() * 5;
            } else if (opcode == 102) {
                headIcon = rsStream.getShort();
            } else if (opcode == 103) {
                degreesToTurn = rsStream.getShort();
            } else if (opcode == 106) {
                varBitID = rsStream.getShort();
                if (varBitID == 65535) {
                    varBitID = -1;
                }
                settingId = rsStream.getShort();
                if (settingId == 65535) {
                    settingId = -1;
                }
                int childCount = rsStream.getByte();
                childrenIDs = new int[childCount + 1];
                for (int i2 = 0; i2 <= childCount; i2++) {
                    childrenIDs[i2] = rsStream.getShort();
                    if (childrenIDs[i2] == 65535) {
                        childrenIDs[i2] = -1;
                    }
                }

            } else if (opcode == 107) {
                clickable = false;
            }
        } while (true);
    }
}
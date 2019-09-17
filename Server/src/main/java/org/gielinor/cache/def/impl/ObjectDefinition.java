package org.gielinor.cache.def.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.gielinor.cache.def.Definition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.RSStream;
import org.gielinor.utilities.misc.FileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Represents an object's definition.
 *
 * @author Emperor
 */
public class ObjectDefinition extends Definition<GameObject> {

    private static final Logger log = LoggerFactory.getLogger(ObjectDefinition.class);

    /**
     * The item definitions mapping.
     */
    private static final Map<Integer, ObjectDefinition> DEFINITIONS = new HashMap<>();

    /**
     * The default option handlers.
     */
    private static final Map<String, OptionHandler> OPTION_HANDLERS = new HashMap<>();

    /**
     * The object examines.
     */
    private static final Map<Integer, String> EXAMINES = new HashMap<>();

    /**
     * The path for the Object Examines file
     */
    private static final Path EXAMINES_PATH = Paths.get("data", "definition", "examines", "object_examine.json");
    
    private static int highestKey = -1;
    
    /**
     * The original model colors.
     */
    private short[] originalColors;

    /**
     * The children ids.
     */
    public int[] childrenIds;

    /**
     * The model ids.
     */
    public int[] modelIds;

    /**
     * The model configuration.
     */
    public int[] modelConfiguration;

    /**
     * A unknown integer.
     */
    static int anInt3832;

    /**
     * A unkown integer array.
     */
    int[] anIntArray3833 = null;

    /**
     * A unknown integer.
     */
    private int anInt3834;

    /**
     * A unknown integer.
     */
    int anInt3835;

    /**
     * A unknown integer.
     */
    static int anInt3836;

    /**
     * A unknown byte.
     */
    private byte aByte3837;

    /**
     * A unknown integer.
     */
    int anInt3838 = -1;

    /**
     * A unknown boolean.
     */
    boolean aBoolean3839;

    /**
     * A unknown integer.
     */
    public int anInt3840;

    /**
     * A unknown integer.
     */
    public int scaleY;

    /**
     * A unknown integer.
     */
    static int anInt3842;

    /**
     * A unknown integer.
     */
    static int anInt3843;

    /**
     * A unknown integer.
     */
    int anInt3844;

    /**
     * A unknown boolean.
     */
    boolean aBoolean3845;

    /**
     * A unknown integer.
     */
    static int anInt3846;

    /**
     * A unknown byte.
     */
    public byte aByte3847;

    /**
     * A unknown byte.
     */
    public byte aByte3849;

    /**
     * A unknown integer.
     */
    int anInt3850;

    /**
     * A unknown integer.
     */
    int anInt3851;

    /**
     * The second boolean.
     */
    public boolean secondBool;

    /**
     * A unknown boolean.
     */
    public boolean aBoolean3853;

    /**
     * A unknown integer.
     */
    public int anInt3855;

    /**
     * The first boolean.
     */
    public boolean notClipped;

    /**
     * A unknown integer.
     */
    int anInt3857;

    /**
     * A unknown byte array.
     */
    public byte[] aByteArray3858;

    /**
     * A unknown integer array.
     */
    int[] anIntArray3859;

    /**
     * A unknown integer.
     */
    int anInt3860;

    /**
     * The config file id.
     */
    public int configFileId;

    /**
     * The modified colors.
     */
    public short[] modifiedColors;

    /**
     * A unknown integer.
     */
    int anInt3865;

    /**
     * A unknown boolean.
     */
    boolean aBoolean3866;

    /**
     * A unknown boolean.
     */
    public boolean aBoolean3867;

    /**
     * The solid.
     */
    public boolean projectileClipped;

    /**
     * A unknown integer array.
     */
    public int[] anIntArray3869;

    /**
     * A unknown boolean.
     */
    boolean aBoolean3870;

    /**
     * The y-size.
     */
    public int sizeY;

    /**
     * A unknown boolean.
     */
    boolean aBoolean3872;

    /**
     * A unknown boolean.
     */
    public boolean membersOnly;

    /**
     * The third integer.
     */
    public boolean boolean1;

    /**
     * A unknown integer.
     */
    public int anInt3875;

    public int mapscene;

    /**
     * The add object check.
     */
    public int animationId;

    /**
     * A unknown integer.
     */
    public int anInt3877;

    /**
     * A unknown integer.
     */
    public int anInt3878;

    /**
     * The clipping type.
     */
    public int clipType;

    /**
     * A unknown integer.
     */
    public int anInt3881;

    /**
     * A unknown integer.
     */
    public int anInt3882;

    /**
     * A unknown integer.
     */
    public int anInt3883;

    /**
     * A unknown integer.
     */
    public int anInt3889;

    /**
     * The x-size.
     */
    public int sizeX;

    /**
     * A unknown boolean.
     */
    public boolean aBoolean3891;

    /**
     * A unknown integer.
     */
    int anInt3892;

    /**
     * The second integer.
     */
    public int isInteractive;

    /**
     * A unknown boolean.
     */
    boolean aBoolean3894;

    /**
     * A unknown boolean.
     */
    boolean aBoolean3895;

    /**
     * A unknown integer.
     */
    int anInt3896;

    /**
     * The configuration id.
     */
    public int configId;

    /**
     * A unknown byte array.
     */
    public byte[] aByteArray3899;

    /**
     * A unknown integer.
     */
    int anInt3900;

    /**
     * A unknown integer.
     */
    public int scaleX;

    /**
     * A unknown integer.
     */
    int anInt3904;

    /**
     * A unknown integer.
     */
    int anInt3905;

    /**
     * A unknown boolean.
     */
    boolean aBoolean3906;

    /**
     * A unknown integer array.
     */
    int[] anIntArray3908;

    /**
     * A unknown byte.
     */
    public byte aByte3912;

    /**
     * A unknown integer.
     */
    int anInt3913;

    /**
     * A unknown byte.
     */
    public byte aByte3914;

    /**
     * A unknown integer.
     */
    public int anInt3915;

    /**
     * A unknown integer array.
     */
    public int[][] anIntArrayArray3916;

    /**
     * A unknown integer.
     */
    public int scaleZ;

    /**
     * A unknown short array.
     */
    public short[] aShortArray3919;

    /**
     * A unknown short array.
     */
    public short[] aShortArray3920;

    /**
     * A unknown integer.
     */
    int anInt3921;

    /**
     * A unknown object.
     */
    public Object aClass194_3922;

    /**
     * A unknown integer.
     */
    boolean aBoolean3923;

    /**
     * A unknown integer.
     */
    boolean aBoolean3924;

    /**
     * The walking flag.
     */
    int walkingFlag;

    /**
     * If the object has hidden options.
     */
    public boolean hasHiddenOptions;

    /**
     * The map icon.
     */
    public short mapIcon;

    public static void main(String[] args){

        final String keywords[] = new String[]{"bank booth", "deposit", "bank chest"};

        ObjectDefinition.unpackConfig();

        for(int i = 0; i < count; i++)
           ObjectDefinition.forId(i);

        System.out.println("Loaded "+getDefinitions().size()+" object definitions.");

        getDefinitions().values().stream()
            .filter(objectDefinition -> Arrays.stream(keywords).anyMatch(keyword -> objectDefinition.getName().toLowerCase().contains(keyword)))
           // .peek(objectDefinition -> System.out.println("objectname["+objectDefinition.getName()+"] ,examine["+objectDefinition.getExamine()+"]"))
            .mapToInt(ObjectDefinition::getId)
            .forEach(id -> System.out.print(id+","));
    }

    /**
     * Construct a new {@code ObjectDefinition} {@code Object}.
     */
    public ObjectDefinition() {
        anInt3835 = -1;
        anInt3860 = -1;
        setConfigFileId(-1);
        aBoolean3866 = false;
        anInt3851 = -1;
        anInt3865 = 255;
        aBoolean3845 = false;
        aBoolean3867 = false;
        anInt3850 = 0;
        anInt3844 = -1;
        anInt3881 = 0;
        anInt3857 = -1;
        aBoolean3872 = true;
        anInt3882 = -1;
        anInt3834 = 0;
        options = new String[5];
        anInt3875 = 0;
        aBoolean3839 = false;
        anIntArray3869 = null;
        setSizeY(1);
        setBoolean1(false);
        setProjectileClipped(true);
        anInt3883 = 0;
        aBoolean3895 = true;
        anInt3840 = 0;
        aBoolean3870 = false;
        anInt3889 = 0;
        aBoolean3853 = true;
        setSecondBool(false);
        setClipType(2);
        setAnInt3855(-1);
        anInt3878 = 0;
        anInt3904 = 0;
        setSizeX(1);
        setAnimationId(-1);
        setNotClipped(false);
        aBoolean3891 = false;
        anInt3905 = 0;
        name = "null";
        anInt3913 = -1;
        aBoolean3906 = false;
        setMembersOnly(false);
        aByte3914 = (byte) 0;
        anInt3915 = 0;
        anInt3900 = 0;
        setIsInteractive(-1);
        aBoolean3894 = false;
        aByte3912 = (byte) 0;
        anInt3921 = 0;
        setScaleX(128);
        setConfigId(-1);
        anInt3877 = 0;
        walkingFlag = 0;
        anInt3892 = 64;
        aBoolean3923 = false;
        aBoolean3924 = false;
        setScaleY(128);
        scaleZ = 128;
        mapIcon = -1;
    }

    /**
     * Gets an object definition.
     *
     * @param objectId The object's id.
     * @return The object definition.
     */
    public static ObjectDefinition forId(int objectId) {
        ObjectDefinition def = DEFINITIONS.get(objectId);
        if (def != null) {
            return def;
        } else {
            def = new ObjectDefinition();
        }
        if (objectId > indices.length) {
            objectId = indices.length - 1;
        }

        if (objectId < 0) {
            DEFINITIONS.put(objectId, def);

            return def;
        }
        buffer.currentPosition = indices[objectId];
        def.id = objectId;

        if (objectId >= 28266 && objectId <= 28295) {
            def.name = "Snowman";
            def.modelIds = new int[]{ 45693 + (objectId - 28266) };
            def.options = new String[]{ "Add-to", null, null, null, null };
            def.isInteractive = 1;
            return def;
        }
        switch (objectId) {
            case 28296:
            case 28297:
                def.name = "Snow";
                def.modelIds = new int[]{ objectId == 28296 ? 45724 : 45723 };
                def.options = new String[]{ "Collect", null, null, null, null };
                def.isInteractive = 1;
                return def;

            case 28717:
                def.name = "Inert obelisk";
                def.modelIds = new int[]{ 45727 };
                def.isInteractive = 1;
                def.animationId = 8510;
                return def;

            case 28718:
                def.name = "Inert obelisk";
                def.modelIds = new int[]{ 45728 };
                def.isInteractive = 1;
                def.animationId = 8510;
                return def;

            case 28716:
                def.name = "Obelisk";
                def.options = new String[]{ "Infuse-Pouch", "Renew-Points", null, null, null, };
                def.modelIds = new int[]{ 45729 };
                def.sizeX = 2;
                def.sizeY = 2;
                def.isInteractive = 1;
                def.animationId = 8510;
                return def;

            case 28714:
                def.name = "Ladder";
                def.options = new String[]{ "Climb", null, null, null, null, };
                def.modelIds = new int[]{ 45730 };
                def.isInteractive = 1;
                return def;

            case 29882:
                def.name = "Small obelisk";
                def.options = new String[]{ "Renew-points", null, null, null, null };
                def.modelIds = new int[]{ 45728 };
                def.isInteractive = 1;
                def.animationId = 8510;
                return def;
        }
        def.readValues(buffer);
        def.configureObject();
        try {
            def.loadExamine();
        } catch (IOException e) {
            EXAMINES.put(-1, "Error"); // To prevent loadExamine() trying again
            log.error("Failed to load examines file!");
            e.printStackTrace();
        }

        if (objectId == 8772) {
            def.options = new String[]{ "Sit", null, null, null, null };
        }

        DEFINITIONS.put(objectId, def);
        return def;
    }

    private void loadExamine() throws IOException {
        if (EXAMINES.size() == 0) {
            JsonParser parser = new JsonParser();
            try (BufferedReader reader = Files.newBufferedReader(EXAMINES_PATH, StandardCharsets.UTF_8)) {
                JsonArray definitionJsonArray = parser.parse(reader).getAsJsonArray();
                for (JsonElement element : definitionJsonArray) {
                    JsonObject examine = (JsonObject) element;
                    int id = examine.get("id").getAsInt();
                    String description = examine.get("description").getAsString();

                    EXAMINES.put(id, description);
                }
            }
            log.info("Loaded {} Object Examines!", EXAMINES.size());
        }
        if (EXAMINES.containsKey(id)) {
            examine = EXAMINES.get(id);
        }
    }

    static RSStream buffer;
    static int count;
    static int[] indices;

    public static void unpackConfig() {
        ByteBuffer indexBuffer;
        buffer = new RSStream(FileOperations.getBytes(new File(Constants.CACHE_DIRECTORY + "/osrs/loc.dat")));
        indexBuffer = ByteBuffer.wrap(FileOperations.getBytes(new File(Constants.CACHE_DIRECTORY + "/osrs/loc.idx")));

        count = indexBuffer.getShort();
        indices = new int[count];
        int offset = 2;
        for (int index = 0; index < count; index++) {
            indices[index] = offset;
            offset += indexBuffer.getShort() & 0xFFFF;
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
                if ((modelIds == null)) {
                    modelConfiguration = new int[count];
                    modelIds = new int[count];

                    for (index = 0; index < count; index++) {
                        modelIds[index] = buffer.getShort();
                        modelConfiguration[index] = buffer.getByte();
                    }

                    return;
                }

                buffer.currentPosition += count * 3;
            }
        } else {
            if (opcode == 2) {
                name = buffer.readNewString();
                return;
            }

            if (opcode == 5) {
                count = buffer.getByte();
                if (count > 0) {
                    if ((modelIds == null)) {
                        modelConfiguration = null;
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
                projectileClipped = false;
                return;
            }

            if (opcode == 19) {
                isInteractive = buffer.getByte();
                return;
            }

            if (opcode == 21) {
                aByte3912 = 0;
                //contouredGround = true;
                return;
            }

            if (opcode == 22) {
                aBoolean3867 = true;
                return;
            }

            if (opcode == 23) {
                boolean1 = true;
                return;
            }

            if (opcode == 24) {
                animationId = buffer.getShort() + 20_000;
                if (animationId == 65535) {
                    animationId = -1;
                    return;
                }
            } else {
                if (opcode == 27) {
                    clipType = 1;
                    return;
                }

                if (opcode == 28) {
                    anInt3892 = buffer.getByte();
                    return;
                }

                if (opcode == 29) {
                    anInt3878 = buffer.getSignedByte();
                    return;
                }

                if (opcode == 39) {
                    anInt3840 = buffer.getSignedByte();
                    return;
                }

                if ((opcode >= 30) && (opcode < 35)) {
                    if (options == null) {
                        options = new String[5];
                    }
                    options[opcode - 30] = buffer.readNewString();
                    if (options[opcode - 30].equalsIgnoreCase("Hidden")) {
                        options[opcode - 30] = null;
                        return;
                    }
                } else {
                    if (opcode == 40) {
                        count = buffer.getByte();
                        //  modifiedModelColors = new int[count];
                        //  originalModelColors = new int[count];

                        for (index = 0; index < count; index++) {
                            /* modifiedModelColors[index] = (short)*/
                            buffer.getShort();
                            /*originalModelColors[index] = (short)*/
                            buffer.getShort();
                        }

                        return;
                    }

                    if (opcode == 41) {
                        count = buffer.getByte();
                        //textureToFind = new short[count];
                        //textureToReplace = new short[count];

                        for (index = 0; index < count; index++) {
                            buffer.getShort();
                            buffer.getShort();
                        }

                        return;
                    }

                    if (opcode == 62) {
                        aBoolean3839 = true;
                        return;
                    }

                    if (opcode == 64) {
                        aBoolean3872 = false;
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
                        walkingFlag = buffer.getByte();
                        return;
                    }

                    if (opcode == 70) {
                        buffer.readShort();
                        return;
                    }

                    if (opcode == 71) {
                        buffer.readShort();
                        return;
                    }

                    if (opcode == 72) {
                        buffer.readShort();
                        return;
                    }

                    if (opcode == 73) {
                        secondBool = true;
                        return;
                    }

                    if (opcode == 74) {
                        notClipped = true;
                        return;
                    }

                    if (opcode == 75) {
                        anInt3855 = buffer.getByte();
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
                        anInt3860 = buffer.getShort();
                        anInt3904 = buffer.getByte();
                        return;
                    }

                    if (opcode == 79) {
                        anInt3900 = buffer.getShort();
                        anInt3905 = buffer.getShort();
                        anInt3904 = buffer.getByte();
                        int i_64_ = buffer.getByte();
                        anIntArray3859 = new int[i_64_];
                        for (int i_65_ = 0; i_65_ < i_64_; i_65_++) {
                            anIntArray3859[i_65_] = buffer.getShort();
                        }
                        return;
                    }

                    if (opcode == 81) {
                        aByte3912 = 2;
                        anInt3882 = buffer.getByte() * 256;
                        return;
                    }

                    if (opcode == 82) {
                        mapIcon = (short) buffer.getShort();
                        return;
                    }

                    if (opcode == 249) {
                        method177(buffer);
                    } else {
                        log.warn("Unknown opcode: {}.", opcode);
                    }
                }
            }
        }

    }

    static final void method177(RSStream buffer) {
        final int count = buffer.getByte();
        int int_1 = method386(count);
        for (int_1 = 0; int_1 < count; int_1++) {
            final boolean bool_0 = buffer.getByte() == 1;
            final int int_2 = buffer.getTri();
            Object object_0;
            if (bool_0) {
                buffer.readNewString();
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

    final void configureObject() {
        if (isInteractive == -1) {
            isInteractive = 0;
            if (modelIds != null && (getModelConfiguration() == null || getModelConfiguration()[0] == 10)) {
                isInteractive = 1;
            }
            for (int i = 0; i < 5; i++) {
                if (options[i] != null) {
                    isInteractive = 1;
                    break;
                }
            }
        }
        if (notClipped) {
            clipType = 0;
            projectileClipped = false;
        }
        if (anInt3855 == -1) {
            anInt3855 = clipType == 0 ? 0 : 1;
        }
        if (id == 31017) {
            sizeX = sizeY = 2;
        }
    }

    /**
     * Checks if the object is visible.
     *
     * @return {@code True} if so.
     */
    public boolean hasActions() {
        if (childrenIds == null) {
            return hasOptions(false);
        }
        for (int i = 0; i < childrenIds.length; i++) {
            if (childrenIds[i] != -1) {
                ObjectDefinition def = forId(childrenIds[i]);
                if (def.hasOptions(false)) {
                    return true;
                }
            }
        }
        return hasOptions(false);
    }

    /**
     * Gets the child object definitions.
     *
     * @param player The player to get it for.
     * @return The object definition.
     */
    public ObjectDefinition getChildObject(Player player) {
        if (childrenIds == null || childrenIds.length < 1) {
            return this;
        }
        int configValue = -1;
        if (player != null) {
            if (configFileId != -1) {
                ConfigFileDefinition def = ConfigFileDefinition.forId(configFileId);
                if (def != null) {
                    configValue = def.getValue(player);
                }
            } else if (configId != -1) {
                configValue = player.getConfigManager().get(configId);
            }
        } else {
            configValue = 0;
        }
        if (configValue < 0 || configValue >= childrenIds.length - 1 || childrenIds[configValue] == -1) {
            int objectId = childrenIds[childrenIds.length - 1];
            if (objectId != -1) {
                return forId(objectId);
            }
            return this;
        }
        return forId(childrenIds[configValue]);
    }

    /**
     * Gets the config file definition.
     *
     * @return The config file definition.
     */
    public ConfigFileDefinition getConfigFile() {
        if (configFileId != -1) {
            return ConfigFileDefinition.forId(configFileId);
        }
        return null;
    }

    /**
     * Get the aBoolean3839.
     *
     * @return the aBoolean3839
     */
    public boolean isaBoolean3839() {
        return aBoolean3839;
    }

    /**
     * @param aBoolean3839 the aBoolean3839 to set
     */
    public void setaBoolean3839(boolean aBoolean3839) {
        this.aBoolean3839 = aBoolean3839;
    }

    /**
     * Get the originalColors.
     *
     * @return the originalColors
     */
    public short[] getOriginalColors() {
        return originalColors;
    }

    /**
     * Get the childrenIds.
     *
     * @return the childrenIds
     */
    public int[] getChildrenIds() {
        return childrenIds;
    }

    /**
     * Get the anInt3832.
     *
     * @return the anInt3832
     */
    public static int getAnInt3832() {
        return anInt3832;
    }

    /**
     * Get the anIntArray3833.
     *
     * @return the anIntArray3833
     */
    public int[] getAnIntArray3833() {
        return anIntArray3833;
    }

    /**
     * Get the anInt3834.
     *
     * @return the anInt3834
     */
    public int getAnInt3834() {
        return anInt3834;
    }

    /**
     * Get the anInt3835.
     *
     * @return the anInt3835
     */
    public int getAnInt3835() {
        return anInt3835;
    }

    /**
     * Get the anInt3836.
     *
     * @return the anInt3836
     */
    public static int getAnInt3836() {
        return anInt3836;
    }

    /**
     * Get the aByte3837.
     *
     * @return the aByte3837
     */
    public byte getaByte3837() {
        return aByte3837;
    }

    /**
     * Get the anInt3838.
     *
     * @return the anInt3838
     */
    public int getAnInt3838() {
        return anInt3838;
    }

    /**
     * Get the anInt3840.
     *
     * @return the anInt3840
     */
    public int getAnInt3840() {
        return anInt3840;
    }

    /**
     * Get the scaleY.
     *
     * @return the scaleY
     */
    public int getScaleY() {
        return scaleY;
    }

    /**
     * Get the anInt3842.
     *
     * @return the anInt3842
     */
    public static int getAnInt3842() {
        return anInt3842;
    }

    /**
     * Get the anInt3843.
     *
     * @return the anInt3843
     */
    public static int getAnInt3843() {
        return anInt3843;
    }

    /**
     * Get the anInt3844.
     *
     * @return the anInt3844
     */
    public int getAnInt3844() {
        return anInt3844;
    }

    /**
     * Get the aBoolean3845.
     *
     * @return the aBoolean3845
     */
    public boolean isaBoolean3845() {
        return aBoolean3845;
    }

    /**
     * Get the anInt3846.
     *
     * @return the anInt3846
     */
    public static int getAnInt3846() {
        return anInt3846;
    }

    /**
     * Get the aByte3847.
     *
     * @return the aByte3847
     */
    public byte getaByte3847() {
        return aByte3847;
    }

    /**
     * Get the aByte3849.
     *
     * @return the aByte3849
     */
    public byte getaByte3849() {
        return aByte3849;
    }

    /**
     * Get the anInt3850.
     *
     * @return the anInt3850
     */
    public int getAnInt3850() {
        return anInt3850;
    }

    /**
     * Get the anInt3851.
     *
     * @return the anInt3851
     */
    public int getAnInt3851() {
        return anInt3851;
    }

    /**
     * Get the secondBool.
     *
     * @return the secondBool
     */
    public boolean isSecondBool() {
        return secondBool;
    }

    /**
     * Get the aBoolean3853.
     *
     * @return the aBoolean3853
     */
    public boolean isaBoolean3853() {
        return aBoolean3853;
    }

    /**
     * Get the anInt3855.
     *
     * @return the anInt3855
     */
    public int getAnInt3855() {
        return anInt3855;
    }

    /**
     * Get the firstBool.
     *
     * @return the firstBool
     */
    public boolean isFirstBool() {
        return notClipped;
    }

    /**
     * Get the anInt3857.
     *
     * @return the anInt3857
     */
    public int getAnInt3857() {
        return anInt3857;
    }

    /**
     * Get the aByteArray3858.
     *
     * @return the aByteArray3858
     */
    public byte[] getaByteArray3858() {
        return aByteArray3858;
    }

    /**
     * Get the anIntArray3859.
     *
     * @return the anIntArray3859
     */
    public int[] getAnIntArray3859() {
        return anIntArray3859;
    }

    /**
     * Get the anInt3860.
     *
     * @return the anInt3860
     */
    public int getAnInt3860() {
        return anInt3860;
    }

    /**
     * Get the options.
     *
     * @return the options
     */
    @Override
    public String[] getOptions() {
        return options;
    }

    /**
     * Get the configFileId.
     *
     * @return the configFileId
     */
    public int getConfigFileId() {
        return configFileId;
    }

    /**
     * Get the modifiedColors.
     *
     * @return the modifiedColors
     */
    public short[] getModifiedColors() {
        return modifiedColors;
    }

    /**
     * Get the anInt3865.
     *
     * @return the anInt3865
     */
    public int getAnInt3865() {
        return anInt3865;
    }

    /**
     * Get the aBoolean3866.
     *
     * @return the aBoolean3866
     */
    public boolean isaBoolean3866() {
        return aBoolean3866;
    }

    /**
     * Get the aBoolean3867.
     *
     * @return the aBoolean3867
     */
    public boolean isaBoolean3867() {
        return aBoolean3867;
    }

    /**
     * Get the solid.
     *
     * @return the solid
     */
    public boolean isProjectileClipped() {
        return projectileClipped;
    }

    /**
     * Get the anIntArray3869.
     *
     * @return the anIntArray3869
     */
    public int[] getAnIntArray3869() {
        return anIntArray3869;
    }

    /**
     * Get the aBoolean3870.
     *
     * @return the aBoolean3870
     */
    public boolean isaBoolean3870() {
        return aBoolean3870;
    }

    /**
     * Get the sizeY.
     *
     * @return the sizeY
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     * Get the aBoolean3872.
     *
     * @return the aBoolean3872
     */
    public boolean isaBoolean3872() {
        return aBoolean3872;
    }

    /**
     * Get the membersOnly.
     *
     * @return the membersOnly
     */
    public boolean isaBoolean3873() {
        return membersOnly;
    }

    /**
     * Get the thirdInt.
     *
     * @return the thirdInt
     */
    public boolean getThirdBoolean() {
        return boolean1;
    }

    /**
     * Get the anInt3875.
     *
     * @return the anInt3875
     */
    public int getAnInt3875() {
        return anInt3875;
    }

    /**
     * Get the addObjectCheck.
     *
     * @return the addObjectCheck
     */
    public int getAddObjectCheck() {
        return animationId;
    }

    /**
     * Get the anInt3877.
     *
     * @return the anInt3877
     */
    public int getAnInt3877() {
        return anInt3877;
    }

    /**
     * Get the anInt3878.
     *
     * @return the anInt3878
     */
    public int getAnInt3878() {
        return anInt3878;
    }

    /**
     * Get the clipType.
     *
     * @return the clipType
     */
    public int getClipType() {
        return clipType;
    }

    /**
     * Get the anInt3881.
     *
     * @return the anInt3881
     */
    public int getAnInt3881() {
        return anInt3881;
    }

    /**
     * Get the anInt3882.
     *
     * @return the anInt3882
     */
    public int getAnInt3882() {
        return anInt3882;
    }

    /**
     * Get the anInt3883.
     *
     * @return the anInt3883
     */
    public int getAnInt3883() {
        return anInt3883;
    }

    /**
     * Get the anInt3889.
     *
     * @return the anInt3889
     */
    public int getAnInt3889() {
        return anInt3889;
    }

    /**
     * Get the sizeX.
     *
     * @return the sizeX
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * Get the aBoolean3891.
     *
     * @return the aBoolean3891
     */
    public boolean isaBoolean3891() {
        return aBoolean3891;
    }

    /**
     * Get the anInt3892.
     *
     * @return the anInt3892
     */
    public int getAnInt3892() {
        return anInt3892;
    }

    /**
     * Get the isInteractive.
     *
     * @return the isInteractive
     */
    public int getIsInteractive() {
        return isInteractive;
    }

    /**
     * Get the aBoolean3894.
     *
     * @return the aBoolean3894
     */
    public boolean isaBoolean3894() {
        return aBoolean3894;
    }

    /**
     * Get the aBoolean3895.
     *
     * @return the aBoolean3895
     */
    public boolean isaBoolean3895() {
        return aBoolean3895;
    }

    /**
     * Get the anInt3896.
     *
     * @return the anInt3896
     */
    public int getAnInt3896() {
        return anInt3896;
    }

    /**
     * Get the configId.
     *
     * @return the configId
     */
    public int getConfigId() {
        return configId;
    }

    /**
     * Get the aByteArray3899.
     *
     * @return the aByteArray3899
     */
    public byte[] getaByteArray3899() {
        return aByteArray3899;
    }

    /**
     * Get the anInt3900.
     *
     * @return the anInt3900
     */
    public int getAnInt3900() {
        return anInt3900;
    }

    /**
     * Get the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the scaleX.
     *
     * @return the scaleX
     */
    public int getScaleX() {
        return scaleX;
    }

    /**
     * Get the anInt3904.
     *
     * @return the anInt3904
     */
    public int getAnInt3904() {
        return anInt3904;
    }

    /**
     * Get the anInt3905.
     *
     * @return the anInt3905
     */
    public int getAnInt3905() {
        return anInt3905;
    }

    /**
     * Get the aBoolean3906.
     *
     * @return the aBoolean3906
     */
    public boolean isaBoolean3906() {
        return aBoolean3906;
    }

    /**
     * Get the anIntArray3908.
     *
     * @return the anIntArray3908
     */
    public int[] getAnIntArray3908() {
        return anIntArray3908;
    }

    /**
     * Get the aByte3912.
     *
     * @return the aByte3912
     */
    public byte getaByte3912() {
        return aByte3912;
    }

    /**
     * Get the anInt3913.
     *
     * @return the anInt3913
     */
    public int getAnInt3913() {
        return anInt3913;
    }

    /**
     * Get the aByte3914.
     *
     * @return the aByte3914
     */
    public byte getaByte3914() {
        return aByte3914;
    }

    /**
     * Get the anInt3915.
     *
     * @return the anInt3915
     */
    public int getAnInt3915() {
        return anInt3915;
    }

    /**
     * Get the anIntArrayArray3916.
     *
     * @return the anIntArrayArray3916
     */
    public int[][] getAnIntArrayArray3916() {
        return anIntArrayArray3916;
    }

    /**
     * Get the scaleZ.
     *
     * @return the scaleZ
     */
    public int getScaleZ() {
        return scaleZ;
    }

    /**
     * Get the aShortArray3919.
     *
     * @return the aShortArray3919
     */
    public short[] getaShortArray3919() {
        return aShortArray3919;
    }

    /**
     * Get the aShortArray3920.
     *
     * @return the aShortArray3920
     */
    public short[] getaShortArray3920() {
        return aShortArray3920;
    }

    /**
     * Get the anInt3921.
     *
     * @return the anInt3921
     */
    public int getAnInt3921() {
        return anInt3921;
    }

    /**
     * Get the aClass194_3922.
     *
     * @return the aClass194_3922
     */
    public Object getaClass194_3922() {
        return aClass194_3922;
    }

    /**
     * Get the aBoolean3923.
     *
     * @return the aBoolean3923
     */
    public boolean isaBoolean3923() {
        return aBoolean3923;
    }

    /**
     * Get the aBoolean3924.
     *
     * @return the aBoolean3924
     */
    public boolean isaBoolean3924() {
        return aBoolean3924;
    }

    /**
     * Gets the object's model ids.
     *
     * @return The model ids array.
     */
    public int[] getModelIds() {
        return modelIds;
    }

    /**
     * If the object has a action.
     *
     * @param action The specified action.
     * @return If the object has the action {@code true}.
     */
    public boolean hasAction(String action) {
        if (options == null) {
            return false;
        }
        for (String option : options) {
            if (option == null) {
                continue;
            }
            if (option.equalsIgnoreCase(action)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the definitions.
     *
     * @return the definitions
     */
    public static Map<Integer, ObjectDefinition> getDefinitions() {
        return DEFINITIONS;
    }
    
    public static int getHighestKey() {
        if (highestKey == -1) {
            Comparator<? super Map.Entry<Integer, ObjectDefinition>> maxValueComparator = Comparator.comparing(Map.Entry::getKey);
            Optional<Map.Entry<Integer, ObjectDefinition>> maxValue = DEFINITIONS.entrySet()
                                                                          .stream().max(maxValueComparator);
            highestKey = maxValue.get().getKey();
        }
        
        return highestKey;
    }

    /**
     * Gets the option handler for the given option name.
     *
     * @param nodeId The node id.
     * @param name   The name.
     * @return The option handler, or {@code null} if there was no default option handler.
     */
    public static OptionHandler getOptionHandler(int nodeId, String name) {
        ObjectDefinition def = forId(nodeId);
        OptionHandler handler = def.getConfiguration("option:" + name);
        if (handler != null) {
            return handler;
        }
        return OPTION_HANDLERS.get(name);
    }

    /**
     * Sets the default option handler for an option.
     *
     * @param name    The option name.
     * @param handler The default option handler.
     * @return {@code True} if there was a previous default handler mapped.
     */
    public static boolean setOptionHandler(String name, OptionHandler handler) {
        return OPTION_HANDLERS.put(name, handler) != null;
    }

    /**
     * Gets the hasHiddenOptions.
     *
     * @return The hasHiddenOptions.
     */
    public boolean isHasHiddenOptions() {
        return hasHiddenOptions;
    }

    /**
     * Sets the hasHiddenOptions.
     *
     * @param hasHiddenOptions The hasHiddenOptions to set.
     */
    public void setHasHiddenOptions(boolean hasHiddenOptions) {
        this.hasHiddenOptions = hasHiddenOptions;
    }

    /**
     * Gets the walking flag.
     *
     * @return The walking flag.
     */
    public int getWalkingFlag() {
        return walkingFlag;
    }

    /**
     * Gets the modelConfiguration.
     *
     * @return The modelConfiguration.
     */
    public int[] getModelConfiguration() {
        return modelConfiguration;
    }

    /**
     * Sets the modelConfiguration.
     *
     * @param modelConfiguration The modelConfiguration to set.
     */
    public void setModelConfiguration(int[] modelConfiguration) {
        this.modelConfiguration = modelConfiguration;
    }

    /**
     * Gets the mapIcon.
     *
     * @return The mapIcon.
     */
    public short getMapIcon() {
        return mapIcon;
    }

    public void boom() {
        StringBuilder sb = new StringBuilder();
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                if (field.getName().equals("OPTION_HANDLERS") || field.getName().equals("DEFINITIONS")) {
                    continue;
                }
                Object data = field.get(this);
                String array = "";
                if (field.get(this) instanceof int[]) {
                    int[] intArray = (int[]) field.get(this);
                    for (int index = 0; index < intArray.length; index++) {
                        array += field.getName() + "[" + index + "] = " + intArray[index] + "\r\n";
                    }
                    data = array;
                }
                if (field.get(this) instanceof int[][]) {
                    int[][] intArray = (int[][]) field.get(this);
                    for (int index = 0; index < intArray.length; index++) {
                        for (int index1 = 0; index1 < intArray[index].length; index1++) {
                            array += field.getName() + "[" + index + "][" + index1 + "] = " + intArray[index][index1] + "\r\n";
                        }
                    }
                    data = array;
                }
                if (field.get(this) instanceof String[]) {
                    String[] stringArray = (String[]) field.get(this);
                    for (int index = 0; index < stringArray.length; index++) {
                        array += field.getName() + "[" + index + "] = \"" + stringArray[index] + "\"\r\n";
                    }
                    data = array;
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                sb.append("rsComponent.").append(field.getName()).append(" = ").append(data).append(";\r\n");
            } catch (IllegalAccessException ex) {
                log.error("Error in objcet definition boom.", ex);
            }
        }
        System.out.println(sb.toString());
    }


    public void setModelIds(int[] modelIds) {
        this.modelIds = modelIds;
    }

    public void setScaleY(int scaleY) {
        this.scaleY = scaleY;
    }

    public void setSecondBool(boolean secondBool) {
        this.secondBool = secondBool;
    }

    public void setAnInt3855(int anInt3855) {
        this.anInt3855 = anInt3855;
    }

    public void setNotClipped(boolean notClipped) {
        this.notClipped = notClipped;
    }

    public void setConfigFileId(int configFileId) {
        this.configFileId = configFileId;
    }

    public void setProjectileClipped(boolean projectileClipped) {
        this.projectileClipped = projectileClipped;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }

    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
    }

    public void setAnimationId(int animationId) {
        this.animationId = animationId;
    }

    public void setClipType(int clipType) {
        this.clipType = clipType;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public void setIsInteractive(int isInteractive) {
        this.isInteractive = isInteractive;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public void setScaleX(int scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleZ(int scaleZ) {
        this.scaleZ = scaleZ;
    }

    public void setMapIcon(int mapIcon) {
        this.mapIcon = (short) mapIcon;
    }

    public void setWalkingFlag(int walkingFlag) {
        this.walkingFlag = walkingFlag;
    }

    public void setChildrenIds(int[] childrenIds) {
        this.childrenIds = childrenIds;
    }

}

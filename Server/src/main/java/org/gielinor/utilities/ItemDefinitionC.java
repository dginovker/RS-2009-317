package org.gielinor.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.eco.grandexchange.GrandExchangeDatabase;
import org.gielinor.utilities.misc.FileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Refactored reference from rename317
 * https://code.google.com/p/rename317/source
 * /browse/trunk/src/rs2/ItemDef.java?spec=svn202&r=202
 */
public class ItemDefinitionC {

    private static final Logger log = LoggerFactory.getLogger(ItemDefinitionC.class);

    /**
     * The interface model id opcode.
     */
    private static final int OPCODE_INTERFACE_MODEL_ID = 1;

    /**
     * The name opcode.
     */
    private static final int OPCODE_NAME = 2;

    /**
     * The model zoom opcode.
     */
    private static final int OPCODE_MODEL_ZOOM = 3;

    /**
     * The model rotation x opcode.
     */
    private static final int OPCODE_MODEL_ROTATION_X = 4;

    /**
     * The model rotation y opcode.
     */
    private static final int OPCODE_MODEL_ROTATION_Y = 5;

    /**
     * The model rotation z opcode.
     */
    private static final int OPCODE_MODEL_ROTATION_Z = 6;

    /**
     * The model position x opcode.
     */
    private static final int OPCODE_MODEL_POSITION_X = 7;

    /**
     * The model position y opcode.
     */
    private static final int OPCODE_MODEL_POSITION_Y = 8;

    /**
     * The stacking type opcode.
     */
    private static final int OPCODE_STACKING_TYPE = 9;

    /**
     * The value opcode.
     */
    private static final int OPCODE_VALUE = 10;

    /**
     * The members opcode.
     */
    private static final int OPCODE_MEMBERS = 11;

    /**
     * The male model id opcode.
     */
    private static final int OPCODE_MALE_MODEL_ID = 12;

    /**
     * The male model id2 opcode.
     */
    private static final int OPCODE_MALE_MODEL_ID2 = 13;

    /**
     * The male model id3 opcode.
     */
    private static final int OPCODE_MALE_MODEL_ID3 = 14;

    /**
     * The equipped_model_male_dialogue_1 opcode.
     */
    private static final int OPCODE_EQUIPPED_MODEL_MALE_DIALOGUE_1 = 15;

    /**
     * The equipped_model_male_dialogue_2 opcode.
     */
    private static final int OPCODE_EQUIPPED_MODEL_MALE_DIALOGUE_2 = 16;

    /**
     * The female model id opcode.
     */
    private static final int OPCODE_FEMALE_MODEL_ID = 17;

    /**
     * The female model id2 opcode.
     */
    private static final int OPCODE_FEMALE_MODEL_ID2 = 18;

    /**
     * The female model id3 opcode.
     */
    private static final int OPCODE_FEMALE_MODEL_ID3 = 19;

    /**
     * The equipped_model_female_dialogue_1 opcode.
     */
    private static final int OPCODE_EQUIPPED_MODEL_FEMALE_DIALOGUE_1 = 20;

    /**
     * The equipped_model_female_dialogue_2 opcode.
     */
    private static final int OPCODE_EQUIPPED_MODEL_FEMALE_DIALOGUE_2 = 21;

    /**
     * The floor options start opcode.
     */
    private static final int OPCODE_FLOOR_OPTIONS_START = 22;

    /**
     * The floor options end opcode.
     */
    private static final int OPCODE_FLOOR_OPTIONS_END = 26;

    /**
     * The inventory options start opcode.
     */
    private static final int OPCODE_INVENTORY_OPTIONS_START = 27;

    /**
     * The inventory options end opcode.
     */
    private static final int OPCODE_INVENTORY_OPTIONS_END = 31;

    /**
     * The equipment options start opcode.
     */
    private static final int OPCODE_EQUIPMENT_OPTIONS_START = 32;

    /**
     * The equipment options end opcode.
     */
    private static final int OPCODE_EQUIPMENT_OPTIONS_END = 36;

    /**
     * The colors opcode.
     */
    private static final int OPCODE_COLORS = 37;

    /**
     * The note item id opcode.
     */
    private static final int OPCODE_NOTE_ITEM_ID = 38;

    /**
     * The note template id opcode.
     */
    private static final int OPCODE_NOTE_TEMPLATE_ID = 39;

    /**
     * The stack ids start opcode.
     */
    private static final int OPCODE_STACK_IDS_START = 40;

    /**
     * The stack ids end opcode.
     */
    private static final int OPCODE_STACK_IDS_END = 49;

    /**
     * The model scale x opcode.
     */
    private static final int OPCODE_MODEL_SCALE_X = 50;

    /**
     * The model scale y opcode.
     */
    private static final int OPCODE_MODEL_SCALE_Y = 51;

    /**
     * The model scale z opcode.
     */
    private static final int OPCODE_MODEL_SCALE_Z = 52;

    /**
     * The light intensity opcode.
     */
    private static final int OPCODE_LIGHT_INTENSITY = 53;

    /**
     * The light magnitude opcode.
     */
    private static final int OPCODE_LIGHT_MAGNITUDE = 54;

    /**
     * The team opcode.
     */
    private static final int OPCODE_TEAM = 55;

    /**
     * The lent id opcode.
     */
    private static final int OPCODE_LENT_ID = 56;

    /**
     * The lent template id opcode.
     */
    private static final int OPCODE_LENT_TEMPLATE_ID = 57;

    /**
     * The tradable opcode.
     */
    private static final int OPCODE_TRADABLE = 58;

    /**
     * The blacklisted opcode.
     */
    private static final int OPCODE_BLACKLISTED = 59;

    /**
     * The OSRS opcode.
     */
    private static final int OPCODE_OSRS = 60;

    static boolean newDefs = true;

    public static void unpackConfig() {
        RSStream stream = null;
        String file = newDefs ? "objFA" : "obj317";
        itemStream = new RSStream(FileOperations.readFile(file + ".dat"));
        stream = new RSStream(FileOperations.readFile(file + ".idx"));
        itemCount = stream.getShort() + 21;
        streamIndices = new int[itemCount + CUSTOM_ITEMS];
        int offset = 2;

        for (int _ctr = 0; _ctr < itemCount - 21; _ctr++) {
            streamIndices[_ctr] = offset;
            offset += stream.getShort();
        }
        cache = new ItemDefinitionC[10];
        for (int _ctr = 0; _ctr < 10; _ctr++) {
            cache[_ctr] = new ItemDefinitionC(-1);
        }
    }

    private void setDefaults() {
        interfaceModelId = 0;
        name = null;
        description = null;
        modifiedColors = null;
        originalColors = null;
        modelZoom = 2000;
        modelRotationY = 0;
        modelRotationX = 0;
        modelRotationZ = 0;
        modelPositionX = 0;
        modelPositionY = 0;
        modelOffsetXOriginal = 0;
        modelOffsetYOriginal = 0;
        stackingType = false;
        value = 1;
        members = false;
        equipmentActions = new String[6];
        floorOptions = null;
        options = null;
        maleModelId = -1;
        maleArmModelId = -1;
        maleModelTranslationY = 0;
        femaleModelId = -1;
        femaleArmModelId = -1;
        femaleModelTranslationY = 0;
        maleModelId3 = -1;
        femaleModelId3 = -1;
        equipped_model_male_dialogue_1 = -1;
        equipped_model_male_dialogue_2 = -1;
        equipped_model_female_dialogue_1 = -1;
        equipped_model_female_dialogue_2 = -1;
        stackIds = null;
        stackAmounts = null;
        noteItemId = -1;
        noteTemplateId = -1;
        modelScaleX = 128;
        modelScaleY = 128;
        modelScaleZ = 128;
        lightIntensity = 0;
        lightMagnitude = 0;
        team = 0;
        lentId = -1;
        lentTemplateId = -1;
        modelType = ModelType.ITEM;
        trade = false;
    }

    public static ItemDefinitionC forId(int itemId, boolean newDefinition, boolean setDefaults, boolean copy, int test) {
        for (int count = 0; count < 10; count++) {
            if (cache[count].id == itemId) {
                //return cache[count];
            }
        }

        cacheIndex = (cacheIndex + 1) % 10;
        ItemDefinitionC itemDefinition = cache[cacheIndex];
        itemStream.currentPosition = streamIndices[itemId];
        itemDefinition.id = itemId;
        if (setDefaults) {
            itemDefinition.setDefaults();
        }
        if (itemDefinition.id < 14761) {
            if (newDefinition) {
                itemDefinition.readValuesNew(itemStream);
            } else {
                itemDefinition.readValues(itemStream);
            }
        }

        ItemDefinition itm = ItemDefinition.forId(itemId);
        if (itm != null) {
            itemDefinition.trade = itm.isTradeable();
            itemDefinition.blacklisted = GrandExchangeDatabase.getDatabase().get(itemId) == null;
        }
        if (ItemRepository.forId(itemId, itemDefinition) != null) {
            return ItemRepository.forId(itemId, itemDefinition);
        }
        copy = (test == 1);
        if (itemId > 14761 && copy) {
            int offset = 14761 - 11686;
            //12924
            if ((itemId - offset) > 21072) {
                return itemDefinition;
            }
            itemDefinition = itemDefinition.copyFrom(itemId, OSRSItemDefinition.forId(itemId - offset));
            return itemDefinition;
        }
        return itemDefinition;
    }

    public void toNote() {
        ItemDefinitionC itemDef = forId(noteTemplateId, true, false, false, 1);
        interfaceModelId = itemDef.interfaceModelId;
        modelZoom = itemDef.modelZoom;
        modelRotationY = itemDef.modelRotationY;
        modelRotationX = itemDef.modelRotationX;

        modelRotationZ = itemDef.modelRotationZ;
        modelPositionX = itemDef.modelPositionX;
        modelPositionY = itemDef.modelPositionY;
        originalColors = itemDef.originalColors;
        modifiedColors = itemDef.modifiedColors;
        ItemDefinitionC itemDef_1 = forId(noteItemId, true, false, false, 1);
        name = itemDef_1.name;
        members = itemDef_1.members;
        value = itemDef_1.value;
        String s = "a";
        char c = itemDef_1.name.charAt(0);
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
            s = "an";
        }
        stackingType = true;
    }

    public void readValuesNew(RSStream rsStream) {
        int[] opcodeHistory = new int[5];
        do {
            int opcode = rsStream.getByte();
            if (opcode == 0) {
                return;
            }
            switch (opcode) {
                case OPCODE_INTERFACE_MODEL_ID:
                    interfaceModelId = rsStream.getShort();
                    break;

                case OPCODE_NAME:
                    name = rsStream.readString();
                    break;

                case OPCODE_MODEL_ZOOM:
                    modelZoom = rsStream.getShort();
                    break;

                case OPCODE_MODEL_ROTATION_X:
                    modelRotationX = rsStream.getShort();
                    break;

                case OPCODE_MODEL_ROTATION_Y:
                    modelRotationY = rsStream.getShort();
                    break;

                case OPCODE_MODEL_ROTATION_Z:
                    modelRotationZ = rsStream.getShort();
                    break;

                case OPCODE_MODEL_POSITION_X:
                    int positionX = rsStream.getShort();
                    modelPositionX = positionX;
                    modelOffsetXOriginal = positionX;
                    if (modelPositionX > 32767) {
                        modelPositionX -= 0x10000;
                    }
                    break;

                case OPCODE_MODEL_POSITION_Y:
                    int positionY = rsStream.getShort();
                    modelPositionY = positionY;
                    modelOffsetYOriginal = positionY;
                    if (modelPositionY > 32767) {
                        modelPositionY -= 0x10000;
                    }
                    break;

                case OPCODE_STACKING_TYPE:
                    stackingType = true;
                    break;

                case OPCODE_VALUE:
                    value = rsStream.getInt();
                    break;

                case OPCODE_MEMBERS:
                    members = true;
                    break;

                case OPCODE_MALE_MODEL_ID:
                    maleModelId = rsStream.getShort();
                    maleModelTranslationY = rsStream.getSignedByte();
                    break;

                case OPCODE_MALE_MODEL_ID2:
                    maleArmModelId = rsStream.getShort();
                    break;

                case OPCODE_MALE_MODEL_ID3:
                    maleModelId3 = rsStream.getShort();
                    break;

                case OPCODE_EQUIPPED_MODEL_MALE_DIALOGUE_1:
                    equipped_model_male_dialogue_1 = rsStream.getShort();
                    break;

                case OPCODE_EQUIPPED_MODEL_MALE_DIALOGUE_2:
                    equipped_model_male_dialogue_2 = rsStream.getShort();
                    break;

                case OPCODE_FEMALE_MODEL_ID:
                    femaleModelId = rsStream.getShort();
                    femaleModelTranslationY = rsStream.getSignedByte();
                    break;

                case OPCODE_FEMALE_MODEL_ID2:
                    femaleArmModelId = rsStream.getShort();
                    break;

                case OPCODE_FEMALE_MODEL_ID3:
                    femaleModelId3 = rsStream.getShort();
                    break;

                case OPCODE_EQUIPPED_MODEL_FEMALE_DIALOGUE_1:
                    equipped_model_female_dialogue_1 = rsStream.getShort();
                    break;

                case OPCODE_EQUIPPED_MODEL_FEMALE_DIALOGUE_2:
                    equipped_model_female_dialogue_2 = rsStream.getShort();
                    break;

                case OPCODE_COLORS:
                    int length = rsStream.getByte();
                    modifiedColors = new int[length];
                    originalColors = new int[length];
                    for (int k = 0; k < length; k++) {
                        modifiedColors[k] = rsStream.getShort();
                        originalColors[k] = rsStream.getShort();
                    }
                    break;

                case OPCODE_NOTE_ITEM_ID:
                    noteItemId = rsStream.getShort();
                    break;

                case OPCODE_NOTE_TEMPLATE_ID:
                    noteTemplateId = rsStream.getShort();
                    break;

                case OPCODE_MODEL_SCALE_X:
                    modelScaleX = rsStream.getShort();
                    break;

                case OPCODE_MODEL_SCALE_Y:
                    modelScaleY = rsStream.getShort();
                    break;

                case OPCODE_MODEL_SCALE_Z:
                    modelScaleZ = rsStream.getShort();
                    break;

                case OPCODE_LIGHT_INTENSITY:
                    lightIntensity = rsStream.getSignedByte();
                    break;

                case OPCODE_LIGHT_MAGNITUDE:
                    lightMagnitude = rsStream.getSignedByte() * 5;
                    break;

                case OPCODE_TEAM:
                    team = rsStream.getByte();
                    break;

                case OPCODE_LENT_ID:
                    lentId = rsStream.getShort();
                    break;

                case OPCODE_LENT_TEMPLATE_ID:
                    lentTemplateId = rsStream.getShort();
                    break;

                case OPCODE_TRADABLE:
                    trade = true;
                    break;

                case OPCODE_BLACKLISTED:
                    blacklisted = true;
                    break;

                default:
                    if (opcode >= OPCODE_FLOOR_OPTIONS_START && opcode <= OPCODE_FLOOR_OPTIONS_END) {
                        if (floorOptions == null) {
                            floorOptions = new String[5];
                        }
                        hasGroundActions = true;
                        floorOptions[opcode - OPCODE_FLOOR_OPTIONS_START] = rsStream.readString();
                        if (floorOptions[opcode - OPCODE_FLOOR_OPTIONS_START] != null &&
                            floorOptions[opcode - OPCODE_FLOOR_OPTIONS_START].equals("Take")) {
                            floorOptions[opcode - OPCODE_FLOOR_OPTIONS_START] = "hidden";
                        }
                        if (floorOptions[opcode - OPCODE_FLOOR_OPTIONS_START].equalsIgnoreCase("hidden")) {
                            floorOptions[opcode - OPCODE_FLOOR_OPTIONS_START] = null;
                        }
                    } else if (opcode >= OPCODE_INVENTORY_OPTIONS_START && opcode <= OPCODE_INVENTORY_OPTIONS_END) {
                        if (options == null) {
                            options = new String[5];
                        }
                        hasInventoryOptions = true;
                        options[opcode - OPCODE_INVENTORY_OPTIONS_START] = rsStream.readString();
                        if (options[opcode - OPCODE_INVENTORY_OPTIONS_START].equalsIgnoreCase("hidden")) {
                            options[opcode - OPCODE_INVENTORY_OPTIONS_START] = null;
                        }
                    } else if (opcode >= OPCODE_EQUIPMENT_OPTIONS_START && opcode <= OPCODE_EQUIPMENT_OPTIONS_END) {
                        if (equipmentActions == null) {
                            equipmentActions = new String[5];
                        }
                        hasEquipmentOptions = true;
                        equipmentActions[opcode - OPCODE_EQUIPMENT_OPTIONS_START] = rsStream.readString();
                        if (equipmentActions[opcode - OPCODE_EQUIPMENT_OPTIONS_START].equalsIgnoreCase("hidden")) {
                            equipmentActions[opcode - OPCODE_EQUIPMENT_OPTIONS_START] = null;
                        }
                    } else if (opcode >= OPCODE_STACK_IDS_START && opcode <= OPCODE_STACK_IDS_END) {
                        if (stackIds == null) {
                            stackIds = new int[10];
                            stackAmounts = new int[10];
                        }
                        stackIds[opcode - OPCODE_STACK_IDS_START] = rsStream.getShort();
                        stackAmounts[opcode - OPCODE_STACK_IDS_START] = rsStream.getShort();
                    } else {
                        log.warn("Missing definition for item [{}]. Opcode: [{}]. History: [{}].",
                            id, opcode, Arrays.toString(opcodeHistory));
                    }
                    break;
            }
            System.arraycopy(opcodeHistory, 0, opcodeHistory, 1, opcodeHistory.length - 2 + 1);
            opcodeHistory[0] = opcode;
        } while (true);
    }

    public void readValues(RSStream rsStream) {
        do {
            int opcode = rsStream.getByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                interfaceModelId = rsStream.getShort();
            } else if (opcode == 2) {
                name = rsStream.readString();
            } else if (opcode == 3) {
                description = rsStream.readBytes();
            } else if (opcode == 4) {
                modelZoom = rsStream.getShort();
            } else if (opcode == 5) {
                modelRotationY = rsStream.getShort();
            } else if (opcode == 6) {
                modelRotationX = rsStream.getShort();
            } else if (opcode == 7) {
                int a = rsStream.getShort();
                modelPositionX = a;
                modelOffsetXOriginal = a;
                if (modelPositionX > 32767) {
                    modelPositionX -= 0x10000;
                }
            } else if (opcode == 8) {
                int b = rsStream.getShort();
                modelPositionY = b;
                modelOffsetYOriginal = b;
                if (modelPositionY > 32767) {
                    modelPositionY -= 0x10000;
                }
            } else if (opcode == 10) {
                rsStream.getShort();
            } else if (opcode == 11) {
                stackingType = true;
            } else if (opcode == 12) {
                value = rsStream.getInt();
            } else if (opcode == 16) {
                members = true;
            } else if (opcode == 23) {
                maleModelId = rsStream.getShort();
                maleModelTranslationY = rsStream.getSignedByte();
            } else if (opcode == 24) {
                maleArmModelId = rsStream.getShort();
            } else if (opcode == 25) {
                femaleModelId = rsStream.getShort();
                femaleModelTranslationY = rsStream.getSignedByte();
            } else if (opcode == 26) {
                femaleArmModelId = rsStream.getShort();
            } else if (opcode >= 30 && opcode < 35) {
                if (floorOptions == null) {
                    floorOptions = new String[5];
                }
                hasGroundActions = true;
                floorOptions[opcode - 30] = rsStream.readString();
                if (floorOptions[opcode - 30].equalsIgnoreCase("hidden")) {
                    floorOptions[opcode - 30] = null;
                }
            } else if (opcode >= 35 && opcode < 40) {
                if (options == null) {
                    options = new String[5];
                }
                hasInventoryOptions = true;
                options[opcode - 35] = rsStream.readString();
            } else if (opcode == 40) {
                int length = rsStream.getByte();
                modifiedColors = new int[length];
                originalColors = new int[length];
                for (int k = 0; k < length; k++) {
                    modifiedColors[k] = rsStream.getShort();
                    originalColors[k] = rsStream.getShort();
                }
            } else if (opcode == 78) {
                maleModelId3 = rsStream.getShort();
            } else if (opcode == 79) {
                femaleModelId3 = rsStream.getShort();
            } else if (opcode == 90) {
                equipped_model_male_dialogue_1 = rsStream.getShort();
            } else if (opcode == 91) {
                equipped_model_female_dialogue_1 = rsStream.getShort();
            } else if (opcode == 92) {
                equipped_model_male_dialogue_2 = rsStream.getShort();
            } else if (opcode == 93) {
                equipped_model_female_dialogue_2 = rsStream.getShort();
            } else if (opcode == 95) {
                modelRotationZ = rsStream.getShort();
            } else if (opcode == 97) {
                noteItemId = rsStream.getShort();
            } else if (opcode == 98) {
                noteTemplateId = rsStream.getShort();
            } else if (opcode >= 100 && opcode < 110) {
                if (stackIds == null) {
                    stackIds = new int[10];
                    stackAmounts = new int[10];
                }
                stackIds[opcode - 100] = rsStream.getShort();
                stackAmounts[opcode - 100] = rsStream.getShort();
            } else if (opcode == 110) {
                modelScaleX = rsStream.getShort();
            } else if (opcode == 111) {
                modelScaleY = rsStream.getShort();
            } else if (opcode == 112) {
                modelScaleZ = rsStream.getShort();
            } else if (opcode == 113) {
                lightIntensity = rsStream.getSignedByte();
            } else if (opcode == 114) {
                lightMagnitude = rsStream.getSignedByte() * 5;
            } else if (opcode == 115) {
                team = rsStream.getByte();
            } else if (opcode == 121) {
                lentId = rsStream.getShort();
            } else if (opcode == 122) {
                lentTemplateId = rsStream.getShort();
            }
        } while (true);
    }

    public ItemDefinitionC(int id) {
        this.id = id;
    }

    public byte femaleModelTranslationY;
    public int lentId;
    public int lentTemplateId;
    public int value;
    public int[] modifiedColors;
    public int id;
    public int[] originalColors;
    public boolean members;
    public int femaleModelId3;
    public int noteTemplateId;
    public int femaleArmModelId;
    public int maleModelId;
    public int equipped_model_male_dialogue_2;
    public int modelScaleX;
    public String[] equipmentActions;
    public String[] floorOptions;
    boolean hasGroundActions = false;
    public boolean hasInventoryOptions = false;
    public boolean hasEquipmentOptions = false;
    public int modelPositionX;
    public int modelOffsetXOriginal;
    public int modelOffsetYOriginal;
    public String name;
    private static ItemDefinitionC[] cache;
    public int equipped_model_female_dialogue_2;
    public int interfaceModelId;
    public int equipped_model_male_dialogue_1;
    public boolean stackingType;
    public byte[] description;
    public int noteItemId;
    private static int cacheIndex;
    public int modelZoom;
    public static boolean isMembers = true;
    private static RSStream itemStream;
    public int lightMagnitude;
    public int maleModelId3;
    public int maleArmModelId;
    public String[] options;
    public int modelRotationY;
    public int modelScaleZ;
    public int modelScaleY;
    public int[] stackIds;
    public int modelPositionY;//
    private static int[] streamIndices;
    public int lightIntensity;
    public int equipped_model_female_dialogue_1;
    public int modelRotationX;
    public int femaleModelId;
    public int[] stackAmounts;
    public int team;
    public static int itemCount;
    public int modelRotationZ;
    public byte maleModelTranslationY;
    public ModelType modelType;
    public boolean trade;
    public boolean blacklisted;
    public boolean osrs;

    /**
     * Represents a model type.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public enum ModelType {

        ITEM(4),
        PLAYER(4),
        NPC(4),
        CUSTOM_NPC(5),
        CUSTOM_ITEM(5),
        OBJECT(-1),
        COMPONENT(-1),
        GRAPHIC(-1),
        IDENTITY_KIT(-1),;
        /**
         * The type.
         */
        private final int type;

        /**
         * Constructs a model type.
         *
         * @param type The type.
         */
        ModelType(int type) {
            this.type = type;
        }

        /**
         * Gets the type.
         *
         * @return the type.
         */
        public int getType() {
            return type;
        }

        /**
         * Gets a {@link org.gielinor.utilities.ItemDefinitionC.ModelType} by the type.
         *
         * @param type The type.
         * @return The <code>ModelType</code>.
         */
        public static ModelType forType(int type) {
            for (ModelType modelType : values()) {
                if (modelType.type == type) {
                    return modelType;
                }
            }
            return null;
        }
    }

    /**
     * Copies this definition to a new definition.
     *
     * @param copy           The id of the item to copy.
     * @param itemDefinition The new item definition.
     */
    public static void copy(int copy, ItemDefinitionC itemDefinition) {
        ItemDefinitionC copyDefinition = new ItemDefinitionC(copy);//forId(copy);
        copyDefinition.id = copy;
        itemDefinition.name = copyDefinition.name;
        itemDefinition.interfaceModelId = copyDefinition.interfaceModelId;
        itemDefinition.description = copyDefinition.description;
        itemDefinition.modifiedColors = copyDefinition.modifiedColors;
        itemDefinition.originalColors = copyDefinition.originalColors;
        itemDefinition.modelZoom = copyDefinition.modelZoom;
        itemDefinition.modelRotationY = copyDefinition.modelRotationY;
        itemDefinition.modelRotationX = copyDefinition.modelRotationX;
        itemDefinition.modelRotationZ = copyDefinition.modelRotationZ;
        itemDefinition.modelPositionX = copyDefinition.modelPositionX;
        itemDefinition.modelPositionY = copyDefinition.modelPositionY;
        itemDefinition.modelOffsetXOriginal = copyDefinition.modelOffsetXOriginal;
        itemDefinition.modelOffsetYOriginal = copyDefinition.modelOffsetYOriginal;
        itemDefinition.stackingType = copyDefinition.stackingType;
        itemDefinition.value = copyDefinition.value;
        itemDefinition.members = copyDefinition.members;
        itemDefinition.equipmentActions = copyDefinition.equipmentActions;
        itemDefinition.floorOptions = copyDefinition.floorOptions;
        itemDefinition.options = copyDefinition.options;
        itemDefinition.maleModelId = copyDefinition.maleModelId;
        itemDefinition.maleArmModelId = copyDefinition.maleArmModelId;
        itemDefinition.maleModelTranslationY = copyDefinition.maleModelTranslationY;
        itemDefinition.femaleModelId = copyDefinition.femaleModelId;
        itemDefinition.femaleArmModelId = copyDefinition.femaleArmModelId;
        itemDefinition.femaleModelTranslationY = copyDefinition.femaleModelTranslationY;
        itemDefinition.maleModelId3 = copyDefinition.maleModelId3;
        itemDefinition.femaleModelId3 = copyDefinition.femaleModelId3;
        itemDefinition.equipped_model_male_dialogue_1 = copyDefinition.equipped_model_male_dialogue_1;
        itemDefinition.equipped_model_male_dialogue_2 = copyDefinition.equipped_model_male_dialogue_2;
        itemDefinition.equipped_model_female_dialogue_1 = copyDefinition.equipped_model_female_dialogue_1;
        itemDefinition.equipped_model_female_dialogue_2 = copyDefinition.equipped_model_female_dialogue_2;
        itemDefinition.stackIds = copyDefinition.stackIds;
        itemDefinition.stackAmounts = copyDefinition.stackAmounts;
        itemDefinition.noteItemId = copyDefinition.noteItemId;
        itemDefinition.noteTemplateId = copyDefinition.noteTemplateId;
        itemDefinition.modelScaleX = copyDefinition.modelScaleX;
        itemDefinition.modelScaleY = copyDefinition.modelScaleY;
        itemDefinition.modelScaleZ = copyDefinition.modelScaleZ;
        itemDefinition.lightIntensity = copyDefinition.lightIntensity;
        itemDefinition.lightMagnitude = copyDefinition.lightMagnitude;
    }

    /**
     */
    public ItemDefinitionC copyFrom(int newId, OSRSItemDefinition osrsItemDefinition) {
        this.id = newId;
        interfaceModelId = osrsItemDefinition.interfaceModelId;
        name = osrsItemDefinition.name;
        modelZoom = osrsItemDefinition.modelZoom;
        modelRotationX = osrsItemDefinition.modelRotationX;
        modelRotationY = osrsItemDefinition.modelRotationY;
        modelRotationZ = osrsItemDefinition.modelRotationZ;
        int positionX = osrsItemDefinition.modelOffsetXOriginal;
        modelPositionX = positionX;
        modelOffsetXOriginal = positionX;
        if (modelPositionX > 32767) {
            modelPositionX -= 0x10000;
        }
        int positionY = osrsItemDefinition.modelOffsetYOriginal;
        modelPositionY = positionY;
        modelOffsetYOriginal = positionY;
        if (modelPositionY > 32767) {
            modelPositionY -= 0x10000;
        }
        stackingType = osrsItemDefinition.stackingType;
        value = osrsItemDefinition.value;
        members = true;
        maleModelId = osrsItemDefinition.maleModelId;
        maleModelTranslationY = osrsItemDefinition.maleModelTranslationY;
        maleArmModelId = osrsItemDefinition.maleArmModelId;
        maleModelId3 = osrsItemDefinition.maleModelId3;
        equipped_model_male_dialogue_1 = osrsItemDefinition.equipped_model_male_dialogue_1;
        equipped_model_male_dialogue_2 = osrsItemDefinition.equipped_model_male_dialogue_2;
        femaleModelId = osrsItemDefinition.femaleModelId;
        femaleModelTranslationY = osrsItemDefinition.femaleModelTranslationY;
        femaleArmModelId = osrsItemDefinition.femaleArmModelId;
        femaleModelId3 = osrsItemDefinition.femaleModelId3;
        equipped_model_female_dialogue_1 = osrsItemDefinition.equipped_model_female_dialogue_1;
        equipped_model_female_dialogue_2 = osrsItemDefinition.equipped_model_female_dialogue_2;
        modifiedColors = osrsItemDefinition.modifiedColors;
        originalColors = osrsItemDefinition.originalColors;
        noteItemId = osrsItemDefinition.noteItemId;
        noteTemplateId = osrsItemDefinition.noteTemplateId;

        modelScaleX = osrsItemDefinition.modelScaleX;
        modelScaleY = osrsItemDefinition.modelScaleY;
        modelScaleZ = osrsItemDefinition.modelScaleZ;
        lightIntensity = osrsItemDefinition.lightIntensity;
        lightMagnitude = osrsItemDefinition.lightMagnitude;
        team = osrsItemDefinition.team;
        blacklisted = true;
        if (osrsItemDefinition.floorOptions != null) {
            hasGroundActions = true;
            floorOptions = osrsItemDefinition.floorOptions;
        }
        if (osrsItemDefinition.options != null) {
            hasInventoryOptions = true;
            options = osrsItemDefinition.options;
        }
        stackIds = osrsItemDefinition.stackIds;
        stackAmounts = osrsItemDefinition.stackAmounts;
        return this;
    }

    public ItemDefinitionC copy() {
        ItemDefinitionC newDefinition = new ItemDefinitionC(id);
        newDefinition.id = id;
        newDefinition.interfaceModelId = interfaceModelId;
        newDefinition.name = name;
        newDefinition.modelZoom = modelZoom;
        newDefinition.modelRotationX = modelRotationX;
        newDefinition.modelRotationY = modelRotationY;
        newDefinition.modelRotationZ = modelRotationZ;
        int positionX = modelOffsetXOriginal;
        newDefinition.modelPositionX = positionX;
        newDefinition.modelOffsetXOriginal = positionX;
        if (modelPositionX > 32767) {
            newDefinition.modelPositionX -= 0x10000;
        }
        int positionY = modelOffsetYOriginal;
        newDefinition.modelPositionY = positionY;
        newDefinition.modelOffsetYOriginal = positionY;
        if (modelPositionY > 32767) {
            newDefinition.modelPositionY -= 0x10000;
        }
        newDefinition.stackingType = stackingType;
        newDefinition.value = value;
        newDefinition.members = true;
        newDefinition.maleModelId = maleModelId;
        newDefinition.maleModelTranslationY = maleModelTranslationY;
        newDefinition.maleArmModelId = maleArmModelId;
        newDefinition.maleModelId3 = maleModelId3;
        newDefinition.equipped_model_male_dialogue_1 = equipped_model_male_dialogue_1;
        newDefinition.equipped_model_male_dialogue_2 = equipped_model_male_dialogue_2;
        newDefinition.femaleModelId = femaleModelId;
        newDefinition.femaleModelTranslationY = femaleModelTranslationY;
        newDefinition.femaleArmModelId = femaleArmModelId;
        newDefinition.femaleModelId3 = femaleModelId3;
        newDefinition.equipped_model_female_dialogue_1 = equipped_model_female_dialogue_1;
        newDefinition.equipped_model_female_dialogue_2 = equipped_model_female_dialogue_2;
        newDefinition.modifiedColors = modifiedColors;
        newDefinition.originalColors = originalColors;
        newDefinition.noteItemId = noteItemId;
        newDefinition.noteTemplateId = noteTemplateId;

        newDefinition.modelScaleX = modelScaleX;
        newDefinition.modelScaleY = modelScaleY;
        newDefinition.modelScaleZ = modelScaleZ;
        newDefinition.lightIntensity = lightIntensity;
        newDefinition.lightMagnitude = lightMagnitude;
        newDefinition.team = team;
        newDefinition.blacklisted = true;
        if (floorOptions != null) {
            newDefinition.hasGroundActions = true;
            newDefinition.floorOptions = floorOptions;
        }
        if (options != null) {
            newDefinition.hasInventoryOptions = true;
            newDefinition.options = options;
        }
        newDefinition.stackIds = stackIds;
        newDefinition.stackAmounts = stackAmounts;
        return newDefinition;
    }

    public void dump() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("case ").append(this.id).append(":\n");
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                if (field.getName().equals("cache")) {
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

                stringBuilder.append("itemDefinition.").append(field.getName()).append(" = ").append(data).append(";\r\n");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        stringBuilder.append("break;\n\n");
        System.out.println(stringBuilder.toString());
    }

    public static final int CUSTOM_ITEMS = 41 + 9390;

    public static int getItemCount() {
        return 24125;//itemCount + CUSTOM_ITEMS;
    }
}

package org.gielinor.utilities;

import org.gielinor.utilities.misc.FileOperations;

public final class OSRSItemDefinition extends ItemDefinitionC {

    public boolean hasGroundActions = false;
    public boolean hasInventoryOptions = false;
    boolean blacklisted = true;

    public static void unpackConfig() {
        RSStream stream = null;
        itemStream = new RSStream(FileOperations.readFile("obj.dat"));
        stream = new RSStream(FileOperations.readFile("obj.idx"));
        itemCount = stream.getShort() + 21;
        streamIndices = new int[itemCount + 2];
        int offset = 2;

        for (int _ctr = 0; _ctr < itemCount - 21; _ctr++) {
            streamIndices[_ctr] = offset;
            offset += stream.getShort();
        }
        cache = new OSRSItemDefinition[10];
        for (int _ctr = 0; _ctr < 10; _ctr++) {
            cache[_ctr] = new OSRSItemDefinition();
        }
    }

    public void setDefaults() {
        interfaceModelId = 0;
        name = null;
        description = null;
        originalColors = null;
        modifiedColors = null;
        modelZoom = 2000;
        modelRotationY = 0;
        modelRotationX = 0;
        modelRotationZ = 0;
        modelOffsetX = 0;
        modelOffsetY = 0;
        stackingType = false;
        value = 1;
        members = false;
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
    }

    public static OSRSItemDefinition forId(int itemId) {
        for (int j = 0; j < 10; j++) {
            if (cache[j].id == itemId) {
                return cache[j];
            }
        }
        cacheIndex = (cacheIndex + 1) % 10;
        OSRSItemDefinition itemDef = cache[cacheIndex];
        itemStream.currentPosition = streamIndices[itemId];
        itemDef.id = itemId;
        itemDef.setDefaults();
        itemDef.readValues(itemStream);
        if (itemDef.noteTemplateId != -1) {
            // itemDef.toNote();
        }
        return itemDef;
    }

    public void toNote() {
        OSRSItemDefinition itemDef = forId(noteTemplateId);
        interfaceModelId = itemDef.interfaceModelId;
        modelZoom = itemDef.modelZoom;
        modelRotationY = itemDef.modelRotationY;
        modelRotationX = itemDef.modelRotationX;
        modelRotationZ = itemDef.modelRotationZ;
        modelOffsetX = itemDef.modelOffsetX;
        modelOffsetY = itemDef.modelOffsetY;
        originalColors = itemDef.originalColors;
        modifiedColors = itemDef.modifiedColors;
        OSRSItemDefinition itemDef_1 = forId(noteItemId);
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

    public int modelOffsetXOriginal;
    public int modelOffsetYOriginal;

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
                description = rsStream.readString();
            } else if (opcode == 4) {
                modelZoom = rsStream.getShort();
            } else if (opcode == 5) {
                modelRotationY = rsStream.getShort();
            } else if (opcode == 6) {
                modelRotationX = rsStream.getShort();
            } else if (opcode == 7) {
                modelOffsetX = rsStream.getShort();
                modelOffsetXOriginal = modelOffsetX;
                if (modelOffsetX > 32767) {
                    modelOffsetX -= 0x10000;
                }
            } else if (opcode == 8) {
                modelOffsetY = rsStream.getShort();
                modelOffsetYOriginal = modelOffsetY;
                if (modelOffsetY > 32767) {
                    modelOffsetY -= 0x10000;
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
                int j = rsStream.getByte();
                modifiedColors = new int[j];
                originalColors = new int[j];
                for (int k = 0; k < j; k++) {
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
//            } else if (opcode == 100) {
//                int length = rsStream.getByte();
//                stackIds = new int[length];
//                stackAmounts = new int[length];
//                for (int i2 = 0; i2 < length; i2++) {
//                    stackIds[i2] = rsStream.getShort();
//                    stackAmounts[i2] = rsStream.getShort();
//                }
//            }
            } else if (opcode >= 100 && opcode < 110) {
                if (null == this.stackIds) {
                    this.stackIds = new int[10];
                    this.stackAmounts = new int[10];
                }

                this.stackIds[opcode - 100] = rsStream.getShort();
                this.stackAmounts[opcode - 100] = rsStream.getShort();
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
            } else if (opcode == 139) {
                rsStream.getShort();
            } else if (opcode == 140) {
                rsStream.getShort();
            } else if (opcode == 148) {
                rsStream.getShort();
            } else if (opcode == 149) {
                rsStream.getShort();
            }
        } while (true);
    }

    public OSRSItemDefinition() {
        super(-1);
    }

    public byte femaleModelTranslationY;
    public int value;// anInt155
    public int[] originalColors;// newModelColor
    public int id;// anInt157
    public int[] modifiedColors;
    public boolean members;// aBoolean161
    public int femaleModelId3;
    public int noteTemplateId;
    public int femaleArmModelId;// femArmModel
    public int maleModelId;// maleWieldModel
    public int equipped_model_male_dialogue_2;
    public int modelScaleX;
    public String floorOptions[];
    public int modelOffsetX;
    public String name;// itemName
    public static OSRSItemDefinition[] cache;
    public int equipped_model_female_dialogue_2;
    public int interfaceModelId;// dropModel
    public int equipped_model_male_dialogue_1;
    public boolean stackingType;// itemStackable
    public String description;// itemExamine
    public int noteItemId;
    public static int cacheIndex;
    public int modelZoom;
    public static boolean isMembers = true;
    public static RSStream itemStream;
    public int lightMagnitude;
    public int maleModelId3;
    public int maleArmModelId;// maleArmModel
    public String options[];// itemMenuOption
    public int modelRotationY;// modelRotateUp
    public int modelScaleZ;
    public int modelScaleY;
    public int[] stackIds;// modelStack
    public int modelOffsetY;//
    public static int[] streamIndices;
    public int lightIntensity;
    public int equipped_model_female_dialogue_1;
    public int modelRotationX;// modelRotateRight
    public int femaleModelId;// femWieldModel
    public int[] stackAmounts;// itemAmount
    public int team;
    public static int itemCount;
    public int modelRotationZ;// modelPositionUp
    public byte maleModelTranslationY;

}

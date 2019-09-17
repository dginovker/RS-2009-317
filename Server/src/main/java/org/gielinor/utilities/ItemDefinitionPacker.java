package org.gielinor.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Packs item definitions.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ItemDefinitionPacker {

    private static final Logger log = LoggerFactory.getLogger(ItemDefinitionPacker.class);

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

    static final Map<Integer, ItemDefinitionC> map = new HashMap<>();

    /**
     * Packs the item definitions.
     *
     * @throws IOException
     */
    public static void pack() throws IOException {
        DataOutputStream datFile = new DataOutputStream(new FileOutputStream("obj1.dat"));
        DataOutputStream indexFile = new DataOutputStream(new FileOutputStream("obj1.idx"));
        indexFile.writeShort(ItemDefinitionC.getItemCount());
        datFile.writeShort(ItemDefinitionC.getItemCount());
        for (int itemId = 0; itemId < ItemDefinitionC.getItemCount(); itemId++) {
            if (itemId < 14762) {
                continue;
            }
            ItemDefinitionC itemDefinition1 = ItemDefinitionC.forId(itemId, true, false, false, 1);
            ItemDefinitionC itemDefinition = itemDefinition1;
            if (itemDefinition.noteItemId != -1) {
                int id = itemDefinition.noteItemId - 11686;
                itemDefinition.noteItemId = (14761 + id);
            }
            if (itemDefinition.noteTemplateId > 0) {
                itemDefinition.toNote();
            }
            if (itemDefinition.stackIds != null) {
                for (int ii = 0; ii < itemDefinition.stackIds.length; ii++) {
                    int newStackId = itemDefinition.stackIds[ii] - 11686;
                    newStackId = 14761 + newStackId;
                    itemDefinition.stackIds[ii] = newStackId;
                }
            }
            map.put(itemId, itemDefinition.copy());
        }
        for (int itemId = 0; itemId < ItemDefinitionC.getItemCount(); itemId++) {
            ItemDefinitionC itemDefinition = itemId > 14761 ? map.get(itemId) : ItemDefinitionC.forId(itemId, true, itemId < 14762, false, 0);
            if (itemDefinition == null) {
                log.warn("Missing item definition for item [{}].", itemId);
                break;
            }
            if (itemId > 14761) {
                itemDefinition.osrs = true;
            }
            int currentOffset = datFile.size();
            if (itemDefinition.interfaceModelId != 0) {
                datFile.writeByte(OPCODE_INTERFACE_MODEL_ID);
                datFile.writeShort(itemDefinition.interfaceModelId);
            }
            if (itemDefinition.name != null) {
                datFile.writeByte(OPCODE_NAME);
                putString(itemDefinition.name, datFile);
            }
            if (itemDefinition.modelZoom != 2000) {
                datFile.writeByte(OPCODE_MODEL_ZOOM);
                datFile.writeShort(itemDefinition.modelZoom);
            }
            if (itemDefinition.modelRotationX != 0) {
                datFile.writeByte(OPCODE_MODEL_ROTATION_X);
                datFile.writeShort(itemDefinition.modelRotationX);
            }
            if (itemDefinition.modelRotationY != 0) {
                datFile.writeByte(OPCODE_MODEL_ROTATION_Y);
                datFile.writeShort(itemDefinition.modelRotationY);
            }
            if (itemDefinition.modelRotationZ != 0) {
                datFile.writeByte(OPCODE_MODEL_ROTATION_Z);
                datFile.writeShort(itemDefinition.modelRotationZ);
            }
            if (itemDefinition.modelOffsetXOriginal != -1) {
                datFile.writeByte(OPCODE_MODEL_POSITION_X);
                datFile.writeShort(itemDefinition.modelOffsetXOriginal);
            }
            if (itemDefinition.modelOffsetYOriginal != -1) {
                datFile.writeByte(OPCODE_MODEL_POSITION_Y);
                datFile.writeShort(itemDefinition.modelOffsetYOriginal);
            }

            if (itemDefinition.stackingType) {
                datFile.writeByte(OPCODE_STACKING_TYPE);
            }

            if (itemDefinition.value != -1) {
                datFile.writeByte(OPCODE_VALUE);
                datFile.writeInt(itemDefinition.value);
            }
            if (itemDefinition.members) {
                datFile.writeByte(OPCODE_MEMBERS);
            }
            if (itemDefinition.maleModelId != -1) {
                datFile.writeByte(OPCODE_MALE_MODEL_ID);
                datFile.writeShort(itemDefinition.maleModelId);
                datFile.writeByte(itemDefinition.maleModelTranslationY);
            }
            if (itemDefinition.maleArmModelId != -1) {
                datFile.writeByte(OPCODE_MALE_MODEL_ID2);
                datFile.writeShort(itemDefinition.maleArmModelId);
            }
            if (itemDefinition.maleModelId3 != -1) {
                datFile.writeByte(OPCODE_MALE_MODEL_ID3);
                datFile.writeShort(itemDefinition.maleModelId3);
            }
            if (itemDefinition.equipped_model_male_dialogue_1 != -1) {
                datFile.writeByte(OPCODE_EQUIPPED_MODEL_MALE_DIALOGUE_1);
                datFile.writeShort(itemDefinition.equipped_model_male_dialogue_1);
            }
            if (itemDefinition.equipped_model_male_dialogue_2 != -1) {
                datFile.writeByte(OPCODE_EQUIPPED_MODEL_MALE_DIALOGUE_2);
                datFile.writeShort(itemDefinition.equipped_model_male_dialogue_2);
            }
            if (itemDefinition.femaleModelId != -1) {
                datFile.writeByte(OPCODE_FEMALE_MODEL_ID);
                datFile.writeShort(itemDefinition.femaleModelId);
                datFile.writeByte(itemDefinition.femaleModelTranslationY);
            }
            if (itemDefinition.femaleArmModelId != -1) {
                datFile.writeByte(OPCODE_FEMALE_MODEL_ID2);
                datFile.writeShort(itemDefinition.femaleArmModelId);
            }
            if (itemDefinition.femaleModelId3 != -1) {
                datFile.writeByte(OPCODE_FEMALE_MODEL_ID3);
                datFile.writeShort(itemDefinition.femaleModelId3);
            }
            if (itemDefinition.equipped_model_female_dialogue_1 != -1) {
                datFile.writeByte(OPCODE_EQUIPPED_MODEL_FEMALE_DIALOGUE_1);
                datFile.writeShort(itemDefinition.equipped_model_female_dialogue_1);
            }
            if (itemDefinition.equipped_model_female_dialogue_2 != -1) {
                datFile.writeByte(OPCODE_EQUIPPED_MODEL_FEMALE_DIALOGUE_2);
                datFile.writeShort(itemDefinition.equipped_model_female_dialogue_2);
            }
            if (itemDefinition.hasGroundActions && itemDefinition.floorOptions != null) {
                for (int index = 0; index < itemDefinition.floorOptions.length; index++) {
                    if (itemDefinition.floorOptions[index] == null) {
                        continue;
                    }
                    datFile.writeByte(OPCODE_FLOOR_OPTIONS_START + index);
                    putString(itemDefinition.floorOptions[index], datFile);
                }
            }
            if (itemDefinition.hasInventoryOptions && itemDefinition.options != null) {
                for (int index = 0; index < itemDefinition.options.length; index++) {
                    if (itemDefinition.options[index] == null) {
                        continue;
                    }
                    datFile.writeByte(OPCODE_INVENTORY_OPTIONS_START + index);
                    putString(itemDefinition.options[index], datFile);
                }
            }
            if (itemDefinition.hasEquipmentOptions &&
                itemDefinition.equipmentActions != null) {
                for (int index = 0; index <
                    itemDefinition.equipmentActions.length; index++) {
                    if (itemDefinition.equipmentActions[index] == null) {
                        continue;
                    }
                    datFile.writeByte(OPCODE_EQUIPMENT_OPTIONS_START + index);
                    putString(itemDefinition.equipmentActions[index], datFile);
                }
            }
            if (itemDefinition.modifiedColors != null) {
                datFile.writeByte(OPCODE_COLORS);
                datFile.writeByte(itemDefinition.modifiedColors.length);
                for (int ii = 0; ii < itemDefinition.modifiedColors.length; ii++) {
                    if (itemDefinition.modifiedColors != null) {
                        datFile.writeShort(itemDefinition.modifiedColors[ii]);
                    }
                    if (itemDefinition.originalColors != null) {
                        datFile.writeShort(itemDefinition.originalColors[ii]);
                    }
                }
            }
            if (itemDefinition.noteItemId != -1) {
                datFile.writeByte(OPCODE_NOTE_ITEM_ID);
                datFile.writeShort(itemDefinition.noteItemId);
            }
            if (itemDefinition.noteTemplateId > 0) {
                datFile.writeByte(OPCODE_NOTE_TEMPLATE_ID);
                datFile.writeShort(itemDefinition.noteTemplateId);
            }
            if (itemDefinition.stackIds != null) {
                for (int index = 0; index < itemDefinition.stackIds.length; index++) {
                    datFile.writeByte(OPCODE_STACK_IDS_START + index);
                    datFile.writeShort(itemDefinition.stackIds[index]);
                    datFile.writeShort(itemDefinition.stackAmounts[index]);
                }
            }
            if (itemDefinition.modelScaleX != 128) {
                datFile.writeByte(OPCODE_MODEL_SCALE_X);
                datFile.writeShort(itemDefinition.modelScaleX);
            }
            if (itemDefinition.modelScaleY != 128) {
                datFile.writeByte(OPCODE_MODEL_SCALE_Y);
                datFile.writeShort(itemDefinition.modelScaleY);
            }
            if (itemDefinition.modelScaleZ != 128) {
                datFile.writeByte(OPCODE_MODEL_SCALE_Z);
                datFile.writeShort(itemDefinition.modelScaleZ);
            }
            if (itemDefinition.lightIntensity != 0) {
                datFile.writeByte(OPCODE_LIGHT_INTENSITY);
                datFile.writeByte(itemDefinition.lightIntensity);
            }
            if (itemDefinition.lightMagnitude != 0) {
                datFile.writeByte(OPCODE_LIGHT_MAGNITUDE);
                datFile.writeByte(itemDefinition.lightMagnitude / 5);
            }
            if (itemDefinition.team != 0) {
                datFile.writeByte(OPCODE_TEAM);
                datFile.writeByte(itemDefinition.team);
            }
            if (itemDefinition.lentId != -1) {
                datFile.writeByte(OPCODE_LENT_ID);
                datFile.writeShort(itemDefinition.lentId);
            }
            if (itemDefinition.lentTemplateId != -1) {
                datFile.writeByte(OPCODE_LENT_TEMPLATE_ID);
                datFile.writeShort(itemDefinition.lentTemplateId);
            }
            if (itemDefinition.lentTemplateId != -1) {
                datFile.writeByte(OPCODE_LENT_TEMPLATE_ID);
                datFile.writeShort(itemDefinition.lentTemplateId);
            }
            if (itemDefinition.trade) {
                datFile.writeByte(OPCODE_TRADABLE);
            }
            if (itemDefinition.blacklisted) {
                datFile.writeByte(OPCODE_BLACKLISTED);
            }
            if (itemDefinition.osrs) {
                datFile.writeByte(60);
            }
            datFile.writeByte(0);
            int finalOffset = datFile.size();
            int writeOffset = finalOffset - currentOffset;
            indexFile.writeInt(writeOffset);
        }
        datFile.close();
        indexFile.close();
    }

    public static void putString(String string, DataOutputStream out) {
        try {
            out.write(string.getBytes());
            out.writeByte(10);
        } catch (IOException ex) {
            log.error("Unable to write string [{}].", string, ex);
        }
    }

}

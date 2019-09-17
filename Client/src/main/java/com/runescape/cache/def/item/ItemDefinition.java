package com.runescape.cache.def.item;

import com.runescape.Constants;
import com.runescape.cache.def.DefinitionData;
import com.runescape.cache.def.impl.ItemRepository;
import com.runescape.cache.media.Sprite;
import com.runescape.collection.ReferenceCache;
import com.runescape.media.Raster;
import com.runescape.media.renderable.Model;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.scene.graphic.Rasterizer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Refactored reference from rename317
 * https://code.google.com/p/rename317/source
 * /browse/trunk/src/rs2/ItemDef.java?spec=svn202&r=202
 */
public class ItemDefinition {
    /**
     * The {@link java.util.logging.Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(ItemDefinition.class.getName());
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
    public static ReferenceCache sprites = new ReferenceCache(100);
    public static ReferenceCache models = new ReferenceCache(50);
    public static ReferenceCache osrsModels = new ReferenceCache(50);
    public static boolean isMembers = true;
    private static DefinitionData definitionData = new DefinitionData();
    private static ItemDefinition[] cache;
    public byte femaleModelTranslationY;
    public int value;
    public int[] modifiedColors;
    public int id;
    public int[] originalColors;
    public boolean members;
    public int femaleModelId3;
    public int noteTemplateId;
    public int lentId;
    public int lentTemplateId;
    public int femaleArmModelId;
    public int maleModelId;
    public boolean osrs;
    public int equipped_model_male_dialogue_2;
    public int modelScaleX;
    public boolean tradable;
    public boolean blacklisted;
    public String[] equipmentActions;
    public String[] floorOptions;
    public int modelPositionX;
    public int modelOffsetXOriginal;
    public int modelOffsetYOriginal;
    public String name;
    public int equipped_model_female_dialogue_2;
    public int interfaceModelId;
    public int equipped_model_male_dialogue_1;
    public boolean stackingType;
    public byte[] description;
    public int noteItemId;
    public int modelZoom;
    public int lightMagnitude;
    public int maleModelId3;
    public int maleArmModelId;
    public String[] options;
    public int modelRotationY;
    public int modelScaleZ;
    public int modelScaleY;
    public int[] stackIds;
    public int modelPositionY;//
    public int lightIntensity;
    public int equipped_model_female_dialogue_1;
    public int modelRotationX;
    public int femaleModelId;
    public int[] stackAmounts;
    public int team;
    public int modelRotationZ;
    public byte maleModelTranslationY;
    public boolean hasInventoryOptions = false;
    public boolean hasEquipmentOptions = false;
    boolean hasGroundActions = false;
    public boolean stockmarket; //g.e?
    private short[] retextureSrc;
    private short[] retextureDst;

    public ItemDefinition() {
        this.id = -1;
    }

    public static ItemDefinition[] getCache() {
        return cache;
    }

    public static void clearCache() {
        models = null;
        sprites = null;
        cache = null;
        definitionData.clear();
    }

    public static void unpackConfig(CacheArchive cacheArchive) {
        definitionData.load(cacheArchive, "obj", false, 21);
        cache = new ItemDefinition[10];
        for (int index = 0; index < 10; index++) {
            cache[index] = new ItemDefinition();
        }
    }

    public static ItemDefinition forId(int itemId) {
        if (itemId >= 20000) {
            return OSRSItemDefinition.forId(itemId);
        }
        for (int count = 0; count < 10; count++) {
            if (cache[count].id == itemId) {
                return cache[count];
            }
        }

        definitionData.setCacheIndex((definitionData.getCacheIndex() + 1) % 10);
        ItemDefinition itemDefinition = cache[definitionData.getCacheIndex()];
        if (itemId < 0 || itemId >= definitionData.getIndices().length) {
            itemId = 0;
        }
        definitionData.getBuffer().currentPosition = definitionData.getOffset(itemId);
        itemDefinition.id = itemId;
        itemDefinition.setDefaults();

        if (itemId < definitionData.getCount()) {
            itemDefinition.readValues498(definitionData.getBuffer());
        }
        if (itemDefinition.noteTemplateId != -1) {
            itemDefinition.toNote();
        }
//        if (itemDefinition.lentTemplateId != -1) {
//            itemDefinition.toLend();
//        }
        if (itemDefinition.id == itemId && itemDefinition.modifiedColors == null) {
            itemDefinition.modifiedColors = new int[1];
            itemDefinition.originalColors = new int[1];
            itemDefinition.modifiedColors[0] = 0;
            itemDefinition.originalColors[0] = 1;
        }
        if (itemDefinition.modifiedColors != null && itemDefinition.osrs) { // TODO
            int[] copy = itemDefinition.modifiedColors;
            itemDefinition.modifiedColors = itemDefinition.originalColors;
            itemDefinition.originalColors = copy;
        }
        if (ItemRepository.forId(itemId, itemDefinition) != null) {
            return ItemRepository.forId(itemId, itemDefinition);
        }
        return itemDefinition;
    }

    public static Sprite getSprite(int itemId, int amount, int outlineColor) {
        if (outlineColor == 0) {
            Sprite sprite = (Sprite) sprites.get(itemId);
            if (sprite != null && sprite.maxHeight != amount && sprite.maxHeight != -1) {
                sprite.unlink();
                sprite = null;
            }
            if (sprite != null) {
                return sprite;
            }
        }
        ItemDefinition itemDefinition = forId(itemId);
        if (itemDefinition.stackIds == null) {
            amount = -1;
        }
        if (amount > 1) {
            int stackedId = -1;
            for (int j1 = 0; j1 < 10; j1++) {
                if (amount >= itemDefinition.stackAmounts[j1] && itemDefinition.stackAmounts[j1] != 0) {
                    stackedId = itemDefinition.stackIds[j1];
                }
            }
            if (stackedId != -1) {
                itemDefinition = forId(stackedId);
            }
        }
        Model model = itemDefinition.getModel(1);
        if (model == null) {
            return null;
        }
        Sprite sprite = null;
        if (itemDefinition.noteTemplateId != -1) {
            sprite = getSprite(itemDefinition.noteItemId, 10, -1);
            if (sprite == null) {
                return null;
            }
        }
//        if (itemDef.lentItemID != -1) {
//            sprite = getSprite(itemDef.lendID, 50, 0);
//            if (sprite == null) {
//                return null;
//            }
//        }
        Sprite sprite2 = new Sprite(32, 32);
        int k1 = Rasterizer.textureInt1;
        int l1 = Rasterizer.textureInt2;
        int ai[] = Rasterizer.lineOffsets;
        int ai1[] = Raster.pixels;
        int i2 = Raster.width;
        int j2 = Raster.height;
        int k2 = Raster.topX;
        int l2 = Raster.bottomX;
        int i3 = Raster.topY;
        int j3 = Raster.bottomY;
        Rasterizer.notTextured = false;
        Raster.initDrawingArea(32, 32, sprite2.myPixels);
        Raster.drawPixels(32, 0, 0, 0, 32);
        Rasterizer.setDefaultBounds();
        int k3 = itemDefinition.modelZoom;
        if (outlineColor == -1) {
            k3 = (int) ((double) k3 * 1.5D);
        }
        if (outlineColor > 0) {
            k3 = (int) ((double) k3 * 1.04D);
        }
        int l3 = Rasterizer.SINE[itemDefinition.modelRotationY] * k3 >> 16;
        int i4 = Rasterizer.COSINE[itemDefinition.modelRotationY] * k3 >> 16;
        try {
            model.renderSingle(itemDefinition.modelRotationX, itemDefinition.modelRotationZ, itemDefinition.modelRotationY, itemDefinition.modelPositionX, l3 + model.modelHeight / 2 + itemDefinition.modelPositionY, i4 + itemDefinition.modelPositionY);
        } catch (Exception e) {
            System.out.println(itemDefinition.osrs + " " + itemDefinition.id);
            //e.printStackTrace();
        }
        for (int i5 = 31; i5 >= 0; i5--) {
            for (int j4 = 31; j4 >= 0; j4--) {
                if (sprite2.myPixels[i5 + j4 * 32] == 0) {
                    if (i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1) {
                        sprite2.myPixels[i5 + j4 * 32] = 1;
                    } else if (j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1) {
                        sprite2.myPixels[i5 + j4 * 32] = 1;
                    } else if (i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1) {
                        sprite2.myPixels[i5 + j4 * 32] = 1;
                    } else if (j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1) {
                        sprite2.myPixels[i5 + j4 * 32] = 1;
                    }
                }
            }
        }
        if (outlineColor > 0) {
            for (int j5 = 31; j5 >= 0; j5--) {
                for (int k4 = 31; k4 >= 0; k4--) {
                    if (sprite2.myPixels[j5 + k4 * 32] == 0) {
                        if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1) {
                            sprite2.myPixels[j5 + k4 * 32] = outlineColor;
                        } else if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1) {
                            sprite2.myPixels[j5 + k4 * 32] = outlineColor;
                        } else if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1) {
                            sprite2.myPixels[j5 + k4 * 32] = outlineColor;
                        } else if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1) {
                            sprite2.myPixels[j5 + k4 * 32] = outlineColor;
                        }
                    }
                }
            }
        } else if (outlineColor == 0) {
            for (int k5 = 31; k5 >= 0; k5--) {
                for (int l4 = 31; l4 >= 0; l4--) {
                    if (sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0) {
                        sprite2.myPixels[k5 + l4 * 32] = 0x302020;
                    }
                }
            }
        }
        if (itemDefinition.noteTemplateId != -1) {
            int l5 = sprite.maxWidth;
            int j6 = sprite.maxHeight;
            sprite.maxWidth = 32;
            sprite.maxHeight = 32;
            sprite.drawSprite(0, 0);
            sprite.maxWidth = l5;
            sprite.maxHeight = j6;
        }
//        if (itemDef.lentItemID != -1) {
//            int l5 = sprite.maxWidth;
//            int j6 = sprite.maxHeight;
//            sprite.maxWidth = 32;
//            sprite.maxHeight = 32;
//            sprite.drawSprite(0, 0);
//            sprite.maxWidth = l5;
//            sprite.maxHeight = j6;
//        }
        if (outlineColor == 0) {
            sprites.put(sprite2, itemId);
        }
        Raster.initDrawingArea(j2, i2, ai1);
        Raster.setDrawingArea(j3, k2, l2, i3);
        Rasterizer.textureInt1 = k1;
        Rasterizer.textureInt2 = l1;
        Rasterizer.lineOffsets = ai;
        Rasterizer.notTextured = true;
        if (itemDefinition.stackingType) {
            sprite2.maxWidth = 33;
        } else {
            sprite2.maxWidth = 32;
        }
        sprite2.maxHeight = amount;
        return sprite2;
    }

    /**
     * Copies this definition to a new definition.
     *
     * @param copy           The id of the item to copy.
     * @param itemDefinition The new item definition.
     */
    public static void copy(int copy, ItemDefinition itemDefinition) {
        ItemDefinition copyDefinition = forId(copy);
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

    public static int getItemCount() {
        return definitionData.getCount();
    }

    /**
     * Gets the next free item id.
     *
     * @return The free item id.
     */
    public static int getFreeId() {
        return definitionData.getCount() + 1;
    }

    /**
     * If the item has the specified item.
     *
     * @param optionName The action.
     * @return If the item has the specified action <code>True</code>.
     */
    public boolean hasAction(String optionName) {
        if (options == null) {
            return false;
        }
        for (String action : options) {
            if (action == null) {
                continue;
            }
            if (action.equalsIgnoreCase(optionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    public int[] getEquipModels() {
        return new int[]{interfaceModelId,
                maleModelId, maleArmModelId, maleModelId3, equipped_model_male_dialogue_1, equipped_model_male_dialogue_2,
                femaleModelId, femaleArmModelId, femaleModelId3, equipped_model_female_dialogue_1, equipped_model_female_dialogue_2
        };
    }

    public boolean isDialogueModelCached(int gender) {
        int model_1 = equipped_model_male_dialogue_1;
        int model_2 = equipped_model_male_dialogue_2;
        if (gender == 1) {
            model_1 = equipped_model_female_dialogue_1;
            model_2 = equipped_model_female_dialogue_2;
        }
        if (model_1 == -1) {
            return true;
        }
        boolean cached = true;
        if (!Model.isCached(model_1, osrs)) {
            cached = false;
        }
        if (model_2 != -1 && !Model.isCached(model_2, osrs)) {
            cached = false;
        }
        return cached;
    }

    public Model getChatEquipModel(int gender) {
        int dialogueModel = equipped_model_male_dialogue_1;
        int dialogueHatModel = equipped_model_male_dialogue_2;
        if (gender == 1) {
            dialogueModel = equipped_model_female_dialogue_1;
            dialogueHatModel = equipped_model_female_dialogue_2;
        }
        if (dialogueModel == -1) {
            return null;
        }
        Model dialogueModel_ = Model.getModel(dialogueModel, osrs);
        if (dialogueHatModel != -1) {
            Model hatModel_ = Model.getModel(dialogueHatModel, osrs);
            Model models[] = {dialogueModel_, hatModel_};
            dialogueModel_ = new Model(2, models);
        }
        if (modifiedColors != null) {
            for (int i1 = 0; i1 < modifiedColors.length; i1++) {
                dialogueModel_.recolor(modifiedColors[i1], originalColors[i1]);
            }

        }
        return dialogueModel_;
    }

    public boolean isEquippedModelCached(int gender) {
        int primaryModel = maleModelId;
        int secondaryModel = maleArmModelId;
        int emblem = maleModelId3;
        if (gender == 1) {
            primaryModel = femaleModelId;
            secondaryModel = femaleArmModelId;
            emblem = femaleModelId3;
        }
        if (primaryModel == -1) {
            return true;
        }
        boolean cached = true;
        if (!Model.isCached(primaryModel, osrs)) {
            cached = false;
        }
        if (secondaryModel != -1 && !Model.isCached(secondaryModel, osrs)) {
            cached = false;
        }
        if (emblem != -1 && !Model.isCached(emblem, osrs)) {
            cached = false;
        }
        return cached;
    }

    public Model getEquippedModel(int gender) {
        int primaryModel = maleModelId;
        int secondaryModel = maleArmModelId;
        int emblem = maleModelId3;
        if (gender == 1) {
            primaryModel = femaleModelId;
            secondaryModel = femaleArmModelId;
            emblem = femaleModelId3;
        }
        if (primaryModel == -1) {
            return null;
        }
        Model primaryModel_ = Model.getModel(primaryModel, osrs);
        if (secondaryModel != -1) {
            if (emblem != -1) {
                Model secondaryModel_ = Model.getModel(secondaryModel, osrs);
                Model emblemModel = Model.getModel(emblem, osrs);
                Model models[] = {primaryModel_, secondaryModel_, emblemModel};
                primaryModel_ = new Model(3, models);
            } else {
                Model model_2 = Model.getModel(secondaryModel, osrs);
                Model models[] = {primaryModel_, model_2};
                primaryModel_ = new Model(2, models);
            }
        }
        if (gender == 0 && maleModelTranslationY != 0) {
            primaryModel_.translate(0, maleModelTranslationY, 0);
        }
        if (gender == 1 && femaleModelTranslationY != 0) {
            primaryModel_.translate(0, femaleModelTranslationY, 0);
        }
        if (modifiedColors != null) {
            for (int i1 = 0; i1 < modifiedColors.length; i1++) {
                primaryModel_.recolor(modifiedColors[i1], originalColors[i1]);
            }

        }
        return primaryModel_;
    }

    public void setDefaults() {
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
        tradable = false;
        blacklisted = false;
        osrs = false;
    }

    public void toNote() {
        ItemDefinition itemDefinition = forId(noteTemplateId);
        interfaceModelId = itemDefinition.interfaceModelId;
        modelZoom = itemDefinition.modelZoom;
        modelRotationY = itemDefinition.modelRotationY;
        modelRotationX = itemDefinition.modelRotationX;
        modelRotationZ = itemDefinition.modelRotationZ;
        modelPositionX = itemDefinition.modelPositionX;
        modelPositionY = itemDefinition.modelPositionY;
        modifiedColors = itemDefinition.modifiedColors;
        originalColors = itemDefinition.originalColors;
        ItemDefinition itemDefinition1 = forId(noteItemId);
        name = itemDefinition1.name;
        members = itemDefinition1.members;
        value = itemDefinition1.value;
        String s = "a";
        if (itemDefinition1.name != null && itemDefinition1.name.trim().length() > 0) {
            char c = itemDefinition1.name.charAt(0);
            if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
                s = "an";
            }
        }
        description = ("Swap this note at any bank for " + s + " " + itemDefinition1.name + ".").getBytes();
        stackingType = true;
    }

//    private void toLend() {
//        ItemDefinition itemDefinition = forId(lentTemplateId);
//        options = new String[5];
//        interfaceModelId = itemDefinition.interfaceModelId;
//        modelPositionX = itemDefinition.modelPositionX;
//        modelRotationX = itemDefinition.modelRotationX;
//        modelPositionY = itemDefinition.modelPositionY;
//        modelZoom = itemDefinition.modelZoom;
//        modelRotationY = itemDefinition.modelRotationY;
//        modelRotationZ = itemDefinition.modelRotationZ;
//        value = 0;
//        ItemDefinition itemDefinition1 = forId(lentId);
//        equipped_model_male_dialogue_2 = itemDefinition1.equipped_model_male_dialogue_2;
//        modifiedColors = itemDefinition1.modifiedColors;
//        maleModelId3 = itemDefinition1.maleModelId3;
//        maleArmModelId = itemDefinition1.maleArmModelId;
//        equipped_model_female_dialogue_2 = itemDefinition1.equipped_model_female_dialogue_2;
//        equipped_model_male_dialogue_1 = itemDefinition1.equipped_model_male_dialogue_1;
//        floorOptions = itemDefinition1.floorOptions;
//        maleModelId = itemDefinition1.maleModelId;
//        name = itemDefinition1.name;
//        femaleModelId = itemDefinition1.femaleModelId;
//        members = itemDefinition1.members;
//        equipped_model_female_dialogue_1 = itemDefinition1.equipped_model_female_dialogue_1;
//        femaleArmModelId = itemDefinition1.femaleArmModelId;
//        femaleModelId3 = itemDefinition1.femaleModelId3;
//        originalColors = itemDefinition1.originalColors;
//        team = itemDefinition1.team;
//        if (itemDefinition1.options != null) {
//            System.arraycopy(itemDefinition1.options, 0, options, 0, 4);
//        }
//        options[4] = "Discard";
//    }

    public Model getModel(int amount) {
        if (stackIds != null && amount > 1) {
            int stackId = -1;
            for (int k = 0; k < 10; k++) {
                if (amount >= stackAmounts[k] && stackAmounts[k] != 0) {
                    stackId = stackIds[k];
                }
            }
            if (stackId != -1) {
                return forId(stackId).getModel(1);
            }
        }
        Model model = (Model) (osrs ? osrsModels.get(id) : models.get(id));
        if (model != null) {
            return model;
        }
        model = Model.getModel(interfaceModelId, osrs);
        if (model == null) {
            return null;
        }
        if (modelScaleX != 128 || modelScaleY != 128 || modelScaleZ != 128) {
            model.scaleT(modelScaleX, modelScaleZ, modelScaleY);
        }
        if (modifiedColors != null) {
            for (int l = 0; l < modifiedColors.length; l++) {
                model.recolor(modifiedColors[l], originalColors[l]);
            }
        }
        model.light(64 + lightIntensity, 768 + lightMagnitude, -50, -10, -50, true);
        model.fitsOnSingleSquare = true;
        (osrs ? osrsModels : models).put(model, id);
        return model;
    }

    public Model getUnshadedModel(int stack_size) {
        if (stackIds != null && stack_size > 1) {
            int stack_item_id = -1;
            for (int count = 0; count < 10; count++) {
                if (stack_size >= stackAmounts[count]
                        && stackAmounts[count] != 0) {
                    stack_item_id = stackIds[count];
                }
            }

            if (stack_item_id != -1) {
                return forId(stack_item_id).getUnshadedModel(1);
            }
        }
        Model model = Model.getModel(interfaceModelId, osrs);
        if (model == null) {
            return null;
        }
        if (modifiedColors != null) {
            for (int colorPtr = 0; colorPtr < modifiedColors.length; colorPtr++) {
                model.recolor(modifiedColors[colorPtr], originalColors[colorPtr]);
            }
        }
        return model;
    }
    private void readValues498(RSStream stream) {
        do {
            int i = stream.getByte();
            if (i == 0)
                return;
            if (i == 1) {
                interfaceModelId = stream.getShort();
            } else if (i == 2)
                name = stream.getString();
            else if (i == 3)
                description = stream.readBytes();
            else if (i == 4)
                modelZoom = stream.getShort();
            else if (i == 5)
                modelRotationY = stream.getShort();
            else if (i == 6)
                modelRotationX = stream.getShort();
            else if (i == 7) {
                modelPositionX = stream.getShort();
                if (modelPositionX > 32767)
                    modelPositionX -= 0x10000;
            } else if (i == 8) {
                modelPositionY = stream.getShort();
                if (modelPositionY > 32767)
                    modelPositionY -= 0x10000;
            } else if (i == 10)
                stream.getShort();
            else if (i == 11)
                stackingType = true;
            else if (i == 12)
                value = stream.getShort();
            else if (i == 16)
                members = true;
            else if (i == 23) {
                maleModelId = stream.getShort();
                maleModelTranslationY = stream.getSignedByte();
            } else if (i == 24)
                maleArmModelId = stream.getShort();
            else if (i == 25) {
                femaleModelId = stream.getShort();
                femaleModelTranslationY = stream.getSignedByte();
            } else if (i == 26)
                femaleArmModelId = stream.getShort();
            else if (i >= 30 && i < 35) {
                if (floorOptions == null)//TODO
                    floorOptions = new String[5];
                floorOptions[i - 30] = stream.getString();
                if (floorOptions[i - 30].equalsIgnoreCase("hidden"))
                    floorOptions[i - 30] = null;
            } else if (i >= 35 && i < 40) {
                if (options == null)
                    options = new String[5];
                options[i - 35] = stream.getString();
                if (options[i - 35].equalsIgnoreCase("null"))
                    options[i - 35] = null;
            } else if (i == 40) {
                int j = stream.getByte();
                modifiedColors = new int[j];
                originalColors = new int[j];
                for (int k = 0; k < j; k++) {
                    modifiedColors[k] = stream.getShort();
                    originalColors[k] = stream.getShort();
                }
            } else if (i == 78)//colourEquip1
                maleModelId3 = stream.getShort();
            else if (i == 79)//colourEquip2
                femaleModelId3 = stream.getShort();
            else if (i == 90)
                equipped_model_male_dialogue_1 = stream.getShort();
            else if (i == 91)
                equipped_model_female_dialogue_1 = stream.getShort();
            else if (i == 92)
                equipped_model_male_dialogue_2 = stream.getShort();
            else if (i == 93)
                equipped_model_female_dialogue_2 = stream.getShort();
            else if (i == 95)
                modelRotationZ = stream.getShort();
            else if (i == 97)
                noteItemId = stream.getShort();
            else if (i == 98)
                noteTemplateId = stream.getShort();
            else if (i >= 100 && i < 110) {
                if (stackIds == null) {
                    stackIds = new int[10];
                    stackAmounts = new int[10];
                }
                stackIds[i - 100] = stream.getShort();
                stackAmounts[i - 100] = stream.getShort();
            } else if (i == 110)
                modelScaleX = stream.getShort();
            else if (i == 111)
                modelScaleY = stream.getShort();
            else if (i == 112)
                modelScaleZ = stream.getShort();
            else if (i == 113)
                lightIntensity = stream.getSignedByte();
            else if (i == 114)
                lightMagnitude = stream.getSignedByte() * 5;
            else if (i == 115)
                team = stream.getByte();
            else if (i == 116)
                lentId = stream.getShort();
            else if (i == 117)
                lentTemplateId = stream.getShort();


        } while (true);
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

    public void decode(RSStream buffer, int opcode) {
        if (opcode == 1)
            interfaceModelId = buffer.getShort();
        else if (opcode == 2)
            name = osrs ? buffer.getNewString() : buffer.getString();
        else if (opcode == 4)
            modelZoom = buffer.getShort();
        else if (opcode == 5)
            modelRotationY = buffer.getShort();
        else if (opcode == 6)
            modelRotationX = buffer.getShort();
        else if (opcode == 7) {
            int positionX = buffer.getShort();
            modelPositionX = positionX;
            if (modelPositionX > 32767)
                modelPositionX -= 65536;
        } else if (opcode == 8) {
            int positionY = buffer.getShort();
            modelPositionY = positionY;
            if (modelPositionY > 32767)
                modelPositionY -= 65536;
        } else if (opcode == 11)
            stackingType = true;
        else if (opcode == 12)
            value = buffer.getInt();
        else if (opcode == 16)
            members = true;
        else if (opcode == 23) {
            maleModelId = buffer.getShort();
            maleModelTranslationY = (byte) buffer.getByte();
        } else if (opcode == 24)
            maleArmModelId = buffer.getShort();
        else if (opcode == 25) {
            femaleModelId = buffer.getShort();
            femaleModelTranslationY = (byte) buffer.getByte();
        } else if (opcode == 26)
            femaleArmModelId = buffer.getShort();
        else if (opcode >= 30 && opcode < 35) {
            if (floorOptions == null) {
                floorOptions = new String[5];
            }
            floorOptions[opcode - 30] = osrs ? buffer.getNewString() : buffer.getString();
            if (floorOptions[opcode - 30].equalsIgnoreCase("Hidden"))
                floorOptions[opcode - 30] = null;
        } else if (opcode >= 35 && opcode < 40) {
            if (options == null) {
                options = new String[5];
            }
            options[opcode - 35] = osrs ? buffer.getNewString() : buffer.getString();
        } else {
            int i_4;
            int i_5;
            if (opcode == 40) {
                i_4 = buffer.getByte();
                modifiedColors = new int[i_4];
                originalColors = new int[i_4];

                for (i_5 = 0; i_5 < i_4; i_5++) {
                    modifiedColors[i_5] = (short) buffer.getShort();
                    originalColors[i_5] = (short) buffer.getShort();
                }
            } else if (opcode == 41) {
                i_4 = buffer.getByte();
                retextureSrc = new short[i_4];
                retextureDst = new short[i_4];

                for (i_5 = 0; i_5 < i_4; i_5++) {
                    retextureSrc[i_5] = (short) buffer.getShort();
                    retextureDst[i_5] = (short) buffer.getShort();
                }
            } else if (opcode == 42)
                buffer.getSignedByte();
            else if (opcode == 65)
                stockmarket = true;
            else if (opcode == 78)
                maleModelId3 = buffer.getShort();
            else if (opcode == 79)
                femaleModelId3 = buffer.getShort();
            else if (opcode == 90)
                equipped_model_male_dialogue_1 = buffer.getShort();
            else if (opcode == 91)
                equipped_model_female_dialogue_1 = buffer.getShort();
            else if (opcode == 92)
                equipped_model_male_dialogue_2 = buffer.getShort();
            else if (opcode == 93)
                equipped_model_female_dialogue_2 = buffer.getShort();
            else if (opcode == 95)
                modelRotationZ = buffer.getShort();
            else if (opcode == 97)
                noteItemId = buffer.getShort();
            else if (opcode == 98)
                noteTemplateId = buffer.getShort();
            else if (opcode >= 100 && opcode < 110) {
                if (stackIds == null) {
                    stackIds = new int[10];
                    stackAmounts = new int[10];
                }

                stackIds[opcode - 100] = buffer.getShort();
                stackAmounts[opcode - 100] = buffer.getShort();
            } else if (opcode == 110)
                modelScaleX = buffer.getShort();
            else if (opcode == 111)
                modelScaleY = buffer.getShort();
            else if (opcode == 112)
                modelScaleZ = buffer.getShort();
            else if (opcode == 113)
                lightIntensity = buffer.getSignedByte();
            else if (opcode == 114)
                lightMagnitude = buffer.getSignedByte() * 5;
            else if (opcode == 115)
                team = buffer.getByte();
            else if (opcode == 139)
                lentId = buffer.getShort();
            else if (opcode == 140)
                lentTemplateId = buffer.getShort();
            else if (opcode == 148)
                buffer.getShort();
            else if (opcode == 149)
                buffer.getShort();
            else if (opcode == 249)
                buffer.staticMethod195();
            else
                System.out.println("Unknown opcode for item [" + id + "] - " + opcode + ".");
        }

    }
    public void readValues(RSStream rsStream) {
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
                    name = rsStream.getString();
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
                    tradable = true;
                    break;

                case OPCODE_BLACKLISTED:
                    blacklisted = true;
                    break;

                case OPCODE_OSRS:
                    osrs = true;
                    break;

                default:
                    if (opcode >= OPCODE_FLOOR_OPTIONS_START && opcode <= OPCODE_FLOOR_OPTIONS_END) {
                        if (floorOptions == null) {
                            floorOptions = new String[5];
                        }
                        hasGroundActions = true;
                        floorOptions[opcode - OPCODE_FLOOR_OPTIONS_START] = rsStream.getString();
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
                        options[opcode - OPCODE_INVENTORY_OPTIONS_START] = rsStream.getString();
                        if (options[opcode - OPCODE_INVENTORY_OPTIONS_START].equalsIgnoreCase("hidden")) {
                            options[opcode - OPCODE_INVENTORY_OPTIONS_START] = null;
                        }
                    } else if (opcode >= OPCODE_EQUIPMENT_OPTIONS_START && opcode <= OPCODE_EQUIPMENT_OPTIONS_END) {
                        if (equipmentActions == null) {
                            equipmentActions = new String[5];
                        }
                        hasEquipmentOptions = true;
                        equipmentActions[opcode - OPCODE_EQUIPMENT_OPTIONS_START] = rsStream.getString();
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
                        if (Constants.DEBUG_MODE) {
                            logger.log(Level.WARNING, "Unknown item definition ({2}) opcode: {0}, history: {1}", new Object[]{opcode, Arrays.toString(opcodeHistory), id});
                        }
                    }
                    break;
            }
            System.arraycopy(opcodeHistory, 0, opcodeHistory, 1, opcodeHistory.length - 2 + 1);
            opcodeHistory[0] = opcode;
        } while (true);
    }

    public String dump() {
        StringBuilder stringBuilder = new StringBuilder();
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
        return stringBuilder.toString();
    }

}
